package com.example.stoktakip.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.R;

import java.util.jar.Attributes;

public class LoginFragment extends Fragment {

    private Button fragmentLogin_sign, button_fragmentLogin_login;
    private EditText fragmentLogin_email, fragmentLogin_password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login_design, container, false);

        defineAttributes(rootView);


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

    }


}
