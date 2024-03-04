package com.example.handyman.worker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handyman.R;
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

public class WorkerProfileActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_profile);

        Bundle bundle = getIntent().getExtras();

        String workerName = bundle.getString("Name", "");
        String workerProfession = bundle.getString("Profession", "");
        String workerAddress = bundle.getString("Address", "");

        TextView textName = findViewById(R.id.textWorkerName);
        TextView textProfession = findViewById(R.id.textWorkerProfession);
        TextView textAddress = findViewById(R.id.textWorkerAddress);

        textName.setText(workerName);
        textProfession.setText(workerProfession);
        textAddress.setText(workerAddress);

        // Google maps extension here
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Enabling zoom controls for the user
        mMap.getUiSettings().setZoomControlsEnabled(true);
        Bundle bundle = getIntent().getExtras();
        String workerAddress = bundle.getString("Address", "");

        // Update the map with the provided address
        updateMapWithAddress(workerAddress);
    }
    private void updateMapWithAddress(String address) {
        LatLng location = getLocationFromAddress(this, address);

        // Move the camera to the specified location (address entered)
        if (location != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17.0f));
            // Adding a marker at the specific location
            mMap.addMarker(new MarkerOptions().position(location).title(address));
        } else {
            // Handle the case where the address couldn't be converted to coordinates
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

}