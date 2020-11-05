package com.example.stoktakip.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.CustomerListAdapter;
import com.example.stoktakip.Models.Customer;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.StockUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CustomersFragment extends Fragment {

    private RecyclerView recyclerView_fragmentCustomers;
    private FloatingActionButton floatingActionButton_fragmentCustomers_add;
    private Toolbar toolbar_fragmentCustomers;

    private List<Customer> customerList;

    private CustomerListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_customers_design, container, false);

        defineAttributes(rootView);
        actionAttributes();


        return rootView;
    }


    /**
     * Gorsel nesneler tanimlanir ve baglanir .
     */
    public void defineAttributes(View rootView){

        recyclerView_fragmentCustomers = rootView.findViewById(R.id.recyclerView_fragmentCustomers);
        floatingActionButton_fragmentCustomers_add = rootView.findViewById(R.id.floatingActionButton_fragmentCustomers_add);
        toolbar_fragmentCustomers = rootView.findViewById(R.id.toolbar_fragmentCustomers);

        customerList = new ArrayList<>();


    }


    /**
     * Gorsel nesnelerin action lari tetiklenir .
     */
    public void actionAttributes(){

        floatingActionButton_fragmentCustomers_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddCustomerFragment addCustomerFragment = new AddCustomerFragment();
                StockUtils.gotoFragment(getActivity(), addCustomerFragment, R.id.frameLayoutEntryActivity_holder, 1);

            }
        });

    }


    /**
     * RecyclerView i tanimlar .
     */
    public void defineRecyclerView(){

        recyclerView_fragmentCustomers.setHasFixedSize(true);
        recyclerView_fragmentCustomers.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new CustomerListAdapter(getActivity(), customerList);
        recyclerView_fragmentCustomers.setAdapter(adapter);

    }


}
