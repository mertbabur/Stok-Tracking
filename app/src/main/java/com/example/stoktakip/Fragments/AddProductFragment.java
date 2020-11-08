package com.example.stoktakip.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.Models.Product;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.CaptureAct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.UUID;

public class AddProductFragment extends Fragment {

    private EditText editText_fragmentAddProduct_productName, editText_fragmentAddProduct_productCode, editText_fragmentAddProduct_unitPurchasePriceProduct
                     , editText_fragmentAddProduct_unitSellingPrice, editText_fragmentAddProduct_howManyUnit;
    private ImageView imageView_fragmentAddProduct_barcodeButton;
    private Button button_fragmentAddProduct_add;
    private RadioGroup radioGroup_fragmentAddProduct_typeProduct, radioGroup_who;
    private RadioButton radioButton_typeProduct_unit, radioButton_typePorduct_weight, radioButton_typePorduct_volume
                        , radioButton_who_me, radioButton_who_supplier;

    private String isSelectedWho = "notSelected";
    private String isSelectedTypeProduct = "notSelected";

    private String SUPPLIER_KEY;
    private String USER_UID;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private FirebaseAuth mAuth;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_product_design, container, false);

        defineAttributes(rootView);
        actionAttributes();

        return rootView;
    }


    /**
     * Gorsel nesneler tanimlandi ve baglandi .
     */
    public void defineAttributes(View rootView){

        editText_fragmentAddProduct_productName = rootView.findViewById(R.id.editText_fragmentAddProduct_productName);
        editText_fragmentAddProduct_productCode = rootView.findViewById(R.id.editText_fragmentAddProduct_productCode);
        editText_fragmentAddProduct_unitPurchasePriceProduct = rootView.findViewById(R.id.editText_fragmentAddProduct_unitPurchasePriceProduct);
        editText_fragmentAddProduct_unitSellingPrice = rootView.findViewById(R.id.editText_fragmentAddProduct_unitSellingPrice);
        editText_fragmentAddProduct_howManyUnit = rootView.findViewById(R.id.editText_fragmentAddProduct_howManyUnit);
        imageView_fragmentAddProduct_barcodeButton = rootView.findViewById(R.id.imageView_fragmentAddProduct_barcodeButton);
        button_fragmentAddProduct_add = rootView.findViewById(R.id.button_fragmentAddProduct_add);
        radioGroup_fragmentAddProduct_typeProduct = rootView.findViewById(R.id.radioGroup_fragmentAddProduct_typeProduct);
        radioGroup_who = rootView.findViewById(R.id.radioGroup_who);
        radioButton_typeProduct_unit = rootView.findViewById(R.id.radioButton_typeProduct_unit);
        radioButton_typePorduct_weight = rootView.findViewById(R.id.radioButton_typePorduct_weight);
        radioButton_typePorduct_volume = rootView.findViewById(R.id.radioButton_typePorduct_volume);
        radioButton_who_me = rootView.findViewById(R.id.radioButton_who_me);
        radioButton_who_supplier = rootView.findViewById(R.id.radioButton_who_supplier);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();
        USER_UID = mAuth.getUid();

    }


    /**
     * Gorsel nesnelerin action u tetiklenir .
     */
    public void actionAttributes(){

        // Product i kaydetme kismi .
        button_fragmentAddProduct_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setSelected_typeProduct();
                setSelected_who();

                if (!isSelectedTypeProduct.equals("notSelected") && ! isSelectedWho.equals("notSelected") && isFilled()){ // gerekli bilgiler eksiksiz dolduruldu mu ? ...

                    if (isSelectedWho.equals("Kendim Ekle")) // eger kendim ekle ise ...
                        saveProductDB();
                    else // eger tedarikciden ekle ise ...
                        isSupplierIfThereIsIt();

                }

            }
        });


        // Barkod okutma kismi ...
        imageView_fragmentAddProduct_barcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scanCode();

            }
        });

    }


    /**
     * Radio Group taki secilenleri değişkenlere atar .
     * Bos birakilamaz .
     */
    public void setSelected_typeProduct(){

        int selectId = radioGroup_fragmentAddProduct_typeProduct.getCheckedRadioButtonId();
        switch (selectId){
            case R.id.radioButton_typeProduct_unit:
                isSelectedTypeProduct = radioButton_typeProduct_unit.getText().toString();
                break;
            case R.id.radioButton_typePorduct_weight:
                isSelectedTypeProduct = radioButton_typePorduct_weight.getText().toString();
                break;
            case R.id.radioButton_typePorduct_volume:
                isSelectedTypeProduct = radioButton_typePorduct_volume.getText().toString();
                break;
        }

    }


    /**
     * Radio Group taki secilenleri değişkenlere atar .
     * Bos birakilamaz .
     */
    public void setSelected_who(){

        int selectId = radioGroup_who.getCheckedRadioButtonId();
        Log.e("se", selectId+"");
        switch (selectId){
            case R.id.radioButton_who_me:
                isSelectedWho = radioButton_who_me.getText().toString();
                break;
            case R.id.radioButton_who_supplier:
                isSelectedWho = radioButton_who_supplier.getText().toString();
                break;
        }

    }


    /**
     * Barkod okumak icin .
     */
    public void scanCode(){

        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();

    }


    /**
     * Barkoddan okunan kodu yakalar .
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null){

            String barcodeCode = result.getContents();
            editText_fragmentAddProduct_productCode.setText(barcodeCode);

        }

    }


    /**
     * Gerekli bilgiler eksiksiz dolduruldu mu ? .
     * @return -->dolduruldu ise (true) , eksik var ise (false) .
     */
    public boolean isFilled(){

        String name = editText_fragmentAddProduct_productName.getText().toString().trim();
        String purchasePrice = editText_fragmentAddProduct_unitPurchasePriceProduct.getText().toString().trim();
        String sellingPrice = editText_fragmentAddProduct_unitSellingPrice.getText().toString().trim();
        String productCode = editText_fragmentAddProduct_productCode.getText().toString().trim();
        String howManyUnit = editText_fragmentAddProduct_howManyUnit.getText().toString().trim();

        if (!name.equals("") && !purchasePrice.equals("") && !sellingPrice.equals("") && !productCode.equals("") && !howManyUnit.equals(""))
            return true;

        return false;

    }

    /**
     * Tedarikci radioButton u secili ise kayitli tedarikci var mi? .
     * Varsa --> tedarikci sec , Yoksa --> kullaniciya uyari gonder .
     * Secilen tedarikcinin supplierKey i alinacak .
     */
    public void isSupplierIfThereIsIt(){

        myRef.child("Suppliers").child(USER_UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                long supplierCount = snapshot.getChildrenCount();

                if (supplierCount != 0){
                   // musteri secme ekle ...
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**
     * Product i db ye kaydeder .
     */
    public void saveProductDB(){

        String name = editText_fragmentAddProduct_productName.getText().toString().trim();
        String purchasePrice = editText_fragmentAddProduct_unitPurchasePriceProduct.getText().toString().trim();
        String sellingPrice = editText_fragmentAddProduct_unitSellingPrice.getText().toString().trim();
        String howManyUnit = editText_fragmentAddProduct_howManyUnit.getText().toString().trim();
        String productCode = editText_fragmentAddProduct_productCode.getText().toString().trim();
        String productType = isSelectedTypeProduct;
        String from = isSelectedWho;
        String fromKey;
        if (isSelectedWho.equals("Kendim Ekle"))
            fromKey = FirebaseAuth.getInstance().getUid();
        else
            fromKey = ""; // daha sonra tedarikci keyini bul ve ekle .

        final String productKey = UUID.randomUUID().toString();

        Product product = new Product(name, productCode, purchasePrice, sellingPrice, howManyUnit, productType, from, fromKey);

        myRef.child("Products").child(productKey).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    saveProductKeyDB(productKey);
                }
            }
        });

    }


    /**
     * product i kullanici ekledi ise productKey i; kendine , tedarikciden ekledi ise tedarikci db ye ekler .
     * @param productKey
     */
    public void saveProductKeyDB(String productKey){

        if (isSelectedWho.equals("Kendim Ekle")){ // eger kendi eklerse ...
            myRef.child("Users").child(USER_UID).child("ProductsKey").child(productKey).setValue(productKey);
        }
        else{ // eger tedarikciden eklerse ...
            myRef.child("Suppliers").child(USER_UID).child(SUPPLIER_KEY).child("ProductsKey").child(productKey).setValue(productKey);
        }

    }


}