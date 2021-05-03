package com.example.recyclemania;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;

public class RecyclingCategory {
    RecyclingSub [] subcategories;
    FragmentItem frag;
    Context context;
    String[] names;
    String [] examples;
    String [] tips;

    RecyclingCategory(RecyclingSub [] sub, Context co){
        subcategories = sub;
        context = co;
        createFragment();
    }


    private void createFragment(){
        List<String> namesList = new ArrayList();
        List<String> examplesList = new ArrayList();
        List<String> tipsList = new ArrayList();
        for (RecyclingSub subcat : subcategories){
            namesList.add(subcat.name);
            examplesList.add(subcat.examples);
            tipsList.add(subcat.tip);
        }
        names = namesList.toArray(new String[0]);
        examples = examplesList.toArray(new String[0]);
        tips = tipsList.toArray(new String[0]);
        frag = new FragmentItem(names, examples, (parent, view, position, id) -> {
            if(subcategories[position].recyclable == true){
                alertRecyclable(tips[position]);
            }else{
                alertNotRecyclable();
            }
        });
    }

    private void alertRecyclable(String text) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setMessage(text);
        dialog.setTitle("Recycle Item?");
        dialog.setPositiveButton("Recycle",
                (dialog1, which) -> backToMain());
        dialog.setNegativeButton("Back", (dialog12, which) -> {
            //Toast.makeText(getApplicationContext(),"cancel is clicked",Toast.LENGTH_LONG).show();
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    private void alertNotRecyclable() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setMessage("This item is not recyclable.");
        dialog.setTitle("Not Recyclable");
        dialog.setNegativeButton("Back", (dialog12, which) -> {
            //Toast.makeText(getApplicationContext(),"cancel is clicked",Toast.LENGTH_LONG).show();
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    private void backToMain(){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public FragmentItem getFrag() {
        return frag;
    }
}
