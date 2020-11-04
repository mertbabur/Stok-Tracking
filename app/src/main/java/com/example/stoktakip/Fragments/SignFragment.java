package com.example.stoktakip.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.R;
import com.example.stoktakip.Utils.StockUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignFragment extends Fragment {

    private TextInputEditText fragmentSign_name, fragmentSign_userName, fragmentSign_email, fragmentSign_password;
    private ImageView imageView_fragmentSign_back;
    private Button button_fragmentSign_sign;

    private FirebaseAuth auth;
    private FirebaseUser user;

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
        fragmentSign_userName = rootView.findViewById(R.id.fragmentSign_userName);
        fragmentSign_email = rootView.findViewById(R.id.fragmentSign_email);
        fragmentSign_password = rootView.findViewById(R.id.fragmentSign_password);
        imageView_fragmentSign_back = rootView.findViewById(R.id.imageView_fragmentSign_back);
        button_fragmentSign_sign = rootView.findViewById(R.id.button_fragmentSign_sign);

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
                StockUtils.gotoFragment(getActivity(), loginFragment, R.id.mainActivity_fragmentHolder, "whichButton", "backButton");

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
                    StockUtils.gotoFragment(getActivity(), loginFragment, R.id.mainActivity_fragmentHolder, "whichButton", "signButton");

                }
                else{
                    Log.e("emailVerify", task.getException().toString());
                }
            }
        });
    }


    /**
     * kullaniciyi kayit eder .
     */
    public void createAccountWitEmail(){

        String email = fragmentSign_email.getText().toString().trim();
        String password = fragmentSign_password.getText().toString().trim();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

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
        String userName = fragmentSign_userName.getText().toString().trim();
        String email = fragmentSign_email.getText().toString().trim();
        String password = fragmentSign_password.getText().toString().trim();

        if (!name.equals("") && !userName.equals("") && !email.equals("") && !password.equals("")){
            return true;
        }

        return false;

    }



}
