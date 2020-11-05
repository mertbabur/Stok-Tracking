package com.example.stoktakip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.Models.Customer;

import java.util.List;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.CardHolder>{

    private Context mContex;
    private List<Customer> customerList;

    public CustomerListAdapter(Context mContex, List<Customer> customerList) {
        this.mContex = mContex;
        this.customerList = customerList;
    }

    public class CardHolder extends RecyclerView.ViewHolder{

        ImageView imageView_cardViewCustomer_customerPP, imageView_cardViewCustomer_phoneCall, imageView_cardViewCustomer_sendMessage;
        TextView textView_cardViewCustomer_customerName;
        CardView cardView_cardViewCustomer;

        public CardHolder(@NonNull View itemView) {
            super(itemView);

            imageView_cardViewCustomer_customerPP = itemView.findViewById(R.id.imageView_cardViewCustomer_customerPP);
            imageView_cardViewCustomer_phoneCall = itemView.findViewById(R.id.imageView_cardViewCustomer_phoneCall);
            imageView_cardViewCustomer_sendMessage = itemView.findViewById(R.id.imageView_cardViewCustomer_sendMessage);
            textView_cardViewCustomer_customerName = itemView.findViewById(R.id.textView_cardViewCustomer_customerName);
            cardView_cardViewCustomer = itemView.findViewById(R.id.cardView_cardViewCustomer);

        }
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_customer_design, parent, false);

        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }



}
