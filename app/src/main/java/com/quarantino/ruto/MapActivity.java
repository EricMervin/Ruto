package com.quarantino.ruto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.quarantino.ruto.Activities.MainDashboard;
import com.quarantino.ruto.HelperClasses.DirectionHelpers.FetchURL;
import com.quarantino.ruto.HelperClasses.DirectionHelpers.PointsParser;
import com.quarantino.ruto.HelperClasses.DirectionHelpers.TaskLoadedCallback;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesHelperClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap googleMap;
    private double currentLat, currentLong;
    private Polyline currentPolyline;
    private ArrayList<NearbyPlacesHelperClass> selectedPlacesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle bundle = getIntent().getExtras();
        selectedPlacesList = bundle.getParcelableArrayList("selectedPlaces");
        currentLat = getIntent().getDoubleExtra("Current Latitude", 0);
        currentLong = getIntent().getDoubleExtra("Current Longitude", 0);

        for(int i = 0; i < selectedPlacesList.size(); i++){
            selectedPlacesList.get(i).getNameOfPlace();
        }

        new FetchURL(MapActivity.this).execute(getUrl("driving"), "driving");

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;

        //Applying map styles
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getApplicationContext(), R.raw.mapstyle));
            if (!success) {
                Log.d("Map Error", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.d("Map Error", "Can't find style. Error: ", e);
        }

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(currentLat, currentLong), 10
        ));

        double placeLat, placeLong;

        googleMap.addMarker(new MarkerOptions().position(new LatLng(currentLat, currentLong)).title("Start/Stop").icon(bitmapFromVector(getApplicationContext(), R.drawable.map_marker)));

        for(int i = 0; i < selectedPlacesList.size(); i++){
            placeLat = selectedPlacesList.get(i).getPlaceLat();
            placeLong = selectedPlacesList.get(i).getPlaceLong();
            googleMap.addMarker(new MarkerOptions().position(new LatLng(placeLat, placeLong)).title("Stop " + (i+1)).icon(bitmapFromVector(getApplicationContext(), R.drawable.map_marker)));
        }
    }

    private BitmapDescriptor bitmapFromVector(Context applicationContext, int map_marker) {
        Drawable vectorDrawable = ContextCompat.getDrawable(applicationContext, map_marker);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private String getUrl(String directionMode) {
        String strOrigin = "origin=" + currentLat + "," + currentLong;
        String strDest = "destination=" + selectedPlacesList.get(selectedPlacesList.size() - 1).getPlaceLat() + "," + selectedPlacesList.get(selectedPlacesList.size() - 1).getPlaceLong();
        String mode = "mode=" + directionMode;

        String waypoints = "";
        for (int i = 0; i < selectedPlacesList.size()-1; i++) {
            if (i == 0)
                waypoints = "waypoints=optimize:false|";
            if (i == selectedPlacesList.size() - 1) {
                waypoints += selectedPlacesList.get(i).getPlaceLat() + "," + selectedPlacesList.get(i).getPlaceLong();
            } else {
                waypoints += selectedPlacesList.get(i).getPlaceLat() + "," + selectedPlacesList.get(i).getPlaceLong()+ "|";
            }
        }

        String parameters = strOrigin + "&" + strDest + "&" + waypoints + "&" + mode;

        String url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters + "&key=" + getString(R.string.places_api_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();

        currentPolyline = googleMap.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}