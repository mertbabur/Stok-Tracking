package com.example.stoktakip.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.Models.CustomerOrSupplier;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.StockUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class DetailCustomerOrSupplierFragment extends Fragment {

    private ImageView imageView_fragmentDetailCustomer_customerCall, imageView_fragmentDetailCustomer_sendMessage, imageView_fragmentDetailCustomer_customerPP
                      , imageView_fragmentDetailCustomer_deleteCustomer, imageView_fragmentDetailCustomer_edit;
    private TextView textView_fragmentDetailCustomer_companyName, textView_fragmentDetailCustomer_customerName, textView_fragmentDetailCustomer_customerSurname
                     , textView_fragmentDetailCustomer_customerNum, textView_fragmentDetailCustomer_customerAddress;

    private String CUSTOMER_OR_SUPPLIER_KEY;
    private String WHICH_BUTTON;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_customer_design, container, false);

        defineAttributes(rootView);

        if (WHICH_BUTTON.equals("customerButton"))
            setCustomerInfo("Customers");
        else
            setCustomerInfo("Suppliers");

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
        imageView_fragmentDetailCustomer_edit = rootView.findViewById(R.id.imageView_fragmentDetailCustomer_edit);

        CUSTOMER_OR_SUPPLIER_KEY = getArguments().getString("customerOrSupplierKey", "bos customer or supplier key");
        WHICH_BUTTON = getArguments().getString("whichButton", "bos button ");

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
                openSMS(textView_fragmentDetailCustomer_customerNum.getText().toString());
            }
        });

        // customer silme kismi ...
        imageView_fragmentDetailCustomer_deleteCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // edit kismi ...
        imageView_fragmentDetailCustomer_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddCustomerOrSupplierFragment addCustomerOrSupplierFragment = new AddCustomerOrSupplierFragment();
                if (WHICH_BUTTON.equals("customerButton"))
                    StockUtils.gotoFragment(getActivity(), addCustomerOrSupplierFragment, R.id.frameLayoutEntryActivity_holder, "whichButton", "editCustomerButton", "customerOrSupplierKey", CUSTOMER_OR_SUPPLIER_KEY, 1);
                else
                    StockUtils.gotoFragment(getActivity(), addCustomerOrSupplierFragment, R.id.frameLayoutEntryActivity_holder, "whichButton", "editSupplierButton", "customerOrSupplierKey", CUSTOMER_OR_SUPPLIER_KEY, 1);

            }
        });

    }


    /**
     * customerKey ile db den bilgileri getirir .
     * Customer bilgilerini gerekli gorsel nesnelere yerlestirir ...
     */
    public void setCustomerInfo(String whichDB){

        final String userUID = FirebaseAuth.getInstance().getUid();

        FirebaseDatabase.getInstance().getReference().child(whichDB).child(userUID).child(CUSTOMER_OR_SUPPLIER_KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CustomerOrSupplier customerOrSupplier = snapshot.getValue(CustomerOrSupplier.class);

                textView_fragmentDetailCustomer_companyName.setText(customerOrSupplier.getCompanyName());
                textView_fragmentDetailCustomer_customerName.setText(customerOrSupplier.getName());
                textView_fragmentDetailCustomer_customerSurname.setText(customerOrSupplier.getSurname());
                textView_fragmentDetailCustomer_customerNum.setText(customerOrSupplier.getNum());
                textView_fragmentDetailCustomer_customerAddress.setText(customerOrSupplier.getAddress());


                String photoKey = customerOrSupplier.getPhoto();
                if (photoKey != "null") {
                    if (WHICH_BUTTON.equals("customerButton"))
                        setCustomerPP(photoKey, userUID, "CustomersPictures");
                    else
                        setCustomerPP(photoKey, userUID, "SuppliersPictures");
                }
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
    public void setCustomerPP(String photoKey, String userUID, String whichStorage){
        Log.e("keyyy", photoKey);
        FirebaseStorage.getInstance().getReference().child(whichStorage).child(userUID).child(photoKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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


    /**
     * Sms ekranini acar .
     * @param phone
     */
    public void openSMS(String phone){

        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse("smsto:" + phone));
        getActivity().startActivity(smsIntent);

    }

}
