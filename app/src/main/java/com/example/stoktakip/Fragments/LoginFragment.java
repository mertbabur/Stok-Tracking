package com.example.stoktakip.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.R;
import com.example.stoktakip.Utils.StockUtils;
import com.google.android.material.textfield.TextInputEditText;


public class LoginFragment extends Fragment {

    private Button fragmentLogin_sign, button_fragmentLogin_login;
    private TextInputEditText fragmentLogin_email, fragmentLogin_password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login_design, container, false);

        defineAttributes(rootView);
        actionAttributes();


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


    /**
     * Action i olan görsel nesneler burada tetiklenir .
     */
    public void actionAttributes(){

        // giris yapma kismi ...
        button_fragmentLogin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    }


}
