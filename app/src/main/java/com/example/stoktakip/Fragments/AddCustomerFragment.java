package com.example.stoktakip.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.R;
import com.example.stoktakip.Utils.StockUtils;

public class AddCustomerFragment extends Fragment {

    private ImageView imageView_fragmentAddCustomer_customerPP, imageView_fragmentAddCustomer_back;
    private TextView textView_fragmentAddCustomer_addCustomerPP;
    private EditText editText_fragmentAddCustomer__customerName, editText_fragmentAddCustomer_customerSurname
                     , editTextText_fragmentAddCustomer_companyName, editTextText_fragmentAddCustomer_customerNum
                     , editText_fragmentAddCustomer_customerAddress;
    private Button button_fragmentAddCustomer_save;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_customer_design,  container, false);

        defineAttributes(rootView);
        actionAttributes();
        return rootView;

    }


    /**
     * Gorsel nesneler tanimlandi ve baglandi .
     */
    public void defineAttributes(View rootView){

        imageView_fragmentAddCustomer_customerPP = rootView.findViewById(R.id.imageView_fragmentAddCustomer_customerPP);
        textView_fragmentAddCustomer_addCustomerPP = rootView.findViewById(R.id.textView_fragmentAddCustomer_addCustomerPP);
        editText_fragmentAddCustomer__customerName = rootView.findViewById(R.id.editText_fragmentAddCustomer__customerName);
        editText_fragmentAddCustomer_customerSurname = rootView.findViewById(R.id.editText_fragmentAddCustomer_customerSurname);
        editTextText_fragmentAddCustomer_companyName = rootView.findViewById(R.id.editTextText_fragmentAddCustomer_companyName);
        editTextText_fragmentAddCustomer_customerNum = rootView.findViewById(R.id.editTextText_fragmentAddCustomer_customerNum);
        editText_fragmentAddCustomer_customerAddress = rootView.findViewById(R.id.editText_fragmentAddCustomer_customerAddress);
        button_fragmentAddCustomer_save = rootView.findViewById(R.id.button_fragmentAddCustomer_save);
        imageView_fragmentAddCustomer_back = rootView.findViewById(R.id.imageView_fragmentAddCustomer_back);

    }



    /**
     * Gorsel nesnelerin action lari tetiklenir .
     */
    public void actionAttributes(){

        // resim secme kismi ...
        textView_fragmentAddCustomer_addCustomerPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // kaydet kismi ...
        button_fragmentAddCustomer_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Customer Fragment a geri donme kismi ...
        imageView_fragmentAddCustomer_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomersFragment customersFragment = new CustomersFragment();
                StockUtils.gotoFragment(getActivity(), customersFragment, R.id.frameLayoutEntryActivity_holder, 1);

            }
        });

    }



}
