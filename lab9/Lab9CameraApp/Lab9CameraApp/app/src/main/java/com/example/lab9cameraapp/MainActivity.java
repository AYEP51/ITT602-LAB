package com.example.lab9cameraapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    Button btnCamera, btnGallery;
    ImageView imageView;

    Uri photoUri;

    ActivityResultLauncher<Intent> cameraLauncher;
    ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        imageView = findViewById(R.id.imageView);

        cameraLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == RESULT_OK) {
                                imageView.setImageURI(photoUri);
                            }
                        });

        galleryLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == RESULT_OK
                                    && result.getData() != null) {

                                Uri selectedImage =
                                        result.getData().getData();

                                imageView.setImageURI(selectedImage);
                            }
                        });

        btnCamera.setOnClickListener(v -> openCamera());

        btnGallery.setOnClickListener(v -> openGallery());
    }

    private void openCamera() {

        File photoFile =
                new File(getExternalFilesDir(null),
                        "photo.jpg");

        photoUri =
                FileProvider.getUriForFile(
                        this,
                        getPackageName() + ".provider",
                        photoFile);

        Intent cameraIntent =
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        cameraIntent.putExtra(
                MediaStore.EXTRA_OUTPUT,
                photoUri);

        cameraLauncher.launch(cameraIntent);
    }

    private void openGallery() {

        Intent galleryIntent =
                new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        galleryLauncher.launch(galleryIntent);
    }
}