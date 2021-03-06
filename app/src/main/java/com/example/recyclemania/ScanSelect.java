package com.example.recyclemania;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.AdapterView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ScanSelect extends AppCompatActivity {
    FragmentManager fragManager;
    String category;
    String barcode;
    String user;


    //if you change these, make sure to change the Map
    String[] categories = new String[]{"Paper", "Plastic" , "Glass", "Metal", "Other"};
    Map<String, ScanCategory> categoryMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Resources res = ScanSelect.this.getResources();

        ScanCategory plastic = new ScanCategory(
                new RecyclingSub[]{
                        new RecyclingSub("♻️1 - PET", "Soft drinks, bottles, juice containers, oil bottles.", res.getString(R.string.plastic1tip) ,true),
                        new RecyclingSub("♻️2 - HDPE", "Milk jugs, cleaning agents, laundry agents, shampoo bottles.", res.getString(R.string.plastic2tip),true),
                        new RecyclingSub("♻️3 - PVC", "Pipes, auto product bottles, fruit trays, food foils, plastic wrap.", false),
                        new RecyclingSub("♻️4 - LDPE", "Squeeze bottles, most bags, six-pack rings", res.getString(R.string.plastic4tip) , true),
                        new RecyclingSub("♻️5 - PP", "Auto parts, industrial fibres", res.getString(R.string.plastic5tip),true),
                        new RecyclingSub("♻️6 - PS", "Plastic utensils, styrofoam, cafeteria trays.", false),
                        new RecyclingSub("♻️7 - Other", "Any other plastics.", false),
                },
                ScanSelect.this
        );

        ScanCategory paper = new ScanCategory(
                new RecyclingSub[] {
                        new RecyclingSub("♻️20 - PAP", "Cardboard boxes.", res.getString(R.string.paper20tip), true),
                        new RecyclingSub("♻️21 - PAP", "Cereal and snack boxes.", res.getString(R.string.paper21tip), true),
                        new RecyclingSub("♻️22 - PAP", "Newspaper, books, magazines, wrapping paper, wallpaper, paper bags, paper straws.", getString(R.string.paper22tip), true),
                        new RecyclingSub("♻️81 - PAP/PET", "Consumer packaging, pet food bags, cold store grocery bags, icecream containers, cardboard cans, disposable plates.", false),
                        new RecyclingSub("♻️84 - C/PAP", "Liquid storage containers, juice boxes, cardboard cans, cigarette pack liners, wax paper, gum wrappers, cartridge shells for blanks, fireworks colouring material, Tetra Brik.", false),
                        new RecyclingSub("♻️87 - CSL", "Laminating material, special occasion cards, bookmarks, business cards, flyers/advertising.", false)
                },
                ScanSelect.this
        );

        //was not sure if we should include all the glass categories since they're all mostly recyclable
        ScanCategory glass = new ScanCategory(
                new RecyclingSub[] {
                        new RecyclingSub("♻️70-74 - GL", "Various color glass bottles.", res.getString(R.string.glass70tip), true),
                        new RecyclingSub("Other", "Window panes, mirrors, monitors.", false)
                },
                ScanSelect.this
        );
        ScanCategory metal = new ScanCategory(
                new RecyclingSub[] {
                        new RecyclingSub("♻️40 - FE", "Food cans.", res.getString(R.string.metal40tip), true),
                        new RecyclingSub("♻️41 - ALU", "Soft drink cans, deodorant cans, disposable food containers, aluminium foil, heat sinks.", res.getString(R.string.metal41tip), true),
                },
                ScanSelect.this
        );

        ScanCategory other = new ScanCategory(
                new RecyclingSub[] {
                        new RecyclingSub("E-waste", "Computers, phones, batteries.", res.getString(R.string.ewastetip), true),
                        new RecyclingSub("Organic", "Food waste, peels, egg shells, etc.", res.getString(R.string.organictip), true)
                },
                ScanSelect.this
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_select);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        categoryMap.put("Plastic", plastic);
        categoryMap.put("Paper", paper);
        categoryMap.put("Glass",glass);
        categoryMap.put("Metal",metal);
        categoryMap.put("Other",other);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            barcode = extras.getString("barcode");
            user = extras.getString("user");
        }

        if (savedInstanceState == null) {
            fragManager = getSupportFragmentManager();
            FragmentTransaction fragTransaction = fragManager.beginTransaction();

            AdapterView.OnItemClickListener categoryListener = (parent, view, position, id) -> {
                category = categories[position];
                Bundle bundle = new Bundle();
                bundle.putString("category", category);
                bundle.putString("barcode", barcode);
                bundle.putString("user", user);
                Fragment frag = categoryMap.get(category).getFrag();
                frag.setArguments(bundle);
                switchFrag(frag);
                //map.get gets the recycling item, can do more with this
                //to change return behavior to pass things to mainActivity, change fragment in ScanCategory class
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


    public void switchFrag(Fragment fragItem){
        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        fragTransaction.replace(R.id.fragment_container, fragItem);
        fragTransaction.addToBackStack(null);
        fragTransaction.commit();
    }


    private void backToMain(){
        Intent intent = new Intent(ScanSelect.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }




}