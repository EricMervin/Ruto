package com.quarantino.ruto.MainDashboardFragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.quarantino.ruto.HelperClasses.JsonParser;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesCreateFragAdapter;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesHelperClass;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.SelectedPlacesAdapter;
import com.quarantino.ruto.ItineraryActivity;
import com.quarantino.ruto.NearbyPlaceTemplate;
import com.quarantino.ruto.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class create_frag extends Fragment implements AdapterView.OnItemClickListener, NearbyPlacesCreateFragAdapter.OnNearbyPlaceRouteListener, SelectedPlacesAdapter.OnSelectedPlaceListener {

    private RecyclerView nearbyPlacesRecycler, selectedPlacesRecycler;
    private RecyclerView.Adapter nearbyPlacesRecyclerAdapter, selectedPlacesRecyclerAdapter;
    private AutoCompleteTextView autoCompleteTextView;
    private TextView continueToItinerary;
    private Button filterButton;
    private String placeTypeSelected;
    private int searchRadius = 15000;

    private String[] placesArr = {"Restaurant", "Museum", "Cafe", "Airport", "Library", "Bank", "Church", "Gym"};

    private ProgressDialog loadingDialog;
    private double userCurrentLat = 0, userCurrentLong = 0;

    FusedLocationProviderClient fusedLocationProviderClient;

    private ArrayList<NearbyPlacesHelperClass> selectedPlacesList = new ArrayList<>();
    private ArrayList<NearbyPlacesHelperClass> nearbyPlaces;

    public create_frag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        continueToItinerary = view.findViewById(R.id.continueSelectedPlace);
        continueToItinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continueToItinerary();
            }
        });

        autoCompleteTextView = view.findViewById(R.id.autoCompletePlace);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        nearbyPlacesRecycler = view.findViewById(R.id.nearbyPlacesOptRecycler);
        nearbyPlacesRecycler.setHasFixedSize(true);
        nearbyPlacesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.custom_row, placesArr);
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

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setDismissWithAnimation(true);

        View bottomSheetView = inflater.inflate(R.layout.radius_bottom_sheet, null);
        final TextView seekBarVal = bottomSheetView.findViewById(R.id.radiusValText);

        Button cancelBottomSheet = bottomSheetView.findViewById(R.id.cancelValBtn);
        cancelBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
            }
        });

        Button changeRadiusBottomSheet = bottomSheetView.findViewById(R.id.changeValBtn);
        changeRadiusBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                searchRadius = Integer.parseInt(String.valueOf(seekBarVal.getText()));
                String radiusStr = (String) seekBarVal.getText();
                String numberStr= radiusStr.replaceAll("[^0-9]", "");
                searchRadius = Integer.parseInt(String.valueOf(numberStr));
                bottomSheetDialog.cancel();
                if (placeTypeSelected != null)
                    getNearbyPlaces(userCurrentLat, userCurrentLong, placeTypeSelected);

                bottomSheetDialog.cancel();
            }
        });

        final SeekBar seek = bottomSheetView.findViewById(R.id.radiusSeekBar);
        seek.setProgress(40);
        seek.incrementProgressBy(2);
        seek.setMax(100);

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarVal.setText(String.format("%d km", i * 300));
                seekBarVal.setTextColor(getResources().getColor(R.color.colorPrimary));
                Log.d("Radius Value", String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);

        filterButton = view.findViewById(R.id.radiusButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });

        //Selected Places Recycler View
        selectedPlacesRecycler = view.findViewById(R.id.selectedPlaceRecycler);
        selectedPlacesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        selectedPlacesRecyclerAdapter = new SelectedPlacesAdapter(selectedPlacesList, this);
        selectedPlacesRecycler.setAdapter(selectedPlacesRecyclerAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(selectedPlacesRecycler);

        return view;
    }

    final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.START | ItemTouchHelper.END, 0) {
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
                "&radius=" + searchRadius + "&type=" + placeType +
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
        placeTypeSelected = adapterView.getItemAtPosition(i).toString().toLowerCase();
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

            for (int i = 0; i < hashMaps.size(); i++) {
                HashMap<String, String> hashMapList = hashMaps.get(i);

                double placeLat, placeLong;

                if (hashMapList.get("lat") != null && hashMapList.get("lng") != null) {
                    placeLat = Double.parseDouble(hashMapList.get("lat"));
                    placeLong = Double.parseDouble(hashMapList.get("lng"));
                } else {
                    continue;
                }

                boolean placeExists = false;
                String placeId = hashMapList.get("place_id");

                for (int x = 0; x < selectedPlacesList.size(); x++) {
                    if (placeId.equalsIgnoreCase(selectedPlacesList.get(x).getIdOfPlace())) {
                        placeExists = true;
                    }
                }
                if (placeExists) {
                    continue;
                }

                String name = hashMapList.get("name");
                String rating = hashMapList.get("rating");
                String photoRef = hashMapList.get("photo_reference");
                String openNow = hashMapList.get("open_now");
//                String cityName = hashMapList.get("city_name");

                try {
                    nearbyPlaces.add(new NearbyPlacesHelperClass(new photoDownload().execute(photoRef).get(), name, openNow, Float.parseFloat(rating), placeId, placeLat, placeLong));
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                cnt++;
                if (cnt >= 8) {
                    break;
                }
            }
            nearbyPlacesRecycler();
        }
    }

    @SuppressLint("StaticFieldLeak")
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
        nearbyPlacesRecyclerAdapter = new NearbyPlacesCreateFragAdapter(nearbyPlaces, this);
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

        //Passing data to the next activity
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
    }

    @Override
    public void onAddPlaceRouteClick(final int position, final boolean isAdded) {
        if (!isAdded) {
            if (!nearbyPlaces.get(position).getOpenStatus().equalsIgnoreCase("true")) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Location Closed");
                builder.setMessage("The place that you have selected is currently closed, do you still want to add it to your itinerary?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedPlacesList.add(new NearbyPlacesHelperClass(
                                nearbyPlaces.get(position).getImageOfPlace(),
                                nearbyPlaces.get(position).getNameOfPlace(),
                                nearbyPlaces.get(position).getOpenStatus(),
                                nearbyPlaces.get(position).getRating(),
                                nearbyPlaces.get(position).getIdOfPlace(),
                                nearbyPlaces.get(position).getPlaceLat(),
                                nearbyPlaces.get(position).getPlaceLong()));

                        Log.d(nearbyPlaces.get(position).getNameOfPlace(), nearbyPlaces.get(position).getOpenStatus());

                        selectedPlacesRecycler.setAdapter(selectedPlacesRecyclerAdapter);
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                selectedPlacesList.add(new NearbyPlacesHelperClass(
                        nearbyPlaces.get(position).getImageOfPlace(),
                        nearbyPlaces.get(position).getNameOfPlace(),
                        nearbyPlaces.get(position).getOpenStatus(),
                        nearbyPlaces.get(position).getRating(),
                        nearbyPlaces.get(position).getIdOfPlace(),
                        nearbyPlaces.get(position).getPlaceLat(),
                        nearbyPlaces.get(position).getPlaceLong()));

                Log.d(nearbyPlaces.get(position).getNameOfPlace(), nearbyPlaces.get(position).getOpenStatus());

                selectedPlacesRecycler.setAdapter(selectedPlacesRecyclerAdapter);
            }
        } else {
            String idToBeDeleted = nearbyPlaces.get(position).getIdOfPlace();

            for (int i = 0; i < selectedPlacesList.size(); i++) {
                if (idToBeDeleted.equalsIgnoreCase(selectedPlacesList.get(i).getIdOfPlace())) {
                    selectedPlacesList.remove(i);
                    selectedPlacesRecyclerAdapter.notifyItemRemoved(i);
                    selectedPlacesRecyclerAdapter.notifyItemRangeRemoved(i, 1);
                }
            }
            Log.d("Place", String.valueOf(selectedPlacesList.size()));
        }
    }

    @Override
    public void onRemovePlaceClick(int position) {
        selectedPlacesList.remove(position);
        selectedPlacesRecyclerAdapter.notifyItemRemoved(position);
        selectedPlacesRecyclerAdapter.notifyItemRangeRemoved(position, 1);
    }

    public void continueToItinerary() {
        if (selectedPlacesList.size() != 0) {
            ArrayList<NearbyPlacesHelperClass> selectedPlacesListComp = new ArrayList<>();

            for (int i = 0; i < selectedPlacesList.size(); i++) {
                Bitmap bitmapImage = selectedPlacesList.get(i).getImageOfPlace();

                ContextWrapper cw = new ContextWrapper(getContext());
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                File myPath = new File(directory, selectedPlacesList.get(i).getIdOfPlace() + ".jpg");

                FileOutputStream fos = null;

                try {
                    fos = new FileOutputStream(myPath);
                    bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                selectedPlacesListComp.add(new NearbyPlacesHelperClass(
                        selectedPlacesList.get(i).getNameOfPlace(),
                        selectedPlacesList.get(i).getOpenStatus(),
                        selectedPlacesList.get(i).getRating(),
                        selectedPlacesList.get(i).getIdOfPlace(),
                        selectedPlacesList.get(i).getPlaceLat(),
                        selectedPlacesList.get(i).getPlaceLong()
                ));
            }

            Intent intent = new Intent(getActivity(), ItineraryActivity.class);
            intent.putParcelableArrayListExtra("selectedPlaces", selectedPlacesListComp);
            startActivity(intent);
        } else {
            Log.d("Continue", "False");
        }
    }
}
