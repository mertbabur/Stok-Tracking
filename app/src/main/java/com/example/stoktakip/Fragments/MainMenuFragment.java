package com.example.stoktakip.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.R;
import com.example.stoktakip.Utils.StockUtils;
import com.google.firebase.auth.FirebaseAuth;

public class MainMenuFragment extends Fragment {

    private CardView cardView_fragmentMainMenu_customerClick, cardView_fragmentMainMenu_supplier;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main_menu_design, container, false);

        defineAttributes(rootView);
        actionAttributes();
        Toast.makeText(getActivity(), FirebaseAuth.getInstance().getUid(), Toast.LENGTH_SHORT).show();
        return rootView;

    }


    /**
     * Gorsel nesneler tanimlanir ve baglanir .
     */
    public void defineAttributes(View rootView){

        cardView_fragmentMainMenu_customerClick = rootView.findViewById(R.id.cardView_fragmentMainMenu_customerClick);
        cardView_fragmentMainMenu_supplier = rootView.findViewById(R.id.cardView_fragmentMainMenu_supplier);

    }


    /**
     * Gorsel nesnelerin action lari tetiklenir .
     */
    public void actionAttributes(){

        // Musterilerin oldugu kismi acma ...
        cardView_fragmentMainMenu_customerClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomersFragment customersFragment = new CustomersFragment();
                StockUtils.gotoFragment(getActivity(), customersFragment, R.id.frameLayoutEntryActivity_holder, "whichButton", "customerButton", 1);

            }
        });


        // tedarikcilerin oldugu kismi acma ...
        cardView_fragmentMainMenu_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomersFragment customersFragment = new CustomersFragment();
                StockUtils.gotoFragment(getActivity(), customersFragment, R.id.frameLayoutEntryActivity_holder, "whichButton", "supplierButton", 1);
            }
        });

    }

}
