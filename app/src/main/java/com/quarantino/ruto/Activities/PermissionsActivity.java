package com.quarantino.ruto.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.quarantino.ruto.HelperClasses.Preferences.sharedPrefs;
import com.quarantino.ruto.R;

public class PermissionsActivity extends AppCompatActivity {

    private Button btnGrant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        btnGrant = findViewById(R.id.permissionBtn);

//        Manifest.permission.ACCESS_FINE_LOCATION.

        btnGrant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(PermissionsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    sharedPrefs sharedPrefs = new sharedPrefs(getApplicationContext());
                    sharedPrefs.setPermission(false);
                    startActivity(new Intent(PermissionsActivity.this, MainDashboard.class));
                    finish();
                } else {
                    requestLocationPermission();
                }
            }
        });
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("The permission to access your location is required to suggest you places and generate routes.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(PermissionsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        } else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 44){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                sharedPrefs sharedPrefs = new sharedPrefs(getApplicationContext());
                sharedPrefs.setPermission(false);
                startActivity(new Intent(PermissionsActivity.this, MainDashboard.class));
                finish();
            } else {
                Toast.makeText(this, "Permission was denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
