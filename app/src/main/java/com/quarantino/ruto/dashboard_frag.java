package com.quarantino.ruto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.quarantino.ruto.HelperClasses.JsonParser;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesAdapter;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesHelperClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class dashboard_frag extends Fragment {
//    private final String FSClientID = "N141PNZ13EZGGBNP3KIDI1YOVKVPWJE1HGSK3IGUYFJJWU0G";
//    private final String FSClientScr = "MCVDR3ZJ0OR52ZOJFSBTUBSPCZG30FOAHM5TASQBXAJRUR1Y";

    RecyclerView nearbyPlacesRecycler;
    RecyclerView.Adapter recyclerAdapter;

    FusedLocationProviderClient fusedLocationProviderClient;
    double currentLat = 0, currentLong = 0;

    public dashboard_frag() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        nearbyPlacesRecycler = view.findViewById(R.id.nearbyPlaces);
        nearbyPlacesRecycler.setHasFixedSize(true);
        nearbyPlacesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

//        nearbyPlacesRecycler();
        if(ActivityCompat.checkSelfPermission(getContext()
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getCurrentLocation();
        } else{
            ActivityCompat.requestPermissions(getActivity()
                    ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
//        getCurrentLocation();
        return view;
    }

    private class PlaceTask extends AsyncTask<String,Integer,String> {

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
            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException{
        URL url = new URL(string);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line = "";

        while((line = reader.readLine()) != null) {
            builder.append(line);
        }

        String data = builder.toString();
        reader.close();

        Log.d("Process", "Url downloaded");
        Log.d("Data downloaded", data);

        return data;
    }

    private class ParserTask extends AsyncTask<String,Integer, List<HashMap<String,String>>>{
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParser jsonParser = new JsonParser();

            List<HashMap<String,String>> mapList = null;
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
            ArrayList<NearbyPlacesHelperClass> nearbyPlaces = new ArrayList<>();
            for(int i = 0 ; i<5 ; i++) {
                HashMap<String, String> hashMapList = hashMaps.get(i);

                double lat = Double.parseDouble(Objects.requireNonNull(hashMapList.get("lat")));
                double lng = Double.parseDouble(Objects.requireNonNull(hashMapList.get("lng")));

                String rating = hashMapList.get("rating");
//                String photoRef = hashMapList.get("photo_reference");
//                String openNow = hashMapList.get("open_now");

                String name = hashMapList.get("name");
                LatLng latlng = new LatLng(lat, lng);

                Log.d("Process", "On Post Executed");
//                Log.d("Rating", photoRef );
                nearbyPlaces.add(new NearbyPlacesHelperClass(R.drawable.bg, name, Float.parseFloat(rating)));
            }
            recyclerAdapter = new NearbyPlacesAdapter(nearbyPlaces);
            nearbyPlacesRecycler.setAdapter(recyclerAdapter);
        }
    }

    private int getPhotoOfPlace(String photoRef) throws IOException {
        String urlPhoto = "https://maps.googleapis.com/maps/api/place/photo" + "?maxwidth=400" +
                "&photoreference=" + photoRef +
                "&key=" + getResources().getString(R.string.places_api_key);
        URL url = new URL(urlPhoto);
//
//        HttpURLConnection connection = (HttpURLConnection) photoUrl.openConnection();
//        connection.connect();
//        InputStream stream = connection.getInputStream();
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        HttpURLConnection connection = (HttpURLConnection) url
                .openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        Bitmap myBitmap = BitmapFactory.decodeStream(input);
        Drawable d = new BitmapDrawable(getResources(), myBitmap);
        int drawableId = Integer.parseInt(d.toString());

        return drawableId;
//        return reader;
    }


    //Get location of the user
    private void getCurrentLocation(){
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLat = location.getLatitude();
                    currentLong = location.getLongitude();

                    getNearbyRestaurant(currentLat, currentLong);
                }
            }
        });
    }

    private void getNearbyRestaurant(double currentLat, double currentLong) {
        Log.d("Location lat", String.valueOf(currentLat));
        Log.d("Location long", String.valueOf(currentLong));

        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" + currentLat + "," + currentLong +
                "&radius=5000" + "&type=" + "restaurant" +
                "&key=" + getResources().getString(R.string.places_api_key);

        Log.d("Json URL", url);

        new PlaceTask().execute(url);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }

    //Create card for the recycler
    private void nearbyPlacesRecycler() {
        ArrayList<NearbyPlacesHelperClass> nearbyPlaces = new ArrayList<>();
        nearbyPlaces.add(new NearbyPlacesHelperClass(R.drawable.bg, "Name of Place", Float.parseFloat("4.1")));
        recyclerAdapter = new NearbyPlacesAdapter(nearbyPlaces);
        nearbyPlacesRecycler.setAdapter(recyclerAdapter);
    }
}
