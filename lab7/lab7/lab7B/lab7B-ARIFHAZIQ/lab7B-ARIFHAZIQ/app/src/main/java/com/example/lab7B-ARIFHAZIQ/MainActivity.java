package com.example.lab7b_talib;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnCall = findViewById(R.id.btnCall);
        ImageButton btnMessage = findViewById(R.id.btnMessage);
        ImageButton btnWebsite = findViewById(R.id.btnWebsite);
        ImageButton btnEmail = findViewById(R.id.btnEmail);

        btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+60355435452"));
            startActivity(intent);
        });

        btnMessage.setOnClickListener(v -> {
            String phoneNumber = "+60132121704";
            String message = "Hello from my Android app!";

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(
                        "https://wa.me/" +
                                phoneNumber.replace("+", "") +
                                "?text=" + Uri.encode(message)
                ));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
            }
        });

        btnWebsite.setOnClickListener(v -> {
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://fskm.uitm.edu.my/v5/index.php/en/")
            );
            startActivity(intent);
        });

        btnEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:aihakim@uitm.edu.my"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry");
            intent.putExtra(Intent.EXTRA_TEXT, "Dear Sir/Madam,");
            startActivity(Intent.createChooser(intent, "Send Email"));
        });
    }
}