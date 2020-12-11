package com.example.stoktakip.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.Models.CashDesk;
import com.example.stoktakip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CashDeskFragment extends Fragment {

    private TextView textView_cashDeskFragment_totalExpense, textView_cashDeskFragment_totalPurchasedProductPrice, textView_cashDeskFragment_totalSellingProductPrice
                  , textView_cashDeskFragment_totalCollectedProductPrice, textView_cashDeskFragment_totalPaidProductPrice, textView_cashDeskFragment_totalAdditionalExpense
                  , textView_cashDeskFragment_totalCollectedProductPrice_taxExpense, textView_cashDeskFragment_totalCollectedProductPrice_rentExpense, textView_cashDeskFragment_totalCollectedProductPrice_employeeCost
                  , textView_cashDeskFragment_totalCollectedProductPrice_other, textView_cashDeskFragment_totalCollectedProductPrice_fuelExpense;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private FirebaseAuth mAuth;

    private String USER_UID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_cash_desk_design, container, false);

        defineAttributes(rootView);
        getCashDesk();

        return rootView;

    }


    /**
     * Gorsel nesneler burada baglanir .
     * @param rootView
     */
    public void defineAttributes(View rootView){

        textView_cashDeskFragment_totalExpense = rootView.findViewById(R.id.textView_cashDeskFragment_totalExpense);
        textView_cashDeskFragment_totalPurchasedProductPrice = rootView.findViewById(R.id.textView_cashDeskFragment_totalPurchasedProductPrice);
        textView_cashDeskFragment_totalSellingProductPrice = rootView.findViewById(R.id.textView_cashDeskFragment_totalSellingProductPrice);
        textView_cashDeskFragment_totalCollectedProductPrice = rootView.findViewById(R.id.textView_cashDeskFragment_totalCollectedProductPrice);
        textView_cashDeskFragment_totalPaidProductPrice = rootView.findViewById(R.id.textView_cashDeskFragment_totalPaidProductPrice);
        textView_cashDeskFragment_totalAdditionalExpense = rootView.findViewById(R.id.textView_cashDeskFragment_totalAdditionalExpense);
        textView_cashDeskFragment_totalCollectedProductPrice_taxExpense = rootView.findViewById(R.id.textView_cashDeskFragment_totalCollectedProductPrice_taxExpense);
        textView_cashDeskFragment_totalCollectedProductPrice_rentExpense = rootView.findViewById(R.id.textView_cashDeskFragment_totalCollectedProductPrice_rentExpense);
        textView_cashDeskFragment_totalCollectedProductPrice_employeeCost = rootView.findViewById(R.id.textView_cashDeskFragment_totalCollectedProductPrice_employeeCost);
        textView_cashDeskFragment_totalCollectedProductPrice_other = rootView.findViewById(R.id.textView_cashDeskFragment_totalCollectedProductPrice_other);
        textView_cashDeskFragment_totalCollectedProductPrice_fuelExpense = rootView.findViewById(R.id.textView_cashDeskFragment_totalCollectedProductPrice_fuelExpense);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();

        USER_UID = mAuth.getUid();

    }

    public void getCashDesk(){

        myRef.child("CashDesk").child(USER_UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CashDesk cashDesk = snapshot.getValue(CashDesk.class);

                textView_cashDeskFragment_totalExpense.setText(cashDesk.getTotalExpense() + " TL");
                textView_cashDeskFragment_totalPurchasedProductPrice.setText(cashDesk.getTotalPurchasedProductPrice() + " TL");
                textView_cashDeskFragment_totalSellingProductPrice.setText(cashDesk.getTotalSellingProductPrice() + " TL");
                textView_cashDeskFragment_totalCollectedProductPrice.setText(cashDesk.getTotalCollectedProductPrice() + " TL");
                textView_cashDeskFragment_totalPaidProductPrice.setText(cashDesk.getTotalPaidProductPrice() + " TL");
                textView_cashDeskFragment_totalAdditionalExpense.setText(cashDesk.getTotalAdditionalExpense() + " TL");
                textView_cashDeskFragment_totalCollectedProductPrice_taxExpense.setText("VERGİ : " + cashDesk.getTotalTaxExpense() + " TL");
                textView_cashDeskFragment_totalCollectedProductPrice_rentExpense.setText("KİRA : " + cashDesk.getTotalRentExpense() + " TL");
                textView_cashDeskFragment_totalCollectedProductPrice_other.setText("DİĞER : " + cashDesk.getTotalOtherExpense() + " TL");
                textView_cashDeskFragment_totalCollectedProductPrice_fuelExpense.setText("YAKIT : " + cashDesk.getTotalFuelExpense() + " TL");
                textView_cashDeskFragment_totalCollectedProductPrice_employeeCost.setText("ÇALIŞAN MA. : " + cashDesk.getTotalEmployeeCost() + " TL");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




}
