package com.example.stoktakip.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.Adapters.SoldProductAdapter;
import com.example.stoktakip.Adapters.SummaryProductAdapter;
import com.example.stoktakip.Models.CustomerOrSupplier;
import com.example.stoktakip.Models.Product;
import com.example.stoktakip.Models.SoldProduct;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.StockUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailCustomerOrSupplierFragment extends Fragment {

    private ImageView imageView_fragmentDetailCustomer_customerCall, imageView_fragmentDetailCustomer_sendMessage, imageView_fragmentDetailCustomer_customerPP
                      , imageView_fragmentDetailCustomer_deleteCustomer, imageView_fragmentDetailCustomer_edit;
    private TextView textView_fragmentDetailCustomer_companyName, textView_fragmentDetailCustomer_customerName, textView_fragmentDetailCustomer_customerSurname
                     , textView_fragmentDetailCustomer_customerNum, textView_fragmentDetailCustomer_customerAddress, textView_fragmentDetailCustomer_totalDebt
                     , textView_fragmentDetailCustomer_title, textView_fragmentDetailCustomer_getPaid;
    private RecyclerView recyclerViewfragmentDetailCustomer;
    private TextInputEditText textInputEditText_alertView_getPaid_paidQuantity;

    private String CUSTOMER_OR_SUPPLIER_KEY;
    private String WHICH_BUTTON;
    private String USER_UID;

    private SoldProductAdapter soldProductAdapter;
    private SummaryProductAdapter productAdapter;
    private List<SoldProduct> soldProductList;
    private List<Product> productList;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private FirebaseAuth mAuth;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_customer_design, container, false);

        defineAttributes(rootView);
        actionAttributes();

        if (WHICH_BUTTON.equals("customerButton")) {
            setCustomerInfo("Customers");
            getSoldProductFromDB();
        }
        else {

            textView_fragmentDetailCustomer_getPaid.setText("ÖDEME YAP");
            textView_fragmentDetailCustomer_title.setText("SATIN ALINAN ÜRÜNLER");
            setCustomerInfo("Suppliers");
            getProductFromDB();
        }


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
        recyclerViewfragmentDetailCustomer = rootView.findViewById(R.id.recyclerViewfragmentDetailCustomer);
        textView_fragmentDetailCustomer_totalDebt = rootView.findViewById(R.id.textView_fragmentDetailCustomer_totalDebt);
        textView_fragmentDetailCustomer_title = rootView.findViewById(R.id.textView_fragmentDetailCustomer_title);
        textView_fragmentDetailCustomer_getPaid = rootView.findViewById(R.id.textView_fragmentDetailCustomer_getPaid);

        CUSTOMER_OR_SUPPLIER_KEY = getArguments().getString("customerOrSupplierKey", "bos customer or supplier key");
        WHICH_BUTTON = getArguments().getString("whichButton", "bos button ");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();

        soldProductList = new ArrayList();
        productList = new ArrayList<>();

        USER_UID = mAuth.getUid();

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

        // odeme alma kismi ...
        textView_fragmentDetailCustomer_getPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (WHICH_BUTTON.equals("customerButton")) {
                    StockUtils.createAlertViewForGetPaid(getActivity(), textInputEditText_alertView_getPaid_paidQuantity, textView_fragmentDetailCustomer_getPaid, textView_fragmentDetailCustomer_totalDebt, USER_UID, CUSTOMER_OR_SUPPLIER_KEY );
                }
                else{

                    /**
                     * BURADAN DEVAM ... Supplier icin odeme yapma kismi ...
                     */

                }


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

                if (WHICH_BUTTON.equals("customerButton"))
                    textView_fragmentDetailCustomer_totalDebt.setText("TOPLAM TAHSİL EDİLECEK TUTAR : " + customerOrSupplier.getTotalDebt() + " TL");
                else
                    textView_fragmentDetailCustomer_totalDebt.setText("TOPLAM ÖDENECEK TUTAR : " + customerOrSupplier.getTotalDebt() + " TL");

                String photoKey = customerOrSupplier.getPhoto();
                if (photoKey != "null") {
                    if (WHICH_BUTTON.equals("customerButton"))
                        setCustomerPP(photoKey, userUID, "CustomersPictures");
                    else
                        setCustomerPP(photoKey, userUID, "SuppliersPictures");
                }

                if (customerOrSupplier.getTotalDebt().equals("0.0")) // eger 0 ise yazi ve rengi degisir .
                    StockUtils.controlTotalDebt(textView_fragmentDetailCustomer_getPaid);

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


    /**
     * RecyclerView tanimla .
     */
    public void defineRecyclerView(){

        recyclerViewfragmentDetailCustomer.setHasFixedSize(true);
        recyclerViewfragmentDetailCustomer.setLayoutManager(new LinearLayoutManager(getActivity()));



        if (WHICH_BUTTON.equals("customerButton")) {
            soldProductAdapter = new SoldProductAdapter(getActivity(), soldProductList, USER_UID);
            recyclerViewfragmentDetailCustomer.setAdapter(soldProductAdapter);
        }
        else {
            productAdapter = new SummaryProductAdapter(getActivity(), productList, USER_UID);
            recyclerViewfragmentDetailCustomer.setAdapter(productAdapter);
        }
    }


    /**
     * DB den satilan urunleri alip
     */
    public void getSoldProductFromDB(){

        myRef.child("SoldProducts").child(USER_UID).child(CUSTOMER_OR_SUPPLIER_KEY).addChildEventListener(new ChildEventListener() {
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

    /**
     * Get Specific Supplier 's products from DB .
     */
    public void getProductFromDB(){

        myRef.child("Suppliers").child(USER_UID).child(CUSTOMER_OR_SUPPLIER_KEY).child("ProductsKey").addChildEventListener(new ChildEventListener() {
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


}
