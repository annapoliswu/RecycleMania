package com.example.recyclemania;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ScanCategory extends RecyclingCategory{

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ScanCategory(RecyclingSub [] sub, Context co){
        super(sub, co);
        createFragment();
    }

    @Override
    public void createFragment(){
        frag = new FragmentItem(subcategories, (parent, view, position, id) -> {
            alert(subcategories[position]);
        });
    }

    private void alert(RecyclingSub subcat) {
        Bundle bundle = new Bundle();
        bundle.putString("category", frag.category);
        bundle.putString("subcategory", subcat.name);

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Confirm");
        String mess = "Continue to log this item as\n"+ frag.category + " : " + subcat.name + " ?";
        dialog.setMessage(mess);
        dialog.setNegativeButton("Back", (dialog12, which) -> {
            //close confirm dialog
        });
        dialog.setPositiveButton("Confirm", (dialog12, which) -> {
            ProgressBar spinner = (ProgressBar)((Activity)context).findViewById(R.id.progressBar1);
            spinner.setVisibility(View.VISIBLE);

            OkHttpClient client = new OkHttpClient();
            String url = "https://api.upcitemdb.com/prod/trial/lookup?upc=" + frag.barcode;
            //test url  https://reqres.in/api/users?page=2

            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // textView.setText("RIP REQUEST FAILED");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String[] title = {"unknown"};
                    if(response.isSuccessful()){
                        try {
                            String myResponse = response.body().string();
                            JSONObject myJson = new JSONObject(myResponse);
                            JSONArray myArray = (JSONArray) myJson.get("items");
                            JSONObject myItems = (JSONObject) myArray.get(0);
                            title[0] = myItems.get("title").toString();

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override public void run() {

                            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                            dialog.setTitle("Item logged");
                            //dialog.setMessage("Title: "+ title[0] + "\nCategory: " + frag.category + "\nSubcategory: " + subcat.name + "\nBarcode: " +  frag.barcode);
                            if(subcat.recyclable){
                                dialog.setMessage("To recycle:\n" + subcat.tip);
                            }else{
                                dialog.setMessage("Item is not recyclable.");
                            }
                            logItem(title[0], frag.barcode,frag.category,subcat.name, subcat.recyclable, frag.user);
                            dialog.setPositiveButton("Recycle", (dialog1, which) -> {
                                Intent check = new Intent(context.getApplicationContext(), CheckSplash.class);
                                context.startActivity(check);
                            });
                            dialog.setNegativeButton("Back", (dialog12, which) -> {
                                backToMain(bundle);
                            });
                            spinner.setVisibility(View.INVISIBLE);
                            dialog.show();

                        }});
                }
            });
        });

        dialog.show();

    }


    private void logItem(String title, String barcode, String category, String material, Boolean recyclable, String user){
        Map<String, Object> myScan = new HashMap<>();
        myScan.put("barcode", barcode);
        myScan.put("title", title);
        myScan.put("category", category);
        myScan.put("material", material);
        myScan.put("recyclable", recyclable);
        myScan.put("user", user);


        db.collection("barcodes").document(barcode) //Does there need to be something within document()?
                .set(myScan)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid){
                        Log.d("scanPush", "Successfully pushed scan!");
                        Toast.makeText(context,
                                "Successfully logged item!.",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("scanPush", "Error writing document", e);
                    }
                });
    }
}
