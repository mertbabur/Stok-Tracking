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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProductsFragments extends Fragment {

    private Toolbar toolbar_fragmentProducts;
    private RecyclerView recyclerView_fragmentProducts;
    private FloatingActionButton floatingActionButton_fragmentProducts_addProduct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_products_design, container, false);

        defineAttributes(rootView);

        return rootView;

    }


    /**
     * Gorsel nesneler tanimlandi ve baglandi .
     */
    public void defineAttributes(View rootView){

        toolbar_fragmentProducts = rootView.findViewById(R.id.toolbar_fragmentProducts);
        recyclerView_fragmentProducts = rootView.findViewById(R.id.recyclerView_fragmentProducts);
        floatingActionButton_fragmentProducts_addProduct = rootView.findViewById(R.id.floatingActionButton_fragmentProducts_addProduct);

    }


    /**
     * Gorsel nesnelerin action u tetiklenir .
     */
    public void actionAttributes(){

        // product ekleme kismi ...
        floatingActionButton_fragmentProducts_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
