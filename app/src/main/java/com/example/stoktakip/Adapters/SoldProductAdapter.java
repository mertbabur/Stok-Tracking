package com.example.stoktakip.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.Models.Product;
import com.example.stoktakip.Models.SoldProduct;
import com.example.stoktakip.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SoldProductAdapter extends RecyclerView.Adapter<SoldProductAdapter.CardHolder> {

    private Context mContext;
    private List<SoldProduct> soldProductList;
    private String userUID;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public SoldProductAdapter(Context mContext, List<SoldProduct> soldProductList, String userUID) {
        this.mContext = mContext;
        this.soldProductList = soldProductList;
        this.userUID = userUID;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

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

        SoldProduct soldProduct = soldProductList.get(position);

        setInfoSoldProductName(holder, soldProduct);

        String customerKey = soldProduct.getCustomerKey();
        setInfoCompanyName(holder, customerKey);

        setInfoSoldProduct(holder, soldProduct);

    }

    @Override
    public int getItemCount() {
        return soldProductList.size();
    }


    /**
     * products DB sine giderek gorsel nesneye urun adini yerelestirir .
     * setProductSoldQuantity metodunu cagirir .
     * @param holder
     * @param soldProduct --> urun bilgileri bu nesne icinde .
     */
    public void setInfoSoldProductName(final CardHolder holder, final SoldProduct soldProduct){

        myRef.child("Products").child(userUID).child(soldProduct.getProductKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Product product = snapshot.getValue(Product.class);
                holder.textView_cardView_SoldProduct_productName.setText(product.getProductName());

                String typeProduct = product.getTypeProduct();
                setProductSoldQuantity(holder, soldProduct.getSoldQuantity(), typeProduct);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * customers DB sine giderek sirket adini gorsel nesneye yerlestirir .
     * @param holder
     * @param customerKey --> musteri keyi
     */
    public void setInfoCompanyName(final CardHolder holder, String customerKey){

        myRef.child("Customers").child(userUID).child(customerKey).child("companyName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String companyName = snapshot.getValue().toString();

                holder.textView_cardView_SoldProduct_companyName.setText(companyName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * Satilan product bilgilerini gorsel nesnelere yerlestirir .
     * @param holder
     * @param soldProduct --> bilgiler bu nesne icinde .
     */
    public void setInfoSoldProduct(CardHolder holder, SoldProduct soldProduct){

        holder.textView_cardView_SoldProduct_debt.setText("Ödenecek : " + soldProduct.getTotalSoldPrice() + " TL");

        String response;
        if (soldProduct.getIsPaid().equals("true"))
            response = "EVET";
        else
            response = "HAYIR";

        holder.textView_cardView_SoldProduct_isPaid.setText("Ödendi Mi : " + response);

    }


    /**
     * Urunun type ina gore toplam satis miktarini gorsel nesneye yerlestirir .
     * @param holder
     * @param soldQuantity --> satilan urun miktari .
     * @param typeProduct --> urun tipi (adet, agirlik, hacim)
     */
    public void setProductSoldQuantity(CardHolder holder, String soldQuantity, String typeProduct) {

        String type;
        if (typeProduct.equals("Adet"))
            type = "Tane";
        else if(typeProduct.equals("Ağırlık"))
            type = "Kilo";
        else // Hacim
            type = "Litre";

        holder.textView_cardView_SoldProduct_soldQuantity.setText("Satış Miktarı : " + soldQuantity + " " + type);




    }

}
