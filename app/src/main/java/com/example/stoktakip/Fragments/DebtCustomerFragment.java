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

import com.example.stoktakip.Adapters.DebtCustomerAdapter;
import com.example.stoktakip.Adapters.SoldProductAdapter;
import com.example.stoktakip.R;

import java.util.ArrayList;
import java.util.List;

public class DebtCustomerFragment extends Fragment {

    private Toolbar toolbar_debtCustomerFragment;
    private RecyclerView recyclerView_debtCustomerFragment;

    private List customerList;

    private DebtCustomerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_debt_customer_design, container, false);

        defineAttributes(rootView);
        defineRecyclerView();

        return rootView;

    }


    /**
     * Gorsel nesneler tanimlandi .
     * @param rootView
     */
    public void defineAttributes(View rootView){

        toolbar_debtCustomerFragment = rootView.findViewById(R.id.toolbar_debtCustomerFragment);
        recyclerView_debtCustomerFragment = rootView.findViewById(R.id.recyclerView_debtCustomerFragment);

        customerList = new ArrayList();

    }


    /**
     * RecyclerView tanimla .
     */
    public void defineRecyclerView(){

        recyclerView_debtCustomerFragment.setHasFixedSize(true);
        recyclerView_debtCustomerFragment.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new DebtCustomerAdapter(getActivity(), customerList);

        recyclerView_debtCustomerFragment.setAdapter(adapter);

    }

}
