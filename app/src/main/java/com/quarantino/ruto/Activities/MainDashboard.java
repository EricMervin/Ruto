package com.quarantino.ruto.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.quarantino.ruto.MainDashboardFragments.create_frag;
import com.quarantino.ruto.MainDashboardFragments.dashboard_frag;
import com.quarantino.ruto.MainDashboardFragments.user_frag;
import com.quarantino.ruto.R;

public class MainDashboard extends AppCompatActivity {

    private final Fragment dashboardFrag = new dashboard_frag();
    private final Fragment createFrag = new create_frag();
    private final Fragment userFrag = new user_frag();

    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment activeFragment = dashboardFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard);

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
                        fragmentManager.beginTransaction().hide(activeFragment).show(dashboardFrag).commit();
                        activeFragment = dashboardFrag;
                        return true;

                    case R.id.create_frag:
                        fragmentManager.beginTransaction().hide(activeFragment).show(createFrag).commit();
                        activeFragment = createFrag;
                        return true;

                    case R.id.user_frag:
                        fragmentManager.beginTransaction().hide(activeFragment).show(userFrag).commit();
                        activeFragment = userFrag;
                        return true;
                }
                return false;
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