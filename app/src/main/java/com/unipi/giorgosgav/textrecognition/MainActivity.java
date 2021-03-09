package com.unipi.giorgosgav.textrecognition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int GET_CONTENT_REQUEST = 123;

    ImageButton loadPhotoButton, recognizeTextButton;
    ImageView imageView;
    ListView listView;
    Bitmap bitmapImage;
    List<String> textRecognitionList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadPhotoButton = findViewById(R.id.loadPhotoButton);
        recognizeTextButton = findViewById(R.id.recognizeTextButton);
        imageView = findViewById(R.id.imageView);
        listView = findViewById(R.id.listView);

        textRecognitionList = new ArrayList<>();
        adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, android.R.id.text1, textRecognitionList);
        listView.setAdapter(adapter); //"κολλάμε" τον adapter στη listview
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_CONTENT_REQUEST && resultCode == Activity.RESULT_OK && data.getData()!= null)
        {
            Uri imageUri = data.getData();
            try
            {
                bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
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
        InputImage image = InputImage.fromBitmap(bitmapImage, 0);
        TextRecognizer textRecognizer = TextRecognition.getClient();

        Task<Text> result =
                textRecognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                Toast.makeText(MainActivity.this, "Text recognition completed successfully", Toast.LENGTH_LONG).show();
                                String resultText = visionText.getText();
                                textRecognitionList.add(resultText);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, "Text recognition failed", Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                });
    }
}