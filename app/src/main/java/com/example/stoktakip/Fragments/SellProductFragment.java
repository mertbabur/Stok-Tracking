package com.example.stoktakip.Fragments;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.Models.CustomerOrSupplier;
import com.example.stoktakip.Models.Product;
import com.example.stoktakip.Models.SoldProduct;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.StockUtils;
import com.example.stoktakip.Utils.TimeClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SellProductFragment extends Fragment {

    private ImageView imageView_sellProductFragment_customerPP;
    private CardView cardView_sellProductFragment_productInfo, cardView_sellProductFragment_productQuantitySelect;
    private EditText editText_sellProductFragment_productName;
    private TextInputEditText editText_sellProductFragment_productQuantity;
    private TextView imageView_sellProductFragment_companyName, imageView_sellProductFragment_customerName, textView_sellProductFragment_productCode
            , textView_sellProductFragment_productName, textView_sellProductFragment_purchasePrice, textView_sellProductFragment_sellingPrice
            , textView_sellProductFragment_howManyQuantity, textView_sellProductFragment_selectProductClick;
    private Button button_sellProductFragment_sellProduct;

    private String CUSTOMER_KEY;
    private String WHICH_FRAGMENT; // whichButton ve whichFragment seklinde dusun ... *** !!! --> buraya cok dikkat bi bokluk olabilir ...
    private String PRODUCT_KEY; // urun secme kismindan geldik ise ...
    private String USER_UID;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sell_product_design, container, false);

        defineAttributes(rootView);
        actionAttributes();
        setCustomerInfo();

        // Eger urun sat butonuna basip gelmissek ...
        if(WHICH_FRAGMENT.equals("customerOrSupplierAdapter")){

            closeCardViews();

        }
        else { // urun secip buraya geldiysek ...

            setProductInfo();

        }

        return rootView;

    }


    /**
     * Gorsel nesneler tanimlanir .
     * @param rootView
     */
    public void defineAttributes(View rootView){

        imageView_sellProductFragment_customerPP = rootView.findViewById(R.id.imageView_sellProductFragment_customerPP);
        cardView_sellProductFragment_productInfo = rootView.findViewById(R.id.cardView_sellProductFragment_productInfo);
        cardView_sellProductFragment_productQuantitySelect = rootView.findViewById(R.id.cardView_sellProductFragment_productQuantitySelect);
        editText_sellProductFragment_productName = rootView.findViewById(R.id.editText_sellProductFragment_productName);
        editText_sellProductFragment_productQuantity = rootView.findViewById(R.id.editText_sellProductFragment_productQuantity);
        imageView_sellProductFragment_companyName = rootView.findViewById(R.id.imageView_sellProductFragment_companyName);
        imageView_sellProductFragment_customerName = rootView.findViewById(R.id.imageView_sellProductFragment_customerName);
        textView_sellProductFragment_productCode = rootView.findViewById(R.id.textView_sellProductFragment_productCode);
        textView_sellProductFragment_purchasePrice = rootView.findViewById(R.id.textView_sellProductFragment_purchasePrice);
        textView_sellProductFragment_sellingPrice = rootView.findViewById(R.id.textView_sellProductFragment_sellingPrice);
        textView_sellProductFragment_productName = rootView.findViewById(R.id.textView_sellProductFragment_productName);
        textView_sellProductFragment_howManyQuantity = rootView.findViewById(R.id.textView_sellProductFragment_howManyQuantity);
        textView_sellProductFragment_selectProductClick = rootView.findViewById(R.id.textView_sellProductFragment_selectProductClick);
        button_sellProductFragment_sellProduct = rootView.findViewById(R.id.button_sellProductFragment_sellProduct);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mAuth = FirebaseAuth.getInstance();

        CUSTOMER_KEY = getArguments().getString("customerOrSupplierKey", "bos customer key");
        WHICH_FRAGMENT = getArguments().getString("whichFragment", "bos fragment");
        PRODUCT_KEY = getArguments().getString("productKey", "bos productKey");
        USER_UID = mAuth.getUid();

    }


    /**
     * Gorsel nesnelerin actionlari burada tetiklenir .
     */
    public void actionAttributes(){

        // urun secme kismi ...
        textView_sellProductFragment_selectProductClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductsFragments productsFragments = new ProductsFragments();
                StockUtils.gotoFragment(getActivity(), productsFragments, R.id.frameLayoutEntryActivity_holder, "whichFragment", "sellProduct", "customerKey", CUSTOMER_KEY, 0);

            }
        });


        // urun satma kismi ...
        button_sellProductFragment_sellProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String productQuantity = editText_sellProductFragment_productQuantity.getText().toString().trim();
                if (!productQuantity.equals(""))
                    alertView(productQuantity);
                else
                    Toast.makeText(getActivity(), "Lütfen ürün miktarını giriniz .", Toast.LENGTH_SHORT).show();

            }
        });





    }


    /**
     * Customer bilgilerini gerekli gorsel nesnelere yerlestirir .
     */
    public void setCustomerInfo(){


        myRef.child("Customers").child(USER_UID).child(CUSTOMER_KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CustomerOrSupplier customerOrSupplier = snapshot.getValue(CustomerOrSupplier.class);

                imageView_sellProductFragment_companyName.setText(customerOrSupplier.getCompanyName());
                imageView_sellProductFragment_customerName.setText(customerOrSupplier.getName() + customerOrSupplier.getSurname());

                String photoKey = customerOrSupplier.getPhoto();
                if (photoKey != "null"){
                    setCustomerPP(photoKey);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * photo key e gore storage den customer pp sini getirir .
     * @param photoKey
     */
    public void setCustomerPP(String photoKey){

        FirebaseStorage.getInstance().getReference().child("CustomersPictures").child(USER_UID).child(photoKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).into(imageView_sellProductFragment_customerPP);

            }
        });

    }


    /**
     *  product info ve product quantity belirleme cardView larini kapat .
     */
    public void closeCardViews(){

        cardView_sellProductFragment_productInfo.setVisibility(View.INVISIBLE);
        cardView_sellProductFragment_productQuantitySelect.setVisibility(View.INVISIBLE);

    }


    /**
     * Gerekli bilgileri gorsel nesnelere yerlestirir ...
     */
    public void setProductInfo(){


        myRef.child("Products").child(USER_UID).child(PRODUCT_KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Product product = snapshot.getValue(Product.class);

                textView_sellProductFragment_productCode.setText("Ürün Kodu : " + product.getProductCode());
                textView_sellProductFragment_productName.setText("Ürün Adı : " + product.getProductName());
                textView_sellProductFragment_purchasePrice.setText(product.getPurchasePrice());
                textView_sellProductFragment_sellingPrice.setText(product.getSellingPrice());
                textView_sellProductFragment_howManyQuantity.setText(product.getHowManyUnit());
                editText_sellProductFragment_productName.setText(product.getProductName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     *
     * @param productQuantity
     */
    public void alertView(final String productQuantity){

        String productName = textView_sellProductFragment_productName.getText().toString();
        String companyName = imageView_sellProductFragment_companyName.getText().toString();

        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(getActivity());

        alertDialogbuilder.setTitle("Bilgileri Onaylıyor Musunuz ?");
        alertDialogbuilder.setMessage("Evete tıklandıktan sonra değişiklik yapılamaz. Bilgileri kontrol ediniz. \n\n" + "Şirket Adı : " + companyName + "\n" + " " + productName + "\n" + "Ürün Miktarı : " + productQuantity);
        alertDialogbuilder.setIcon(R.drawable.warning_icon);

        alertDialogbuilder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                isEnoughQuantityForSellProduct(productQuantity);
                Toast.makeText(getActivity(), "Ürün başarılı bir şekilde satıldı .", Toast.LENGTH_SHORT).show();

            }
        });

        alertDialogbuilder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                editText_sellProductFragment_productQuantity.setText("");

            }
        });


        alertDialogbuilder.create().show();

    }

    /**
     * Depoda yeterli miktarda urun var mi ?
     * updateTotalSelligProductPrice --> cagirir .
     * sellProduct --> cagirir .
     * decreaseProduct --> cagirir .
     * @param productQuantity --> satilmak istenen urun miktari .
     */
    public void isEnoughQuantityForSellProduct(final String productQuantity){

        myRef.child("Products").child(USER_UID).child(PRODUCT_KEY).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Product product = snapshot.getValue(Product.class);

                if(Float.valueOf(productQuantity) <= Float.valueOf(product.getHowManyUnit())){

                    decreaseProduct(product, productQuantity);
                    sellProduct(product, productQuantity);
                    updateTotalSelligProductPrice(product, productQuantity);


                }
                else{

                    Toast.makeText(getActivity(), "Depoda yeterli miktarda ürün bulunmamaktadır .", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * Satilacak urun miktarini stoktan cikarilarak products, supplier ve users da yazan miktarlari duzenler .
     * @param product --> urun bilgilerini tutar .
     * @param productQuantity --> satilacak urun miktari .
     */
    public void decreaseProduct(Product product, String productQuantity){

        String from = product.getFrom();
        String fromKey = product.getFromKey(); // userKey or supplierKey
        String howManyQuantity = product.getHowManyUnit();
        float lastProductQuantity = Float.valueOf(howManyQuantity) - Float.valueOf(productQuantity);

        myRef.child("Products").child(USER_UID).child(PRODUCT_KEY).child("howManyUnit").setValue(String.valueOf(lastProductQuantity));

        if (from.equals("Kendim Ekle")){ // Eger kullanici kendi stogundan satis yapti ise .

            myRef.child("Users").child(USER_UID).child("ProductsKey").child(PRODUCT_KEY).child("howManyUnit").setValue(String.valueOf(lastProductQuantity));

        }
        else{ // Eger kullanici supplier stogundan satis yapti ise .

            myRef.child("Suppliers").child(USER_UID).child(fromKey).child("ProductsKey").child(PRODUCT_KEY).child("howManyUnit").setValue(String.valueOf(lastProductQuantity));

        }

    }


    /**
     * Urun soldProducts tablosuna eklenir ve customer a totalDebt eklenir .
     * updateCustomer metodunu cagirir .
     * @param product --> urun bilgilerini tutar .
     * @param productQuantity --> satilacak urun miktari .
     */
    public void sellProduct(Product product, String productQuantity){

        String productCode = product.getProductCode();
        String sellingPrice = product.getSellingPrice();
        String date = TimeClass.getDate();
        String clock = TimeClass.getClock();

        float totalSoldPrice = Float.valueOf(productQuantity) * Float.valueOf(sellingPrice);

        String sellKey = UUID.randomUUID().toString();

        SoldProduct soldProduct = new SoldProduct(sellKey, CUSTOMER_KEY, PRODUCT_KEY, productCode, productQuantity, String.valueOf(totalSoldPrice), "false", date + "-" + clock);

        myRef.child("SoldProducts").child(USER_UID).child(CUSTOMER_KEY).child(sellKey).setValue(soldProduct);

        updateCustomer(String.valueOf(totalSoldPrice));

    }


    /**
     * Customer a satilan urunun fiyati totalDebt e yazilarak guncellenir .
     * @param debt
     */
    public void updateCustomer(final String debt){

        final DatabaseReference ref = myRef.child("Customers").child(USER_UID).child(CUSTOMER_KEY).child("totalDebt");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                float totalDebt = Float.valueOf(snapshot.getValue().toString());

                totalDebt += Float.valueOf(debt);
                ref.setValue(String.valueOf(totalDebt));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * Urunun miktarina gore fiyatini hesaplar ve CashDesk DB sindeki totalSellingProductPrice a ekler .
     * @param product --> product object .
     * @param productQuantity --> urun miktari .
     */
    public void updateTotalSelligProductPrice(Product product, String productQuantity){

        Float sellingPrice = Float.valueOf(product.getSellingPrice());
        final Float price = sellingPrice * Float.valueOf(productQuantity); // o urunun miktarina gore satis fiyati .


        myRef.child("CashDesk").child(USER_UID).child("totalSellingProductPrice").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Float totalPrice = Float.valueOf(snapshot.getValue().toString());
                totalPrice += price;
                myRef.child("CashDesk").child(USER_UID).child("totalSellingProductPrice").setValue(String.valueOf(totalPrice));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}
