package com.example.stoktakip.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.R;

import java.util.List;

public class SoldProductAdapter extends RecyclerView.Adapter<SoldProductAdapter.CardHolder> {

    private Context mContext;
    private List productList;

    public SoldProductAdapter(Context mContext, List productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    public class CardHolder extends RecyclerView.ViewHolder{

        TextView textView_cardView_SoldProduct_productName, textView_cardView_SoldProduct_companyName, textView_cardView_SoldProduct_soldQuantity
                , textView_cardView_SoldProduct_debt, textView_cardView_SoldProduct_isPaid;

        CardView cardView_SoldProduct_productClick;

        public CardHolder(@NonNull View itemView) {
            super(itemView);

            textView_cardView_SoldProduct_productName = itemView.findViewById(R.id.textView_cardView_SoldProduct_productName);
            textView_cardView_SoldProduct_companyName = itemView.findViewById(R.id.textView_cardView_SoldProduct_companyName);
            textView_cardView_SoldProduct_soldQuantity = itemView.findViewById(R.id.textView_cardView_SoldProduct_soldQuantity);
            textView_cardView_SoldProduct_debt = itemView.findViewById(R.id.textView_cardView_SoldProduct_debt);
            textView_cardView_SoldProduct_isPaid = itemView.findViewById(R.id.textView_cardView_SoldProduct_isPaid);

            cardView_SoldProduct_productClick = itemView.findViewById(R.id.cardView_SoldProduct_productClick);

        }
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_sold_product_design, parent, false);

        return new CardHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }





}
