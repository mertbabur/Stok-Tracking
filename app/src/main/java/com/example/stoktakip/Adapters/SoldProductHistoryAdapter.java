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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SoldProductHistoryAdapter extends RecyclerView.Adapter<SoldProductHistoryAdapter.CardHolder>{

    private Context mContext;
    private List<SoldProduct> soldProductList;
    private String userUID;

    public SoldProductHistoryAdapter(Context mContext, List<SoldProduct> soldProductList, String userUID) {
        this.mContext = mContext;
        this.soldProductList = soldProductList;
        this.userUID = userUID;
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

        SoldProduct soldProduct = soldProductList.get(position);

        String customerId = soldProduct.getCustomerKey();
        setInfoCompanyName(holder, customerId);

        setInfoSoldProduct(holder, soldProduct);


    }

    @Override
    public int getItemCount() {
        return soldProductList.size();
    }


    /**
     * Customer DB ye giderek companyName i gerekli gorsel nesneye yerlestirir .
     * @param holder
     * @param customerId
     */
    public void setInfoCompanyName(final CardHolder holder, String customerId){

        FirebaseDatabase.getInstance().getReference().child("Customers").child(userUID).child(customerId).child("companyName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                holder.textView_cardViewsoldProductHistory_companyName.setText("ŞİRKET ADI : " + snapshot.getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * SoldProduct bilgilerini gerekli gorsel nesnelere yerlestirir .
     * @param holder
     * @param soldProduct
     */
    public void setInfoSoldProduct(CardHolder holder, SoldProduct soldProduct){

        holder.textView_cardViewsoldProductHistory_date.setText("TARİH : " + soldProduct.getSoldDate());
        holder.textView_cardViewsoldProductHistory_quantity.setText("SATILAN MİKTAR : " + soldProduct.getSoldQuantity());
        holder.textView_cardViewsoldProductHistory_totalSellingPrice.setText("TOPLAM SATIŞ FİYATI : " + soldProduct.getTotalSoldPrice());

    }



}
