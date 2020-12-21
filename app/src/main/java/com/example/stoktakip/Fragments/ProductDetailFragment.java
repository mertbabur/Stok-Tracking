package com.example.stoktakip.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.Adapters.SoldProductAdapter;
import com.example.stoktakip.Adapters.SoldProductHistoryAdapter;
import com.example.stoktakip.Models.Product;
import com.example.stoktakip.Models.SoldProduct;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.FirebaseUtils;
import com.example.stoktakip.Utils.StockUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailFragment extends Fragment {

    private TextView textView_detailProductFragment_name, textView_detailProductFragment_code, textView_detailProductFragment_purchase
                    ,textView_detailProductFragment_selling, textView_detailProductFragment_quantity, textView_detailProductFragment_supplier;

    private ImageView imageView_detailProductFragment_delete ,imageView_detailProductFragment_modify;
    private RecyclerView recyclerView_detailProductFragment;

    private String PRODUCT_KEY;
    private String PRODUCT_CODE;
    private String USER_UID;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private FirebaseAuth mAuth;

    private List<SoldProduct> soldProductList;

    private SoldProductHistoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_product_design, container, false);

        defineAttributes(rootView);

        try {
            getProductsFromDB();
        }
        catch (Exception e){
            Log.e("getProfroDB", e.getMessage());

        }
        getSupplier(); // urun coduna sahip ayni urunleri bulmak icin .
        actionAttributes();
        return rootView;

    }


    /**
     * Gorsel nesneler tanimlandi ve baglandi .
     */
    public void defineAttributes(View rootView){

        textView_detailProductFragment_name = rootView.findViewById(R.id.textView_detailProductFragment_name);
        textView_detailProductFragment_code = rootView.findViewById(R.id.textView_detailProductFragment_code);
        textView_detailProductFragment_purchase = rootView.findViewById(R.id.textView_detailProductFragment_purchase);
        textView_detailProductFragment_selling = rootView.findViewById(R.id.textView_detailProductFragment_selling);
        textView_detailProductFragment_quantity = rootView.findViewById(R.id.textView_detailProductFragment_quantity);
        textView_detailProductFragment_supplier = rootView.findViewById(R.id.textView_detailProductFragment_supplier);
        imageView_detailProductFragment_delete = rootView.findViewById(R.id.imageView_detailProductFragment_delete);
        imageView_detailProductFragment_modify = rootView.findViewById(R.id.imageView_detailProductFragment_modify);
        recyclerView_detailProductFragment = rootView.findViewById(R.id.recyclerView_detailProductFragment);

        PRODUCT_KEY = getArguments().getString("productKey", "bos product key");
        PRODUCT_CODE = getArguments().getString("productCode", "bos product code");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();
        USER_UID = mAuth.getUid();

        soldProductList = new ArrayList<>();

    }


    /**
     * Gorsel nesnlerin action u tetiklenir .
     */
    public void actionAttributes(){

        // Product silme kismi ...
        imageView_detailProductFragment_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertViewForDeleteProduct();

            }
        });


        // Product duzenleme kismi ...
        imageView_detailProductFragment_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddProductFragment addProductFragment = new AddProductFragment();
                StockUtils.gotoFragment(getActivity(), addProductFragment, R.id.frameLayoutEntryActivity_holder, "productKey", PRODUCT_KEY, "whichFragment", "productDetailFragment", 1);

            }
        });


    }


    /**
     * Urun silinmesi icin onay istenir .
     * deleteProduct metodunu cagirir .
     */
    public void alertViewForDeleteProduct(){

        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(getActivity());

        alertDialogbuilder.setTitle("Bilgileri Onaylıyor Musunuz ?");
        alertDialogbuilder.setMessage("Bu ürünü silmek istediğinize emin misiniz ?");
        alertDialogbuilder.setIcon(R.drawable.warning_icon);

        alertDialogbuilder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUtils.deleteProduct(getActivity(), null, USER_UID, PRODUCT_KEY);
                imageView_detailProductFragment_delete.setVisibility(View.INVISIBLE);
                imageView_detailProductFragment_modify.setVisibility(View.INVISIBLE);
            }
        });

        alertDialogbuilder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialogbuilder.create().show();

    }


    /**
     * RecyclerView tanimla .
     */
    public void defineRecyclerView(){

        recyclerView_detailProductFragment.setHasFixedSize(true);
        recyclerView_detailProductFragment.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new SoldProductHistoryAdapter(getActivity(), soldProductList, USER_UID);

        recyclerView_detailProductFragment.setAdapter(adapter);

    }


    /**
     * Product Key ile product bilgilerine ulasir .
     * Product bilgilerini gerekli gorsel nesnelere yerlestirir .
     */
    public void getProductsFromDB(){

        myRef.child("Products").child(USER_UID).child(PRODUCT_KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Product product = snapshot.getValue(Product.class);

                try { // silme isleminden sonra burada exception atar .
                    textView_detailProductFragment_name.setText(product.getProductName());
                    textView_detailProductFragment_purchase.setText(product.getPurchasePrice());
                    textView_detailProductFragment_selling.setText(product.getSellingPrice());
                    textView_detailProductFragment_code.setText(product.getProductCode());

                    if (product.getTypeProduct().equals("Ağırlık"))
                        textView_detailProductFragment_quantity.setText(product.getHowManyUnit() + " Kilo");
                    else if (product.getTypeProduct().equals("Adet"))
                        textView_detailProductFragment_quantity.setText(product.getHowManyUnit() + " Adet");
                    else
                        textView_detailProductFragment_quantity.setText(product.getHowManyUnit() + " Litre");

                    if (product.getFrom().equals("Tedarikçiden Ekle")) // eger tedarikciden ise .
                        textView_detailProductFragment_supplier.setText(product.getCompanyName());
                    else // user ekledi ise .
                        textView_detailProductFragment_supplier.setText("Kendi Stoğum");

                }
                catch (Exception e){
                    Log.e("getProFroDB", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * DB deki tum supplierlari getirir .
     * getSoldProduct metodunu cagirir . --> satilan urunleri bulur .
     */
    public void getSupplier(){

        myRef.child("SoldProducts").child(USER_UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String supplierKey = postSnapshot.getKey();
                    getSoldProduct(supplierKey);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * Belli urun koduna uyan urunleri liste atar .
     * @param supplierKey
     */
    public void getSoldProduct(String supplierKey){

        myRef.child("SoldProducts").child(USER_UID).child(supplierKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()){

                    SoldProduct soldProduct = postSnapshot.getValue(SoldProduct.class);
                    Log.e("kontrol", soldProduct.getProductKey());


                    if (PRODUCT_CODE.trim().equals(soldProduct.getProductCode().trim()))
                        soldProductList.add(soldProduct);

                }

                defineRecyclerView();
                Log.e("sdfsdfsdfsd", String.valueOf(soldProductList.size()));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }






}
