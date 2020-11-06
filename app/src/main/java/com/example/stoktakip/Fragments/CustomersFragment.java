package com.example.stoktakip.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.Adapters.CustomerListAdapter;
import com.example.stoktakip.Models.Customer;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.StockUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CustomersFragment extends Fragment {

    private RecyclerView recyclerView_fragmentCustomers;
    private FloatingActionButton floatingActionButton_fragmentCustomers_add;
    private Toolbar toolbar_fragmentCustomers;

    private List<Customer> customerList;

    private CustomerListAdapter adapter;

    private String WHICH_FRAGMENT;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_customers_design, container, false);

        defineAttributes(rootView);
        actionAttributes();

        if (WHICH_FRAGMENT.equals("customerFragment")) {
            getCustomerFromDBandDefineRecyclerView();
            defineRecyclerView_customer();
        }

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

        WHICH_FRAGMENT = getArguments().getString("whichFragment", "bos fragment");


    }


    /**
     * Gorsel nesnelerin action lari tetiklenir .
     */
    public void actionAttributes(){

        floatingActionButton_fragmentCustomers_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddCustomerFragment addCustomerFragment = new AddCustomerFragment();
                if (WHICH_FRAGMENT.equals("customerFragment"))
                    StockUtils.gotoFragment(getActivity(), addCustomerFragment, R.id.frameLayoutEntryActivity_holder, "whichFragment", "customerFragment", 1);
                else
                    StockUtils.gotoFragment(getActivity(), addCustomerFragment, R.id.frameLayoutEntryActivity_holder, "whichFragment", "supplierFragment", 1);

            }
        });

    }

    public void defineRecyclerView_customer(){

        recyclerView_fragmentCustomers.setHasFixedSize(true);
        recyclerView_fragmentCustomers.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new CustomerListAdapter(getActivity(), customerList);
        recyclerView_fragmentCustomers.setAdapter(adapter);

    }


    /**
     * musterileri dbden alir ve recylerview tanimlar .
     */
    public void getCustomerFromDBandDefineRecyclerView(){

        String userUID = FirebaseAuth.getInstance().getUid();

        FirebaseDatabase.getInstance().getReference().child("Customers").child(userUID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Customer customer = snapshot.getValue(Customer.class);
                Log.e("error", customer.getCompanyName());
                customerList.add(customer);
                //adapter.notifyDataSetChanged();

                recyclerView_fragmentCustomers.setHasFixedSize(true);
                recyclerView_fragmentCustomers.setLayoutManager(new LinearLayoutManager(getActivity()));

                adapter = new CustomerListAdapter(getActivity(), customerList);
                recyclerView_fragmentCustomers.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




}
