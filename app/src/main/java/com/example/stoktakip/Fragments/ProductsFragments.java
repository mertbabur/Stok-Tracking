package com.example.stoktakip.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.Adapters.ProductListAdapter;
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
    private String WHICH_FRAGMENT;
    private String CUSTOMER_KEY;

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
        defineToolbar();
        getProductsFromDB();

        setHasOptionsMenu(true); // toolbar a menu eklemem icin gerekli .

        return rootView;

    }

    /**
     * Gorsel nesneler tanimlandi ve baglandi .
     */
    public void defineAttributes(View rootView){

        toolbar_fragmentProducts = rootView.findViewById(R.id.toolbar_fragmentProducts);
        recyclerView_fragmentProducts = rootView.findViewById(R.id.recyclerView_fragmentProducts);
        floatingActionButton_fragmentProducts_addProduct = rootView.findViewById(R.id.floatingActionButton_fragmentProducts_addProduct);

        WHICH_FRAGMENT = getArguments().getString("whichFragment", "bos button");
        CUSTOMER_KEY = getArguments().getString("customerKey", "bos customer key");
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

    /**
     * Toolbari tanimlar .
     */
    public void defineToolbar(){

        toolbar_fragmentProducts.setTitle("Ürünler");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar_fragmentProducts);

    }

    /**
     * Urunleri DB den alarak productList e yerlestirir.
     * defineRecyclerView metodunu cagirir .
     */
    public void getProductsFromDB(){

        myRef.child("Products").child(USER_UID).orderByChild("productName").addChildEventListener(new ChildEventListener() {
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

    /**
     * RecyclerView i tanimlar .
     */
    public void defineRecyclerView(){

        recyclerView_fragmentProducts.setHasFixedSize(true);
        recyclerView_fragmentProducts.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ProductListAdapter(getActivity(), productList, WHICH_FRAGMENT, CUSTOMER_KEY);
        recyclerView_fragmentProducts.setAdapter(adapter);

    }

    /**
     * Sell product butonuna basilarak gelindi ise butonu gosterme ...
     */
    public void closeFloatingButton(){

        if(WHICH_FRAGMENT.equals("sellProduct"))
            floatingActionButton_fragmentProducts_addProduct.setVisibility(View.INVISIBLE);

    }

    /**
     * Toolbar a menu eklemek icin .
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.toolbar_menu_for_info_design, menu);

    }

    /**
     * Toolbar daki itemlari yakalamak icin .
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.action_for_info_menu){

            String infoText = "\n- Bu kısımda stoğunuzda bulunan ürünleri görebilirsiniz .\n\n" +
                    "- (+) butonunu kullanarak stoğunuza yeni ürünler ekleyebilirsinniz .\n\n";

            StockUtils.alertViewForInfo(getActivity(), infoText);

        }

        return super.onOptionsItemSelected(item);
    }

}
