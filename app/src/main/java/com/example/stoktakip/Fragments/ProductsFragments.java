package com.example.stoktakip.Fragments;

import android.os.Bundle;
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

import com.example.stoktakip.Adapters.ProductListAdapter;
import com.example.stoktakip.Adapters.SupplierListAdapter;
import com.example.stoktakip.Models.Product;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.StockUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragments extends Fragment {

    private Toolbar toolbar_fragmentProducts;
    private RecyclerView recyclerView_fragmentProducts;
    private FloatingActionButton floatingActionButton_fragmentProducts_addProduct;

    private String USER_UID;
    private String WHICH_BUTTON;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private List<Product> productList;

    private ProductListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_products_design, container, false);

        defineAttributes(rootView);
        closeFloatingButton();
        actionAttributes();

        getProductsFromDB();

        return rootView;

    }


    /**
     * Gorsel nesneler tanimlandi ve baglandi .
     */
    public void defineAttributes(View rootView){

        toolbar_fragmentProducts = rootView.findViewById(R.id.toolbar_fragmentProducts);
        recyclerView_fragmentProducts = rootView.findViewById(R.id.recyclerView_fragmentProducts);
        floatingActionButton_fragmentProducts_addProduct = rootView.findViewById(R.id.floatingActionButton_fragmentProducts_addProduct);

        WHICH_BUTTON = getArguments().getString("whichButton", "bos button");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        USER_UID = FirebaseAuth.getInstance().getUid();

        productList = new ArrayList<>();

    }


    /**
     * Gorsel nesnelerin action u tetiklenir .
     */
    public void actionAttributes(){

        // product ekleme kismi ...
        floatingActionButton_fragmentProducts_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddProductFragment addProductFragment = new AddProductFragment();
                StockUtils.gotoFragment(getActivity(), addProductFragment, R.id.frameLayoutEntryActivity_holder, 1);

            }
        });

    }


    public void getProductsFromDB(){

        myRef.child("Products").child(USER_UID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Product product = snapshot.getValue(Product.class);
                productList.add(product);

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


    public void defineRecyclerView(){

        recyclerView_fragmentProducts.setHasFixedSize(true);
        recyclerView_fragmentProducts.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ProductListAdapter(getActivity(), productList, WHICH_BUTTON);
        recyclerView_fragmentProducts.setAdapter(adapter);

    }


    /**
     * Sell product butonuna basilarak gelindi ise butonu gosterme ...
     */
    public void closeFloatingButton(){

        floatingActionButton_fragmentProducts_addProduct.setVisibility(View.INVISIBLE);

    }


}
