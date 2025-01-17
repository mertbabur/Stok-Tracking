package com.example.stoktakip.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    private Toolbar toolbar_fragmentDetailCustomer;

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
        defineToolbar();

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

        setHasOptionsMenu(true); // toolbar a menu eklemem icin gerekli .

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
        toolbar_fragmentDetailCustomer = rootView.findViewById(R.id.toolbar_fragmentDetailCustomer);

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


                if (WHICH_BUTTON.equals("customerButton"))
                    alertViewForDeleteCustomer("Müşterinizi");
                else
                    alertViewForDeleteCustomer("Tedarikçinizi");


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

                    String alertTitle = "ÖDEME AL";
                    String alertMessage = "Lütfen alınacak miktarı geçmeyecek şekilde tutarı giriniz .";

                    StockUtils.createAlertViewForGetPaid(WHICH_BUTTON, alertTitle, alertMessage,getActivity(), textInputEditText_alertView_getPaid_paidQuantity, textView_fragmentDetailCustomer_getPaid, textView_fragmentDetailCustomer_totalDebt, USER_UID, CUSTOMER_OR_SUPPLIER_KEY );
                }
                else{

                    String alertTitle = "ÖDEME YAP";
                    String alertMessage = "Lütfen ödenecek miktarı geçmeyecek şekilde giriniz .";

                    StockUtils.createAlertViewForGetPaid(WHICH_BUTTON, alertTitle, alertMessage,getActivity(), textInputEditText_alertView_getPaid_paidQuantity, textView_fragmentDetailCustomer_getPaid, textView_fragmentDetailCustomer_totalDebt, USER_UID, CUSTOMER_OR_SUPPLIER_KEY );

                }


            }
        });

    }

    /**
     * Toolbari tanimlar .
     */
    public void defineToolbar(){

        if (WHICH_BUTTON.equals("customerButton")) {
            toolbar_fragmentDetailCustomer.setTitle("Müşteri");
        }
        else {
            toolbar_fragmentDetailCustomer.setTitle("Tedarikçi");
        }

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar_fragmentDetailCustomer);

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

                try {// customer sildikten sonr burası null atar .
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
                catch (Exception e){
                    Log.e("setCusInfo", e.getMessage());
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


    public void deleteCustomerFromCustomerDB(){

        myRef.child("Customers").child(USER_UID).child(CUSTOMER_OR_SUPPLIER_KEY).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                    deleteCustomerFromSoldProductsDB();
                else {
                    Toast.makeText(getActivity(), "Silme işlevi başarısız oldu .", Toast.LENGTH_SHORT).show();
                    Log.e("deleteCusFroCusDB", task.getException().toString());
                }


            }
        });

    }

    /**
     * Customerlara satış yapılan urunleri siler .
     */
    public void deleteCustomerFromSoldProductsDB(){

        myRef.child("SoldProducts").child(USER_UID).child(CUSTOMER_OR_SUPPLIER_KEY).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "Silme işlemi başarılı .", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "Silme işlemi başarısız .", Toast.LENGTH_SHORT).show();
                    Log.e("deleteCusFroSoldDB", task.getException().toString());
                }

            }
        });


    }

    /**
     * Customer veya Supplier in profil resimlerini Storage dan siler .
     * @param whichDB
     * @param whichStorage
     */
    public void deleteCustomerOrSupplierPPFromStorage(String whichDB, final String whichStorage){

        myRef.child(whichDB).child(USER_UID).child(CUSTOMER_OR_SUPPLIER_KEY).child("photo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = snapshot.getValue().toString();

                if (!key.equals("null"))
                    FirebaseStorage.getInstance().getReference().child(whichStorage).child(USER_UID).child(key).delete();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * Customer silinmesi icin onay istenir .
     * deleteProduct metodunu cagirir .
     */
    public void alertViewForDeleteCustomer(String who){

        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(getActivity());

        alertDialogbuilder.setTitle("Bilgileri Onaylıyor Musunuz ?");
        alertDialogbuilder.setMessage(who + " silmek istediğinize emin misiniz ?");
        alertDialogbuilder.setIcon(R.drawable.warning_icon);

        alertDialogbuilder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (WHICH_BUTTON.equals("customerButton")){ // Customer silme kismi .

                    deleteCustomerOrSupplierPPFromStorage("Customers", "CustomersPictures");
                    deleteCustomerFromCustomerDB();


                }
                else{ // Supplier silme kismi .

                    deleteCustomerOrSupplierPPFromStorage("Suppliers", "SuppliersPictures");
                    deleteSupplierFromSuppDB();

                }

                imageView_fragmentDetailCustomer_customerCall.setVisibility(View.INVISIBLE);
                imageView_fragmentDetailCustomer_sendMessage.setVisibility(View.INVISIBLE);
                imageView_fragmentDetailCustomer_deleteCustomer.setVisibility(View.INVISIBLE);
                imageView_fragmentDetailCustomer_edit.setVisibility(View.INVISIBLE);
                textView_fragmentDetailCustomer_getPaid.setVisibility(View.INVISIBLE);

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
     * Supplier i DB den siler .
     */
    public void deleteSupplierFromSuppDB(){

        myRef.child("Suppliers").child(USER_UID).child(CUSTOMER_OR_SUPPLIER_KEY).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(getActivity(), "Silme işlemi başarılı .", Toast.LENGTH_SHORT).show();
            }
        });

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

            String infoText;
            if (WHICH_BUTTON.equals("customerButton")) {
                infoText = "\n- Bu kısımda müşteriniz ile ilgili bilgileri ve müşterinize sattığınız ürünleri görebilirsiniz .\n\n" +
                           "- 'Ödeme Al' butonunu kullanarak ödeme yapabilirsiniz .\n\n" +
                           "- 'Kalem' butonunu kullanarak müşteri bilgilerinizi güncelleyebilirsiniz .'\n\n" +
                           "- 'Çöp kutusu' butonunu kullanarak müşterinizi silebilirsiniz .\n\n" +
                           "- 'Telefon' butonunu kullanarak müşterinizi arayabilirsiniz .\n\n" +
                           "- 'Mesaj' butonunu kullanarak müşterinize mesaj gönderebilirsiniz .\n\n";
            }
            else {
                infoText = "\n- Bu kısımda tedarikçiniz ile ilgili bilgileri görebilirsiniz .\n\n" +
                        "- 'Ödeme Yap' butonunu kullanarak ödeme ekleyebilirsiniz .\n\n" +
                        "- 'Kalem' butonunu kullanarak tedarikçi bilgilerinizi güncelleyebilirsiniz .'\n\n" +
                        "- 'Çöp kutusu' butonunu kullanarak tedarikçinizi silebilirsiniz .\n\n" +
                        "- 'Telefon' butonunu kullanarak tedarikçinizi arayabilirsiniz .\n\n" +
                        "- 'Mesaj' butonunu kullanarak tedarikçinize mesaj gönderebilirsiniz .\n\n";
            }

            StockUtils.alertViewForInfo(getActivity(), infoText);

        }

        return super.onOptionsItemSelected(item);
    }

}
