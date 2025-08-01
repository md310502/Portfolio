import RPi.GPIO as GPIO
import time
import boto3
from botocore.exceptions import NoCredentialsError, ClientError

# AWS Configuration
REGION_NAME = "us-east-1"  # Update with your AWS region
TABLE_NAME = "gpio_logs"   # Ensure this matches your DynamoDB table name

# Initialize DynamoDB Client
dynamodb = boto3.client('dynamodb', region_name=REGION_NAME)

# Function to log data to DynamoDB
def log_to_dynamodb(gpio_id, action):
    timestamp = str(int(time.time()))  # Current UNIX timestamp

    try:
        response = dynamodb.put_item(
            TableName=TABLE_NAME,
            Item={
                'gpio_id': {'S': str(gpio_id)},  # Primary Key
                'timestamp': {'N': timestamp},   # Numeric timestamp
                'action': {'S': action}          # "ON" or "OFF"
            }
        )
        print(f" Logged to DynamoDB: GPIO {gpio_id} - {action}")
    except NoCredentialsError:
        print(" AWS credentials not found. Ensure 'aws configure' is set up.")
    except ClientError as e:
        print(f" AWS Error: {e.response['Error']['Message']}")

# Function to check if a GPIO pin is free
def is_pin_free(pin):
    try:
        GPIO.setup(pin, GPIO.OUT)
        GPIO.output(pin, GPIO.LOW)
        GPIO.output(pin, GPIO.HIGH)
        GPIO.output(pin, GPIO.LOW)
        log_to_dynamodb(pin, "PIN_TEST")  # Log to AWS
        return True
    except Exception as e:
        print(f" Error testing GPIO pin {pin}: {e}")
        log_to_dynamodb(pin, "PIN_TEST_FAILED")
        return False
    finally:
        GPIO.cleanup(pin)

# Function to flash an LED
def flash_led(pin, flash_time=1, cycles=10):
    GPIO.setmode(GPIO.BCM)  # Use Broadcom GPIO numbering
    GPIO.setup(pin, GPIO.OUT)

    print(f" Flashing LED on GPIO {pin}")
    try:
        for i in range(cycles):
            GPIO.output(pin, GPIO.HIGH)  # LED ON
            log_to_dynamodb(pin, "ON")  # Log ON event
            time.sleep(flash_time)

            GPIO.output(pin, GPIO.LOW)  # LED OFF
            log_to_dynamodb(pin, "OFF")  # Log OFF event
            time.sleep(flash_time)

            print(f"Flash {i + 1}/{cycles}")
    except KeyboardInterrupt:
        print(" Interrupted by user.")
    finally:
        GPIO.output(pin, GPIO.LOW)  # Ensure LED is OFF
        GPIO.cleanup()
        print(" Cleanup complete.")

# Main script to find open GPIO pins and offer flashing
if __name__ == "__main__":
    GPIO.setmode(GPIO.BCM)  # Use Broadcom numbering
    available_pins = []

    try:
        print(" Scanning GPIO pins...")
        for pin in range(2, 28):  # Test GPIO pins 2 through 27
            print(f"🛠 Testing GPIO pin {pin}...")
            if is_pin_free(pin):
                print(f" GPIO pin {pin} is free!")
                available_pins.append(pin)
            else:
                print(f" GPIO pin {pin} is NOT free.")

        if not available_pins:
            print(" No free GPIO pins found!")
        else:
            print(f"Available GPIO pins: {available_pins}")

            # Ask user to flash LED on a free pin
            flash_pin = int(input(" Enter a free GPIO pin to flash an LED (or 0 to exit): "))
            if flash_pin in available_pins:
                flash_time = float(input(" Enter flash time (e.g., 0.5 seconds): "))
                cycles = int(input(" Enter number of flash cycles: "))
                flash_led(flash_pin, flash_time, cycles)
            elif flash_pin == 0:
                print(" Exiting without flashing.")
            else:
                print("Invalid pin selected. Exiting.")

    finally:
        GPIO.cleanup()
        print(" GPIO cleanup complete.")

