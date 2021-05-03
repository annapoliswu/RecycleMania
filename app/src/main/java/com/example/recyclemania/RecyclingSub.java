package com.example.recyclemania;

import android.content.Context;

public class RecyclingSub {
    public String name;
    public String examples;
    public String tip = "Blank tip";
    Boolean recyclable;

    RecyclingSub(String name, String examples, String tip, Boolean isrecyclable) {
        this.name = name;
        this.examples = examples;
        this.tip = tip;
        this.recyclable = isrecyclable;
    }

    RecyclingSub(String name, String examples, Boolean isrecyclable) {
        this.name = name;
        this.examples = examples;
        this.recyclable = isrecyclable;
    }


}
