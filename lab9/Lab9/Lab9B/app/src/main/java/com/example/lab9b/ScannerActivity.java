package com.example.lab9b;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ScannerActivity extends AppCompatActivity {

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(
                    new ScanContract(),
                    result -> {

                        if (result.getContents() != null) {

                            Intent intent =
                                    new Intent(
                                            ScannerActivity.this,
                                            ResultActivity.class);

                            intent.putExtra(
                                    "result",
                                    result.getContents());

                            startActivity(intent);
                            finish();
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan QR Code");
        options.setBeepEnabled(true);

        barcodeLauncher.launch(options);
    }
}