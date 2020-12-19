package com.example.stoktakip.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.example.stoktakip.Models.CashDesk;
import com.example.stoktakip.Models.CustomerOrSupplier;
import com.example.stoktakip.Models.Product;
import com.example.stoktakip.Models.User;
import com.example.stoktakip.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class FirebaseUtils {

    private static FirebaseDatabase database;
    private static DatabaseReference myRef;

    private static FirebaseStorage storage;
    private static StorageReference stRef;

    private static FirebaseAuth mAuth;
    private static FirebaseUser user;

    /**
     * FirebaseDatabase i tanimlar .
     */
    public static void defineFirebaseDatabase(){

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

    }


    /**
     * FirebaseStorage i tanimlar .
     */
    public static void defineFirebaseStorage(){

        storage = FirebaseStorage.getInstance();
        stRef = storage.getReference();

    }


    /**
     * FirebaseAuth i tanimlar .
     */
    public static void defineFirebaseAuth(){

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

    }


    /**
     * Sifre resetleme linki gonderir .
     * @param auth
     * @param email
     */
    public static void sendResetPasswordMail(FirebaseAuth auth, String email, final FragmentActivity activity){

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(activity, "Şifre sıfırlama linki email hesabınıza gönderildi .", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("resetPasswordMail", task.getException().toString());
                }

            }
        });
    }


    /**
     * database e user kaydini yapar .
     * @param auth --> kayit olanin auth u .
     * @param email --> user email .
     * @param name --> user name .
     * @param photo --> user in photo key i .
     */
    public static void saveUserFirebaseDB(FirebaseAuth auth, String email, String companyName, String name, String photo){

        String userUID = auth.getUid();
        User user = new User(userUID, email, companyName, name, photo);

        defineFirebaseDatabase();

        myRef.child("Users").child(userUID).setValue(user);

    }


    /**
     * yeni kayit olan kisi icin DB ye kasa ekler . --> CashDesk DB si .
     * @param auth
     */
    public static void saveUserToCashDeskDB(FirebaseAuth auth){

        String userUID = auth.getUid();
        defineFirebaseDatabase();

        CashDesk cashDesk = new CashDesk("0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0");
        myRef.child("CashDesk").child(userUID).setValue(cashDesk);

    }


    /**
     * Kullanicinin fotosunu FirebaseStorage a save eder .
     * Ardindan randomKey i db ye kaydeder .
     * @param auth --> User auth .
     * @param photoUri --> Cekilen fotonun uri si .
     */
    public static void saveUserPhotoFirebaseStorage(FirebaseAuth auth, Uri photoUri){

        final String userUID = auth.getUid();
        final String randomKey = UUID.randomUUID().toString();

        defineFirebaseStorage();
        defineFirebaseDatabase();

        final StorageReference ref = stRef.child("UsersPictures").child(randomKey);
        ref.putFile(photoUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()){
                    myRef.child("Users").child(userUID).child("photo").setValue(randomKey);
                }
                else{
                    Log.e("savePhotoFirebaseStr : ", task.getException()+"");
                }
            }
        });

    }


    /**
     * Customer bilgilerini db ye kaydeder.
     * saveCustomerPhotoToFirebaseStorage metodunu cagirir .
     * @param customerName
     * @param customerSurname
     * @param companyName
     * @param customerNum
     * @param customerAddress
     */
    public static void addCustomerToDB(String customerName, String customerSurname, String companyName, String customerNum, String customerAddress, final Uri photoUri, int totalDebt){

        defineFirebaseAuth();
        defineFirebaseDatabase();

        String userUID = mAuth.getUid();

        final String customerKey = UUID.randomUUID().toString();
        CustomerOrSupplier customer = new CustomerOrSupplier(customerKey, customerName, customerSurname, companyName, customerNum, customerAddress, "null", String.valueOf(totalDebt));

        myRef.child("Customers").child(userUID).child(customerKey).setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    if (photoUri != null)
                    savePhotoToFirebaseStorage(photoUri,customerKey, "CustomersPictures", "Customers", "photo");

            }
        });

    }


    /**
     * Supplier bilgilerini db ye kaydeder.
     * savePhotoToFirebaseStorage metodunu cagirir .
     * @param supplierName
     * @param supplierSurname
     * @param companyName
     * @param supplierNum
     * @param supplierAddress
     * @param photoUri
     */
    public static void addSupplierToDB(String supplierName, String supplierSurname, String companyName, String supplierNum, String supplierAddress, final Uri photoUri, int totalDebt){

        defineFirebaseAuth();
        defineFirebaseDatabase();

        String userUID = mAuth.getUid();

        final String supplierKey = UUID.randomUUID().toString();
        CustomerOrSupplier supplier = new CustomerOrSupplier(supplierKey, supplierName, supplierSurname, companyName, supplierNum, supplierAddress, "null", String.valueOf(totalDebt));

        myRef.child("Suppliers").child(userUID).child(supplierKey).setValue(supplier).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    if (photoUri != null)
                        savePhotoToFirebaseStorage(photoUri,supplierKey, "SuppliersPictures", "Suppliers", "photo");

            }
        });

    }


    /**
     * Customer ve tedarikci fotosunu storage a kaydeder .
     * @param photoUri
     * @param customerKey
     */
    public static void savePhotoToFirebaseStorage(Uri photoUri, final String customerKey, String whichStorage, final String whichDB, final String whichChild){

        defineFirebaseAuth();
        defineFirebaseDatabase();
        defineFirebaseStorage();


        final String userUID = mAuth.getUid();
        final String randomKey = UUID.randomUUID().toString();

        defineFirebaseStorage();
        defineFirebaseDatabase();

        final StorageReference ref = stRef.child(whichStorage).child(userUID).child(randomKey);
        ref.putFile(photoUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()){
                    myRef.child(whichDB).child(userUID).child(customerKey).child(whichChild).setValue(randomKey);
                }
                else{
                    Log.e("savePhotoFirebaseStr : ", task.getException()+"");
                }
            }
        });

    }


    public static void deletePhotoFromFirebaseStorage(String whichStorage, String photoKey){

        defineFirebaseAuth();
        defineFirebaseStorage();

        String userUID = mAuth.getUid();

        stRef.child(whichStorage).child(userUID).child(photoKey).delete();



    }


    /**
     * Supplier key ile supplier bilgilerine ulasir .
     * Company name i gerkli gorsel nesneye yerlestirir .
     * @param supplierKey
     */
    public static void setCompanyName(String supplierKey, final TextView textView) {

        defineFirebaseDatabase();
        defineFirebaseAuth();
        String userUID = mAuth.getUid();

        myRef.child("Suppliers").child(userUID).child(supplierKey).child("companyName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String companyName = snapshot.getValue().toString();
                textView.setText(companyName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     ** Musteriden odeme alir veya supplier a odeme yapar .
     * * Customers DB sindeki totalDebt i duzenler.
     * @param whichDB --> Customers mi Suppliers mi .
     * @param activity
     * @param paidQuantityText  --> odeme miktari alma kismi .
     * @param getPaidClick --> odeme al tusu .
     * @param USER_UID
     * @param CUSTOMER_OR_SUPPLIER_KEY
     */
    public static void getPaidFromCustomerOrSupplier(final String whichDB, final FragmentActivity activity, TextInputEditText paidQuantityText, final TextView getPaidClick, final TextView kalanBorcText, final String USER_UID, final String CUSTOMER_OR_SUPPLIER_KEY){

        final Float paidQuantity = Float.valueOf(paidQuantityText.getText().toString());

        defineFirebaseDatabase();
        myRef.child(whichDB).child(USER_UID).child(CUSTOMER_OR_SUPPLIER_KEY).child("totalDebt").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Float totalDebt = Float.valueOf(snapshot.getValue().toString());
                if (totalDebt >= paidQuantity) {
                    totalDebt -= paidQuantity;

                    myRef.child(whichDB).child(USER_UID).child(CUSTOMER_OR_SUPPLIER_KEY).child("totalDebt").setValue(String.valueOf(totalDebt));

                    if (whichDB.equals("Customers"))
                        kalanBorcText.setText("TOPLAM TAHSİL EDİLECEK TUTAR : " + totalDebt + " TL");
                    else // Suppliers DB si ise ...
                        kalanBorcText.setText("TOPLAM ÖDENECEK TUTAR : " + totalDebt + " TL");

                    if (totalDebt == 0.0)
                        StockUtils.controlTotalDebt(getPaidClick);

                }
                else
                    Toast.makeText(activity, "Alınacak miktardan büyük değer girilemez .", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * Musteriden odeme aldikca CashDesk DB sindeki totalCollection u gunceller .
     * @param paidQuantityText
     * @param USER_UID
     */
    public static void updateTotalCollectedProductPrice(TextInputEditText paidQuantityText, final String USER_UID){

        final Float paidQuantity = Float.valueOf(paidQuantityText.getText().toString());

        myRef.child("CashDesk").child(USER_UID).child("totalCollectedProductPrice").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Float totalCollected = Float.valueOf(snapshot.getValue().toString());
                totalCollected += paidQuantity;

                myRef.child("CashDesk").child(USER_UID).child("totalCollectedProductPrice").setValue(String.valueOf(totalCollected));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * Supplier a odeme yaptikca CashDesk DB sindeki totalPaid i gunceller .
     * @param paidQuantityText
     * @param USER_UID
     */
    public static void updateTotalPaidProductPrice(TextInputEditText paidQuantityText, final String USER_UID){

        final Float paidQuantity = Float.valueOf(paidQuantityText.getText().toString());

        myRef.child("CashDesk").child(USER_UID).child("totalPaidProductPrice").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Float totalPaid = Float.valueOf(snapshot.getValue().toString());
                totalPaid += paidQuantity;

                myRef.child("CashDesk").child(USER_UID).child("totalPaidProductPrice").setValue(String.valueOf(totalPaid));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * Products DB sinden istenen product i siler .
     * deleteProductFromUsersDB metodunu cagirir . --> Users DB sinden siler . ( duruma gore cagirir . )
     * deleteProductFromSuppliersDB metodunu cagirir . --> Suppliers DB sinden siler . ( duruma gore cagirir . )
     * @param activity
     * @param context
     * @param userUID
     * @param productKey
     */
    public static void deleteProduct(final FragmentActivity activity, final Context context, final String userUID, final String productKey){

        defineFirebaseDatabase();

        myRef.child("Products").child(userUID).child(productKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Product product = snapshot.getValue(Product.class);

                if (product.getFrom().equals("Kendim Ekle")) // kendi ekledi ise .
                    deleteProductFromUsersDB(activity, context, userUID, productKey);
                else // Supplier dan ekledi ise .
                    deleteProductFromSuppliersDB(activity, context, product.getFromKey(), userUID, productKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    /**
     * Users DB sinden istenen product i siler .
     * @param userUID -->
     * @param productKey -->
     */
    public static void deleteProductFromUsersDB(final FragmentActivity activity, final Context context, final String userUID, final String productKey){

        defineFirebaseDatabase();

        myRef.child("Users").child(userUID).child("ProductsKey").child(productKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    deleteProductFromProductsDB(activity, context, userUID, productKey);
                }
                else {

                    if (activity != null)
                        Toast.makeText(activity, "Silme işlemi sırasında bir sorun oluştu .", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "Silme işlemi sırasında bir sorun oluştu .", Toast.LENGTH_SHORT).show();

                    Log.e("deleteProduct", task.getException().toString());

                }
            }
        });

    }


    /**
     * Suppliers DB sinden istenen product i siler .
     * @param fromKey --> supplierKey
     * @param userUID -->
     * @param productKey -->
     */
    public static void deleteProductFromSuppliersDB(final FragmentActivity activity, final Context context, String fromKey, final String userUID, final String productKey){

        defineFirebaseDatabase();

        myRef.child("Suppliers").child(userUID).child(fromKey).child("ProductsKey").child(productKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    deleteProductFromProductsDB(activity, context, userUID, productKey);
                }
                else {

                    if (activity != null)
                        Toast.makeText(activity, "Silme işlemi sırasında bir sorun oluştu .", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "Silme işlemi sırasında bir sorun oluştu .", Toast.LENGTH_SHORT).show();

                    Log.e("deleteProduct", task.getException().toString());

                }
            }
        });

    }


    /**
     * Products DB den urunu siler .
     * @param activity
     * @param context
     * @param userUID
     * @param productKey
     */
    public static void deleteProductFromProductsDB(final FragmentActivity activity, final Context context, String userUID, String productKey){

        myRef.child("Products").child(userUID).child(productKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    if (activity != null)
                        Toast.makeText(activity, "Silme işlemi başarılı .", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "Silme işlemi başarılı .", Toast.LENGTH_SHORT).show();

                }
                else{

                    if (activity != null)
                        Toast.makeText(activity, "Silme işlemi sırasında bir sorun oluştu .", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "Silme işlemi sırasında bir sorun oluştu .", Toast.LENGTH_SHORT).show();

                    Log.e("deleteProduct", task.getException().toString());

                }

            }
        });


    }

}
