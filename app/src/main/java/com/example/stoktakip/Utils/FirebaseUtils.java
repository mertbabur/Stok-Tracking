package com.example.stoktakip.Utils;

import android.net.Uri;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.stoktakip.Models.CustomerOrSupplier;
import com.example.stoktakip.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    public static void addCustomerToDB(String customerName, String customerSurname, String companyName, String customerNum, String customerAddress, final Uri photoUri){

        defineFirebaseAuth();
        defineFirebaseDatabase();

        String userUID = mAuth.getUid();

        final String customerKey = UUID.randomUUID().toString();
        CustomerOrSupplier customer = new CustomerOrSupplier(customerKey, customerName, customerSurname, companyName, customerNum, customerAddress, "null");

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
    public static void addSupplierToDB(String supplierName, String supplierSurname, String companyName, String supplierNum, String supplierAddress, final Uri photoUri){

        defineFirebaseAuth();
        defineFirebaseDatabase();

        String userUID = mAuth.getUid();

        final String supplierKey = UUID.randomUUID().toString();
        CustomerOrSupplier supplier = new CustomerOrSupplier(supplierKey, supplierName, supplierSurname, companyName, supplierNum, supplierAddress, "null");

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




}
