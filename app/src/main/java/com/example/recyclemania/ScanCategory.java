package com.example.recyclemania;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

public class ScanCategory extends RecyclingCategory{

    ScanCategory(RecyclingSub [] sub, Context co){
        super(sub, co);
        createFragment();
    }

    @Override
    public void createFragment(){
        frag = new FragmentItem(subcategories, (parent, view, position, id) -> {
            if(subcategories[position].recyclable == true){
                alertRecyclable(subcategories[position].name);
            }else{
                alertRecyclable(subcategories[position].name);
            }
        });
    }

    private void alertRecyclable(String text) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setTitle("Recycle Item?");
        dialog.setMessage("Barcode: "+ frag.barcode + " Category: " + frag.category + "Subcategory: " + text);
        dialog.setPositiveButton("Recycle",
                (dialog1, which) -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("category", frag.category);
                    bundle.putString("subcategory", text);
                    backToMain(bundle);
                });
        dialog.setNegativeButton("Back", (dialog12, which) -> {
            //Toast.makeText(getApplicationContext(),"cancel is clicked",Toast.LENGTH_LONG).show();
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    private void alertNotRecyclable() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setTitle("Not Recyclable");
        dialog.setMessage("Not Recyclable");
        dialog.setPositiveButton("Continue",
                (dialog1, which) -> backToMain());
        dialog.setNegativeButton("Back", (dialog12, which) -> {
            //Toast.makeText(getApplicationContext(),"cancel is clicked",Toast.LENGTH_LONG).show();
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

}
