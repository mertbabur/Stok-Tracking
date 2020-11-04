package com.example.stoktakip.Fragments;

import android.os.Bundle;
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
import com.example.stoktakip.Utils.FirebaseUtils;
import com.example.stoktakip.Utils.StockUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordFragment extends Fragment {

    private TextInputEditText textInputEditText_fragmentForgotPassword_email;
    private ImageView imageView_fragmentForgotPassword_back;
    private Button button_fragmentForgotPassword_send;

    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_forgot_password_design, container, false);

        defineAttributes(rootView);
        actionAttributes();

        return rootView;

    }


    /**
     * Gorsel nesneler tanimlandi ve baglandi .
     */
    public void defineAttributes(View rootView){

        textInputEditText_fragmentForgotPassword_email = rootView.findViewById(R.id.textInputEditText_fragmentForgotPassword_email);
        imageView_fragmentForgotPassword_back = rootView.findViewById(R.id.imageView_fragmentForgotPassword_back);
        button_fragmentForgotPassword_send = rootView.findViewById(R.id.button_fragmentForgotPassword_send);

        auth = FirebaseAuth.getInstance();

    }


    /**
     * Gorsel nesnelerin action lari tetiklenir .
     */
    public void actionAttributes(){

        // reset password maili gonderme kismi ...
        button_fragmentForgotPassword_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFilledControl()){
                    FirebaseUtils.sendResetPasswordMail(auth, textInputEditText_fragmentForgotPassword_email.getText().toString().trim(), getActivity());

                    LoginFragment loginFragment = new LoginFragment();
                    StockUtils.gotoFragment(getActivity(), loginFragment, R.id.mainActivity_fragmentHolder, "whichFragment", "sendButton");

                }
                else {
                    Toast.makeText(getActivity(), "Lütfen email hesabınızı giriniz .", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //login fragment a donus kismi ...
        imageView_fragmentForgotPassword_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginFragment loginFragment = new LoginFragment();
                StockUtils.gotoFragment(getActivity(), loginFragment, R.id.mainActivity_fragmentHolder, "whichButton", "forgotPasswordBackButton");

            }
        });

    }


    /**
     * Gerekli alanlar eksiksiz bir sekilde dolduruldu mu ?
     * @return --> dolduruldu ise (true)
     */
    public boolean isFilledControl(){

        String email = textInputEditText_fragmentForgotPassword_email.getText().toString().trim();

        if (!email.equals("")){
            return true;
        }
        else {
            return false;
        }

    }


}
