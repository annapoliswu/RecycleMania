package com.example.recyclemania;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Manual extends AppCompatActivity {
    FragmentManager fragManager;

    //if you change these, make sure to change the Map
    String[] categories = new String[]{"Paper", "Plastic" , "Glass", "Metal", "Organic", "Other"};
    Map<String, RecyclingCategory> categoryMap = new HashMap<>();

    /*
    * Each recycling category item includes list of subcategories, subcategory descriptions, and tips
    * Lists must be of same length
    *
    RecyclingCategory plastic = new RecyclingCategory(
            new String[]{"♻️1 - PET", "♻️2 - HDPE", "♻️3 - PVC", "♻️4 - LDPE", "♻️5 - PP", "♻️6 - PS", "♻️7 - Other"},
            new String[]{
                    "soft drinks, bottles, juice containers, oil bottles",
                    "milk jugs, cleaning agents, laundry agents, shampoo bottles",
                    "pipes, auto product bottles, fruit trays, food foils, plastic wrap",
                    "squeeze bottles, most bags, six-pack rings",
                    "auto parts, industrial fibres",
                    "plastic utensils, styrofoam, cafeteria trays",
                    "any other plastics"
            },
            Manual.this
    );

    * */

    RecyclingCategory plastic = new RecyclingCategory(
            new RecyclingSub[]{
                    new RecyclingSub("♻️1 - PET", "soft drinks, bottles, juice containers, oil bottles", "specific tip" ,true),
                    new RecyclingSub("♻️2 - HDPE", "milk jugs, cleaning agents, laundry agents, shampoo bottles", true),
                    new RecyclingSub("♻️3 - PVC", "pipes, auto product bottles, fruit trays, food foils, plastic wrap", false),
                    new RecyclingSub("♻️4 - LDPE", "squeeze bottles, most bags, six-pack rings", true),
                    new RecyclingSub("♻️5 - PP", "auto parts, industrial fibres", true),
                    new RecyclingSub("♻️6 - PS", "plastic utensils, styrofoam, cafeteria trays", false),
                    new RecyclingSub("♻️7 - Other", "any other plastics", false),
            },
            Manual.this
    );

    RecyclingCategory paper;
    RecyclingCategory glass;
    RecyclingCategory metal;
    RecyclingCategory organic;
    RecyclingCategory other;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        categoryMap.put("Plastic", plastic);
        categoryMap.put("Paper",plastic);
        categoryMap.put("Glass",plastic);
        categoryMap.put("Metal",plastic);
        categoryMap.put("Organic",plastic);
        categoryMap.put("Other",plastic);


        if (savedInstanceState == null) {
            fragManager = getSupportFragmentManager();
            FragmentTransaction fragTransaction = fragManager.beginTransaction();

            AdapterView.OnItemClickListener categoryListener = (parent, view, position, id) -> {
                String category = categories[position];
                switchFrag(categoryMap.get(category).getFrag());
                //map.get gets the recycling item, can do more with this
                //to change return behavior to pass things to mainActivity, change fragment in RecyclingCategory class
            };

            fragTransaction.add(
                    R.id.fragment_container,
                    new FragmentItem(categories, categoryListener)).commit();

            // res.getStringArray(R.array.RecyclingCategories)
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if(fragManager.getBackStackEntryCount() == 0){
                backToMain();
            }else{
                fragManager.popBackStackImmediate();
            }
        });


    }


    public void switchFrag(FragmentItem fragItem){
        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        fragTransaction.replace(R.id.fragment_container, fragItem);
        fragTransaction.addToBackStack(null);
        fragTransaction.commit();
    }


    private void backToMain(){
        Intent intent = new Intent(Manual.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }




}