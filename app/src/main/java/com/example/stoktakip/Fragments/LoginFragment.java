package com.example.stoktakip.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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


public class LoginFragment extends Fragment {

    private Button fragmentLogin_sign, button_fragmentLogin_login;
    private TextInputEditText fragmentLogin_email, fragmentLogin_password;
    private TextView textView_fragmentLogin_forgottenPassword;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private String WHICH_BUTTON;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login_design, container, false);

        defineAttributes(rootView);
        actionAttributes();
        isCameFromWhichFragmentControl();


        return rootView;
    }

    /**
     * Görsel nesneler tanımlanir ve baglanir .
     * @param rootView
     */
    public void defineAttributes(View rootView){

        fragmentLogin_sign = rootView.findViewById(R.id.fragmentLogin_sign);
        button_fragmentLogin_login = rootView.findViewById(R.id.button_fragmentLogin_login);
        fragmentLogin_email = rootView.findViewById(R.id.fragmentLogin_email);
        fragmentLogin_password = rootView.findViewById(R.id.fragmentLogin_password);
        textView_fragmentLogin_forgottenPassword = rootView.findViewById(R.id.textView_fragmentLogin_forgottenPassword);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        WHICH_BUTTON = getArguments().getString("whichButton", "null button");

    }


    /**
     * Action i olan görsel nesneler burada tetiklenir .
     */
    public void actionAttributes(){

        // giris yapma kismi ...
        button_fragmentLogin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFilledControl()){
                    loginApp();
                }
                else {
                    Toast.makeText(getActivity(), "Lütfen bilgileri eksiksiz bir şekilde giriniz .", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // kayit ol fragmenti acan kisim ...
        fragmentLogin_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignFragment signFragment = new SignFragment();
                StockUtils.gotoFragment(getActivity(), signFragment, R.id.mainActivity_fragmentHolder);

            }
        });


        // sifre unutma kismi ...
        textView_fragmentLogin_forgottenPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
            StockUtils.gotoFragment(getActivity(), forgotPasswordFragment, R.id.mainActivity_fragmentHolder);

            }
        });

    }


    /**
     * Sign fragment indan mi gelindigini kontrol eder .
     */
    public void isCameFromWhichFragmentControl(){

        if (WHICH_BUTTON.equals("signButton")){

            Toast.makeText(getActivity(), "Kaydınız başarıyla gerçekleşti ve Email adresinize bir doğrulama linki gönderildi .", Toast.LENGTH_SHORT).show();

        }
        else if(WHICH_BUTTON.equals("sendButton")){
            Toast.makeText(getActivity(), "Şifre sıfırlama linki email hesabınıza gönderildi .", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * Gerekli bilgilerin eksiksiz bir sekilde dolduruldugunu kontrol eder .
     */
    public boolean isFilledControl(){

        String email = fragmentLogin_email.getText().toString().trim();
        String password = fragmentLogin_password.getText().toString().trim();

        if (!email.equals("") && !password.equals("")){
            return true;
        }

        return false;

    }


    /**
     * email ve sifreyi kontrol ederek uygulamaya giris yapar .
     */
    public void loginApp(){

        String email = fragmentLogin_email.getText().toString().trim();
        String password = fragmentLogin_password.getText().toString().trim();

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){


                    if (auth.getCurrentUser().isEmailVerified()) {
                        Log.e("loginApp", "giris yapildi .");
                        // giris yap ...
                    }
                    else {
                        Toast.makeText(getActivity(), "Email hesabınız henüz doğrulanmadı . ", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.e("loginApp",task.getException().toString());
                    Toast.makeText(getActivity(), "Email ya da şifreniz hatalı .", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }


}
