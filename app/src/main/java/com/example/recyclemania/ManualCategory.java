package com.example.recyclemania;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

public class ManualCategory extends RecyclingCategory {

    ManualCategory(RecyclingSub [] sub, Context co){
        super(sub, co);
        createFragment();
    }

    @Override
    public void createFragment(){
        frag = new FragmentItem(subcategories, (parent, view, position, id) -> {
            if(subcategories[position].recyclable == true){
                alertRecyclable(subcategories[position].tip);
            }else{
                alertNotRecyclable();
            }
        });
    }

    private void alertRecyclable(String text) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setMessage(text);
        dialog.setTitle("Recycle Item?");
        //ImageView check = (ImageView) findByViewId(R.id.green_check);
        dialog.setPositiveButton("Recycle",
                ((dialog1, which) -> {
                    Intent check = new Intent(context.getApplicationContext(), CheckSplash.class);
                    context.startActivity(check);
                }));
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
        dialog.setPositiveButton("Done",
                ((dialog1, which) -> {
                    Intent x = new Intent(context.getApplicationContext(), XSplash.class);
                    context.startActivity(x);
        }));
        dialog.setNegativeButton("Back", (dialog12, which) -> {
            //Toast.makeText(getApplicationContext(),"cancel is clicked",Toast.LENGTH_LONG).show();
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }



}
