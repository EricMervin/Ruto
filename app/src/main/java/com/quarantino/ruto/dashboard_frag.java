package com.quarantino.ruto;

import android.Manifest;
import android.content.pm.PackageManager;
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
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        nearbyPlacesRecycler = view.findViewById(R.id.nearbyPlaces);
//        nearbyPlacesRecycler();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        getCurrentLocation();

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
            nearbyPlacesRecycler.setHasFixedSize(true);
            nearbyPlacesRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

            ArrayList<NearbyPlacesHelperClass> nearbyPlaces = new ArrayList<>();
            for(int i = 0 ; i<5 ; i++) {
                HashMap<String, String> hashMapList = hashMaps.get(i);

                double lat = Double.parseDouble(Objects.requireNonNull(hashMapList.get("lat")));
                double lng = Double.parseDouble(Objects.requireNonNull(hashMapList.get("lng")));

                String name = hashMapList.get("name");
                LatLng latlng = new LatLng(lat, lng);

                Log.d("Process", "On Post Executed");

                nearbyPlaces.add(new NearbyPlacesHelperClass(R.drawable.bg, name));
            }
            recyclerAdapter = new NearbyPlacesAdapter(nearbyPlaces);
            nearbyPlacesRecycler.setAdapter(recyclerAdapter);
        }
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
                    Log.d("Location lat", String.valueOf(currentLat));
                    Log.d("Location long", String.valueOf(currentLong));

                    String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                            "?location=" + currentLat + "," + currentLong +
                            "&radius=5000" + "&type=" + "restaurant" +
                            "&key=" + getResources().getString(R.string.places_api_key);

                    new PlaceTask().execute(url);
                }
            }
        });
    }

    //Create card for the recycler
    private void nearbyPlacesRecycler() {
        nearbyPlacesRecycler.setHasFixedSize(true);
        nearbyPlacesRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        ArrayList<NearbyPlacesHelperClass> nearbyPlaces = new ArrayList<>();

        nearbyPlaces.add(new NearbyPlacesHelperClass(R.drawable.bg, "Green Park"));
        nearbyPlaces.add(new NearbyPlacesHelperClass(R.drawable.bg_2, "Taj Mahal"));
        nearbyPlaces.add(new NearbyPlacesHelperClass(R.drawable.bg, "Venice"));

        recyclerAdapter = new NearbyPlacesAdapter(nearbyPlaces);
        nearbyPlacesRecycler.setAdapter(recyclerAdapter);
    }
}
