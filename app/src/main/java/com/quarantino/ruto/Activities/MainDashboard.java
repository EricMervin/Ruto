package com.quarantino.ruto.Activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quarantino.ruto.HelperClasses.NearbyAdapter.NearbyPlacesHelperClass;
import com.quarantino.ruto.HelperClasses.Preferences.sharedPrefs;
import com.quarantino.ruto.MainDashboardFragments.create_frag;
import com.quarantino.ruto.MainDashboardFragments.dashboard_frag;
import com.quarantino.ruto.MainDashboardFragments.user_frag;
import com.quarantino.ruto.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainDashboard extends AppCompatActivity {

    private final Fragment dashboardFrag = new dashboard_frag();
    private final Fragment createFrag = new create_frag();
    private final Fragment userFrag = new user_frag();

    private Window window;

    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment activeFragment = dashboardFrag;

    private ArrayList<NearbyPlacesHelperClass> selectedPlacesListNoImage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard);

        sharedPrefs preference = new sharedPrefs(this);
        if (preference.getRouteGen()) {
            SharedPreferences sharedPreferences = getSharedPreferences("History", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("History Of Places", null);
            Type type = new TypeToken<ArrayList<NearbyPlacesHelperClass>>() {
            }.getType();
            selectedPlacesListNoImage = gson.fromJson(json, type);

            if(selectedPlacesListNoImage == null){
                selectedPlacesListNoImage = new ArrayList<>();
            }

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("listPlaces", selectedPlacesListNoImage);
            userFrag.setArguments(bundle);
            Log.d("MD List Size", String.valueOf(selectedPlacesListNoImage.size()));
        }

        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        fragmentManager.beginTransaction().add(R.id.fragmentCont, userFrag, "3").hide(userFrag).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentCont, createFrag, "2").hide(createFrag).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentCont, dashboardFrag, "1").commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        AnimatedBottomBar bottomNavigationView = findViewById(R.id.bottom_bar);
//        NavController navController = Navigation.findNavController(this, R.id.fragmentCont);
//        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard_frag:
                        window.setStatusBarColor(Color.WHITE);
                        fragmentManager.beginTransaction().hide(activeFragment).show(dashboardFrag).commit();
                        activeFragment = dashboardFrag;
                        return true;

                    case R.id.create_frag:
                        window.setStatusBarColor(getResources().getColor(R.color.lightGrey));
                        fragmentManager.beginTransaction().hide(activeFragment).show(createFrag).commit();
                        activeFragment = createFrag;
                        return true;

                    case R.id.user_frag:
                        window.setStatusBarColor(Color.WHITE);
                        fragmentManager.beginTransaction().hide(activeFragment).show(userFrag).commit();
                        activeFragment = userFrag;
                        return true;
                    default:
                        return false;
                }
            }
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                Log.d("", "Reselected");
            }
        });
    }
}