package com.quarantino.ruto;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class NearbyPlaceTemplate extends AppCompatActivity {

    TextView nameOfPlace;
    RatingBar ratingOfPlace;
    ImageView photoOfPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_place_template);

        nameOfPlace = findViewById(R.id.placeName);
        ratingOfPlace = findViewById(R.id.placeRating);
        photoOfPlace = findViewById(R.id.placeImage);

        String nameOfPlaceStr = getIntent().getStringExtra("Name of Place");
        float ratingOfPlaceVal = getIntent().getFloatExtra("Rating of Place", 0);

        byte[] byteArray = getIntent().getExtras().getByteArray("Image");
        Bitmap imageOfPlace = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        ratingOfPlace.setRating(ratingOfPlaceVal);
        nameOfPlace.setText(nameOfPlaceStr);
        photoOfPlace.setImageBitmap(imageOfPlace);
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
