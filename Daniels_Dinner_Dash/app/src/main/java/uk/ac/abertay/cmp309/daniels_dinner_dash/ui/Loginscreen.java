package uk.ac.abertay.cmp309.daniels_dinner_dash.ui;

// imports required
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import uk.ac.abertay.cmp309.daniels_dinner_dash.R;

public class Loginscreen extends AppCompatActivity {

    EditText editPhoneNumber, editPassword;
    Button btnLogin;
    MyDatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        myDb = new MyDatabaseHelper(this);

        editPhoneNumber = findViewById(R.id.phonenumber);
        editPassword = findViewById(R.id.passwordinput);
        btnLogin = findViewById(R.id.button_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle login button click
                String phoneNumber = editPhoneNumber.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                // Placeholder ID
                String userId = "0001";

                // For Insertion of Data
                boolean isInserted = myDb.insertData(userId, phoneNumber, password, "order_package_example");

                if (isInserted) {
                    Toast.makeText(Loginscreen.this, "User inserted successfully", Toast.LENGTH_SHORT).show();
                    // Pass the userId to the next activity
                    Intent intent = new Intent(Loginscreen.this, OrderScreenActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                } else {
                    Toast.makeText(Loginscreen.this, "Failed to insert user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


