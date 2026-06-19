package com.example.ict602goolemaps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private GoogleMap mMap;

    Button btnNormal;
    Button btnSatellite;
    Button btnNavigate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        btnNormal = findViewById(R.id.btnNormal);
        btnSatellite = findViewById(R.id.btnSatellite);
        btnNavigate = findViewById(R.id.btnNavigate);

        SupportMapFragment mapFragment =
                (SupportMapFragment)
                        getSupportFragmentManager()
                                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnNormal.setOnClickListener(v -> {
            if (mMap != null) {
                mMap.setMapType(
                        GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        btnSatellite.setOnClickListener(v -> {
            if (mMap != null) {
                mMap.setMapType(
                        GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        btnNavigate.setOnClickListener(v -> {

            Uri gmmIntentUri =
                    Uri.parse(
                            "google.navigation:q=2.2216,102.4538");

            Intent mapIntent =
                    new Intent(
                            Intent.ACTION_VIEW,
                            gmmIntentUri);

            mapIntent.setPackage(
                    "com.google.android.apps.maps");

            startActivity(mapIntent);
        });
    }

    @Override
    public void onMapReady(
            @NonNull GoogleMap googleMap) {

        mMap = googleMap;

        LatLng uitm =
                new LatLng(
                        2.2216,
                        102.4538);

        LatLng library =
                new LatLng(
                        2.2205,
                        102.4525);

        LatLng mosque =
                new LatLng(
                        2.2230,
                        102.4550);

        mMap.addMarker(
                new MarkerOptions()
                        .position(uitm)
                        .title("UiTM Jasin"));

        mMap.addMarker(
                new MarkerOptions()
                        .position(library)
                        .title("Library"));

        mMap.addMarker(
                new MarkerOptions()
                        .position(mosque)
                        .title("Mosque"));

        mMap.moveCamera(
                CameraUpdateFactory
                        .newLatLngZoom(
                                uitm,
                                15));
    }
}