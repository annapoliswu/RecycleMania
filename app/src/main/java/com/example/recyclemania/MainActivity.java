package com.example.recyclemania;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    private static final int ALL_PERMISSIONS = 1;
    private static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA
    };

    Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //onRequestPermissionResult gets called after finishes
        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, ALL_PERMISSIONS);

        scanButton = findViewById(R.id.bt_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator intentIntegrator = new IntentIntegrator(
                        MainActivity.this
                );
                intentIntegrator.setPrompt("For flash use volume up key");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setCaptureActivity(Capture.class); //this also has a permissions check I think
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data
        );

        if(intentResult != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MainActivity.this
            );
            builder.setTitle("Result");
            builder.setMessage(intentResult.getContents()); //result of scan is here, should be a lookup code
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }else{
            Toast.makeText(getApplicationContext(),
                    "Didn't scan anything.",
                    Toast.LENGTH_LONG).show();
        }
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