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
    String[] categories = new String[]{"Paper", "Plastic" , "Glass", "Metal", "Other"};
    Map<String, RecyclingCategory> categoryMap = new HashMap<>();
    String generalTip = "Rinse out any contaminants and recycle.";

    RecyclingCategory plastic = new RecyclingCategory(
            new RecyclingSub[]{
                    new RecyclingSub("♻️1 - PET", "Soft drinks, bottles, juice containers, oil bottles.", generalTip ,true),
                    new RecyclingSub("♻️2 - HDPE", "Milk jugs, cleaning agents, laundry agents, shampoo bottles.", generalTip,true),
                    new RecyclingSub("♻️3 - PVC", "Pipes, auto product bottles, fruit trays, food foils, plastic wrap.", false),
                    new RecyclingSub("♻️4 - LDPE", "Squeeze bottles, most bags, six-pack rings", "Clear any contaminants and recycle." , true),
                    new RecyclingSub("♻️5 - PP", "Auto parts, industrial fibres", "Is not recyclable in general waste bin. If recycling, please find an appropriate center.",true),
                    new RecyclingSub("♻️6 - PS", "Plastic utensils, styrofoam, cafeteria trays.", false),
                    new RecyclingSub("♻️7 - Other", "Any other plastics.", false),
            },
            Manual.this
    );

    RecyclingCategory paper = new RecyclingCategory(
            new RecyclingSub[] {
                    new RecyclingSub("♻️20 - PAP", "Cardboard boxes.", "Make sure there is no liquid or contaminant on the box by cutting those parts out.\n\nIf the box has too much contamination, throw it out. Flatten and recycle.", true),
                    new RecyclingSub("♻️21 - PAP", "Cereal and snack boxes.", "Make sure there is no remaining food on the box. Flatten and recycle.", true),
                    new RecyclingSub("♻️22 - PAP", "Newspaper, books, magazines, wrapping paper, wallpaper, paper bags, paper straws.", "Make sure there is no liquid or contaminant on the paper by cutting those parts out.\n\nIf the paper has too much contamination, throw it out.", true),
                    new RecyclingSub("♻️81 - PAP/PET", "Consumer packaging, pet food bags, cold store grocery bags, icecream containers, cardboard cans, disposable plates.", false),
                    new RecyclingSub("♻️84 - C/PAP", "Liquid storage containers, juice boxes, cardboard cans, cigarette pack liners, wax paper, gum wrappers, cartridge shells for blanks, fireworks colouring material, Tetra Brik.", false),
                    new RecyclingSub("♻️87 - CSL", "Laminating material, special occasion cards, bookmarks, business cards, flyers/advertising.", false)
            },
            Manual.this
    );

    //was not sure if we should include all the glass categories since they're all mostly recyclable
    RecyclingCategory glass = new RecyclingCategory(
            new RecyclingSub[] {
                    new RecyclingSub("♻️70-74 - GL", "Various color glass bottles.", "Rinse out any contaminants and recycle.", true),
                    new RecyclingSub("Other", "Window panes, mirrors, monitors.", false)
            },
        Manual.this
    );
    RecyclingCategory metal = new RecyclingCategory(
            new RecyclingSub[] {
                    new RecyclingSub("♻️40 - FE", "Food cans.", "Rinse out any contaminants. Remove the lid, place it inside the can, and recycle.", true),
                    new RecyclingSub("♻️41 - ALU", "Soft drink cans, deodorant cans, disposable food containers, aluminium foil, heat sinks.", "Rinse out any contaminants. Remove any tabs and recycle.", true),
            },
            Manual.this
    );

    RecyclingCategory other = new RecyclingCategory(
            new RecyclingSub[] {
                    new RecyclingSub("E-waste", "Computers, phones, batteries.", "Is not recyclable in general waste bin. If recycling, please find an appropriate center.", true),
                    new RecyclingSub("Organic", "Food waste, peels, egg shells, etc.", "Is not recyclable in general waste bin. If recycling, please try composting.", true)
            },
            Manual.this
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        categoryMap.put("Plastic", plastic);
        categoryMap.put("Paper", paper);
        categoryMap.put("Glass",glass);
        categoryMap.put("Metal",metal);
        categoryMap.put("Other",other);


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