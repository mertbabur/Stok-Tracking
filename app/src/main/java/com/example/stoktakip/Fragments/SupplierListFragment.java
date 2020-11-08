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

import com.example.stoktakip.Adapters.SupplierListAdapter;
import com.example.stoktakip.Models.CustomerOrSupplier;
import com.example.stoktakip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SupplierListFragment extends Fragment {

    private Toolbar toolbar_fragmentSupplierList;
    private RecyclerView recyclerView_fragmentSupplierList;

    private List<CustomerOrSupplier> supplierList;

    private SupplierListAdapter adapter;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private String name;
    private String purchasePrice;
    private String sellingPrice;
    private String howManyUnit;
    private String productCode;
    private String productType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_supplier_list_design, container, false);

        defineAttributes(rootView);

        getSupplierFromDB();

        return rootView;


    }

    /**
     * Gorsel nesneler baglandi ve tanimlandi .
     */
    public void defineAttributes(View rootView){
        toolbar_fragmentSupplierList = rootView.findViewById(R.id.toolbar_fragmentSupplierList);
        recyclerView_fragmentSupplierList = rootView.findViewById(R.id.recyclerView_fragmentSupplierList);

        supplierList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        name = getArguments().getString("name", "bos");
        purchasePrice = getArguments().getString("purchasePrice", "bos");;
        sellingPrice = getArguments().getString("sellingPrice", "bos");;
        howManyUnit = getArguments().getString("howManyUnit", "bos");;
        productCode = getArguments().getString("productCode", "bos");;
        productType = getArguments().getString("productType", "bos");;



    }

    /**
     * Customer icin recyclerView tanimlar .
     */
    public void defineRecyclerView(){

        recyclerView_fragmentSupplierList.setHasFixedSize(true);
        recyclerView_fragmentSupplierList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new SupplierListAdapter(getActivity(), supplierList, name, purchasePrice, sellingPrice, howManyUnit, productCode, productType);
        recyclerView_fragmentSupplierList.setAdapter(adapter);

    }



    public void getSupplierFromDB(){

        String userUID = FirebaseAuth.getInstance().getUid();

        myRef.child("Suppliers").child(userUID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                CustomerOrSupplier supplier = snapshot.getValue(CustomerOrSupplier.class);
                supplierList.add(supplier);

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
