package com.example.lab9b;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnCamera;
    Button btnGallery;

    ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCamera = findViewById(R.id.cameraBtn);
        btnGallery = findViewById(R.id.galleryBtn);

        galleryLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {

                            if (result.getResultCode() == RESULT_OK &&
                                    result.getData() != null) {

                                Intent intent =
                                        new Intent(
                                                MainActivity.this,
                                                ResultActivity.class);

                                intent.putExtra(
                                        "result",
                                        "Image Selected Successfully");

                                startActivity(intent);
                            }
                        });

        btnGallery.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            galleryLauncher.launch(intent);
        });

        btnCamera.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            ScannerActivity.class);

            startActivity(intent);
        });
    }
}