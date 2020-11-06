package com.example.stoktakip.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.Models.Customer;
import com.example.stoktakip.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailCustomerFragment extends Fragment {

    private ImageView imageView_fragmentDetailCustomer_customerCall, imageView_fragmentDetailCustomer_sendMessage, imageView_fragmentDetailCustomer_customerPP
                      , imageView_fragmentDetailCustomer_deleteCustomer;
    private TextView textView_fragmentDetailCustomer_companyName, textView_fragmentDetailCustomer_customerName, textView_fragmentDetailCustomer_customerSurname
                     , textView_fragmentDetailCustomer_customerNum, textView_fragmentDetailCustomer_customerAddress;

    private String CUSTOMER_KEY;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_customer_design, container, false);

        defineAttributes(rootView);
        setCustomerInfo();
        actionAttributes();

        return rootView;
    }

    /**
     * Gorsel nesneler tanimlanir ve baglanir .
     */
    public void defineAttributes(View rootView){

        imageView_fragmentDetailCustomer_customerCall = rootView.findViewById(R.id.imageView_fragmentDetailCustomer_customerCall);
        imageView_fragmentDetailCustomer_sendMessage = rootView.findViewById(R.id.imageView_fragmentDetailCustomer_sendMessage);
        imageView_fragmentDetailCustomer_customerPP = rootView.findViewById(R.id.imageView_fragmentDetailCustomer_customerPP);
        textView_fragmentDetailCustomer_companyName = rootView.findViewById(R.id.textView_fragmentDetailCustomer_companyName);
        textView_fragmentDetailCustomer_customerName = rootView.findViewById(R.id.textView_fragmentDetailCustomer_customerName);
        textView_fragmentDetailCustomer_customerSurname = rootView.findViewById(R.id.textView_fragmentDetailCustomer_customerSurname);
        textView_fragmentDetailCustomer_customerNum = rootView.findViewById(R.id.textView_fragmentDetailCustomer_customerNum);
        textView_fragmentDetailCustomer_customerAddress = rootView.findViewById(R.id.textView_fragmentDetailCustomer_customerAddress);
        imageView_fragmentDetailCustomer_deleteCustomer = rootView.findViewById(R.id.imageView_fragmentDetailCustomer_deleteCustomer);

        CUSTOMER_KEY = getArguments().getString("customerKey", "bos customer key");

    }


    /**
     * Gorsel nesnelerin actionlari tetiklenir .
     */
    public void actionAttributes(){

        // arama kismi ...
        imageView_fragmentDetailCustomer_customerCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone();
            }
        });

        // mesaj gonderme kismi ...
        imageView_fragmentDetailCustomer_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // customer silme kismi ...
        imageView_fragmentDetailCustomer_deleteCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    

    /**
     * customerKey ile db den bilgileri getirir .
     * Customer bilgilerini gerekli gorsel nesnelere yerlestirir ...
     */
    public void setCustomerInfo(){

        final String userUID = FirebaseAuth.getInstance().getUid();

        FirebaseDatabase.getInstance().getReference().child("Customers").child(userUID).child(CUSTOMER_KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Customer customer = snapshot.getValue(Customer.class);

                textView_fragmentDetailCustomer_companyName.setText(customer.getCompanyName());
                textView_fragmentDetailCustomer_customerName.setText(customer.getCustomerName());
                textView_fragmentDetailCustomer_customerSurname.setText(customer.getCustomerSurname());
                textView_fragmentDetailCustomer_customerNum.setText(customer.getCustomerNum());
                textView_fragmentDetailCustomer_customerAddress.setText(customer.getCustomerAddress());


                String photoKey = customer.getCustomerPhoto();
                if (photoKey != "null")
                    setCustomerPP(photoKey, userUID);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * FirebaseStorage dan fotoyu gorsel nesneye yerlestirir .
     * @param photoKey
     */
    public void setCustomerPP(String photoKey, String userUID){

        FirebaseStorage.getInstance().getReference().child("CustomersPictures").child(userUID).child(photoKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).into(imageView_fragmentDetailCustomer_customerPP);

            }
        });

    }


    /**
     * Customer i arar ...
     */
    public void callPhone(){

        String customerNum = "tel:" + textView_fragmentDetailCustomer_customerNum.getText().toString().trim();

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(customerNum));

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CALL_PHONE}, 101 );
        else
            startActivity(callIntent);

    }

}
