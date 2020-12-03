package com.example.stoktakip.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        ImageView imageView_cardView_debtCustomer_customerPP;
        TextView textView_cardView_debtCustomer_companyName, textView_cardView_debtCustomer_customerName, textView_cardView_debtCustomer_padiMoney;

        public CardHolder(@NonNull View itemView) {
            super(itemView);

            imageView_cardView_debtCustomer_customerPP = itemView.findViewById(R.id.imageView_cardView_debtCustomer_customerPP);
            textView_cardView_debtCustomer_companyName = itemView.findViewById(R.id.textView_cardView_debtCustomer_companyName);
            textView_cardView_debtCustomer_customerName = itemView.findViewById(R.id.textView_cardView_debtCustomer_customerName);
            textView_cardView_debtCustomer_padiMoney = itemView.findViewById(R.id.textView_cardView_debtCustomer_padiMoney);

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

