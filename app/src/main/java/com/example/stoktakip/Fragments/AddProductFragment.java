package com.example.stoktakip.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.Models.CashDesk;
import com.example.stoktakip.Models.Product;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.CaptureAct;
import com.example.stoktakip.Utils.StockUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddProductFragment extends Fragment {

    private EditText editText_fragmentAddProduct_productName, editText_fragmentAddProduct_productCode, editText_fragmentAddProduct_unitPurchasePriceProduct
                     , editText_fragmentAddProduct_unitSellingPrice, editText_fragmentAddProduct_howManyUnit, editText_fragmentAddProduct_supplierName;
    private ImageView imageView_fragmentAddProduct_barcodeButton;
    private Button button_fragmentAddProduct_add;
    private RadioGroup radioGroup_fragmentAddProduct_typeProduct, radioGroup_who;
    private RadioButton radioButton_typeProduct_unit, radioButton_typePorduct_weight, radioButton_typePorduct_volume
                        , radioButton_who_me, radioButton_who_supplier;
    private TextView textView_fragmentAddProduct_selectSupplier, textView_fragmentAddProduct_clearAttributes;

    private String isSelectedWho = "notSelected";
    private String isSelectedTypeProduct = "notSelected";

    private String SUPPLIER_KEY;
    private String USER_UID;
    private String COMPANY_NAME;
    private String PRODUCT_KEY;
    private String SUPPLIER_KEY_FOR_MODIFY_PRODUCT;
    private String PRODUCT_WHO;
    private String WHICH_FRAGMENT;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private FirebaseAuth mAuth;

    private String oldProductPurchasedPrice = "";
    private String oldProdutQuantity = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_product_design, container, false);

        defineAttributes(rootView);

        if (WHICH_FRAGMENT != null && WHICH_FRAGMENT.equals("supplierListFragment")) {

            getSupplierKeyFromComeFragment();
            setSupplierIfCameSupplierListFragment();
        }
        else if ((WHICH_FRAGMENT != null && WHICH_FRAGMENT.equals("productDetailFragment")))
            setSupplierIfCameProdcutDetailFragment();


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
        editText_fragmentAddProduct_supplierName = rootView.findViewById(R.id.editText_fragmentAddProduct_supplierName);
        textView_fragmentAddProduct_selectSupplier = rootView.findViewById(R.id.textView_fragmentAddProduct_selectSupplier);
        textView_fragmentAddProduct_clearAttributes = rootView.findViewById(R.id.textView_fragmentAddProduct_clearAttributes);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();
        USER_UID = mAuth.getUid();

        try{
            WHICH_FRAGMENT = getArguments().getString("whichFragment", "bos fragment");
        }catch (Exception e){
            Log.e("defineAttr", e.getMessage());
        }


    }


    /**
     * Gorsel nesnelerin action u tetiklenir .
     */
    public void actionAttributes(){

        // Product i kaydetme kismi .
        button_fragmentAddProduct_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(PRODUCT_KEY == null) {

                    setSelected_typeProduct();
                    setSelected_who();

                    if (!isSelectedTypeProduct.equals("notSelected") && !isSelectedWho.equals("notSelected") && isFilled()) { // gerekli bilgiler eksiksiz dolduruldu mu ? ...

                        //isEarlierAddedProductCode();

                        SUPPLIER_KEY = getActivity().getSharedPreferences("tedarikciKey", Context.MODE_PRIVATE).getString("supplierKey", "bos key");
                        COMPANY_NAME = getActivity().getSharedPreferences("tedarikciKey", Context.MODE_PRIVATE).getString("companyName", "bos key");


                        saveProductDB();
                        updateTotalPurchasedProductPrice();

                        Toast.makeText(getActivity(), "Ürün başaralı bir şekilde eklendi .", Toast.LENGTH_SHORT).show();

                        ProductsFragments productsFragments = new ProductsFragments();
                        StockUtils.gotoFragment(getActivity(), productsFragments, R.id.frameLayoutEntryActivity_holder, "whichFragment", "addProduct", 0);

                    }
                    else
                        Toast.makeText(getActivity(), "Lütfen bilgileri eksiksiz giriniz .", Toast.LENGTH_SHORT).show();
                }
                else{
                    setSelected_typeProduct();
                    updateProduct();

                    String productCode = editText_fragmentAddProduct_productCode.getText().toString();
                    ProductDetailFragment productDetailFragment =new ProductDetailFragment();
                    StockUtils.gotoFragment(getActivity(), productDetailFragment, R.id.frameLayoutEntryActivity_holder, "productKey", PRODUCT_KEY, "productCode", productCode, 0);


                    Toast.makeText(getActivity(), "Ürün başaralı bir şekilde güncellendi .", Toast.LENGTH_SHORT).show();

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

        // Tedarikci secme sayfasini acar ...
        textView_fragmentAddProduct_selectSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (radioGroup_who.getCheckedRadioButtonId() ==  R.id.radioButton_who_supplier)
                    isSupplierIfThereIsIt();
                else
                    Toast.makeText(getActivity(), "Tedarikçiden Ekle kutucuğunu seçiniz.", Toast.LENGTH_SHORT).show();


            }
        });

        // Attribute leri temizleme kismi ...
        textView_fragmentAddProduct_clearAttributes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearAttributes();

            }
        });

    }


    /**
     * Action olunca getArguments yapınca supplierKey null oluyor.
     * Bu yüzden hafızaya kaydedildi .
     */
    public void getSupplierKeyFromComeFragment(){

        SUPPLIER_KEY = getArguments().getString("supplierKeyFromAdapter", "bos supplier key");
        COMPANY_NAME = getArguments().getString("companyNameFromAdapter", "bos company name");

        SharedPreferences mSharedPrefs = getActivity().getSharedPreferences("tedarikciKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putString("supplierKey", SUPPLIER_KEY);
        editor.putString("companyName", COMPANY_NAME);
        editor.commit();

    }

    /**
     * Eger product design fragment indan geldiysek try a girer .
     * Product detailden  product duzenlemeye gecerken saklanan bundle geri burada yakalanir .
     */
    public void setSupplierIfCameProdcutDetailFragment(){

            PRODUCT_KEY = getArguments().getString("productKey", "bos product key");

            myRef.child("Products").child(USER_UID).child(PRODUCT_KEY).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Product product = snapshot.getValue(Product.class);

                    SUPPLIER_KEY_FOR_MODIFY_PRODUCT = product.getFromKey();
                    PRODUCT_WHO = product.getFrom();

                    editText_fragmentAddProduct_productName.setText(product.getProductName());
                    editText_fragmentAddProduct_unitPurchasePriceProduct.setText(product.getPurchasePrice());
                    editText_fragmentAddProduct_unitSellingPrice.setText(product.getSellingPrice());
                    editText_fragmentAddProduct_howManyUnit.setText(product.getHowManyUnit());
                    editText_fragmentAddProduct_productCode.setText(product.getProductCode());

                    oldProductPurchasedPrice = product.getPurchasePrice();
                    oldProdutQuantity = product.getHowManyUnit();

                    //Hangi birim secili ise ...
                    if (product.getTypeProduct().equals("Adet"))
                        radioGroup_fragmentAddProduct_typeProduct.check(R.id.radioButton_typeProduct_unit);
                    else if(product.getTypeProduct().equals("Ağırlık"))
                        radioGroup_fragmentAddProduct_typeProduct.check(R.id.radioButton_typePorduct_weight);
                    else
                        radioGroup_fragmentAddProduct_typeProduct.check(R.id.radioButton_typePorduct_volume);

                    if (PRODUCT_WHO.equals("Tedarikçiden Ekle")) {
                        radioGroup_who.check(R.id.radioButton_who_supplier);
                        editText_fragmentAddProduct_supplierName.setText(product.getCompanyName());
                    }
                    else
                        editText_fragmentAddProduct_supplierName.setText("Kendi Stoğum");

                    radioGroup_who.setVisibility(View.INVISIBLE);
                    textView_fragmentAddProduct_selectSupplier.setVisibility(View.INVISIBLE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }


    /**
     * Eger supplier secme fragment indan geldiysek try a girer .
     * Product eklemeden supplier secmeye gecerken saklanan bundle geri burada yakalanir .
     * Bunun amaci --> Supllier secerken doldurdugumuz bilgiler kaybolmasin diye .
     */
    public void setSupplierIfCameSupplierListFragment(){

        editText_fragmentAddProduct_supplierName.setText(COMPANY_NAME);

        String name = getArguments().getString("name", "bos");
        String purchasePrice = getArguments().getString("purchasePrice", "bos");
        String sellingPrice = getArguments().getString("sellingPrice", "bos");
        String howManyUnit = getArguments().getString("howManyUnit", "bos");
        String productCode = getArguments().getString("productCode", "bos");
        String productType = getArguments().getString("productType", "bos");
        editText_fragmentAddProduct_productName.setText(name);
        editText_fragmentAddProduct_unitSellingPrice.setText(purchasePrice);
        editText_fragmentAddProduct_unitPurchasePriceProduct.setText(sellingPrice);
        editText_fragmentAddProduct_howManyUnit.setText(howManyUnit);
        editText_fragmentAddProduct_productCode.setText(productCode);
        //Hangi birim secili ise ...
        if (productType.equals("Adet"))
            radioGroup_fragmentAddProduct_typeProduct.check(R.id.radioButton_typeProduct_unit);
        else if(productType.equals("Ağırlık"))
            radioGroup_fragmentAddProduct_typeProduct.check(R.id.radioButton_typePorduct_weight);
        else
            radioGroup_fragmentAddProduct_typeProduct.check(R.id.radioButton_typePorduct_volume);
        radioGroup_who.check(R.id.radioButton_who_supplier);

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
                    setSelected_typeProduct();
                    if(isFilled() && !isSelectedTypeProduct.equals("notSelected"))
                        openSupplierListFragment();
                    else
                        Toast.makeText(getActivity(), "Bilgileri eksik bir şekilde doldurmadan tedarikçi seçilemez.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(), "Tedarikçiniz bulunmamaktadır. Önce hesabınıza tedarikçi ekleyiniz.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * Supplier secmek icin supplier listesini acar .
     * Bu listeyi acarken daha once doldurdugumuz alanlari bundle da saklar ve gecilen fragment a ulastirir .
     */
    public void openSupplierListFragment(){

        String name = editText_fragmentAddProduct_productName.getText().toString().trim();
        String purchasePrice = editText_fragmentAddProduct_unitPurchasePriceProduct.getText().toString().trim();
        String sellingPrice = editText_fragmentAddProduct_unitSellingPrice.getText().toString().trim();
        String howManyUnit = editText_fragmentAddProduct_howManyUnit.getText().toString().trim();
        String productCode = editText_fragmentAddProduct_productCode.getText().toString().trim();
        String productType = isSelectedTypeProduct;

        SupplierListFragment supplierListFragment = new SupplierListFragment();
        Bundle bundle = new Bundle();

        bundle.putString("name", name);
        bundle.putString("purchasePrice", purchasePrice);
        bundle.putString("sellingPrice", sellingPrice);
        bundle.putString("howManyUnit", howManyUnit);
        bundle.putString("productCode", productCode);
        bundle.putString("productType", productType);

        supplierListFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutEntryActivity_holder, supplierListFragment).addToBackStack(null).commit();

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
        String fromKey = "";
        String companyName = "";
        if (isSelectedWho.equals("Kendim Ekle")) {
            fromKey = FirebaseAuth.getInstance().getUid();
            companyName = "Kendi Stoğum";
        }
        else {
            fromKey = SUPPLIER_KEY; // daha sonra tedarikci keyini bul ve ekle .
            companyName = COMPANY_NAME;
        }

        final String productKey = UUID.randomUUID().toString();

        final Product product = new Product(productKey, name, productCode, purchasePrice, sellingPrice, howManyUnit, productType, from, fromKey, companyName);

        myRef.child("Products").child(USER_UID).child(productKey).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    saveProductKeyDB(product);
                }
            }
        });

    }


    /**
     * product i kullanici ekledi ise productKey i; kendine , tedarikciden ekledi ise tedarikci db ye ekler .
     * @param product
     */
    public void saveProductKeyDB(Product product){

        if (isSelectedWho.equals("Kendim Ekle")){ // eger kendi eklerse ...
            myRef.child("Users").child(USER_UID).child("ProductsKey").child(product.getProductKey()).setValue(product);
        }
        else{ // eger tedarikciden eklerse ...
            myRef.child("Suppliers").child(USER_UID).child(SUPPLIER_KEY).child("ProductsKey").child(product.getProductKey()).setValue(product);
        }

    }


    /**
     * Dolu olan gorsel nesnleri icini temizler .
     */
    public void clearAttributes(){

        editText_fragmentAddProduct_productName.setText("");
        editText_fragmentAddProduct_unitSellingPrice.setText("");
        editText_fragmentAddProduct_unitPurchasePriceProduct.setText("");
        editText_fragmentAddProduct_howManyUnit.setText("");
        editText_fragmentAddProduct_productCode.setText("");

        if (PRODUCT_WHO == null)
            editText_fragmentAddProduct_supplierName.setText("");

        radioGroup_fragmentAddProduct_typeProduct.clearCheck();
        radioGroup_who.clearCheck();

    }


    /**
     * Urunu gunceller .
     * updateCash metodunu cagirir . --> kasayi gunceller .
     */
    public void updateProduct(){

        String name = editText_fragmentAddProduct_productName.getText().toString().trim();
        String purchasePrice = editText_fragmentAddProduct_unitPurchasePriceProduct.getText().toString().trim();
        String sellingPrice = editText_fragmentAddProduct_unitSellingPrice.getText().toString().trim();
        String howManyUnit = editText_fragmentAddProduct_howManyUnit.getText().toString().trim();
        String productCode = editText_fragmentAddProduct_productCode.getText().toString().trim();

        Map map = new HashMap();
        map.put("productName", name);
        map.put("purchasePrice", purchasePrice);
        map.put("sellingPrice", sellingPrice);
        map.put("howManyUnit", howManyUnit);
        map.put("productCode", productCode);
        map.put("typeProduct", isSelectedTypeProduct);


        myRef.child("Products").child(USER_UID).child(PRODUCT_KEY).updateChildren(map);

        updateCash(Float.valueOf(purchasePrice), Float.valueOf(howManyUnit), Float.valueOf(oldProductPurchasedPrice), Float.valueOf(oldProdutQuantity));

        updateProductForSupplierDB(map);


    }


    /**
     * Supplier DB deki supplier a ait product guncellenir .
     * Eger supplier i silinen bir urun guncellenecekse burasi if in icinde null atar .
     * @param map
     */
    public void updateProductForSupplierDB(Map map){

        try {// supplier silindikten sonra bura calistiginda null atar .

            if (PRODUCT_WHO.equals("Tedarikçiden Ekle"))
                myRef.child("Suppliers").child(USER_UID).child(SUPPLIER_KEY_FOR_MODIFY_PRODUCT).child("ProductsKey").child(PRODUCT_KEY).updateChildren(map);
            else
                myRef.child("Users").child(USER_UID).child("ProductsKey").child(PRODUCT_KEY).updateChildren(map);

        }
        catch (Exception e){
            Log.e("updateProduct", e.getMessage());
        }


    }


    /**
     * Supplierdan urun alindiktan sonra CashDesk DB sindeki totalPurchasedProductPrice ve totalExpense i gunceller .
     */
    public void updateTotalPurchasedProductPrice(){

        Float purchasedPrice = Float.valueOf(editText_fragmentAddProduct_unitPurchasePriceProduct.getText().toString().trim());
        Float quantity = Float.valueOf(editText_fragmentAddProduct_howManyUnit.getText().toString().trim());
        final Float totalPriceForProduct = purchasedPrice * quantity;

        myRef.child("CashDesk").child(USER_UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CashDesk cashDesk = snapshot.getValue(CashDesk.class);
                Float totalExpense = Float.valueOf(cashDesk.getTotalExpense());

                Float totalPurchasedProductPrice = Float.valueOf(cashDesk.getTotalPurchasedProductPrice());
                totalPurchasedProductPrice += totalPriceForProduct;
                totalExpense += totalPriceForProduct;

                myRef.child("CashDesk").child(USER_UID).child("totalPurchasedProductPrice").setValue(String.valueOf(totalPurchasedProductPrice));
                myRef.child("CashDesk").child(USER_UID).child("totalExpense").setValue(String.valueOf(totalExpense));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * Ayni kodlu urun var mi ?
     * Varsa o urunu eklemez.
     * saveProductDB --> metodunu cagirir .
     * updateTotalPurchasedProductPrice --> metodunu cagirir .
     */
    public void isEarlierAddedProductCode(){

        final String code = editText_fragmentAddProduct_productCode.getText().toString().trim();

        // Eger ilk urun varsa buradan devam eder .
        myRef.child("Products").child(USER_UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                boolean control = false; // kod daha once kullanildi mi ?

                for (DataSnapshot postSnapshot : snapshot.getChildren()){

                    Product product = postSnapshot.getValue(Product.class);

                    if (product.getProductCode().equals(code)){

                        Toast.makeText(getActivity(), "Aynı koda sahip ürün eklenemez .", Toast.LENGTH_SHORT).show();
                        control = true;
                        break;

                    }

                }

                if (!control) { // daha once kullanilmadi ise false doner ...
                    saveProductDB();
                    updateTotalPurchasedProductPrice();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * CashDesk teki totalPurchased ve totalExpense guncellenir .
     * @param purchasedPrice
     * @param quantity
     */
   public void updateCash(Float newPurchased, Float newQuantity, Float purchasedPrice, Float quantity){

        final Float newPrice = (newPurchased * newQuantity) - (purchasedPrice * quantity);

        myRef.child("CashDesk").child(USER_UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CashDesk cashDesk = snapshot.getValue(CashDesk.class);
                Float totalExpense = Float.valueOf(cashDesk.getTotalExpense());
                Float totalPurchased = Float.valueOf(cashDesk.getTotalPurchasedProductPrice());

                totalExpense += newPrice;
                totalPurchased += newPrice;

                Map map = new HashMap();
                map.put("totalExpense", String.valueOf(totalExpense));
                map.put("totalPurchasedProductPrice", String.valueOf(totalPurchased));

                myRef.child("CashDesk").child(USER_UID).updateChildren(map);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

   }



}
