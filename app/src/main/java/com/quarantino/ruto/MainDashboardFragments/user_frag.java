package com.quarantino.ruto.MainDashboardFragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.quarantino.ruto.Activities.NearbyPlaceTemplate;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.HistoryPlacesAdapter;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesHelperClass;
import com.quarantino.ruto.HelperClasses.Preferences.sharedPrefs;
import com.quarantino.ruto.HelperClasses.UserHelperClass;
import com.quarantino.ruto.LoginActivities.LoginScreen;
import com.quarantino.ruto.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class user_frag extends Fragment implements HistoryPlacesAdapter.OnHistoryPlaceListener {

    private RecyclerView historyRecycler;
    private RecyclerView.Adapter historyRecyclerAdapter;
    private TextView userName, userUsername;
    private CircleImageView userProfilePhoto;

    FusedLocationProviderClient fusedLocationProviderClient;
    private double currentLat = 0, currentLong = 0;

    private ArrayList<NearbyPlacesHelperClass> historyPlacesNoImage = new ArrayList<>();
    private ArrayList<NearbyPlacesHelperClass> historyPlaces = new ArrayList<>();

    public user_frag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        final Button logOutBtn = view.findViewById(R.id.logOutBtn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutUser(view);
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        TextView historyPlacesTV = view.findViewById(R.id.historyTitle);

        if (new sharedPrefs(getContext()).getRouteGen()) {
            historyPlacesTV.setVisibility(View.VISIBLE);
            Bundle bundle = getArguments();
            historyPlacesNoImage = bundle.getParcelableArrayList("listPlaces");
        } else {
            historyPlacesTV.setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < historyPlacesNoImage.size(); i++) {
            ContextWrapper cw = new ContextWrapper(getContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            String filePath = directory.getAbsolutePath();
            File myPath = new File(filePath, historyPlacesNoImage.get(i).getIdOfPlace() + ".jpg");

            Bitmap bitmapPlace = null;
            try {
                bitmapPlace = BitmapFactory.decodeStream(new FileInputStream(myPath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            historyPlaces.add(new NearbyPlacesHelperClass(
                    bitmapPlace,
                    historyPlacesNoImage.get(i).getNameOfPlace(),
                    historyPlacesNoImage.get(i).getOpenStatus(),
                    historyPlacesNoImage.get(i).getRating(),
                    historyPlacesNoImage.get(i).getIdOfPlace(),
                    historyPlacesNoImage.get(i).getPlaceLat(),
                    historyPlacesNoImage.get(i).getPlaceLong()
            ));
        }

        userName = view.findViewById(R.id.profileUserName);
        userUsername = view.findViewById(R.id.profileUserUsername);
        userProfilePhoto = view.findViewById(R.id.profilePhoto);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String photoUrl = user.getPhotoUrl().toString().replace("s96-c", "s400-c");
        Log.d("User Photo", photoUrl);

        try {
            userProfilePhoto.setImageBitmap(new photoDownload().execute(photoUrl).get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        historyRecycler = view.findViewById(R.id.historyRecycler);
        historyRecycler.setHasFixedSize(true);
        historyRecyclerAdapter = new HistoryPlacesAdapter(historyPlaces, this);
        historyRecycler.setAdapter(historyRecyclerAdapter);
        historyRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        //Helper class
        UserHelperClass userHelperClass = new UserHelperClass(getContext());
        userUsername.setText(String.format("@%s", userHelperClass.getUsername()));
        userName.setText(userHelperClass.getName());

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
                    currentLat = location.getLatitude();
                    currentLong = location.getLongitude();
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class photoDownload extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlPhoto = strings[0];
            Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg);

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

    public void logOutUser(View view) {
        sharedPrefs preference = new sharedPrefs(getContext());
        preference.setIsLoggedIn(false);
        preference.setIsLoggedOut(true);
        preference.setMDFirstTime(true);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient firebaseGoogleSignInClient = GoogleSignIn.getClient(getContext(), googleSignInOptions);
        firebaseGoogleSignInClient.signOut();

        FirebaseAuth.getInstance().signOut();

        startActivity(new Intent(getContext(), LoginScreen.class));
        getActivity().finish();
    }

    @Override
    public void onHistoryPlaceClick(int position, TextView placeName, ImageView placePhoto, RatingBar placeRating, View imageOverlay) {
        Intent intent = new Intent(getContext(), NearbyPlaceTemplate.class);

        //Converting the photo of place to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        historyPlaces.get(position).getImageOfPlace().compress(Bitmap.CompressFormat.JPEG, 95, stream);
        byte[] byteArray = stream.toByteArray();

        intent.putExtra("Name of Place", historyPlaces.get(position).getNameOfPlace());
        intent.putExtra("Position", position);
        intent.putExtra("Image", byteArray);
        intent.putExtra("Current Latitude", currentLat);
        intent.putExtra("Current Longitude", currentLong);
        intent.putExtra("Latitude of Place", historyPlaces.get(position).getPlaceLat());
        intent.putExtra("Longitude of Place", historyPlaces.get(position).getPlaceLong());
        intent.putExtra("Id Of Place", historyPlaces.get(position).getIdOfPlace());
        intent.putExtra("Rating of Place", historyPlaces.get(position).getRating());

        Pair<View, String> p1 = Pair.create((View) placePhoto, "nearbyImageAnim");
        Pair<View, String> p2 = Pair.create((View) placeName, "nearbyTitleAnim");
        Pair<View, String> p4 = Pair.create((View) imageOverlay, "nearbyImageOverlayAnim");

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2, p4);

        startActivity(intent, optionsCompat.toBundle());
    }
}
