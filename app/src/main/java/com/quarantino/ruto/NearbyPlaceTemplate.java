package com.quarantino.ruto;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.quarantino.ruto.HelperClasses.JsonParserPlace;
import com.quarantino.ruto.HelperClasses.ReviewAdapter.ReviewAdapter;
import com.quarantino.ruto.HelperClasses.ReviewAdapter.ReviewHelperClass;
import com.uber.sdk.android.core.Deeplink;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestDeeplink;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.core.client.SessionConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class NearbyPlaceTemplate extends AppCompatActivity {

    private SupportMapFragment supportMapFragment;
    private GoogleMap map;

    private RideParameters rideParameters;
    private SessionConfiguration configuration;
    private RideRequestDeeplink deeplink;

    private RecyclerView reviewRecycler;
    private RecyclerView.Adapter reviewRecyclerAdapter;
    private final ArrayList<ReviewHelperClass> reviewOfPlace = new ArrayList<>();

    private TextView nameOfPlace, openStatus, phoneNumber, cityOfPlace;
    private RatingBar ratingOfPlace;
    private ImageView photoOfPlace;
//    private RideRequestButton rideRequestButton;

    private String nameOfPlaceStr, cityPlace;
    private float ratingOfPlaceVal;
    private double placeLat, placeLng, currentLat, currentLong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_place_template);

        openStatus = findViewById(R.id.openingHours);
        phoneNumber = findViewById(R.id.phoneNumber);
        nameOfPlace = findViewById(R.id.placeName);
        ratingOfPlace = findViewById(R.id.placeRating);
        photoOfPlace = findViewById(R.id.placeImage);
        cityOfPlace = findViewById(R.id.cityOfPlace);

        reviewRecycler = findViewById(R.id.reviewRecycler);
        reviewRecycler.setHasFixedSize(true);
        reviewRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locationMap);

        //Data from last activity
        nameOfPlaceStr = getIntent().getStringExtra("Name of Place");
        ratingOfPlaceVal = getIntent().getFloatExtra("Rating of Place", 0);
        currentLat = getIntent().getDoubleExtra("Current Latitude", 0);
        currentLong = getIntent().getDoubleExtra("Current Longitude", 0);
        placeLat = getIntent().getDoubleExtra("Latitude of Place", 0);
        placeLng = getIntent().getDoubleExtra("Longitude of Place", 0);

//        openStatus.setText(getIntent().getStringExtra("Open Status"));

        byte[] byteArray = getIntent().getExtras().getByteArray("Image");
        Bitmap imageOfPlace = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        String idOfPlace = getIntent().getStringExtra("Id Of Place");

        ratingOfPlace.setRating(ratingOfPlaceVal);
        nameOfPlace.setText(nameOfPlaceStr);
        photoOfPlace.setImageBitmap(imageOfPlace);

        getPlaceDetails(idOfPlace);
    }

    private void drawMap() {
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.clear();
                map.getUiSettings().setScrollGesturesEnabled(false);
                map.getUiSettings().setZoomGesturesEnabled(false);
                map.getUiSettings().setZoomControlsEnabled(false);

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

                LatLng latLng = new LatLng(placeLat, placeLng);
                MarkerOptions options = new MarkerOptions().position(latLng)
                        .title(nameOfPlaceStr).icon(bitmapFromVector(getApplicationContext(), R.drawable.map_marker));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(placeLat, placeLng), 12
                ));
                map.addMarker(options);
            }
        });
    }

    private BitmapDescriptor bitmapFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void getPlaceDetails(String placeId) {

        // https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJ-yw3J0bkDDkRMTxIHAK_Ous&key=AIzaSyA5L81_-5d2Hy7hHsNVhodk1zS90Qu-aP8

        String url = "https://maps.googleapis.com/maps/api/place/details/json" +
                "?placeid=" + placeId +
                "&key=" + getResources().getString(R.string.places_api_key);

        Log.d("Json URL", url);

        new PlaceTask().execute(url);
    }

    public void bookUber(View view) {
        Log.d("Uber Status", "Uber Booked");
        startActivity(new Intent(Intent.ACTION_VIEW, deeplink.getUri()));
    }

    public void bookmarkPlace(View view) {
        onBackPressed();
        Log.d("Bookmark Pressed", "Added Bookmark");
    }

    private class PlaceTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            rideParameters = new RideParameters.Builder()
                    .setPickupToMyLocation()
                    .setDropoffLocation(placeLat, placeLng, cityPlace, "Place Address")
                    .build();

            configuration = new SessionConfiguration.Builder()
                    .setClientId(getResources().getString(R.string.uberClientId))
                    .setRedirectUri("com.quarantino.ruto://redirect")
                    .setScopes(Arrays.asList(Scope.RIDE_WIDGETS))
                    .setEnvironment(SessionConfiguration.Environment.PRODUCTION)
                    .build();

            UberSdk.initialize(configuration);

            deeplink = new RideRequestDeeplink.Builder(getApplicationContext())
                    .setSessionConfiguration(configuration)
                    .setFallback(Deeplink.Fallback.MOBILE_WEB)
                    .setRideParameters(rideParameters)
                    .build();

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new placeDetailsTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        URL url = new URL(string);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line = "";

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        String data = builder.toString();
        reader.close();

        Log.d("Process", "Url downloaded");
        Log.d("Data downloaded", data);

        return data;
    }

    private class placeDetailsTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParserPlace jsonParser = new JsonParserPlace();

            List<HashMap<String, String>> mapList = null;
            JSONObject object = null;
            try {
                object = new JSONObject(strings[0]);
                mapList = jsonParser.parseResult(object);
                Log.d("JSON Parsed", "Positive");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            HashMap<String, String> hashMapList = hashMaps.get(0);

            String name = hashMapList.get("name");
            cityPlace = hashMapList.get("city_place");

            if (cityPlace != null) {
                cityOfPlace.setText(cityPlace);
            } else {
//                findViewById(R.id.locationIcon).setVisibility(View.INVISIBLE);
                cityOfPlace.setText("Unavailable");
            }

            String contactDetails = hashMapList.get("contact");
            if (contactDetails != null) {
                phoneNumber.setText(contactDetails);
            }

            String open_now = hashMapList.get("open_now");
            if (open_now != null) {
                if (open_now.equalsIgnoreCase("true") || open_now.equalsIgnoreCase(" true"))
                    openStatus.setText("Open Now");
                else
                    openStatus.setText("Closed Now");
            }
            String author_1 = hashMapList.get("review_author_1");
            String review_1 = hashMapList.get("review_text_1");

            String author_2 = hashMapList.get("review_author_2");
            String review_2 = hashMapList.get("review_text_2");

            reviewOfPlace.add(new ReviewHelperClass(author_1, review_1));
            reviewOfPlace.add(new ReviewHelperClass(author_2, review_2));
            drawMap();

            reviewRecycler();
//            Log.d("Author", author);
        }
    }

    private void reviewRecycler() {
        reviewRecyclerAdapter = new ReviewAdapter(reviewOfPlace);
        reviewRecycler.setAdapter(reviewRecyclerAdapter);
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
