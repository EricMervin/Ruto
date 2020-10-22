package com.quarantino.ruto.MainDashboardFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.quarantino.ruto.HelperClasses.Preferences.sharedPrefs;
import com.quarantino.ruto.HelperClasses.userHelperClass;
import com.quarantino.ruto.LoginActivities.LoginScreen;
import com.quarantino.ruto.R;

public class user_frag extends Fragment {

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

        return view ;
    }

    public void logOutUser(View view) {
//        Log.d("Log Out", "Successful");
        sharedPrefs preference = new sharedPrefs(getContext());
        preference.setIsLoggedIn(false);
        preference.setIsLoggedOut(true);
        preference.setMDFirstTime(true);

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(), LoginScreen.class));
        getActivity().finish();
    }
}
