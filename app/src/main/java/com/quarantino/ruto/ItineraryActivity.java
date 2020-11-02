package com.quarantino.ruto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesCreateFragAdapter;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesHelperClass;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesItineraryAdapter;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.SelectedPlacesAdapter;
import com.quarantino.ruto.HelperClasses.ReviewAdapter.ReviewAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ItineraryActivity extends AppCompatActivity implements NearbyPlacesItineraryAdapter.OnNearbyPlaceRouteListener{

    private RecyclerView selectedPlacesRecycler;
    private RecyclerView.Adapter selectedPlacesRecyclerAdapter;
//    private ArrayList<NearbyPlacesHelperClass> selectedPlacesListNoImage = new ArrayList<>();
    private ArrayList<NearbyPlacesHelperClass> selectedPlacesList = new ArrayList<>();

    FusedLocationProviderClient fusedLocationProviderClient;

    private double userCurrentLat = 0, userCurrentLong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);

        Bundle bundle = getIntent().getExtras();
        selectedPlacesList = bundle.getParcelableArrayList("selectedPlaces");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

//        for(int i = 0; i < selectedPlacesListNoImage.size(); i++){
//            selectedPlacesList.add(new NearbyPlacesHelperClass(
//                    BitmapFactory.decodeResource(getResources(), R.drawable.bg),
//                    selectedPlacesListNoImage.get(i).getNameOfPlace(),
//                    selectedPlacesListNoImage.get(i).getOpenStatus(),
//                    selectedPlacesListNoImage.get(i).getRating(),
//                    selectedPlacesListNoImage.get(i).getIdOfPlace(),
//                    selectedPlacesListNoImage.get(i).getPlaceLat(),
//                    selectedPlacesListNoImage.get(i).getPlaceLong()
//            ));
//        }

        selectedPlacesRecycler = findViewById(R.id.itineraryRecycler);
        selectedPlacesRecycler.setHasFixedSize(true);
        selectedPlacesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        selectedPlacesRecyclerAdapter = new NearbyPlacesItineraryAdapter(selectedPlacesList , this);
        selectedPlacesRecycler.setAdapter(selectedPlacesRecyclerAdapter);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    userCurrentLat = location.getLatitude();
                    userCurrentLong = location.getLongitude();
//                    getNearbyRestaurants(currentLat, currentLong);
                }
            }
        });
    }

    public void generateRoute(View view) {
    }

    public void openPlace(View view) {
        onBackPressed();
    }

    @Override
    public void onNearbyPlaceRouteClick(int position, TextView placeName) {
        Intent intent = new Intent(this, NearbyPlaceTemplate.class);

        //Converting the photo of place to byte array
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.bg);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        //Passing data to the next activity
        intent.putExtra("Name of Place", selectedPlacesList.get(position).getNameOfPlace());
        intent.putExtra("Position", position);
        intent.putExtra("Image", byteArray);
        intent.putExtra("Current Latitude", userCurrentLat);
        intent.putExtra("Current Longitude", userCurrentLong);
        intent.putExtra("Latitude of Place", selectedPlacesList.get(position).getPlaceLat());
        intent.putExtra("Longitude of Place", selectedPlacesList.get(position).getPlaceLong());
        intent.putExtra("Id Of Place", selectedPlacesList.get(position).getIdOfPlace());
        intent.putExtra("Rating of Place", selectedPlacesList.get(position).getRating());

//        Pair<View, String> p1 = Pair.create((View) placePhoto, "nearbyImageAnim");
        Pair<View, String> p2 = Pair.create((View) placeName, "nearbyTitleAnim");
//        Pair<View, String> p3 = Pair.create((View) placeRating, "nearbyRatingAnim");
//        Pair<View, String> p4 = Pair.create((View) imageOverlay, "nearbyImageOverlayAnim");

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p2);

        startActivity(intent, optionsCompat.toBundle());
    }

    @Override
    public void onAddPlaceRouteClick(int position, boolean isAdded) {
        selectedPlacesList.remove(position);
        selectedPlacesRecyclerAdapter.notifyItemRemoved(position);
        selectedPlacesRecyclerAdapter.notifyItemRangeRemoved(position, 1);
    }
}