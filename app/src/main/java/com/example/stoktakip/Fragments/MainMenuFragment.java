package com.example.stoktakip.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.Activities.EntryActivity;
import com.example.stoktakip.Activities.MainActivity;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.FirebaseUtils;
import com.example.stoktakip.Utils.StockUtils;
import com.google.firebase.auth.FirebaseAuth;

public class MainMenuFragment extends Fragment {

    private CardView cardView_fragmentMainMenu_customerClick, cardView_fragmentMainMenu_supplier, cardView_fragmentMainMenu_addProduct
                    , cardView_fragmentMainMenu_soldClick, cardView_fragmentMainMenu_cashDesk, cardView_fragmentMainMenu_cashDeskcardView_fragmentMainMenu_addAdditionalExpense
                    , cardView_myStock;
    private ImageView imageView_mainMenuCikis;

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
        cardView_fragmentMainMenu_addProduct = rootView.findViewById(R.id.cardView_fragmentMainMenu_addProduct);
        cardView_fragmentMainMenu_soldClick = rootView.findViewById(R.id.cardView_fragmentMainMenu_soldClick);
        cardView_fragmentMainMenu_cashDesk = rootView.findViewById(R.id.cardView_fragmentMainMenu_cashDesk);
        cardView_myStock = rootView.findViewById(R.id.cardView_myStock);
        imageView_mainMenuCikis = rootView.findViewById(R.id.imageView_mainMenuCikis);
        cardView_fragmentMainMenu_cashDeskcardView_fragmentMainMenu_addAdditionalExpense = rootView.findViewById(R.id.cardView_fragmentMainMenu_cashDeskcardView_fragmentMainMenu_addAdditionalExpense);

    }


    /**
     * Gorsel nesnelerin action lari tetiklenir .
     */
    public void actionAttributes(){

        // Musterilerin oldugu kismi acma ...
        cardView_fragmentMainMenu_customerClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomersOrSuppliersFragment customersOrSuppliersFragment = new CustomersOrSuppliersFragment();
                StockUtils.gotoFragment(getActivity(), customersOrSuppliersFragment, R.id.frameLayoutEntryActivity_holder, "whichButton", "customerButton", 1);

            }
        });


        // tedarikcilerin oldugu kismi acma ...
        cardView_fragmentMainMenu_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomersOrSuppliersFragment customersOrSuppliersFragment = new CustomersOrSuppliersFragment();
                StockUtils.gotoFragment(getActivity(), customersOrSuppliersFragment, R.id.frameLayoutEntryActivity_holder, "whichButton", "supplierButton", 1);
            }
        });

        // product ekleme kismini acar ...
        cardView_fragmentMainMenu_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductsFragments productsFragments = new ProductsFragments();
                StockUtils.gotoFragment(getActivity(), productsFragments, R.id.frameLayoutEntryActivity_holder, "whichFragment", "addProduct", 1);
            }
        });

        // satislara bakma kismi ...
        cardView_fragmentMainMenu_soldClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DebtCustomerFragment debtCustomerFragment = new DebtCustomerFragment();
                StockUtils.gotoFragment(getActivity(),debtCustomerFragment, R.id.frameLayoutEntryActivity_holder, 1);


            }
        });

        // kasaya giris kismi ...
        cardView_fragmentMainMenu_cashDesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CashDeskFragment cashDeskFragment = new CashDeskFragment();
                StockUtils.gotoFragment(getActivity(), cashDeskFragment, R.id.frameLayoutEntryActivity_holder,1);


            }
        });

        // ek gider ekleme kısmı ...
        cardView_fragmentMainMenu_cashDeskcardView_fragmentMainMenu_addAdditionalExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AdditionalExpenseFragment additionalExpenseFragment = new AdditionalExpenseFragment();
                StockUtils.gotoFragment(getActivity(), additionalExpenseFragment, R.id.frameLayoutEntryActivity_holder, 1);

            }
        });


        // kendi stogumu acma ...
        cardView_myStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SettingsFragment settingsFragment = new SettingsFragment();
                StockUtils.gotoFragment(getActivity(), settingsFragment, R.id.frameLayoutEntryActivity_holder, 1);

            }
        });

        // hesaptan cikis kismi .
        imageView_mainMenuCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                FirebaseAuth.getInstance().signOut();

            }
        });


    }

}
