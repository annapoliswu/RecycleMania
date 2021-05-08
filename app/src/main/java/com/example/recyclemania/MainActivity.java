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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
    private EditText result;
    private EditText result2;
    SharedPreferences sharedPreferences;
    final Context context = this;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getPreferences(MODE_PRIVATE); // Access to SharedPreferences local storage


        //onRequestPermissionResult gets called after finishes
        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, ALL_PERMISSIONS);

        textView = findViewById(R.id.text_view);

        manualButton = findViewById(R.id.bt_scan2);
        manualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, Manual.class);
                startActivity(intent);
            }
        });

        result = (EditText) findViewById(R.id.userName);
        String userName = sharedPreferences.getString("user", "");
        result.setText("User: " + userName);

        //TODO: Make this updatePoints() or something
        result2 = (EditText) findViewById(R.id.userPoints);
        db.collection("users").whereEqualTo("name", userName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("GetUserPoints", document.getId() + " => " + document.getData());
                                result2.setText("Points: " + document.getData().get("points"));
                            }
                        } else {
                            Log.d("GetUserPoints", "Error getting documents: ", task.getException());
                        }
                    }
                });




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

                                        //TODO: Make this into fireStore.addUser("name")
                                        Log.d("userExists?", "Entering db query setup");
                                        db.collection("users").whereEqualTo("name", myResponse)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            if(task.getResult().isEmpty()){
                                                                //If user does not exist, add user to the database.
                                                                db.collection("users")
                                                                        .add(user)
                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {
                                                                                Log.d("DocSnippets", "DocumentSnapshot added with ID: " + documentReference.getId());
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.w("DocSnippets", "Error adding document", e);
                                                                            }
                                                                        });
                                                            }
                                                    } else {
                                                            Log.d("userExists?", "Error getting documents: ", task.getException());
                                                        }

                                                    }
                                                });

                                        //TODO: Update user points
                                        db.collection("users").whereEqualTo("name", myResponse)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                Log.d("GetUserPoints", document.getId() + " => " + document.getData());
                                                                result2.setText("Points: " + document.getData().get("points"));
                                                            }
                                                        } else {
                                                            Log.d("GetUserPoints", "Error getting documents: ", task.getException());
                                                        }
                                                    }
                                                });
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


        scanButton = findViewById(R.id.bt_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String persistentResponse = sharedPreferences.getString("response", "None");

//                // If a barcode has not been scanned and stored in SharedPreference, then activate scanning environment
//                if(persistentResponse == "None") {

                    Log.i("StartScan", "Response not present, entering Scan");

                    IntentIntegrator intentIntegrator = new IntentIntegrator(
                            MainActivity.this
                    );
                    intentIntegrator.setPrompt("For flash use volume up key");
                    intentIntegrator.setBeepEnabled(true);
                    intentIntegrator.setOrientationLocked(true);
                    intentIntegrator.setCaptureActivity(Capture.class); //this also has a permissions check I think
                    intentIntegrator.initiateScan();
//                }
//                else{ // Else, continue without having to scan and API request, using the same response that was stored during a previous run
//                    Log.i("StartScan", "Response already present, using SharedPreference stored response");
//                    updateResultScreen(persistentResponse);
//
//                }
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

            OkHttpClient client = new OkHttpClient();
            String url = "https://api.upcitemdb.com/prod/trial/lookup?upc=" + intentResult.getContents();
            //test url  https://reqres.in/api/users?page=2

            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("RIP REQUEST FAILED");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        String myResponse = response.body().string();

                        // Store the API response in SharedPreferences local memory, so that for development we dont have to constantly re-scan and make API requests
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("response", myResponse);
                        editor.commit();

                        updateResultScreen(myResponse);
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

    private void updateResultScreen(String response){



        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
    }
