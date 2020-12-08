package com.example.stoktakip.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.Models.Product;
import com.example.stoktakip.R;

import java.util.List;

public class SummaryProductAdapter extends RecyclerView.Adapter<SummaryProductAdapter.CardHolder>{

    private Context mContext;
    private List<Product> productList;
    private String userUID;

    public SummaryProductAdapter(Context mContext, List<Product> productList, String userUID) {
        this.mContext = mContext;
        this.productList = productList;
        this.userUID = userUID;
    }

    public class CardHolder extends RecyclerView.ViewHolder{

        TextView textView_cardView_summaryProduct_productName, textView_cardView_summaryProduct_companyName, textView_cardView_summaryProduct_debt
                , textView_cardView_summaryProduct_purchasedQuantity, textView_cardView_summaryProduct_purchasedPrice, textView_cardView_summaryProduct_sellingPrice;

        public CardHolder(@NonNull View itemView) {
            super(itemView);

            textView_cardView_summaryProduct_productName = itemView.findViewById(R.id.textView_cardView_summaryProduct_productName);
            textView_cardView_summaryProduct_companyName = itemView.findViewById(R.id.textView_cardView_summaryProduct_companyName);
            textView_cardView_summaryProduct_debt = itemView.findViewById(R.id.textView_cardView_summaryProduct_debt);
            textView_cardView_summaryProduct_purchasedQuantity = itemView.findViewById(R.id.textView_cardView_summaryProduct_purchasedQuantity);
            textView_cardView_summaryProduct_purchasedPrice = itemView.findViewById(R.id.textView_cardView_summaryProduct_purchasedPrice);
            textView_cardView_summaryProduct_sellingPrice = itemView.findViewById(R.id.textView_cardView_summaryProduct_sellingPrice);

        }
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_summary_purchased_product_design, parent, false);

        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

        Product product = productList.get(position);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }




}
