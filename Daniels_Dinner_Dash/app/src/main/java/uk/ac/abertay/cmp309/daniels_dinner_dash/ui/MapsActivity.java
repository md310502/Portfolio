package uk.ac.abertay.cmp309.daniels_dinner_dash.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import uk.ac.abertay.cmp309.daniels_dinner_dash.R;
import uk.ac.abertay.cmp309.daniels_dinner_dash.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int MARKER_CLICK_DELAY = 2000; // 2 seconds delay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Marker listener for fictitious restaurants
        mMap.setOnMarkerClickListener(this);

        // Markers for other restaurant locations
        LatLng kims_pizza = new LatLng(56.455831618171835, -2.991433208091635);
        LatLng Toms_fish_and_chips = new LatLng(56.456289539028354, -2.9867744234212092);
        LatLng micheals_sushi = new LatLng(56.46061777571568, -2.969055522274517);
        LatLng golden_wok = new LatLng(56.46214257502184, -2.9717764644753384);
        LatLng muhgal_palace = new LatLng(56.45942042093028, -2.970583447438804);
        LatLng mikes_kebabs = new LatLng(56.463010540015496, -2.967138884750841);

        // Add markers for restaurants
        mMap.addMarker(new MarkerOptions().position(kims_pizza).title("Toni's Pizza").snippet("Best pizza in Dundee!"));
        mMap.addMarker(new MarkerOptions().position(Toms_fish_and_chips).title("Toms fish and chips").snippet("Fresh and tasty Battered fish"));
        mMap.addMarker(new MarkerOptions().position(micheals_sushi).title("Micheal's Sushi").snippet("Authentic Japanese sushi."));
        mMap.addMarker(new MarkerOptions().position(golden_wok).title("Golden Wok").snippet("Best Chinese food in Tayside."));
        mMap.addMarker(new MarkerOptions().position(muhgal_palace).title("Mughal Palace").snippet("No1 Curry House in Dundee."));
        mMap.addMarker(new MarkerOptions().position(mikes_kebabs).title("Mikes Kebabs").snippet("Reasonably Priced Kebabs"));


        // Checks location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // Enable user location
        mMap.setMyLocationEnabled(true);

        // Gets user location
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                    }
                });
    }

    @Override // once permissions have been received, grabs permissions
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);

                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(this, location -> {
                                if (location != null) {
                                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                                }
                            });
                }
            } else {
                Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override // to access the order screen once a marker has been clicked
    public boolean onMarkerClick(final Marker marker) {
        // Delay before opening the order screen
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MapsActivity.this, OrderScreenActivity.class);
            intent.putExtra("restaurant_name", marker.getTitle());
            startActivity(intent);
        }, MARKER_CLICK_DELAY);
        return true; // Return true to indicate that we have handled the click
    }
}


