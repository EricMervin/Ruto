package com.quarantino.ruto.Activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quarantino.ruto.HelperClasses.DirectionHelpers.FetchURL;
import com.quarantino.ruto.HelperClasses.DirectionHelpers.TaskLoadedCallback;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.ItineraryAdapter;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesHelperClass;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesItineraryAdapter;
import com.quarantino.ruto.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, NearbyPlacesItineraryAdapter.OnNearbyPlaceRouteListener {

    private GoogleMap googleMap;
    private double currentLat, currentLong;
    private boolean roundYesNo;
    private Polyline currentPolyline;
    private ArrayList<NearbyPlacesHelperClass> selectedPlacesList = new ArrayList<>();
    private ArrayList<NearbyPlacesHelperClass> selectedPlacesListImage = new ArrayList<>();

    private RecyclerView itineraryRecycler;
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageButton expandButton;
    private RecyclerView.Adapter itineraryRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        selectedPlacesList = bundle.getParcelableArrayList("selectedPlaces");

        currentLat = getIntent().getDoubleExtra("Current Latitude", 0);
        currentLong = getIntent().getDoubleExtra("Current Longitude", 0);
        roundYesNo = getIntent().getExtras().getBoolean("Round Trip");

        new FetchURL(MapActivity.this).execute(getUrl(), "driving");

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);

        expandButton = findViewById(R.id.expandBottomButton);

        View bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    expandButton.setImageDrawable(getDrawable(R.drawable.expand_icon));
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    expandButton.setImageDrawable(getDrawable(R.drawable.collapse_icon));
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        for (int i = 0; i < selectedPlacesList.size(); i++) {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            String filePath = directory.getAbsolutePath();
            File myPath = new File(filePath, selectedPlacesList.get(i).getIdOfPlace() + ".jpg");

            Bitmap bitmapPlace = null;
            try {
                bitmapPlace = BitmapFactory.decodeStream(new FileInputStream(myPath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            selectedPlacesListImage.add(new NearbyPlacesHelperClass(
                    bitmapPlace,
                    selectedPlacesList.get(i).getNameOfPlace(),
                    selectedPlacesList.get(i).getOpenStatus(),
                    selectedPlacesList.get(i).getRating(),
                    selectedPlacesList.get(i).getIdOfPlace(),
                    selectedPlacesList.get(i).getPlaceLat(),
                    selectedPlacesList.get(i).getPlaceLong()
            ));
        }

        itineraryRecycler = findViewById(R.id.finalItinerary);
        itineraryRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        itineraryRecyclerAdapter = new ItineraryAdapter(selectedPlacesListImage);
        itineraryRecycler.setAdapter(itineraryRecyclerAdapter);

        Button openMaps = findViewById(R.id.openMapsBtn);
        openMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder uri;
                if(roundYesNo) {
                    uri = new StringBuilder("http://maps.google.com/maps?saddr=" + currentLat + "," + currentLong + "&daddr=" + selectedPlacesList.get(0).getPlaceLat() + "," + selectedPlacesList.get(0).getPlaceLong());
                    for(int i = 1; i < selectedPlacesList.size(); i++){
//                        Log.d("Place Name", selectedPlacesList.get(i).getNameOfPlace());
                        uri.append("+to:").append(selectedPlacesList.get(i).getPlaceLat()).append(",").append(selectedPlacesList.get(i).getPlaceLong());
                    }
                    uri.append("+to:").append(currentLat).append(",").append(currentLong);
                } else {
                    uri = new StringBuilder("http://maps.google.com/maps?saddr=" + currentLat + "," + currentLong + "&daddr=" + selectedPlacesList.get(0).getPlaceLat() + "," + selectedPlacesList.get(0).getPlaceLong());
                    for(int i = 1; i < selectedPlacesList.size(); i++){
//                        Log.d("Place Name", selectedPlacesList.get(i).getNameOfPlace());
                        uri.append("+to:").append(selectedPlacesList.get(i).getPlaceLat()).append(",").append(selectedPlacesList.get(i).getPlaceLong());
                    }
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString()));
                startActivity(intent);
            }
        });
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
        String placeName;

        if (roundYesNo)
            googleMap.addMarker(new MarkerOptions().position(new LatLng(currentLat, currentLong)).title("Start/Stop").icon(bitmapFromVector(getApplicationContext(), R.drawable.map_marker)));
        else
            googleMap.addMarker(new MarkerOptions().position(new LatLng(currentLat, currentLong)).title("Start").icon(bitmapFromVector(getApplicationContext(), R.drawable.map_marker)));

        for (int i = 0; i < selectedPlacesList.size(); i++) {
            placeLat = selectedPlacesList.get(i).getPlaceLat();
            placeLong = selectedPlacesList.get(i).getPlaceLong();
            placeName = selectedPlacesList.get(i).getNameOfPlace();
            googleMap.addMarker(new MarkerOptions().position(new LatLng(placeLat, placeLong)).title(placeName).icon(bitmapFromVector(getApplicationContext(), R.drawable.map_marker)));
        }
    }

    private BitmapDescriptor bitmapFromVector(Context applicationContext, int map_marker) {
        Drawable vectorDrawable = ContextCompat.getDrawable(applicationContext, map_marker);

        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private String getUrl() {
        int loopLen;
        String strDest;

        String strOrigin = "origin=" + currentLat + "," + currentLong;

        if (!roundYesNo) {
            strDest = "destination=" + selectedPlacesList.get(selectedPlacesList.size() - 1).getPlaceLat() + "," + selectedPlacesList.get(selectedPlacesList.size() - 1).getPlaceLong();
            loopLen = selectedPlacesList.size() - 1;
        } else {
            strDest = "destination=" + currentLat + "," + currentLong;
            loopLen = selectedPlacesList.size();
        }

        String mode = "mode=" + "driving";

        String wayPoints = "";
        for (int i = 0; i < loopLen; i++) {
            if (i == 0)
                wayPoints = "waypoints=optimize:false|";
            if (i == selectedPlacesList.size() - 1) {
                wayPoints += selectedPlacesList.get(i).getPlaceLat() + "," + selectedPlacesList.get(i).getPlaceLong();
            } else {
                wayPoints += selectedPlacesList.get(i).getPlaceLat() + "," + selectedPlacesList.get(i).getPlaceLong() + "|";
            }
        }

        String parameters = strOrigin + "&" + strDest + "&" + wayPoints + "&" + mode;

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
    public void onNearbyPlaceRouteClick(int position, TextView placeName) {

    }

    @Override
    public void onAddPlaceRouteClick(int position, boolean isAdded) {

    }

    @Override
    public void onBackPressed() {
//        final Intent intent = new Intent(getApplicationContext(), MainDashboard.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sure you want to exit?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), MainDashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.putParcelableArrayListExtra("lastPlaces", selectedPlacesList);

                saveData();
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("History", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(selectedPlacesList);
        editor.putString("History Of Places", json);
        editor.apply();
    }
}