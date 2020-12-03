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

public class SellListFragment extends Fragment {

    private Toolbar toolbar_sellListFragment;
    private RecyclerView recyclerView_sellListFragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sell_list_design, container, false);

        defineAttributes(rootView);

        return rootView;

    }


    /**
     * Gorsel nesneleri tanimla ...
     * @param rootView
     */
    public void defineAttributes(View rootView){

        toolbar_sellListFragment = rootView.findViewById(R.id.toolbar_sellListFragment);
        recyclerView_sellListFragment = rootView.findViewById(R.id.recyclerView_sellListFragment);

    }


}
