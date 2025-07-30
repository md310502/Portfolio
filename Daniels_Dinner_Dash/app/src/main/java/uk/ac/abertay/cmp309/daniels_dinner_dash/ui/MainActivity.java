// By Myles Daniels 2104397
package uk.ac.abertay.cmp309.daniels_dinner_dash.ui;
// Application Arrives here
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import uk.ac.abertay.cmp309.daniels_dinner_dash.R;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper MyDatabaseHelper;
    Button map_screen;
    Button order_screen;
    Button login_screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity); // displays xml file

        map_screen = findViewById(R.id.map_screen); // finds map screen button
        map_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // loads up map when button is clicked
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);

                Toast.makeText(MainActivity.this, "Loading map", Toast.LENGTH_SHORT).show();
            }
        });

        order_screen = findViewById(R.id.order_screen); // finds order screen button
        order_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // loads up order screen on button click
                Intent intent = new Intent(MainActivity.this, OrderScreenActivity.class);
                startActivity(intent);

                Toast.makeText(MainActivity.this, "Loading Order Screen", Toast.LENGTH_SHORT).show();
            }
        });

        login_screen = findViewById(R.id.login_screen); // finds login screen button
        login_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // loads up login screen on button click
                Intent intent = new Intent(MainActivity.this, Loginscreen.class);
                startActivity(intent);

                Toast.makeText(MainActivity.this, "Loading Login Screen", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Delete the database when the app is closed
        MyDatabaseHelper.deleteDatabase();
    }
}