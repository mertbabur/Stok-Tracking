package com.example.stoktakip.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.Adapters.CustomerOrSupplierListAdapter;
import com.example.stoktakip.Adapters.SoldProductAdapter;
import com.example.stoktakip.Models.SoldProduct;
import com.example.stoktakip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SellListFragment extends Fragment {

    private Toolbar toolbar_sellListFragment;
    private RecyclerView recyclerView_sellListFragment;

    private SoldProductAdapter adapter;
    private List productList;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private FirebaseAuth mAuth;

    private String USER_UID;


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

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();

        productList = new ArrayList();

        USER_UID = mAuth.getUid();

    }

    /**
     * RecyclerView tanimla .
     */
    public void defineRecyclerView(){

        recyclerView_sellListFragment.setHasFixedSize(true);
        recyclerView_sellListFragment.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new SoldProductAdapter(getActivity(), productList);

        recyclerView_sellListFragment.setAdapter(adapter);

    }






}
