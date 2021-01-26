package com.example.stoktakip.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.Models.User;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.FirebaseUtils;
import com.example.stoktakip.Utils.StockUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment {

    private Toolbar toolbar_myStock;
    private ImageView imageView_settingsFragment_userPP;
    private TextView textView_settingsFragment_companyName, textView_settingsFragment_userName, textView_settingsFragment_mail
                   , textView_settingsFragment_updateInfo, textView_settingsFragment_updatePassword, textView_settingsFragment_deleteAccount
                   , textView_settingsFragment_signOut, alertView_updatePassword_password, alertView_updatePassword_verificiationPassword
                   , alertview_companyName, alertview_userName, textView_settingsFragment_chanceUserPP;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private StorageReference stRef;
    private Uri getPhotoFromGalleryURI;

    private String USER_UID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings_design, container, false);

        defineAttributes(rootView);
        actionAttributes();
        setInfoToAttributes();

        return rootView;

    }


    /**
     * Gorsel nesneler burada baglanir .
     */
    public void defineAttributes(View rootView){

        imageView_settingsFragment_userPP = rootView.findViewById(R.id.imageView_settingsFragment_userPP);
        textView_settingsFragment_companyName = rootView.findViewById(R.id.textView_settingsFragment_companyName);
        textView_settingsFragment_userName = rootView.findViewById(R.id.textView_settingsFragment_userName);
        textView_settingsFragment_mail = rootView.findViewById(R.id.textView_settingsFragment_mail);
        textView_settingsFragment_updateInfo = rootView.findViewById(R.id.textView_settingsFragment_updateInfo);
        textView_settingsFragment_updatePassword = rootView.findViewById(R.id.textView_settingsFragment_updatePassword);
        textView_settingsFragment_deleteAccount = rootView.findViewById(R.id.textView_settingsFragment_deleteAccount);
        textView_settingsFragment_signOut = rootView.findViewById(R.id.textView_settingsFragment_signOut);
        textView_settingsFragment_chanceUserPP = rootView.findViewById(R.id.textView_settingsFragment_chanceUserPP);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        storage = FirebaseStorage.getInstance();
        stRef = storage.getReference();

        USER_UID = FirebaseAuth.getInstance().getUid();

    }


    /**
     * Gorsel nesnelerin actionlari burada calistirilir .
     */
    public void actionAttributes(){

        // Bilgi guncelleme kismi .
        textView_settingsFragment_updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertViewForUpdateInfo();
            }
        });

        // sifre guncelleme kismi .
        textView_settingsFragment_updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertViewForUpdatePassword();
            }
        });

        // hesap silme kismi .
        textView_settingsFragment_deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePPForDeleteAccount();
            }
        });

        // hesaptan cikis kismi .
        textView_settingsFragment_signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginFragment loginFragment = new LoginFragment();
                StockUtils.gotoFragment(getActivity(), loginFragment, R.id.frameLayoutEntryActivity_holder, "whichButton", "exitButton", 0);
                mAuth.signOut();
            }
        });

        // user pp secme kismi .
        textView_settingsFragment_chanceUserPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });


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
            Picasso.get().load(getPhotoFromGalleryURI).into(imageView_settingsFragment_userPP);

            deleteUserPPFromStorage();

        }


    }

    /**
     * Kullanici bilgilerini gerekli gorsel nesnelere yerlestirir .
     */
    public void setInfoToAttributes(){

        myRef.child("Users").child(USER_UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                textView_settingsFragment_companyName.setText("ŞİRKET ADI : " + user.getCompanyName());
                textView_settingsFragment_userName.setText("İSİM : " + user.getName());

                String photoKey = user.getPhoto();
                if (!photoKey.equals("null")) {
                    FirebaseStorage.getInstance().getReference().child("UsersPictures").child(photoKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Picasso.get().load(uri).into(imageView_settingsFragment_userPP);

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







    }

    /**
     * Kullanicinin sifresini degistirir .
     * @param password
     */
    public void updatePassword(String password){

        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(getActivity(), "Şifre değiştirildi .", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), "Şifre değiştirme başarısız .", Toast.LENGTH_SHORT).show();

                    }
                });

    }


    /**
     * updatePassword metodunu cagirir .
     */
    public void alertViewForUpdatePassword(){

        View desing = getLayoutInflater().inflate(R.layout.alertview_updatepassword_design, null);

        alertView_updatePassword_password = desing.findViewById(R.id.alertView_updatePassword_password);
        alertView_updatePassword_verificiationPassword = desing.findViewById(R.id.alertView_updatePassword_verificiationPassword);

        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(getActivity());

        alertDialogbuilder.setTitle("Lütfen Yeni Şifrenizi Giriniz ?");
        alertDialogbuilder.setIcon(R.drawable.password_icon);

        alertDialogbuilder.setView(desing);

        alertDialogbuilder.setPositiveButton("DEĞİŞTİR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = alertView_updatePassword_password.getText().toString().trim();
                String verificiationPassword = alertView_updatePassword_verificiationPassword.getText().toString().trim();

                if (password.equals(verificiationPassword) && isEmptyForPassword(password, verificiationPassword)) {
                    updatePassword(password);
                }
                else{
                    Toast.makeText(getActivity(), "Şifreleri düzgün bir şekilde giriniz (uzunluk, eşleşmeme vs ...).", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialogbuilder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialogbuilder.create().show();

    }


    /**
     * girilen alanlar dolu mu ? Ve uzunlugu 6ya esit veya buyuk mu ?
     * @param password
     * @param verifyPassword
     * @return dolu ise --> true degil ise --> false
     */
    public boolean isEmptyForPassword(String password, String verifyPassword){

        if (!password.equals("") && !verifyPassword.equals(""))
            if (password.length() >= 6)
                return true;

        return false;

    }


    /**
     * Girilen alanlar dolu mu ?
     * @param str1
     * @param str2
     * @return dolu ise --> true, bos ise --> false .
     */
    public boolean isEmptyForUpdateInfo(String str1, String str2){

        if (!str1.equals("") && !str2.equals(""))
            return true;

        return false;

    }


    /**UpdateInfo metodunu cagirir .
     * update
     */
    public void alertViewForUpdateInfo(){

        View desing = getLayoutInflater().inflate(R.layout.alertview_update_info, null);

        alertview_companyName = desing.findViewById(R.id.alertview_companyName);
        alertview_userName = desing.findViewById(R.id.alertview_userName);

        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(getActivity());

        alertDialogbuilder.setTitle("Lütfen Yeni Bilgilerinizi Giriniz ?");
        alertDialogbuilder.setIcon(R.drawable.info_icon);

        alertDialogbuilder.setView(desing);

        alertDialogbuilder.setPositiveButton("DEĞİŞTİR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String companyName = alertview_companyName.getText().toString().trim();
                String userName = alertview_userName.getText().toString().trim();

                if (isEmptyForUpdateInfo(companyName, userName)) {
                    updateInfo(companyName, userName);
                } else {
                    Toast.makeText(getActivity(), "Lütfen ilgili alanları doldurunuz .", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialogbuilder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialogbuilder.create().show();

    }


    /**
     * Kullanici bilgilerini gunceller .
     * @param companyName
     * @param userName
     */
    public void updateInfo(final String companyName, final String userName){

        Map map = new HashMap();
        map.put("companyName", companyName);
        map.put("name", userName);

        myRef.child("Users").child(USER_UID).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                    Toast.makeText(getActivity(), "Bilgi güncelleme başarılı .", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "Bilgi güncelleme başarısız .", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Kullanicinin pp sini storage dan siler ve secileni storage a yukler .
     * Eger pp si yoksa sadece secilen fotoyu storage e yukler .
     * FirebaseUtils.saveUserPhotoFirebaseStorage metodunu cagirir .
     */
    public void deleteUserPPFromStorage(){

        myRef.child("Users").child(USER_UID).child("photo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = snapshot.getValue().toString();

                if (!key.equals("null")) {
                    FirebaseStorage.getInstance().getReference().child("UsersPictures").child(key).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseUtils.saveUserPhotoFirebaseStorage(mAuth, getPhotoFromGalleryURI);
                                Toast.makeText(getActivity(), "Fotoğraf güncelleme başarılı .", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Fotoğraf güncelleme başarısız .", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    FirebaseUtils.saveUserPhotoFirebaseStorage(mAuth, getPhotoFromGalleryURI);
                    Toast.makeText(getActivity(), "Fotoğraf güncelleme başarılı .", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**
     * Kullanici silmeden once pp sini storage dan kaldirir.
     * deleteAccount metodunu cagirir .
     */
    public void deletePPForDeleteAccount(){
        myRef.child("Users").child(USER_UID).child("photo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = snapshot.getValue().toString();

                if (!key.equals("null")) {
                    FirebaseStorage.getInstance().getReference().child("UsersPictures").child(key).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                deleteAccount();
                            } else {
                                Toast.makeText(getActivity(), "Hesap silme basarisiz .", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void deleteAccount(){

        // Eger db de olmayan bir seyi silmeye kalkarsak exception firlatir .
        try {
            myRef.child("UserExpenses").child(USER_UID).removeValue();
        }catch (Exception e){
            Log.e("deleteAcc", e.getMessage());
        }

        // Eger db de olmayan bir seyi silmeye kalkarsak exception firlatir .
        try {
            myRef.child("CashDesk").child(USER_UID).removeValue();
        }catch (Exception e){
            Log.e("deleteAcc", e.getMessage());
        }

        // Eger db de olmayan bir seyi silmeye kalkarsak exception firlatir .
        try {
            myRef.child("Customers").child(USER_UID).removeValue();
        }catch (Exception e){
            Log.e("deleteAcc", e.getMessage());
        }

        // Eger db de olmayan bir seyi silmeye kalkarsak exception firlatir .
        try {
            myRef.child("Products").child(USER_UID).removeValue();
        }catch (Exception e){
            Log.e("deleteAcc", e.getMessage());
        }

        // Eger db de olmayan bir seyi silmeye kalkarsak exception firlatir .
        try {
            myRef.child("SoldProducts").child(USER_UID).removeValue();
        }catch (Exception e){
            Log.e("deleteAcc", e.getMessage());
        }

        // Eger db de olmayan bir seyi silmeye kalkarsak exception firlatir .
        try {
            myRef.child("Suppliers").child(USER_UID).removeValue();
        }catch (Exception e){
            Log.e("deleteAcc", e.getMessage());
        }

        // Eger db de olmayan bir seyi silmeye kalkarsak exception firlatir .
        try {
            myRef.child("Users").child(USER_UID).removeValue();
        }catch (Exception e){
            Log.e("deleteAcc", e.getMessage());
        }

        // Eger db de olmayan bir seyi silmeye kalkarsak exception firlatir .
        try {
            myRef.child("Users").child(USER_UID).removeValue();
        }catch (Exception e){
            Log.e("deleteAcc", e.getMessage());
        }

        // Eger db de olmayan bir seyi silmeye kalkarsak exception firlatir .
        try {
            myRef.child("Users").child(USER_UID).removeValue();
        }catch (Exception e){
            Log.e("deleteAcc", e.getMessage());
        }

        /**
         * Bu code blogunda kullanicinin musteri ve supplier larinin resimlerini silmede sıkıntı var daha sonra bak ...
         */
/*
        // Eger db de olmayan bir seyi silmeye kalkarsak exception firlatir .
        try {
            FirebaseStorage.getInstance().getReference().child("SuppliersPictures").child(USER_UID).delete();
        }catch (Exception e){
            Log.e("deleteAcc", e.getMessage());
        }

        // Eger db de olmayan bir seyi silmeye kalkarsak exception firlatir .
        try {
            FirebaseStorage.getInstance().getReference().child("CustomersPictures").child(USER_UID).delete();
        }catch (Exception e){
            Log.e("deleteAcc", e.getMessage());
        }
*/

        user = FirebaseAuth.getInstance().getCurrentUser();
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Silme işlemi başarılı .", Toast.LENGTH_SHORT).show();

                            LoginFragment loginFragment = new LoginFragment();
                            StockUtils.gotoFragment(getActivity(), loginFragment, R.id.frameLayoutEntryActivity_holder, "whichButton", "deleteButton", 0);
                        }
                    }
                });


    }



}
