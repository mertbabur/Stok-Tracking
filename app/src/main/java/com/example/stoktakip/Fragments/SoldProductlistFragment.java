package com.example.stoktakip.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SoldProductlistFragment extends Fragment {

    private Toolbar toolbar_soldProductFragment;
    private RecyclerView recyclerView_soldProductFragment;

    private SoldProductAdapter adapter;
    private List<SoldProduct> soldProductList;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private FirebaseAuth mAuth;

    private String USER_UID;
    private String DEBT_CUSTOMER_KEY;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sold_product_design, container, false);

        defineAttributes(rootView);
        getSoldProductFromDB();
        return rootView;

    }


    /**
     * Gorsel nesneleri tanimla ...
     * @param rootView
     */
    public void defineAttributes(View rootView){

        toolbar_soldProductFragment = rootView.findViewById(R.id.toolbar_soldProductFragment);
        recyclerView_soldProductFragment = rootView.findViewById(R.id.recyclerView_soldProductFragment);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();

        soldProductList = new ArrayList();

        USER_UID = mAuth.getUid();
        DEBT_CUSTOMER_KEY = getArguments().getString("debtCustomerKey", "bos debtCustomer");
        Toast.makeText(getContext(), DEBT_CUSTOMER_KEY, Toast.LENGTH_SHORT).show();

    }

    /**
     * RecyclerView tanimla .
     */
    public void defineRecyclerView(){

        recyclerView_soldProductFragment.setHasFixedSize(true);
        recyclerView_soldProductFragment.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new SoldProductAdapter(getActivity(), soldProductList, USER_UID);

        recyclerView_soldProductFragment.setAdapter(adapter);

    }


    /**
     * DB den satilan urunleri alip
     */
    public void getSoldProductFromDB(){

        myRef.child("SoldProducts").child(USER_UID).child(DEBT_CUSTOMER_KEY).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                SoldProduct soldProduct = snapshot.getValue(SoldProduct.class);
                Log.e("urun:", soldProduct.getProductKey());
                soldProductList.add(soldProduct);
                defineRecyclerView();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }





}
