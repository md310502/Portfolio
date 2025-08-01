#include <Streaming.h> // declaration of the files used in the program
#include <TM1638plus.h> // used in the 7seg
#include <Adafruit_GFX.h> // used in OLED Screen
#include <Adafruit_SSD1306.h> // used in OLED screen

// defining or setting parameters 
#define OLED_RESET -1 // used in OLED
#define OLED_SCREEN_I2C_ADDRESS 0x3C // used in OLED
#define SCREEN_WIDTH 128 // used in setting the width of the OLED screen
#define SCREEN_HEIGHT 64 // used in setting the Height of the OLED screen
#define  STROBE_TM D4 // All 3 used in setting up the connections for the 7seg
#define  CLOCK_TM D6  
#define  DIO_TM D7 
bool high_freq = false; 

TM1638plus tm(STROBE_TM, CLOCK_TM , DIO_TM, high_freq); // setting up the connections for 7seg
Adafruit_SSD1306 display(OLED_RESET); // setting up OLED screen
int buttonPin = D3; // setting up the button used in the 9th score

void setup() 
{
 Serial.begin(115200); // sets the serial number used for the serial outputs
 display.begin(SSD1306_SWITCHCAPVCC, OLED_SCREEN_I2C_ADDRESS); // all of these are used the oled screen settup
 display.display(); // displays 
 delay(2000); // delay before the screen code moves on
 display.clearDisplay(); // clears the display 
 display.setCursor(0,0);
 display.setTextSize(2); // Sets OLED Screen text size
 display.setTextColor(WHITE);
 tm.displayBegin();

 pinMode(buttonPin, INPUT_PULLUP); // sets up the button on the breadboard
 Serial << "Welcome to the Premier League Scorboard system" << endl;  // tells the user what the system does
 Serial << "This system is for displaying Premier League scores" << endl; 
 Serial << "These Scores are from Matchday 16" << endl;
 Serial << "Note: these scores are pre programmed in" << endl;
}
void buttons() // allows the scoreboard program to work
{
  while (1) // Loops program continuosly
  {
    uint8_t buttons = tm.readButtons(); // reads the buttons on the 7seg
   while(buttons == 1) // loops code whilst button 1 is pressed
   {
    tm.displayText("BRE2WAT1"); // displays text on 7seg, scores are split on each of the 2 displays repeat for other 7seg buttons
    display << "Brentford 2 - Watford 1" << endl; // displays the score on the OLED, repeat for other 7seg buttons
    display.display(); // outputs the display to the OLED
    display.setCursor(0,0); // resets back to corner
    display.clearDisplay(); // clears display
   }
   display.clearDisplay(); // makes sure OLED screen does not loop
   while(buttons == 2) // loops code whilst button 2 is pressed
   {
    tm.displayText("MCI1WOV0"); 
    display << "Manchester City 1 - Wolverhampton Wanderers 0" << endl; 
    display.display();
    display.setCursor(0,0);
    display.clearDisplay();
   }
   display.clearDisplay();
   while(buttons == 4) // loops code whilst button 3 is pressed
   {
    tm.displayText("CHE3LEE2");
    display << "Chelsea 3 - Leeds United 2" << endl;
    display.display();
    display.setCursor(0,0);
    display.clearDisplay();
   }
   display.clearDisplay();
   while(buttons == 8) // loops code whilst button 4 is pressed
   {
    tm.displayText("LIV1AVL0");
    display << "Liverpool FC 1 - Aston Villa 0" << endl;
    display.display();
    display.setCursor(0,0);
    display.clearDisplay();
   }
   display.clearDisplay();
   while(buttons == 16) // loops code whilst button 5 is pressed
   {
    tm.displayText("ARS3SOU0");
    display << "Arsenal 3 - Southampton 0" << endl;
    display.display();
    display.setCursor(0,0);
    display.clearDisplay();
   }
   display.clearDisplay();
   while(buttons == 32) // loops code whilst button 6 is pressed
   {
    tm.displayText("NOR0MAN1");
    display << "Norwich City 0 - Manchester United 1" << endl;
    display.display();
    display.setCursor(0,0);
    display.clearDisplay();
   }
   display.clearDisplay();
   while(buttons == 64) // loops code whilst button 7 is pressed
   {
    tm.displayText("LCC4NEW0");
    display << "Lecister City 4 - Newcastle United 0" << endl;
    display.display();
    display.setCursor(0,0);
    display.clearDisplay();
   }
   display.clearDisplay();
   while(buttons == 128) // loops code whilst button 8 is pressed
   {
    tm.displayText("CPY3EVE1");
    display << "Crystal Palace 3 - Everton 0" << endl;
    display.display();
    display.setCursor(0,0);
    display.clearDisplay();
   }
   display.clearDisplay();
  while(digitalRead(buttonPin)== 1); // is is for the 9th button works similar to the buttons on the 7seg and loops whilst button is held odwn 
  {
     tm.displayText("BUR0WHA0");
    display << "Burnley 0 - West Ham Untied 0" << endl;
    display.display();
    display.setCursor(0,0);
    display.clearDisplay();
  }
  }
    delay(250);} // delay before program resets
   



void loop() // main looping code of the program
{
  display.clearDisplay();
 display.setCursor(0,0);
 buttons(); // buttons function
  display.display();
 display.clearDisplay()
}
