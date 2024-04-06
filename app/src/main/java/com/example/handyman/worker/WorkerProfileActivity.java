package com.example.handyman.worker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handyman.ChatActivity;
import com.example.handyman.MainActivity;
import com.example.handyman.R;
import com.example.handyman.job.CreateJobActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

// -------------- to show worker profile and information --------------
public class WorkerProfileActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_profile);

        // -------------- get worker data from bundle --------------
        Bundle bundle = getIntent().getExtras();
        int workerId = bundle.getInt("id", -1);
        String workerName = bundle.getString("Name", "");
        String workerProfession = bundle.getString("Profession", "");
        String workerAddress = bundle.getString("Address", "");

        TextView textName = findViewById(R.id.textWorkerName);
        TextView textProfession = findViewById(R.id.textWorkerProfession);
        TextView textAddress = findViewById(R.id.textWorkerAddress);

        textName.setText(workerName);
        textProfession.setText(workerProfession);
        textAddress.setText(workerAddress);

        // -------------- google maps --------------
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        Button buttonCreateJob = findViewById(R.id.buttonCreateJob);
        buttonCreateJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkerProfileActivity.this, CreateJobActivity.class);
                intent.putExtra("workerId", workerId);

                // -------------- get owner id from SharedPreference --------------
                SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                intent.putExtra("ownerId", sharedPreferences.getInt("ownerId", -1));

                startActivity(intent);
            }
        });

    }


// -------------- google maps implementation --------------
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true); // enabling zoom controls for the user
        Bundle bundle = getIntent().getExtras();
        String workerAddress = bundle.getString("Address", "");

        updateMapWithAddress(workerAddress); // update the map with the provided address
    }
    private void updateMapWithAddress(String address) {
        LatLng location = getLocationFromAddress(this, address);

        if (location != null) { // move the camera to the specified location (address entered)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17.0f));
            mMap.addMarker(new MarkerOptions().position(location).title(address)); // adding a marker at the specific location
        } else {
            // if address doesn't exists
            Toast.makeText(this, "Could not find coordinates for the address", Toast.LENGTH_SHORT).show();
        }
    }

    private LatLng getLocationFromAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                return new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // ------------- for home button ----------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}