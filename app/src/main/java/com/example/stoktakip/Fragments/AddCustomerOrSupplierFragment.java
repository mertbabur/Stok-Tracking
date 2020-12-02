package com.example.stoktakip.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.Models.CustomerOrSupplier;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.FirebaseUtils;
import com.example.stoktakip.Utils.StockUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AddCustomerOrSupplierFragment extends Fragment {

    private ImageView imageView_fragmentAddCustomer_customerPP;
    private TextView textView_fragmentAddCustomer_addCustomerPP;
    private EditText editText_fragmentAddCustomer__customerName, editText_fragmentAddCustomer_customerSurname
                     , editTextText_fragmentAddCustomer_companyName, editTextText_fragmentAddCustomer_customerNum
                     , editText_fragmentAddCustomer_customerAddress;
    private Button button_fragmentAddCustomer_save;

    private Uri getPhotoFromGalleryURI;

    private String WHICH_BUTTON;
    private String CUSTOMER_OR_SUPPLIER_KEY;

    private String USER_UID;

    private String PHOTO_KEY;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_customer_design,  container, false);

        defineAttributes(rootView);
        actionAttributes();

        if (WHICH_BUTTON.equals("editCustomerButton"))
            setInfoCustomerOrSupplier("Customers");
        else if (WHICH_BUTTON.equals("editSupplierButton"))
            setInfoCustomerOrSupplier("Suppliers");

        return rootView;

    }


    /**
     * Gorsel nesneler tanimlandi ve baglandi .
     */
    public void defineAttributes(View rootView){

        imageView_fragmentAddCustomer_customerPP = rootView.findViewById(R.id.imageView_fragmentAddCustomer_customerPP);
        textView_fragmentAddCustomer_addCustomerPP = rootView.findViewById(R.id.textView_fragmentAddCustomer_addCustomerPP);
        editText_fragmentAddCustomer__customerName = rootView.findViewById(R.id.editText_fragmentAddCustomer__customerName);
        editText_fragmentAddCustomer_customerSurname = rootView.findViewById(R.id.editText_fragmentAddCustomer_customerSurname);
        editTextText_fragmentAddCustomer_companyName = rootView.findViewById(R.id.editTextText_fragmentAddCustomer_companyName);
        editTextText_fragmentAddCustomer_customerNum = rootView.findViewById(R.id.editTextText_fragmentAddCustomer_customerNum);
        editText_fragmentAddCustomer_customerAddress = rootView.findViewById(R.id.editText_fragmentAddCustomer_customerAddress);
        button_fragmentAddCustomer_save = rootView.findViewById(R.id.button_fragmentAddCustomer_save);

        CUSTOMER_OR_SUPPLIER_KEY = getArguments().getString("customerOrSupplierKey", "bos customer or supplier key");
        WHICH_BUTTON = getArguments().getString("whichButton", "bos button");

        USER_UID = FirebaseAuth.getInstance().getUid();

    }



    /**
     * Gorsel nesnelerin action lari tetiklenir .
     */
    public void actionAttributes(){

        // resim secme kismi ...
        textView_fragmentAddCustomer_addCustomerPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });

        // kaydet kismi ...
        button_fragmentAddCustomer_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFilled()) {
                    if (WHICH_BUTTON.equals("customerButton") || WHICH_BUTTON.equals("supplierButton"))
                        saveDB();
                    else // edit customer veya supplier
                        updateDB();
                }
                else
                    Toast.makeText(getActivity(), "Lütfen bilgileri eksiksiz bir şekilde doldurunuz .", Toast.LENGTH_SHORT).show();

            }
        });

        /*// Customer Fragment a geri donme kismi ...
        imageView_fragmentAddCustomer_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomersOrSuppliersFragment customersOrSuppliersFragment = new CustomersOrSuppliersFragment();
                if (WHICH_BUTTON.equals("customerButton"))
                    StockUtils.gotoFragment(getActivity(), customersOrSuppliersFragment, R.id.frameLayoutEntryActivity_holder, "whichButton", "customerButton", 1);
                else if(WHICH_BUTTON.equals("supplierButton"))
                    StockUtils.gotoFragment(getActivity(), customersOrSuppliersFragment, R.id.frameLayoutEntryActivity_holder, "whichButton", "supplierButton", 1);
                else
                    StockUtils.gotoFragment(getActivity(), customersOrSuppliersFragment, R.id.frameLayoutEntryActivity_holder, "whichButton", "editButton", 0);

            }
        });*/

    }


    /**
     * Galeriden seçilen fotoyu yakalar .
     * Sadece galeriden seçim olacagi icin request koda gore herhangi bir yakalama yapmadik .
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        getPhotoFromGalleryURI = data.getData();

        if(getPhotoFromGalleryURI != null){
            Toast.makeText(getActivity(), "Fotoğraf seçildi .", Toast.LENGTH_SHORT).show();
            Picasso.get().load(getPhotoFromGalleryURI).into(imageView_fragmentAddCustomer_customerPP);
        }


    }


    /**
     * Gorsel nesnelerden bilgileri alir  ve DB ye kaydeder .
     * Secilen photo null degilse icerideki metod fotoyu da storage a kaydeder .
     */
    public void saveDB(){

        String name = editText_fragmentAddCustomer__customerName.getText().toString().trim();
        String surname = editText_fragmentAddCustomer_customerSurname.getText().toString().trim();
        String companyName = editTextText_fragmentAddCustomer_companyName.getText().toString().trim();
        String num = editTextText_fragmentAddCustomer_customerNum.getText().toString().trim();
        String address = editText_fragmentAddCustomer_customerAddress.getText().toString().trim();

        if (WHICH_BUTTON.equals("customerButton"))
            FirebaseUtils.addCustomerToDB(name, surname, companyName, num, address, getPhotoFromGalleryURI, 0);
        else if (WHICH_BUTTON.equals("supplierButton"))
            FirebaseUtils.addSupplierToDB(name, surname, companyName, num, address, getPhotoFromGalleryURI, 0);


        editText_fragmentAddCustomer__customerName.setText("");
        editText_fragmentAddCustomer_customerSurname.setText("");
        editTextText_fragmentAddCustomer_companyName.setText("");
        editTextText_fragmentAddCustomer_customerNum.setText("");
        editText_fragmentAddCustomer_customerAddress.setText("");

        getPhotoFromGalleryURI = null;

        imageView_fragmentAddCustomer_customerPP.setImageDrawable(getResources().getDrawable(R.drawable.default_pp_icon));

    }


    /**
     * Gerekli bilgiler eksiksiz bir sekilde dolduruldu mu .
     * @return --> Eger dolu ise (true) , bos ise (false)
     */
    public boolean isFilled(){

        String name = editText_fragmentAddCustomer__customerName.getText().toString().trim();
        String surname = editText_fragmentAddCustomer_customerSurname.getText().toString().trim();
        String companyName = editTextText_fragmentAddCustomer_companyName.getText().toString().trim();
        String num = editTextText_fragmentAddCustomer_customerNum.getText().toString().trim();
        String address = editText_fragmentAddCustomer_customerAddress.getText().toString().trim();

        if (!name.equals("") && !surname.equals("") && !companyName.equals("") && !num.equals("") && !address.equals(""))
            return true;

        return false;

    }


    /**
     * Düzenleme kismi icin bilgileri gerekli gorsel nesnelere yerlestirir .
     * @param whichDB
     */
    public void setInfoCustomerOrSupplier(String whichDB){

        FirebaseDatabase.getInstance().getReference().child(whichDB).child(USER_UID).child(CUSTOMER_OR_SUPPLIER_KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CustomerOrSupplier customerOrSupplier = snapshot.getValue(CustomerOrSupplier.class);

                editText_fragmentAddCustomer__customerName.setText(customerOrSupplier.getName());
                editText_fragmentAddCustomer_customerSurname.setText(customerOrSupplier.getSurname());
                editTextText_fragmentAddCustomer_companyName.setText(customerOrSupplier.getCompanyName());
                editTextText_fragmentAddCustomer_customerNum.setText(customerOrSupplier.getNum());
                editText_fragmentAddCustomer_customerAddress.setText(customerOrSupplier.getAddress());

                PHOTO_KEY = customerOrSupplier.getPhoto();
                if (PHOTO_KEY != null){
                    if (WHICH_BUTTON.equals("editCustomerButton"))
                        setCustomerOrSupplierPP("CustomersPictures", PHOTO_KEY);
                    else
                        setCustomerOrSupplierPP("SuppliersPictures", PHOTO_KEY);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * Duzenleme kismi icin fotoyu storage dan alip gerekli gorsel nesneye yerlestirir .
     * @param whichStorage
     * @param photoKey
     */
    public void setCustomerOrSupplierPP(String whichStorage, String photoKey){

        FirebaseStorage.getInstance().getReference().child(whichStorage).child(USER_UID).child(photoKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView_fragmentAddCustomer_customerPP);
            }
        });

    }


    public void updateDB(){

        String name = editText_fragmentAddCustomer__customerName.getText().toString().trim();
        String surname = editText_fragmentAddCustomer_customerSurname.getText().toString().trim();
        String companyName = editTextText_fragmentAddCustomer_companyName.getText().toString().trim();
        String num = editTextText_fragmentAddCustomer_customerNum.getText().toString().trim();
        String address = editText_fragmentAddCustomer_customerAddress.getText().toString().trim();

        Map map = new HashMap();
        map.put("name", name);
        map.put("surname", surname);
        map.put("companyName", companyName);
        map.put("num", num);
        map.put("address", address);



        if (WHICH_BUTTON.equals("editCustomerButton"))
            FirebaseDatabase.getInstance().getReference().child("Customers").child(USER_UID).child(CUSTOMER_OR_SUPPLIER_KEY).updateChildren(map);
        else if(WHICH_BUTTON.equals("editSupplierButton"))
            FirebaseDatabase.getInstance().getReference().child("Suppliers").child(USER_UID).child(CUSTOMER_OR_SUPPLIER_KEY).updateChildren(map);

        if (getPhotoFromGalleryURI != null){
            if (WHICH_BUTTON.equals("editCustomerButton")) {
                FirebaseUtils.deletePhotoFromFirebaseStorage("CustomersPictures", PHOTO_KEY);
                FirebaseUtils.savePhotoToFirebaseStorage(getPhotoFromGalleryURI, CUSTOMER_OR_SUPPLIER_KEY, "CustomersPictures", "Customers", "photo");
            }
            else if(WHICH_BUTTON.equals("editSupplierButton")) {
                FirebaseUtils.deletePhotoFromFirebaseStorage("SuppliersPictures", PHOTO_KEY);
                FirebaseUtils.savePhotoToFirebaseStorage(getPhotoFromGalleryURI, CUSTOMER_OR_SUPPLIER_KEY, "SuppliersPictures", "Suppliers", "photo");
            }
        }




    }



}
