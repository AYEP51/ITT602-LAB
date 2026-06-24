package com.example.lab9b;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView resultText;
    Button openBrowserBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultText = findViewById(R.id.resultText);
        openBrowserBtn = findViewById(R.id.openBrowserBtn);
        backBtn = findViewById(R.id.backBtn);

        String result =
                getIntent().getStringExtra("result");

        resultText.setText(result);

        openBrowserBtn.setOnClickListener(v -> {

            Intent browser =
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(result));

            startActivity(browser);
        });

        backBtn.setOnClickListener(v -> finish());
    }
}