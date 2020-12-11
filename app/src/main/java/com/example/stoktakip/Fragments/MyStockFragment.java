package com.example.stoktakip.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.R;

public class MyStockFragment extends Fragment {

    private Toolbar toolbar_myStock;
    private RecyclerView recyclerView_myStock;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_stock_design, container, false);

        defineAttributes(rootView);

        return rootView;

    }


    /**
     * Gorsel nesneler burada baglanir .
     */
    public void defineAttributes(View rootView){

        toolbar_myStock = rootView.findViewById(R.id.toolbar_myStock);
        recyclerView_myStock = rootView.findViewById(R.id.recyclerView_myStock);

    }





}
