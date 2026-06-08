package com.example.electricitybillestimator_arif;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    TextView tvWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("About App");
        setContentView(R.layout.activity_about);

        tvWebsite = findViewById(R.id.tvWebsite);

        tvWebsite.setOnClickListener(v -> {
            String url = "https://github.com/yourusername/ElectricityBillEstimator";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }
}