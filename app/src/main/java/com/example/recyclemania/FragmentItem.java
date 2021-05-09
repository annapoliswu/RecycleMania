package com.example.recyclemania;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.FragmentManager;
import android.app.FragmentTransaction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

 public class FragmentItem extends Fragment{
     String[] list1;
     String[] list2;
     String[][] list2D;
     RecyclingSub[] listSub;
     String category;
     String barcode = "init";

     AdapterView.OnItemClickListener clickListener;

     public FragmentItem(){ //need default else error
         super(R.layout.fragment_item);
         list1 = new String[]{"No list input"};
     }

     /**
      * @param items a list of items you want to display
      * @param cl listener with action of what to do on each item click
      */
    public FragmentItem(String[] items,  AdapterView.OnItemClickListener cl){
        super(R.layout.fragment_item);
        list1 = items;
        clickListener = cl;
    }

     /**
      * @param items list of items you want displayed
      * @param descriptions list of descriptions of the items, list must be same length
      */
     public FragmentItem(String[] items, String[] descriptions,  AdapterView.OnItemClickListener cl){
         super(R.layout.fragment_item);
         list1 = items;
         list2 = descriptions;
         clickListener = cl;
     }

     /**
      * @param items 2D list of items with [item name][description]
      */
     public FragmentItem(String[][] items, AdapterView.OnItemClickListener cl){
         super(R.layout.fragment_item);
         list2D = items;
         clickListener = cl;
     }

     /**
      * @param items a list of RecyclingSub
      */
     public FragmentItem(RecyclingSub[] items, AdapterView.OnItemClickListener cl){
         super(R.layout.fragment_item);
         listSub = items;
         clickListener = cl;
     }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
         if(args != null){
             if(args.getString("category") != null) {
                 category = args.getString("category");
             }
             if(args.getString("barcode") != null) {
                 barcode = args.getString("barcode");
             }

         }
        return inflater.inflate(R.layout.fragment_item, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = (ListView)view.findViewById(R.id.itemlist);
        ArrayAdapter adapter;

        if(listSub != null){
            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, listSub) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                    text1.setText(listSub[position].name);
                    text2.setText(listSub[position].examples);
                    return view;
                }
            };
        }else if(list2D != null){
            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, list2D) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                    text1.setText(list2D[position][0]);
                    text2.setText(list2D[position][1]);
                    return view;
                }
            };
        }else if( (list1 != null && list2 != null) && (list1.length == list2.length ) ){
            adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, list1) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                    text1.setText(list1[position]);
                    text2.setText(list2[position]);
                    return view;
                }
            };
        }else{
            adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, list1);
        }

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(clickListener);
    }

/*
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "" + list[position] + ": " + position, Toast.LENGTH_SHORT).show();

        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new FragmentItem(new String[]{"aaa","Adsada"})
        ).commit();
    }
    */
}
