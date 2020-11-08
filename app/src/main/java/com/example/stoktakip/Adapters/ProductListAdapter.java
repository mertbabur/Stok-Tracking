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

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.CardHolder>{

    private Context mContext;
    private List<Product> productList;

    public ProductListAdapter(Context mContext, List<Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    public class CardHolder extends RecyclerView.ViewHolder{

        private TextView textView_cardViewProduct_productCode, textView_cardViewProduct_productName, textView_cardViewProduct_purchasePrice
                       , textView_cardViewProduct_sellingPrice, textView_cardViewProduct_howManyUnit, textView_cardViewProduct_whichUnit;

        public CardHolder(@NonNull View itemView) {
            super(itemView);

            textView_cardViewProduct_productCode = itemView.findViewById(R.id.textView_cardViewProduct_productCode);
            textView_cardViewProduct_productName = itemView.findViewById(R.id.textView_cardViewProduct_productName);
            textView_cardViewProduct_purchasePrice = itemView.findViewById(R.id.textView_cardViewProduct_purchasePrice);
            textView_cardViewProduct_sellingPrice = itemView.findViewById(R.id.textView_cardViewProduct_sellingPrice);
            textView_cardViewProduct_howManyUnit = itemView.findViewById(R.id.textView_cardViewProduct_howManyUnit);
            textView_cardViewProduct_whichUnit = itemView.findViewById(R.id.textView_cardViewProduct_whichUnit);



        }
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_product_list_design, parent, false);

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
