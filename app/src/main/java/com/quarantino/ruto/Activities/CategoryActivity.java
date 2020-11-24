package com.quarantino.ruto.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.quarantino.ruto.HelperClasses.JsonParser;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.CategoryPlacesAdapter;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesAdapter;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesHelperClass;
import com.quarantino.ruto.MainDashboardFragments.create_frag;
import com.quarantino.ruto.MainDashboardFragments.dashboard_frag;
import com.quarantino.ruto.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class CategoryActivity extends AppCompatActivity implements CategoryPlacesAdapter.OnCategoryPlaceListener {

    private RecyclerView categoryRecycler;
    private RecyclerView.Adapter categoryRecyclerAdapter;
    private String categoryString;
    private TextView categoryTitle;
    private ArrayList<NearbyPlacesHelperClass> categoryPlace = new ArrayList<>();

//    FusedLocationProviderClient fusedLocationProviderClient;

    private ProgressDialog loadingDialog;
    private double currentLat = 0, currentLong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categoryRecycler = findViewById(R.id.categoryRecycler);
        categoryTitle = findViewById(R.id.categoryType);

        categoryRecycler.setHasFixedSize(true);
        categoryRecycler.setLayoutManager(new GridLayoutManager(this,2));

//        categoryRecyclerAdapter = new CategoryPlacesAdapter(categoryPlace, this);

        //Data from last activity
        categoryString = getIntent().getStringExtra("Category Type");
        currentLat = getIntent().getDoubleExtra("Current Latitude", 0);
        currentLong = getIntent().getDoubleExtra("Current Longitude", 0);

        categoryTitle.setText(categoryString);
        categoryString = categoryString.toLowerCase();

        getNearbyPlaces(currentLat, currentLong);
    }

    public void goBack(View view) {
        onBackPressed();
    }

    @SuppressLint("StaticFieldLeak")
    private class nearbyPlaceTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new nearbyPlacesParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        URL url = new URL(string);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        String data = builder.toString();
        reader.close();
        return data;
    }

    @SuppressLint("StaticFieldLeak")
    private class nearbyPlacesParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParser jsonParser = new JsonParser();

            List<HashMap<String, String>> mapList = null;
            JSONObject object;
            try {
                object = new JSONObject(strings[0]);
                mapList = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            int cnt = 0;

            for (int i = 0; cnt < 16; i++) {
                HashMap<String, String> hashMapList = hashMaps.get(i);

                double placeLat, placeLong;

                if (hashMapList.get("lat") != null && hashMapList.get("lng") != null) {
                    placeLat = Double.parseDouble(hashMapList.get("lat"));
                    placeLong = Double.parseDouble(hashMapList.get("lng"));
                } else {
                    continue;
                }

                String name = hashMapList.get("name");
                String rating = hashMapList.get("rating");
                String placeId = hashMapList.get("place_id");
                String photoRef = hashMapList.get("photo_reference");

                try {
                    categoryPlace.add(
                            new NearbyPlacesHelperClass(new photoDownload().execute(photoRef, placeId).get(), name, Float.parseFloat(rating), placeId, placeLat, placeLong));
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                cnt++;
            }
            nearbyPlacesRecycler();
        }
    }

    private void getNearbyPlaces(double currentLat, double currentLong) {
        loadingDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        loadingDialog.setMessage("Finding places near you");
        loadingDialog.setIndeterminate(false);
        loadingDialog.show();

        String touristUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" + currentLat + "," + currentLong +
                "&radius=21000" + "&type=" + categoryString +
                "&key=" + getResources().getString(R.string.places_api_key);

        new nearbyPlaceTask().execute(touristUrl);
    }

    private void nearbyPlacesRecycler() {
        categoryRecyclerAdapter = new CategoryPlacesAdapter(categoryPlace, this);
        categoryRecycler.setAdapter(categoryRecyclerAdapter);
        loadingDialog.dismiss();
    }

    @SuppressLint("StaticFieldLeak")
    private class photoDownload extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String photoReference = strings[0];
            String placeId = strings[1];
            Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg);

            String urlPhoto = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1200&photoreference="
                    + photoReference + "&key=" + getResources().getString(R.string.places_api_key);

            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            String filePath = directory.getAbsolutePath();
            File myPath = new File(filePath, placeId + ".jpg");

            Bitmap bitmapPlace = null;
            try {
                bitmapPlace = BitmapFactory.decodeStream(new FileInputStream(myPath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if (bitmapPlace == null) {
                try {
                    InputStream in = new java.net.URL(urlPhoto).openStream();
                    Bitmap resultBitmap = BitmapFactory.decodeStream(in);

                    ContextWrapper cwNew = new ContextWrapper(getApplicationContext());
                    File directoryNew = cwNew.getDir("imageDir", Context.MODE_PRIVATE);
                    File myPathNew = new File(directoryNew, placeId + ".jpg");

                    FileOutputStream fos = null;

                    try {
                        fos = new FileOutputStream(myPathNew);
                        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    return resultBitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                return bitmapPlace;
            }

            return icon1;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

        }
    }

    @Override
    public void onNearbyPlaceClick(int position, TextView placeName, ImageView placePhoto, RatingBar placeRating, View imageOverlay) {
        Intent intent = new Intent(getApplicationContext(), NearbyPlaceTemplate.class);

        //Converting the photo of place to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        categoryPlace.get(position).getImageOfPlace().compress(Bitmap.CompressFormat.JPEG, 95, stream);
        byte[] byteArray = stream.toByteArray();

        intent.putExtra("Name of Place", categoryPlace.get(position).getNameOfPlace());
        intent.putExtra("Position", position);
        intent.putExtra("Image", byteArray);
        intent.putExtra("Current Latitude", currentLat);
        intent.putExtra("Current Longitude", currentLong);
        intent.putExtra("Latitude of Place", categoryPlace.get(position).getPlaceLat());
        intent.putExtra("Longitude of Place", categoryPlace.get(position).getPlaceLong());
        intent.putExtra("Id Of Place", categoryPlace.get(position).getIdOfPlace());
        intent.putExtra("Rating of Place", categoryPlace.get(position).getRating());

        Pair<View, String> p1 = Pair.create((View) placePhoto, "nearbyImageAnim");
        Pair<View, String> p2 = Pair.create((View) placeName, "nearbyTitleAnim");
//        Pair<View, String> p3 = Pair.create((View) placeRating, "nearbyRatingAnim");
        Pair<View, String> p4 = Pair.create((View) imageOverlay, "nearbyImageOverlayAnim");

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2, p4);

        startActivity(intent, optionsCompat.toBundle());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}