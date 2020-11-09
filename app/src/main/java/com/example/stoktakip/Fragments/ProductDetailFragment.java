package com.example.stoktakip.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.Models.Product;
import com.example.stoktakip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductDetailFragment extends Fragment {

    private TextView textView_detailProductFragment_name, textView_detailProductFragment_code, textView_detailProductFragment_purchase
                    ,textView_detailProductFragment_selling, textView_detailProductFragment_quantity, textView_detailProductFragment_supplier;

    private String PRODUCT_KEY;
    private String USER_UID;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_product_design, container, false);

        defineAttributes(rootView);
        getProductsFromDB();

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

        PRODUCT_KEY = getArguments().getString("productKey", "bos product key");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();
        USER_UID = mAuth.getUid();

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
                    setCompanyName(product.getFromKey());
                else // user ekledi ise .
                    textView_detailProductFragment_supplier.setText("Kendi Stoğum");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * Supplier key ile supplier bilgilerine ulasir .
     * Company name i gerkli gorsel nesneye yerlestirir .
     * @param supplierKey
     */
    public void setCompanyName(String supplierKey){

        myRef.child("Suppliers").child(USER_UID).child(supplierKey).child("companyName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String companyName = snapshot.getValue().toString();
                textView_detailProductFragment_supplier.setText(companyName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
