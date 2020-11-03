package com.example.stoktakip.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.R;
import com.example.stoktakip.Utils.StockUtils;
import com.google.android.material.textfield.TextInputEditText;

public class SignFragment extends Fragment {

    private TextInputEditText fragmentSign_name, fragmentSign_userName, fragmentSign_email, fragmentSign_password;
    private ImageView imageView_fragmentSign_back;
    private Button button_fragmentSign_sign;

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
                StockUtils.gotoFragment(getActivity(), loginFragment, R.id.mainActivity_fragmentHolder);

            }
        });

    }

}
