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
import com.example.stoktakip.Adapters.SupplierListAdapter;
import com.example.stoktakip.Models.CustomerOrSupplier;
import com.example.stoktakip.R;

import java.util.ArrayList;
import java.util.List;

public class SupplierListFragment extends Fragment {

    private Toolbar toolbar_fragmentSupplierList;
    private RecyclerView recyclerView_fragmentSupplierList;

    private List<CustomerOrSupplier> supplierList;

    private SupplierListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_supplier_list_design, container, false);

        defineAttributes(rootView);
        supplierList.add(new CustomerOrSupplier("","","","","","",""));
        supplierList.add(new CustomerOrSupplier("","","","","","",""));
        supplierList.add(new CustomerOrSupplier("","","","","","",""));

        defineRecyclerView();

        return rootView;


    }

    /**
     * Gorsel nesneler baglandi ve tanimlandi .
     */
    public void defineAttributes(View rootView){
        toolbar_fragmentSupplierList = rootView.findViewById(R.id.toolbar_fragmentSupplierList);
        recyclerView_fragmentSupplierList = rootView.findViewById(R.id.recyclerView_fragmentSupplierList);

        supplierList = new ArrayList<>();



    }

    /**
     * Customer icin recyclerView tanimlar .
     */
    public void defineRecyclerView(){

        recyclerView_fragmentSupplierList.setHasFixedSize(true);
        recyclerView_fragmentSupplierList.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new SupplierListAdapter(getActivity(), supplierList);


        recyclerView_fragmentSupplierList.setAdapter(adapter);

    }



}
