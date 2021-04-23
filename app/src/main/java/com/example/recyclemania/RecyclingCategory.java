package com.example.recyclemania;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AlertDialog;

public class RecyclingCategory {
    String [] types;
    String [] descriptions;
    String [] tips;
    FragmentItem frag;
    Context context;

    RecyclingCategory(String[] ty, Context co){
        types = ty;
        int length = ty.length;
        descriptions =  new String[length];
        tips =  new String[length];
        for(int i = 0; i < length; i++){
            descriptions[i] = "blank description";
            tips[i] = "blank tip";

        }
        context = co;
        createFragment();
    }

    RecyclingCategory(String[] ty, String[] des, Context co){
        types = ty;
        descriptions = des;
        int length = ty.length;
        tips =  new String[length];
        for(int i = 0; i < length; i++){
            tips[i] = "blank tip";
        }
        context = co;
        createFragment();
    }

    RecyclingCategory(String[] ty, String[] des, String[] ti, Context co){
        types = ty;
        descriptions = des;
        tips = ti;
        context = co;
        createFragment();
    }

    private void createFragment(){
        frag = new FragmentItem(types, descriptions, (parent, view, position, id) -> alertDialog(tips[position]));
    }

    private void alertDialog(String text) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setMessage(text);
        dialog.setTitle("Recycle Item?");
        dialog.setPositiveButton("Recycle",
                (dialog1, which) -> backToMain());
        dialog.setNegativeButton("Cancel", (dialog12, which) -> {
            //Toast.makeText(getApplicationContext(),"cancel is clicked",Toast.LENGTH_LONG).show();
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    private void backToMain(){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public String[] getTypes() {
        return types;
    }

    public String[] getDescriptions() {
        return descriptions;
    }

    public String[] getTips() {
        return tips;
    }

    public String getType(int i){
        if(types != null && i < types.length){
            return types[i];
        }else{
            return null;
        }
    }

    public String getDescription(int i){
        if(descriptions != null && i < descriptions.length){
            return descriptions[i];
        }else{
            return null;
        }
    }
    public String getTips(int i){
        if(tips != null && i < tips.length){
            return tips[i];
        }else{
            return null;
        }
    }

    public FragmentItem getFrag() {
        return frag;
    }
}
