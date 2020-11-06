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

import com.example.stoktakip.Adapters.CustomerOrSupplierListAdapter;
import com.example.stoktakip.Models.CustomerOrSupplier;
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

public class CustomersOrSuppliersFragment extends Fragment {

    private RecyclerView recyclerView_fragmentCustomers;
    private FloatingActionButton floatingActionButton_fragmentCustomers_add;
    private Toolbar toolbar_fragmentCustomers;

    private List<CustomerOrSupplier> customerList;

    private CustomerOrSupplierListAdapter adapter;

    private String WHICH_BUTTON;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_customers_design, container, false);

        defineAttributes(rootView);
        actionAttributes();

        if (WHICH_BUTTON.equals("customerButton"))
            getCustomerOrSupplierFromDBandDefineRecyclerView("Customers");
        else
            getCustomerOrSupplierFromDBandDefineRecyclerView("Suppliers");


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

        WHICH_BUTTON = getArguments().getString("whichButton", "bos button");


    }


    /**
     * Gorsel nesnelerin action lari tetiklenir .
     */
    public void actionAttributes(){

        floatingActionButton_fragmentCustomers_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddCustomerFragment addCustomerFragment = new AddCustomerFragment();
                if (WHICH_BUTTON.equals("customerButton"))
                    StockUtils.gotoFragment(getActivity(), addCustomerFragment, R.id.frameLayoutEntryActivity_holder, "whichButton", "customerButton", 1);
                else
                    StockUtils.gotoFragment(getActivity(), addCustomerFragment, R.id.frameLayoutEntryActivity_holder, "whichButton", "supplierButton", 1);

            }
        });

    }


    /**
     * musterileri dbden alir ve recylerview tanimlar .
     */
    public void getCustomerOrSupplierFromDBandDefineRecyclerView(final String whichDB){

        String userUID = FirebaseAuth.getInstance().getUid();

        FirebaseDatabase.getInstance().getReference().child(whichDB).child(userUID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                CustomerOrSupplier customer = snapshot.getValue(CustomerOrSupplier.class);
                customerList.add(customer);
                defineRecyclerView();

                //adapter.notifyDataSetChanged();
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


    /**
     * Customer icin recyclerView tanimlar .
     */
    public void defineRecyclerView(){

        recyclerView_fragmentCustomers.setHasFixedSize(true);
        recyclerView_fragmentCustomers.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CustomerOrSupplierListAdapter(getActivity(), customerList);
        recyclerView_fragmentCustomers.setAdapter(adapter);

    }





}
