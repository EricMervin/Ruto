package com.quarantino.ruto.MainDashboardFragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.CircledImageView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.HistoryPlacesAdapter;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesAdapter;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesHelperClass;
import com.quarantino.ruto.HelperClasses.Preferences.sharedPrefs;
import com.quarantino.ruto.HelperClasses.UserHelperClass;
import com.quarantino.ruto.LoginActivities.LoginScreen;
import com.quarantino.ruto.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class user_frag extends Fragment implements HistoryPlacesAdapter.OnHistoryPlaceListener {

    private RecyclerView historyRecycler;
    private RecyclerView.Adapter historyRecyclerAdapter;
    private TextView userName, userUsername;
    private CircleImageView userProfilePhoto;

    private ArrayList<NearbyPlacesHelperClass> historyPlaces = new ArrayList<>();

    public user_frag() {
        // Required empty public constructor
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

        return view ;
    }

    @SuppressLint("StaticFieldLeak")
    private class photoDownload extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlPhoto = strings[0];
            Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.bg);

//            String urlPhoto = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1200&photoreference="
//                    + photoReference + "&key=" + getResources().getString(R.string.places_api_key);
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
//        Log.d("Log Out", "Successful");
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

    }
}
