package com.quarantino.ruto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class ItineraryActivity extends AppCompatActivity implements NearbyPlacesItineraryAdapter.OnNearbyPlaceRouteListener{

    private RecyclerView selectedPlacesRecycler;
    private RecyclerView.Adapter selectedPlacesRecyclerAdapter;
    private ArrayList<NearbyPlacesHelperClass> selectedPlacesListNoImage = new ArrayList<>();
    private ArrayList<NearbyPlacesHelperClass> selectedPlacesList = new ArrayList<>();

    FusedLocationProviderClient fusedLocationProviderClient;

    private double userCurrentLat = 0, userCurrentLong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);

        Bundle bundle = getIntent().getExtras();
        selectedPlacesListNoImage = bundle.getParcelableArrayList("selectedPlaces");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        for(int i = 0; i < selectedPlacesListNoImage.size(); i++){

            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            String filePath = directory.getAbsolutePath();
            File myPath = new File(filePath, selectedPlacesListNoImage.get(i).getIdOfPlace() + ".jpg");

            Bitmap bitmapPlace = null;
            try {
                bitmapPlace = BitmapFactory.decodeStream(new FileInputStream(myPath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            selectedPlacesList.add(new NearbyPlacesHelperClass(
                    bitmapPlace,
                    selectedPlacesListNoImage.get(i).getNameOfPlace(),
                    selectedPlacesListNoImage.get(i).getOpenStatus(),
                    selectedPlacesListNoImage.get(i).getRating(),
                    selectedPlacesListNoImage.get(i).getIdOfPlace(),
                    selectedPlacesListNoImage.get(i).getPlaceLat(),
                    selectedPlacesListNoImage.get(i).getPlaceLong()
            ));
        }

        selectedPlacesRecycler = findViewById(R.id.itineraryRecycler);
        selectedPlacesRecycler.setHasFixedSize(true);
        selectedPlacesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        selectedPlacesRecyclerAdapter = new NearbyPlacesItineraryAdapter(selectedPlacesList , this);
        selectedPlacesRecycler.setAdapter(selectedPlacesRecyclerAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(selectedPlacesRecycler);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(selectedPlacesList, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

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
        final Boolean[] roundYesNo = {true};
        ArrayList<NearbyPlacesHelperClass> selectedPlacesListComp = new ArrayList<>();

        for (int i = 0; i < selectedPlacesList.size(); i++) {
            selectedPlacesListComp.add(new NearbyPlacesHelperClass(
                    selectedPlacesList.get(i).getNameOfPlace(),
                    selectedPlacesList.get(i).getOpenStatus(),
                    selectedPlacesList.get(i).getRating(),
                    selectedPlacesList.get(i).getIdOfPlace(),
                    selectedPlacesList.get(i).getPlaceLat(),
                    selectedPlacesList.get(i).getPlaceLong()
            ));
        }
        final Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        intent.putParcelableArrayListExtra("selectedPlaces", selectedPlacesListComp);
        intent.putExtra("Current Latitude", userCurrentLat);
        intent.putExtra("Current Longitude", userCurrentLong);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Round Trip");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                intent.putExtra("Round Trip", roundYesNo[0]);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                roundYesNo[0] = false;
                intent.putExtra("Round Trip", roundYesNo[0]);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void openPlace(View view) {
        onBackPressed();
    }

    @Override
    public void onNearbyPlaceRouteClick(int position, TextView placeName) {
        Intent intent = new Intent(this, NearbyPlaceTemplate.class);

        //Converting the photo of place to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedPlacesList.get(position).getImageOfPlace().compress(Bitmap.CompressFormat.JPEG, 95, stream);
        byte[] byteArray = stream.toByteArray();

        //Passing data to the next activity
        intent.putExtra("Name of Place", selectedPlacesList.get(position).getNameOfPlace());
        intent.putExtra("Position", position);
        intent.putExtra("Image", byteArray);
        intent.putExtra("Open Status", selectedPlacesList.get(position).getOpenStatus());
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

//        startActivity(intent, optionsCompat.toBundle());
    }

    @Override
    public void onAddPlaceRouteClick(int position, boolean isAdded) {
        selectedPlacesList.remove(position);
        selectedPlacesRecyclerAdapter.notifyItemRemoved(position);
        selectedPlacesRecyclerAdapter.notifyItemRangeRemoved(position, 1);
    }
}