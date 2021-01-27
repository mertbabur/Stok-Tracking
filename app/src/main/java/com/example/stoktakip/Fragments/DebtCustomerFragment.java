package com.example.stoktakip.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.Adapters.DebtCustomerAdapter;
import com.example.stoktakip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DebtCustomerFragment extends Fragment {

    private Toolbar toolbar_debtCustomerFragment;
    private RecyclerView recyclerView_debtCustomerFragment;

    private List<String> debtCustomerKeyList;

    private DebtCustomerAdapter adapter;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private FirebaseAuth mAuth;

    private String USER_UID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_debt_customer_design, container, false);

        defineAttributes(rootView);
        defineRecyclerView();
        defineToolbar();
        getDebtCustomerFromDB();

        return rootView;

    }


    /**
     * Gorsel nesneler tanimlandi .
     * @param rootView
     */
    public void defineAttributes(View rootView){

        toolbar_debtCustomerFragment = rootView.findViewById(R.id.toolbar_debtCustomerFragment);
        recyclerView_debtCustomerFragment = rootView.findViewById(R.id.recyclerView_debtCustomerFragment);

        debtCustomerKeyList = new ArrayList();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();

        USER_UID = mAuth.getUid();

    }

    /**
     * RecyclerView tanimla .
     */
    public void defineRecyclerView(){

        recyclerView_debtCustomerFragment.setHasFixedSize(true);
        recyclerView_debtCustomerFragment.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new DebtCustomerAdapter(getActivity(), debtCustomerKeyList, USER_UID);

        recyclerView_debtCustomerFragment.setAdapter(adapter);

    }

    /**
     * Toolbari tanimlar .
     */
    public void defineToolbar(){

        toolbar_debtCustomerFragment.setTitle("Satışlar");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar_debtCustomerFragment);

    }

    /**
     * DB den satis yapilan musterileri getirir ve debtCustomerList e atar .
     */
    public void getDebtCustomerFromDB(){

        myRef.child("SoldProducts").child(USER_UID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String customerKey = snapshot.getKey().toString();
                debtCustomerKeyList.add(customerKey);

                defineRecyclerView();

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
