package com.example.stoktakip.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.Models.SoldProduct;
import com.example.stoktakip.R;

import java.util.List;

public class SoldProductHistoryAdapter extends RecyclerView.Adapter<SoldProductHistoryAdapter.CardHolder>{

    private Context mContext;
    private List<SoldProduct> soldProductList;

    public SoldProductHistoryAdapter(Context mContext, List<SoldProduct> soldProductList) {
        this.mContext = mContext;
        this.soldProductList = soldProductList;
    }

    public class CardHolder extends RecyclerView.ViewHolder{

        TextView textView_cardViewsoldProductHistory_date, textView_cardViewsoldProductHistory_companyName
                , textView_cardViewsoldProductHistory_quantity, textView_cardViewsoldProductHistory_totalSellingPrice;

        public CardHolder(@NonNull View itemView) {
            super(itemView);

            textView_cardViewsoldProductHistory_date = itemView.findViewById(R.id.textView_cardViewsoldProductHistory_date);
            textView_cardViewsoldProductHistory_companyName = itemView.findViewById(R.id.textView_cardViewsoldProductHistory_companyName);
            textView_cardViewsoldProductHistory_quantity = itemView.findViewById(R.id.textView_cardViewsoldProductHistory_quantity);
            textView_cardViewsoldProductHistory_totalSellingPrice = itemView.findViewById(R.id.textView_cardViewsoldProductHistory_totalSellingPrice);

        }
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_soldproducthistory_design, parent, false);

        return new CardHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }





}
