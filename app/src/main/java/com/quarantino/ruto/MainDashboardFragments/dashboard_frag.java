package com.quarantino.ruto.MainDashboardFragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.quarantino.ruto.HelperClasses.CategoriesAdapter.CategoriesAdapter;
import com.quarantino.ruto.HelperClasses.CategoriesAdapter.CategoriesHelperClass;
import com.quarantino.ruto.HelperClasses.JsonParser;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesAdapter;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesHelperClass;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.ParksAdapter;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.RestaurantsAdapter;
import com.quarantino.ruto.HelperClasses.UserHelperClass;
import com.quarantino.ruto.NearbyPlaceTemplate;
import com.quarantino.ruto.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class dashboard_frag extends Fragment implements NearbyPlacesAdapter.OnNearbyPlaceListener, RestaurantsAdapter.OnRestaurantListener, ParksAdapter.OnParkListener{

    private TextView cityOfUser;

    private RecyclerView nearbyPlacesRecycler, categoriesRecycler, topRestaurantsRecycler, shoppingMallRecycler;
    private RecyclerView.Adapter nearbyPlacesRecyclerAdapter, categoriesRecyclerAdapter, topRestaurantsRecyclerAdapter, shoppingMallRecyclerAdapter;

    private ArrayList<NearbyPlacesHelperClass> nearbyPlaces = new ArrayList<>();
    private ArrayList<NearbyPlacesHelperClass> topRestaurants = new ArrayList<>();
    private ArrayList<NearbyPlacesHelperClass> shoppingMall = new ArrayList<>();
    private ArrayList<CategoriesHelperClass> categoriesList = new ArrayList<>();

    private ProgressDialog loadingDialog;

    FusedLocationProviderClient fusedLocationProviderClient;
    private double currentLat = 0, currentLong = 0;

    public dashboard_frag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        //City Text View
        cityOfUser = view.findViewById(R.id.currentCity);

        //Nearby Places Recycler
        nearbyPlacesRecycler = view.findViewById(R.id.nearbyPlaces);
        nearbyPlacesRecycler.setHasFixedSize(true);
        nearbyPlacesRecyclerAdapter = new NearbyPlacesAdapter(nearbyPlaces, this);
        nearbyPlacesRecycler.setAdapter(nearbyPlacesRecyclerAdapter);
        nearbyPlacesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        //Categories Recycler
        categoriesRecycler = view.findViewById(R.id.categoriesRecycler);
        categoriesRecycler();

        //Top Restaurants Recycler
        topRestaurantsRecycler = view.findViewById(R.id.restaurantsRecycler);
        topRestaurantsRecycler.setHasFixedSize(true);
        topRestaurantsRecyclerAdapter = new RestaurantsAdapter(topRestaurants, this);
        topRestaurantsRecycler.setAdapter(topRestaurantsRecyclerAdapter);
        topRestaurantsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        //Shopping Mall Recycler
        shoppingMallRecycler = view.findViewById(R.id.shoppingMallRecycler);
        shoppingMallRecycler.setHasFixedSize(true);
        shoppingMallRecyclerAdapter = new ParksAdapter(shoppingMall, this);
        shoppingMallRecycler.setAdapter(shoppingMallRecyclerAdapter);
        shoppingMallRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        return view;
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

    @SuppressLint("StaticFieldLeak")
    private class restaurantTask extends AsyncTask<String, Integer, String> {
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
            new restaurantParserTask().execute(s);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class shoppingTask extends AsyncTask<String, Integer, String> {
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
            new shoppingParserTask().execute(s);
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

        Log.d("Process", "Url downloaded");
        Log.d("Data downloaded", data);

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

            for (int i = 0; cnt < 7; i++) {
                HashMap<String, String> hashMapList = hashMaps.get(i);

                double placeLat, placeLong;

                if(hashMapList.get("lat") != null && hashMapList.get("lng") != null){
                    placeLat = Double.parseDouble(hashMapList.get("lat"));
                    placeLong = Double.parseDouble(hashMapList.get("lng"));
                    Log.d("Place Lat", String.valueOf(placeLat));
                    Log.d("Place Long", String.valueOf(placeLong));
                } else{
                    continue;
                }

                String name = hashMapList.get("name");
                String rating = hashMapList.get("rating");
                String placeId = hashMapList.get("place_id");
                String photoRef = hashMapList.get("photo_reference");
//                String openNow = hashMapList.get("open_now");

                try {
                    nearbyPlaces.add(
                            new NearbyPlacesHelperClass(new photoDownload().execute(photoRef).get(), name, Float.parseFloat(rating), placeId, placeLat, placeLong));
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                cnt++;
            }
            nearbyPlacesRecycler();
//            loadingDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class restaurantParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            loadingDialog = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
//            loadingDialog.setMessage("Finding places near you");
//            loadingDialog.setIndeterminate(false);
//            loadingDialog.show();
//        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParser jsonParser = new JsonParser();

            List<HashMap<String, String>> mapList = null;
            JSONObject object = null;
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

            for (int i = 0; cnt < 4; i++) {
                HashMap<String, String> hashMapList = hashMaps.get(i);

                double placeLat, placeLong;

                if(hashMapList.get("lat") != null && hashMapList.get("lng") != null){
                    placeLat = Double.parseDouble(hashMapList.get("lat"));
                    placeLong = Double.parseDouble(hashMapList.get("lng"));
                    Log.d("Place Lat", String.valueOf(placeLat));
                    Log.d("Place Long", String.valueOf(placeLong));
                } else{
                    continue;
                }

                String name = hashMapList.get("name");
                String rating = hashMapList.get("rating");
                String placeId = hashMapList.get("place_id");
                String photoRef = hashMapList.get("photo_reference");
//                String openNow = hashMapList.get("open_now");

//                Log.d("Process", "On Post Executed");
//                Log.d("Place Id", placeId);
//                Log.d("PhotoRef", photoRef);

                try {
                    topRestaurants.add(new NearbyPlacesHelperClass(new photoDownload().execute(photoRef).get(), name, Float.parseFloat(rating), placeId, placeLat, placeLong));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                cnt++;
            }
            topRestaurantsRecycler();
//            loadingDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class shoppingParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            loadingDialog = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
//            loadingDialog.setMessage("Finding places near you");
//            loadingDialog.setIndeterminate(false);
//            loadingDialog.show();
//        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParser jsonParser = new JsonParser();

            List<HashMap<String, String>> mapList = null;
            JSONObject object = null;
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

            for (int i = 0; cnt < 4; i++) {
                HashMap<String, String> hashMapList = hashMaps.get(i);

                double placeLat, placeLong;

                if(hashMapList.get("lat") != null && hashMapList.get("lng") != null){
                    placeLat = Double.parseDouble(hashMapList.get("lat"));
                    placeLong = Double.parseDouble(hashMapList.get("lng"));
                    Log.d("Place Lat", String.valueOf(placeLat));
                    Log.d("Place Long", String.valueOf(placeLong));
                } else{
                    continue;
                }

                String name = hashMapList.get("name");
                String rating = hashMapList.get("rating");
                String placeId = hashMapList.get("place_id");
                String photoRef = hashMapList.get("photo_reference");
//                String openNow = hashMapList.get("open_now");

//                Log.d("Process", "On Post Executed");
//                Log.d("Place Id", placeId);
//                Log.d("PhotoRef", photoRef);

                try {
                    shoppingMall.add(new NearbyPlacesHelperClass(new photoDownload().execute(photoRef).get(), name, Float.parseFloat(rating), placeId, placeLat, placeLong));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                cnt++;
            }
            shoppingMallRecycler();
//            loadingDialog.dismiss();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class photoDownload extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String photoReference = strings[0];
            Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg);

            String urlPhoto = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1200&photoreference="
                    + photoReference + "&key=" + getResources().getString(R.string.places_api_key);
            try {
                InputStream in = new java.net.URL(urlPhoto).openStream();
                return BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return icon1;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

        }
    }

    //Get location of the user
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        loadingDialog = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
        loadingDialog.setMessage("Finding places near you");
        loadingDialog.setIndeterminate(false);
        loadingDialog.show();

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLat = location.getLatitude();
                    currentLong = location.getLongitude();

                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(currentLat, currentLong, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String cityName = addresses.get(0).getLocality();
                    cityOfUser.setText(cityName);

                    getNearbyPlaces(currentLat, currentLong);
//                    getNearbyRestaurants(currentLat, currentLong);
                }
            }
        });
    }

    private void getNearbyPlaces(double currentLat, double currentLong) {
        Log.d("Location lat", String.valueOf(currentLat));
        Log.d("Location long", String.valueOf(currentLong));

        String touristUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" + currentLat + "," + currentLong +
                "&radius=21000" + "&type=" + "tourist_attraction" +
                "&key=" + getResources().getString(R.string.places_api_key);

        String restaurantUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" + currentLat + "," + currentLong +
                "&radius=8000" + "&type=" + "restaurant" +
                "&key=" + getResources().getString(R.string.places_api_key);

        String museumUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" + currentLat + "," + currentLong +
                "&radius=15000" + "&type=" + "museum" +
                "&key=" + getResources().getString(R.string.places_api_key);

        Log.d("Json URL", museumUrl);

        new nearbyPlaceTask().execute(touristUrl);
        new restaurantTask().execute(restaurantUrl);
        new shoppingTask().execute(museumUrl);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    //Create card for the recycler
    private void nearbyPlacesRecycler() {
        nearbyPlacesRecyclerAdapter = new NearbyPlacesAdapter(nearbyPlaces, this);
        nearbyPlacesRecycler.setAdapter(nearbyPlacesRecyclerAdapter);
    }

    private void categoriesRecycler() {
        categoriesRecycler.setHasFixedSize(true);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        categoriesList.add(new CategoriesHelperClass("Cafe", ContextCompat.getDrawable(getContext(), R.drawable.cafe_icon)));
        categoriesList.add(new CategoriesHelperClass("Monument", ContextCompat.getDrawable(getContext(), R.drawable.monument_icon)));
        categoriesList.add(new CategoriesHelperClass("Restaurant", ContextCompat.getDrawable(getContext(), R.drawable.restaurant_icon)));
        categoriesList.add(new CategoriesHelperClass("Art", ContextCompat.getDrawable(getContext(), R.drawable.art_icon)));
        categoriesList.add(new CategoriesHelperClass("Theatre", ContextCompat.getDrawable(getContext(), R.drawable.theatre_icon)));

        categoriesRecyclerAdapter = new CategoriesAdapter(categoriesList);
        categoriesRecycler.setAdapter(categoriesRecyclerAdapter);
    }

    private void topRestaurantsRecycler() {
//        Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        topRestaurantsRecyclerAdapter = new RestaurantsAdapter(topRestaurants, this);
        topRestaurantsRecycler.setAdapter(topRestaurantsRecyclerAdapter);
    }

    private void shoppingMallRecycler() {
        shoppingMallRecycler.setAdapter(shoppingMallRecyclerAdapter);
        loadingDialog.dismiss();
    }

    @Override
    public void onNearbyPlaceClick(int position, TextView placeName, ImageView placePhoto, RatingBar placeRating, View imageOverlay) {
        Intent intent = new Intent(getContext(), NearbyPlaceTemplate.class);

        //Converting the photo of place to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        nearbyPlaces.get(position).getImageOfPlace().compress(Bitmap.CompressFormat.JPEG, 95, stream);
        byte[] byteArray = stream.toByteArray();

        intent.putExtra("Name of Place", nearbyPlaces.get(position).getNameOfPlace());
        intent.putExtra("Position", position);
        intent.putExtra("Image", byteArray);
        intent.putExtra("Current Latitude", currentLat);
        intent.putExtra("Current Longitude", currentLong);
        intent.putExtra("Latitude of Place", nearbyPlaces.get(position).getPlaceLat());
        intent.putExtra("Longitude of Place", nearbyPlaces.get(position).getPlaceLong());
        intent.putExtra("Id Of Place", nearbyPlaces.get(position).getIdOfPlace());
        intent.putExtra("Rating of Place", nearbyPlaces.get(position).getRating());

        Pair<View, String> p1 = Pair.create((View) placePhoto, "nearbyImageAnim");
        Pair<View, String> p2 = Pair.create((View) placeName, "nearbyTitleAnim");
        Pair<View, String> p3 = Pair.create((View) placeRating, "nearbyRatingAnim");
        Pair<View, String> p4 = Pair.create((View) imageOverlay, "nearbyImageOverlayAnim");

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2, p3, p4);

        startActivity(intent, optionsCompat.toBundle());
//        startActivity(intent);

//        Log.d("Position Clicked", String.valueOf(position) + " " + (nearbyPlaces.get(position).getRating() + 1));
    }

    @Override
    public void onRestaurantClick(int position, TextView placeName, ImageView placePhoto, TextView placeRating, View imageOverlay) {
        Intent intent = new Intent(getContext(), NearbyPlaceTemplate.class);

        //Converting the photo of place to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        topRestaurants.get(position).getImageOfPlace().compress(Bitmap.CompressFormat.JPEG, 95, stream);
        byte[] byteArray = stream.toByteArray();

        intent.putExtra("Name of Place", topRestaurants.get(position).getNameOfPlace());
        intent.putExtra("Position", position);
        intent.putExtra("Image", byteArray);
        intent.putExtra("Current Latitude", currentLat);
        intent.putExtra("Current Longitude", currentLong);
        intent.putExtra("Latitude of Place", topRestaurants.get(position).getPlaceLat());
        intent.putExtra("Longitude of Place", topRestaurants.get(position).getPlaceLong());
        intent.putExtra("Id Of Place", topRestaurants.get(position).getIdOfPlace());
        intent.putExtra("Rating of Place", topRestaurants.get(position).getRating());

        Pair<View, String> p1 = Pair.create((View) placePhoto, "nearbyImageAnim");
        Pair<View, String> p2 = Pair.create((View) placeName, "nearbyTitleAnim");
//        Pair<View, String> p3 = Pair.create((View) placeRating, "nearbyRatingAnim");
        Pair<View, String> p4 = Pair.create((View) imageOverlay, "nearbyImageOverlayAnim");

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2, p4);

        startActivity(intent, optionsCompat.toBundle());
    }

    @Override
    public void onParkClick(int position, TextView placeName, ImageView placePhoto, TextView placeRating, View imageOverlay) {
        Intent intent = new Intent(getContext(), NearbyPlaceTemplate.class);

        //Converting the photo of place to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        shoppingMall.get(position).getImageOfPlace().compress(Bitmap.CompressFormat.JPEG, 95, stream);
        byte[] byteArray = stream.toByteArray();

        intent.putExtra("Name of Place", shoppingMall.get(position).getNameOfPlace());
        intent.putExtra("Position", position);
        intent.putExtra("Image", byteArray);
        intent.putExtra("Current Latitude", currentLat);
        intent.putExtra("Current Longitude", currentLong);
        intent.putExtra("Latitude of Place", shoppingMall.get(position).getPlaceLat());
        intent.putExtra("Longitude of Place", shoppingMall.get(position).getPlaceLong());
        intent.putExtra("Id Of Place", shoppingMall.get(position).getIdOfPlace());
        intent.putExtra("Rating of Place", shoppingMall.get(position).getRating());

        Pair<View, String> p1 = Pair.create((View) placePhoto, "nearbyImageAnim");
        Pair<View, String> p2 = Pair.create((View) placeName, "nearbyTitleAnim");
//        Pair<View, String> p3 = Pair.create((View) placeRating, "nearbyRatingAnim");
        Pair<View, String> p4 = Pair.create((View) imageOverlay, "nearbyImageOverlayAnim");

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2, p4);

        startActivity(intent, optionsCompat.toBundle());
    }

    // Obsolete
    public Bitmap getPhotoOfPlace(String photoRef) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String urlPhoto = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1200&photoreference="
                + photoRef + "&key=" + getResources().getString(R.string.places_api_key);
        Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg);

        try {
            InputStream in = new java.net.URL(urlPhoto).openStream();
            return BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return icon1;
    }
}