package com.example.stoktakip.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.R;
import com.example.stoktakip.Utils.FirebaseUtils;
import com.example.stoktakip.Utils.StockUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class SignFragment extends Fragment {

    private TextInputEditText fragmentSign_name, fragmentSign_companyName, fragmentSign_email, fragmentSign_password;
    private ImageView imageView_fragmentSign_back, imageView_fragmentSign_userPP;
    private Button button_fragmentSign_sign;
    private TextView textView_fragmentSign_addPP;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private Uri getPhotoFromGalleryURI;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sign_design, container, false);

        defineAttributes(rootView);
        actionAttributes();

        return rootView;

    }


    /**
     * Görsel nesneler tanimlandi ve baglandi .
     */
    public void defineAttributes(View rootView){

        fragmentSign_name = rootView.findViewById(R.id.fragmentSign_name);
        fragmentSign_companyName = rootView.findViewById(R.id.fragmentSign_companyName);
        fragmentSign_email = rootView.findViewById(R.id.fragmentSign_email);
        fragmentSign_password = rootView.findViewById(R.id.fragmentSign_password);
        imageView_fragmentSign_back = rootView.findViewById(R.id.imageView_fragmentSign_back);
        button_fragmentSign_sign = rootView.findViewById(R.id.button_fragmentSign_sign);
        imageView_fragmentSign_userPP = rootView.findViewById(R.id.imageView_fragmentSign_userPP);
        textView_fragmentSign_addPP = rootView.findViewById(R.id.textView_fragmentSign_addPP);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

    }


    /**
     * Görsel nesnelerin actionlari burada tetiklenir .
     */
    public void actionAttributes(){

        // geri donme kismi ...
        imageView_fragmentSign_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginFragment loginFragment = new LoginFragment();
                StockUtils.gotoFragment(getActivity(), loginFragment, R.id.mainActivity_fragmentHolder, "whichButton", "backButton", 1);

            }
        });

        // kayit olma buton kismi
        button_fragmentSign_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFilledControl()){

                    createAccountWitEmail();

                }
                else{
                    Toast.makeText(getActivity(), "Lütfen bilgileri eksiksiz doldurunurz .", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // resim ekleme kismi ...
        textView_fragmentSign_addPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            Picasso.get().load(getPhotoFromGalleryURI).into(imageView_fragmentSign_userPP);
        }


    }

    /**
     * Maile dogrulama kodu gonderir .
     * Basarili bir sekilde dogrulama kodu gonderildi ise login fragmentina gecis yapar .
     */
    public void sendEmailVerification(){
        user = auth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.e("emailVerify", "dogrulama linki gonderildi .");

                    LoginFragment loginFragment = new LoginFragment();
                    StockUtils.gotoFragment(getActivity(), loginFragment, R.id.mainActivity_fragmentHolder, "whichButton", "signButton", 1);

                }
                else{
                    Log.e("emailVerify", task.getException().toString());
                }
            }
        });
    }


    /**
     * kullaniciyi kayit eder .(auth a kayit) .
     * kulaniciyi db ye kaydini yapar .
     */
    public void createAccountWitEmail(){

        final String email = fragmentSign_email.getText().toString().trim();
        String password = fragmentSign_password.getText().toString().trim();
        final String name = fragmentSign_name.getText().toString().trim();
        final String companyName = fragmentSign_companyName.getText().toString().toString();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    FirebaseUtils.saveUserFirebaseDB(auth, email, companyName, name, "null");
                    FirebaseUtils.saveUserToCashDeskDB(auth); /** Buraya dikkat denenmedi hata olabilir .  */

                    if (getPhotoFromGalleryURI != null)
                        FirebaseUtils.saveUserPhotoFirebaseStorage(auth, getPhotoFromGalleryURI);

                    sendEmailVerification();

                }
                else {
                    Log.e("createAcc", task.getException().toString());

                    Toast.makeText(getActivity(), "Bu email daha önceden kullanılmış ! Lütfen farklı bir email deneyiniz .", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }


    /**
     * Gerekli alanların eksiksiz bir sekilde dolduruldugunu kontrol eder .
     */
    public boolean isFilledControl(){

        String name = fragmentSign_name.getText().toString().trim();
        String companyName = fragmentSign_companyName.getText().toString().trim();
        String email = fragmentSign_email.getText().toString().trim();
        String password = fragmentSign_password.getText().toString().trim();

        if (!name.equals("") && !companyName.equals("") && !email.equals("") && !password.equals("")){
            return true;
        }

        return false;

    }



}
