package com.quarantino.ruto.MainDashboardFragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.quarantino.ruto.HelperClasses.JsonParser;
import com.quarantino.ruto.HelperClasses.NearbyRouteAdapter.NearbyPlacesRouteAdapter;
import com.quarantino.ruto.HelperClasses.NearbyRouteAdapter.NearbyPlacesRouteHelperClass;
import com.quarantino.ruto.HelperClasses.SelectedPlacesAdapter.SelectedPlacesAdapter;
import com.quarantino.ruto.HelperClasses.SelectedPlacesAdapter.SelectedPlacesHelperClass;
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
import java.util.concurrent.ExecutionException;

public class create_frag extends Fragment implements AdapterView.OnItemClickListener, NearbyPlacesRouteAdapter.OnNearbyPlaceRouteListener {

    private RecyclerView nearbyPlacesRecycler, selectedPlacesRecycler;
    private RecyclerView.Adapter nearbyPlacesRecyclerAdapter, selectedPlacesRecyclerAdapter;
    private AutoCompleteTextView autoCompleteTextView;

    private String[] placesArr = {"Restaurant", "Museum", "Cafe", "Airport", "Library"};

    private ProgressDialog loadingDialog;
    private double userCurrentLat = 0, userCurrentLong = 0;

    FusedLocationProviderClient fusedLocationProviderClient;

    private ArrayList<SelectedPlacesHelperClass> selectedPlacesList = new ArrayList<>();
    private ArrayList<NearbyPlacesRouteHelperClass> nearbyPlaces;

    public create_frag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        autoCompleteTextView = view.findViewById(R.id.autoCompletePlace);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        nearbyPlacesRecycler = view.findViewById(R.id.nearbyPlacesOptRecycler);
        nearbyPlacesRecycler.setHasFixedSize(true);
        nearbyPlacesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.custom_row, placesArr);
        autoCompleteTextView.setThreshold(0);
        autoCompleteTextView.setDropDownVerticalOffset(9);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.invalidate();

        autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                autoCompleteTextView.showDropDown();
                return false;
            }
        });

        autoCompleteTextView.setOnItemClickListener(this);

        //Selected Places Recycler View
        selectedPlacesRecycler = view.findViewById(R.id.selectedPlaceRecycler);
        selectedPlacesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        selectedPlacesRecyclerAdapter = new SelectedPlacesAdapter(selectedPlacesList);
        selectedPlacesRecycler.setAdapter(selectedPlacesRecyclerAdapter);
        return view;
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
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

    private void getNearbyPlaces(double currentLat, double currentLong, String placeType) {
        nearbyPlaces = new ArrayList<>();
        Log.d("Location lat", String.valueOf(currentLat));
        Log.d("Location long", String.valueOf(currentLong));

        String placeUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" + currentLat + "," + currentLong +
                "&radius=15000" + "&type=" + placeType +
                "&key=" + getResources().getString(R.string.places_api_key);

        Log.d("Json URL", placeUrl);

        loadingDialog = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
        loadingDialog.setMessage("Fetching Results");
        loadingDialog.setIndeterminate(false);
        loadingDialog.show();

        new nearbyPlaceTask().execute(placeUrl);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String placeTypeSelected = adapterView.getItemAtPosition(i).toString().toLowerCase();
        closeKeyboard();
        getNearbyPlaces(userCurrentLat, userCurrentLong, placeTypeSelected);
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

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

    private class nearbyPlacesParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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

            for (int i = 0; cnt < 8; i++) {
                HashMap<String, String> hashMapList = hashMaps.get(i);

                double placeLat, placeLong;

                if (hashMapList.get("lat") != null && hashMapList.get("lng") != null) {
                    placeLat = Double.parseDouble(hashMapList.get("lat"));
                    placeLong = Double.parseDouble(hashMapList.get("lng"));
                    Log.d("Place Lat", String.valueOf(placeLat));
                    Log.d("Place Long", String.valueOf(placeLong));
                } else {
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
                    nearbyPlaces.add(new NearbyPlacesRouteHelperClass(new photoDownload().execute(photoRef).get(), name, Float.parseFloat(rating), placeId, placeLat, placeLong));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                cnt++;
            }
            nearbyPlacesRecycler();
        }
    }

    private class photoDownload extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String photoReference = strings[0];
            Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg);

            String urlPhoto = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=360&photoreference="
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

    private void nearbyPlacesRecycler() {
        nearbyPlacesRecyclerAdapter = new NearbyPlacesRouteAdapter(nearbyPlaces, this);
        nearbyPlacesRecycler.setAdapter(nearbyPlacesRecyclerAdapter);
        loadingDialog.dismiss();
    }

    @Override
    public void onNearbyPlaceRouteClick(int position, TextView placeName, ImageView placePhoto) {
        Intent intent = new Intent(getContext(), NearbyPlaceTemplate.class);

        //Converting the photo of place to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        nearbyPlaces.get(position).getImageOfPlace().compress(Bitmap.CompressFormat.JPEG, 95, stream);
        byte[] byteArray = stream.toByteArray();

        intent.putExtra("Name of Place", nearbyPlaces.get(position).getNameOfPlace());
        intent.putExtra("Position", position);
        intent.putExtra("Image", byteArray);
        intent.putExtra("Current Latitude", userCurrentLat);
        intent.putExtra("Current Longitude", userCurrentLong);
        intent.putExtra("Latitude of Place", nearbyPlaces.get(position).getPlaceLat());
        intent.putExtra("Longitude of Place", nearbyPlaces.get(position).getPlaceLong());
        intent.putExtra("Id Of Place", nearbyPlaces.get(position).getIdOfPlace());
        intent.putExtra("Rating of Place", nearbyPlaces.get(position).getRating());

        Pair<View, String> p1 = Pair.create((View) placePhoto, "nearbyImageAnim");
        Pair<View, String> p2 = Pair.create((View) placeName, "nearbyTitleAnim");
//        Pair<View, String> p3 = Pair.create((View) placeRating, "nearbyRatingAnim");
//        Pair<View, String> p4 = Pair.create((View) imageOverlay, "nearbyImageOverlayAnim");

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2);

        startActivity(intent, optionsCompat.toBundle());

        Log.d("Adapter", "Row clicked");
    }

    @Override
    public void onAddPLaceRouteClick(int position, boolean isAdded) {
        if (!isAdded) {
//            Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
            selectedPlacesList.add(new SelectedPlacesHelperClass(nearbyPlaces.get(position).getIdOfPlace(), nearbyPlaces.get(position).getNameOfPlace(), nearbyPlaces.get(position).getImageOfPlace()));
//            selectedPlacesRecyclerAdapter.
            selectedPlacesRecycler.setAdapter(selectedPlacesRecyclerAdapter);
        } else {
            String idToBeDeleted = nearbyPlaces.get(position).getIdOfPlace();

            for (int i = 0; i < selectedPlacesList.size(); i++) {
                if (idToBeDeleted.equalsIgnoreCase(selectedPlacesList.get(i).getIdOfSelectedPlace())) {
                    selectedPlacesList.remove(i);
                    selectedPlacesRecyclerAdapter.notifyItemRemoved(i);
                    selectedPlacesRecyclerAdapter.notifyItemRangeRemoved(i, 1);
                }
            }
            Log.d("Place", String.valueOf(selectedPlacesList.size()));
        }
    }
}
