package com.example.stoktakip.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.R;

import java.util.List;

public class DebtCustomerAdapter extends RecyclerView.Adapter<DebtCustomerAdapter.CardHolder> {

    private Context mContext;
    private List customerList;

    public DebtCustomerAdapter(Context mContext, List customerList) {
        this.mContext = mContext;
        this.customerList = customerList;
    }

    public class CardHolder extends RecyclerView.ViewHolder{

        public CardHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_debt_customer_design, parent, false);

        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }



}

