package com.quarantino.ruto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.quarantino.ruto.HelperClasses.sharedPrefs;

public class SplashScreen extends AppCompatActivity {

    //Declaring variables
    private static int screenTime = 4000;

    Animation topAnimation;
    TextView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        //Animation
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.ss_titledown);

        //Hooks
        logo = findViewById(R.id.ssTitleText);

        //Animation assignment
        logo.setAnimation(topAnimation);

        //Handler Process
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPrefs preference = new sharedPrefs(getApplicationContext());

                if (preference.getIsFirstTime()) {
                    Intent intent = new Intent(getApplicationContext(), OnBoardScreen.class);
                    startActivity(intent);
                    finish();
                } else if (preference.getIsLoggedIn()) {
                    Intent intent = new Intent(getApplicationContext(), OnBoardScreen.class);
                    startActivity(intent);
                    finish();
                } else if (preference.getIsLoggedOut()) {
                    Intent intent = new Intent(getApplicationContext(), OnBoardScreen.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), OnBoardScreen.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, screenTime);
    }
}
