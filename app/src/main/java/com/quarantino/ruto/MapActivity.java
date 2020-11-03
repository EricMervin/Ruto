package com.quarantino.ruto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesHelperClass;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    private ArrayList<NearbyPlacesHelperClass> selectedPlacesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle bundle = getIntent().getExtras();
        selectedPlacesList = bundle.getParcelableArrayList("selectedPlaces");


    }
}