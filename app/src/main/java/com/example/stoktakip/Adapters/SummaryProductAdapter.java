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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        TextView textView_cardView_summaryProduct_productName, textView_cardView_summaryProduct_debt, textView_cardView_summaryProduct_purchasedQuantity
                , textView_cardView_summaryProduct_purchasedPrice, textView_cardView_summaryProduct_sellingPrice;

        public CardHolder(@NonNull View itemView) {
            super(itemView);

            textView_cardView_summaryProduct_productName = itemView.findViewById(R.id.textView_cardView_summaryProduct_productName);
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

        setProductInfo(holder, product);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    /**
     * 
     * @param holder
     * @param product
     */
    public void setProductInfo(CardHolder holder, Product product){

        holder.textView_cardView_summaryProduct_productName.setText("Ürün Adı : " + product.getProductName());
        holder.textView_cardView_summaryProduct_purchasedPrice.setText("Alınan Fiyat : " + product.getPurchasePrice());
        holder.textView_cardView_summaryProduct_sellingPrice.setText("Satış Fiyatı : " + product.getSellingPrice());

        String type = product.getTypeProduct();
        String productQuantity = product.getHowManyUnit();
        setProductQuantity(holder, type, productQuantity);

    }


    public void setProductQuantity(CardHolder holder, String typeProduct, String productQuantity){

        String type;
        if (typeProduct.equals("Adet"))
            type = "Tane";
        else if(typeProduct.equals("Ağırlık"))
            type = "Kilo";
        else // Hacim
            type = "Litre";

        holder.textView_cardView_summaryProduct_purchasedQuantity.setText(productQuantity + " " + type);

    }


}
