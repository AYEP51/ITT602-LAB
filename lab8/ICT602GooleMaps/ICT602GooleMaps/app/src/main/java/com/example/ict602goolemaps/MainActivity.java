package com.example.ict602goolemaps;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMap = findViewById(R.id.btnMap);

        btnMap.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            MapsActivity.class);

            startActivity(intent);
        });
    }
}