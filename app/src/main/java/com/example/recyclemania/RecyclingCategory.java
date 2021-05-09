package com.example.recyclemania;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public abstract class RecyclingCategory {
    RecyclingSub [] subcategories;
    FragmentItem frag;
    Context context;

    RecyclingCategory(RecyclingSub [] sub, Context co){
        subcategories = sub;
        context = co;
    }

    abstract void createFragment();

    public void backToMain(){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public void backToMain(Bundle bundle){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public FragmentItem getFrag() {
        return frag;
    }
}
