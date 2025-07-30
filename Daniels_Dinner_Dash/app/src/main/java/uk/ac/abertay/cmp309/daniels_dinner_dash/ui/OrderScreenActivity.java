package uk.ac.abertay.cmp309.daniels_dinner_dash.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.abertay.cmp309.daniels_dinner_dash.R;

public class OrderScreenActivity extends Activity {

    private static final int SMS_PERMISSION_CODE = 100; // sms permissions
    private MyDatabaseHelper myDatabaseHelper; // for sms text
    private RecyclerView recyclerView;
    private OrderScreenAdapter adapter;
    private List<OrderItem> orderItems;
    private List<OrderItem> selectedItems; // List to store selected items
    private Spinner basketSpinner;
    private String userId; // Placeholder for user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_screen);

        myDatabaseHelper = new MyDatabaseHelper(this);
        recyclerView = findViewById(R.id.recycler_view);
        Button confirmButton = findViewById(R.id.confirm_order_button);
        basketSpinner = findViewById(R.id.basket_spinner);

        // Get userId from intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("USER_ID");

        orderItems = loadOrderItems(); // Load the restaurant and menu items directly + other related items
        selectedItems = new ArrayList<>(); // Initialize selected items list

        adapter = new OrderScreenAdapter(orderItems, new OrderScreenAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OrderItem item) {
                // Add item to selected items list and update spinner
                selectedItems.add(item);
                updateBasketSpinner();
                Toast.makeText(OrderScreenActivity.this, "Selected: " + item.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Populate Spinner
        updateBasketSpinner();

        // Request SMS permission
        if (!hasSmsPermission()) {
            requestSmsPermission();
        }

        confirmButton.setOnClickListener(new View.OnClickListener() { // for confirm order button in xml
            @Override
            public void onClick(View v) {
                Log.d("OrderScreenActivity", "Confirm button clicked");
                String phoneNumber = myDatabaseHelper.getPhoneNumber(userId);
                Log.d("OrderScreenActivity", "Fetched phone number: " + phoneNumber);
                if (!isValidPhoneNumber(phoneNumber)) {
                    Toast.makeText(OrderScreenActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show(); // section here is for data validation
                    Log.e("OrderScreenActivity", "Invalid phone number: " + phoneNumber);
                    return;
                }
                Log.d("OrderScreenActivity", "Phone number is valid");

                if (isUserLoggedIn() && hasSmsPermission()) {
                    sendConfirmationText(userId);  // Use actual user ID
                } else if (!isUserLoggedIn()) {
                    Toast.makeText(OrderScreenActivity.this, "User not logged in. Please log in first.", Toast.LENGTH_SHORT).show(); // this is for login validation
                } else {
                    Toast.makeText(OrderScreenActivity.this, "SMS permission not granted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isUserLoggedIn() {
        String phoneNumber = myDatabaseHelper.getPhoneNumber(userId);
        boolean isLoggedIn = phoneNumber != null && isValidPhoneNumber(phoneNumber); // further validation
        Log.d("OrderScreenActivity", "User logged in: " + isLoggedIn);
        return isLoggedIn;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        boolean isValid = !TextUtils.isEmpty(phoneNumber) && Patterns.PHONE.matcher(phoneNumber).matches() && phoneNumber.length() >= 10; // relates to phone validation
        Log.d("OrderScreenActivity", "Phone number validation: " + isValid);
        return isValid;
    }

    private void updateBasketSpinner() {
        List<String> basket = new ArrayList<>(); // for updating the spinner with new items clicked
        for (OrderItem item : selectedItems) {
            basket.add(item.getName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, basket);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        basketSpinner.setAdapter(spinnerAdapter);
    }



    private List<OrderItem> loadOrderItems() {
        // Directly loading restaurant and menu items
        List<OrderItem> items = new ArrayList<>();

        // Adding Kim's Pizza items
        items.add(new OrderItem("Kim's Pizza - Spaghetti Carbonara", 8.99));
        items.add(new OrderItem("Kim's Pizza - Margarita Pizza", 10.99));
        items.add(new OrderItem("Kim's Pizza - Peperoni Pizza", 12.99));
        items.add(new OrderItem("Kim's Pizza - Meat Feast Pizza", 15.99));
        items.add(new OrderItem("Kim's Pizza - Veggie Pizza", 12.99));
        items.add(new OrderItem("Kim's Pizza - Garlic Chips", 4.99));
        items.add(new OrderItem("Kim's Pizza - Cola", 1.49));

        // Adding Tom's Fish and Chips items
        items.add(new OrderItem("Tom's Fish and Chips - Cod and Chips", 11.99));
        items.add(new OrderItem("Tom's Fish and Chips - Scampi and Chips", 10.99));
        items.add(new OrderItem("Tom's Fish and Chips - Battered Sausage", 8.99));
        items.add(new OrderItem("Tom's Fish and Chips - Battered Sausage and Chips", 10.99));
        items.add(new OrderItem("Tom's Fish and Chips - Chips", 2.99));
        items.add(new OrderItem("Tom's Fish and Chips - Cola", 1.39));

        // Adding Michael's Sushi items
        items.add(new OrderItem("Michael's Sushi - Sushi Platter", 15.99));
        items.add(new OrderItem("Michael's Sushi - California Roll 8 Piece", 10.99));
        items.add(new OrderItem("Michael's Sushi - Mako Roll 8 Piece", 6.99));
        items.add(new OrderItem("Michael's Sushi - Salmon Roll 8 piece", 5.99));
        items.add(new OrderItem("Michael's Sushi - Avocado Roll 8 piece", 4.99));
        items.add(new OrderItem("Michael's Sushi - Cola", 1.49));

        // Adding Golden Wok items
        items.add(new OrderItem("Golden Wok - Chicken Chow Mein with Rice", 15.99));
        items.add(new OrderItem("Golden Wok - Sweet and Sour Chicken", 10.99));
        items.add(new OrderItem("Golden Wok - Egg Fried Rice", 3.99));
        items.add(new OrderItem("Golden Wok - Chicken and Broccoli", 10.99));
        items.add(new OrderItem("Golden Wok - Shrimp Fried Rice", 5.99));
        items.add(new OrderItem("Golden Wok - Cola", 1.39));

        // Adding Mughal Palace items
        items.add(new OrderItem("Mughal Palace - Chicken Tika Masala", 15.99));
        items.add(new OrderItem("Mughal Palace - Lamb Tika Masala", 10.99));
        items.add(new OrderItem("Mughal Palace - Chicken Tandoori", 15.99));
        items.add(new OrderItem("Mughal Palace - Lamb Tandoori", 10.99));
        items.add(new OrderItem("Mughal Palace - Chicken Vindaloo", 15.99));
        items.add(new OrderItem("Mughal Palace - Pilau Rice", 3.99));

        // Adding Mike's Kebabs items
        items.add(new OrderItem("Mike's Kebabs - Chicken Donner Kebab with Rice", 8.99));
        items.add(new OrderItem("Mike's Kebabs - Lamb Donner Kebab with Rice", 7.99));
        items.add(new OrderItem("Mike's Kebabs - Chicken Shisk Kebab", 9.99));
        items.add(new OrderItem("Mike's Kebabs - Mixed Shisk Kebab", 8.99));
        items.add(new OrderItem("Mike's Kebabs - Quarter Pound Burger with Chips", 5.99));
        items.add(new OrderItem("Mike's Kebabs - Chips", 2.99));

        return items;
    }

    // sends confirmation text once completed
    private void sendConfirmationText(String userId) {
        Log.d("OrderScreenActivity", "Attempting to fetch phone number for user: " + userId);
        String phoneNumber = myDatabaseHelper.getPhoneNumber(userId);
        if (phoneNumber == null || !isValidPhoneNumber(phoneNumber)) {
            Log.e("OrderScreenActivity", "Invalid phone number for user: " + userId);
            Toast.makeText(getApplicationContext(), "Invalid phone number. Please log in first.", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d("OrderScreenActivity", "Phone number found: " + phoneNumber);
        String message = "Your order has been confirmed!";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "Order confirmed and SMS sent!", Toast.LENGTH_LONG).show(); // confirmation message in the app
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean hasSmsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED; // checks for sms permissions
    }

    private void requestSmsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permission granted", Toast.LENGTH_SHORT).show(); // requests permissions
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


