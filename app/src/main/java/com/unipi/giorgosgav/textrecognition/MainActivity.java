package com.unipi.giorgosgav.textrecognition;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final int GET_CONTENT_REQUEST = 123;

    ImageButton loadPhotoButton, recognizeTextButton;
    ImageView imageView;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadPhotoButton = findViewById(R.id.loadPhotoButton);
        recognizeTextButton = findViewById(R.id.recognizeTextButton);
        imageView = findViewById(R.id.imageView);
        listView = findViewById(R.id.listView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_CONTENT_REQUEST && resultCode == Activity.RESULT_OK && data.getData()!= null)
        {
            Uri imageUri = data.getData();
            try
            {
                Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmapImage);
            }
            catch(IOException e)
            {
                Toast.makeText(this, "An error occured while loading the image", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void loadPhoto(View view)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GET_CONTENT_REQUEST);
    }

    public void recognizeText(View view)
    {

    }
}