package com.example.recyclemania;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import okhttp3.*;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static final int ALL_PERMISSIONS = 1;
    private static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET
    };



    Button scanButton;
    Button manualButton;
    Button loginButton;
    TextView textView;
    private TextView result;
    private TextView result2;
    SharedPreferences sharedPreferences;
    final Context context = this;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String category;
    String subcategory;
    Map<String, String> tips = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getPreferences(MODE_PRIVATE); // Access to SharedPreferences local storage

        //onRequestPermissionResult gets called after finishes
        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, ALL_PERMISSIONS);
        textView = findViewById(R.id.text_view);

        Resources res = MainActivity.this.getResources();
        tips.put("♻️1 - PET", res.getString(R.string.plastic1tip));
        tips.put("♻️2 - HDPE", res.getString(R.string.plastic2tip));
        tips.put("♻️4 - LDPE", res.getString(R.string.plastic4tip));
        tips.put("♻️5 - PP", res.getString(R.string.plastic5tip));
        tips.put("♻️20 - PAP", res.getString(R.string.paper20tip));
        tips.put("♻️21 - PAP", res.getString(R.string.paper21tip));
        tips.put("♻️22 - PAP", res.getString(R.string.paper22tip));
        tips.put("♻️70-74 - GL", res.getString(R.string.glass70tip));
        tips.put("♻️40 - FE", res.getString(R.string.metal40tip));
        tips.put("♻️41 - ALU", res.getString(R.string.metal41tip));
        tips.put("E-waste", res.getString(R.string.ewastetip));
        tips.put("Organic", res.getString(R.string.organictip));

        manualButton = findViewById(R.id.bt_scan2);
        manualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, Manual.class);
                startActivity(intent);
            }
        });

        result = findViewById(R.id.userName);
        String userName = sharedPreferences.getString("user", "");
        result.setText("Welcome " + userName + "!");

        //TODO: Make this updatePoints() or something
        result2 = findViewById(R.id.userPoints);

        Firebase.updatePointsView(userName, result2, sharedPreferences);


        loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.custom, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text
                                        result.setText("User: " + userInput.getText());

                                        String myResponse = userInput.getText().toString();

                                        // Store the API response in SharedPreferences local memory, so that for development we dont have to constantly re-scan and make API requests
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("user", myResponse);
                                        editor.commit();

                                        Map<String, Object> user = new HashMap<>();
                                        user.put("name", myResponse);

                                        Firebase.newUser(myResponse);

                                        Firebase.updatePointsView(myResponse, result2, sharedPreferences);

                                    }
                                }
                        ).setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();

            }
        });


        //testing passing stuff
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            category = extras.getString("category");
            subcategory = extras.getString("subcategory");
            String str = "Category: " + category + " Subcategory: " + subcategory;
            textView.setText(str);
        }else{
            category = null;
            subcategory = null;
            textView.setText("hmm");
        }



        scanButton = findViewById(R.id.bt_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String persistentResponse = sharedPreferences.getString("response", "None");

                    Log.i("StartScan", "Response not present, entering Scan");

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

            String barcode = intentResult.getContents();

            db.collection("barcodes").document(barcode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map data = document.getData();
                            builder.setTitle("Item Found");
                            String tip;
                            if( (Boolean)data.get("recyclable") == true){
                                tip = tips.get(data.get("material"));
                                builder.setPositiveButton("Recycle",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //TODO: increment points by 3
                                        String curUserName = sharedPreferences.getString("user", "None");
                                        Firebase.incrementPoints(curUserName, 3);
                                        Firebase.updatePointsView(curUserName, result2, sharedPreferences);

                                        dialog.dismiss();
                                    }
                                });
                            }else{
                                tip = "Item not recyclable";
                            }
                            builder.setMessage(data.get("title") + "\n\nCategory: " + data.get("category") + "\nMaterial: " + data.get("material") + "\n\n"+ tip); //result of scan is here, should be a lookup code
                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            builder.show();
                        } else {

                            builder.setTitle("Item Not Found in Database");
                            builder.setMessage("Kindly enter item into database for points?"); //result of scan is here, should be a lookup code
                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            builder.setPositiveButton("Log in Database", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainActivity.this, ScanSelect.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("barcode", barcode);
                                    String curUser = sharedPreferences.getString("user", "None");
                                    bundle.putString("user", curUser);
                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                }
                            });
                            builder.show();
                        }
                    } else {
                        Log.d("aa", "Failed with: ", task.getException());
                    }
                }
            });



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


    /*

    private void updateResultScreen(String response){

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("2nd");
                dialog.setMessage(response);
                dialog.setNegativeButton("Back", (dialog12, which) -> {});
                dialog.show();

                JSONObject myJson = new JSONObject();
                JSONArray myArray = new JSONArray();
                JSONObject myItems = new JSONObject();


                try {
                    myJson = new JSONObject(response);
                    myArray = (JSONArray) myJson.get("items");
                    myItems = (JSONObject) myArray.get(0);
                    Log.i("Response", myItems.toString());
                    textView.setText("Response:\n\n" + "Title: " + myItems.get("title") + "\nCategory: " + myItems.get("category") + "\nCode: " + myItems.get("ean"));
                    String curUser = sharedPreferences.getString("user", "None");

                    Map<String, Object> myScan = new HashMap<>();
                    myScan.put("ean", myItems.get("ean"));
                    myScan.put("material", "TODO");
                    myScan.put("user", curUser);

                    db.collection("barcodes").document() //Does there need to be something within document()?
                            .set(myScan)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid){
                                    Log.d("scanPush", "Successfully pushed scan!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("scanPush", "Error writing document", e);
                                }
                            });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
                //TODO: Make a nice response screen

            });
        }


     */
    }
