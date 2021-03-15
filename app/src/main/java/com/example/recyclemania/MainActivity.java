package com.example.recyclemania;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int ALL_PERMISSIONS = 1;
    private static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //onRequestPermissionResult gets called after finishes
        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, ALL_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS: {
                boolean allGranted = true;
                for( int result : grantResults){
                    if(result == PackageManager.PERMISSION_DENIED ) {
                        allGranted = false;
                        break;
                    }
                }
                if (allGranted) {
                    //all permissions granted,proceed with program
                    Toast.makeText(MainActivity.this, "Permissions granted", Toast.LENGTH_LONG ).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permissions missing", Toast.LENGTH_LONG ).show();
                }
                break;
            } default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

}