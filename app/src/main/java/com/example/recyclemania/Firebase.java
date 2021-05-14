package com.example.recyclemania;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Firebase extends AppCompatActivity{

    static private FirebaseFirestore db = FirebaseFirestore.getInstance();

    static void newUser(String name) {

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("points", 0);

        db.collection("users").whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                //If user does not exist, add user to the database.
                                db.collection("users").document(name).set(user);

//                                                                db.collection("users")
//                                                                        .add(user)
//                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                                                            @Override
//                                                                            public void onSuccess(DocumentReference documentReference) {
//                                                                                Log.d("DocSnippets", "DocumentSnapshot added with ID: " + documentReference.getId());
//                                                                            }
//                                                                        })
//                                                                        .addOnFailureListener(new OnFailureListener() {
//                                                                            @Override
//                                                                            public void onFailure(@NonNull Exception e) {
//                                                                                Log.w("DocSnippets", "Error adding document", e);
//                                                                            }
//                                                                        });
                            }
                        } else {
                            Log.d("userExists?", "User already exists, change nothing");
                        }

                    }
                });
    }

    static void incrementPoints(String name, int points){
        DocumentReference userRef = db.collection("users").document(name);
        userRef.update("points", FieldValue.increment(points));
    }

    static void updatePointsView(String name, TextView myView, SharedPreferences myPref){
        db.collection("users").whereEqualTo("name", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("GetUserPoints", document.getId() + " => " + document.getData());
                                myView.setText("Points: " + document.getData().get("points"));

                                SharedPreferences.Editor editor = myPref.edit();
                                editor.putLong("points", (Long) document.getData().get("points"));
                                editor.commit();

                            }
                        } else {
                            Log.d("GetUserPoints", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}
