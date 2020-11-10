package com.quarantino.ruto;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.quarantino.ruto.HelperClasses.DirectionHelpers.FetchURL;
import com.quarantino.ruto.HelperClasses.DirectionHelpers.TaskLoadedCallback;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesHelperClass;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap googleMap;
    private double currentLat, currentLong;
    private boolean roundYesNo;
    private Polyline currentPolyline;
    private ArrayList<NearbyPlacesHelperClass> selectedPlacesList = new ArrayList<>();

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

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setDismissWithAnimation(true);
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

        for (int i = 0; i < selectedPlacesList.size(); i++) {
            placeLat = selectedPlacesList.get(i).getPlaceLat();
            placeLong = selectedPlacesList.get(i).getPlaceLong();
            googleMap.addMarker(new MarkerOptions().position(new LatLng(placeLat, placeLong)).title("Stop " + (i + 1)).icon(bitmapFromVector(getApplicationContext(), R.drawable.map_marker)));
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
}