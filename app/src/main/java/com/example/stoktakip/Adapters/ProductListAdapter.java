package com.example.stoktakip.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.Fragments.ProductDetailFragment;
import com.example.stoktakip.Fragments.SellProductFragment;
import com.example.stoktakip.Models.Product;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.StockUtils;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.CardHolder>{

    private Context mContext;
    private List<Product> productList;
    private String whichButton;
    private String customerKey;

    public ProductListAdapter(Context mContext, List<Product> productList, String whichButton, String customerKey) {
        this.mContext = mContext;
        this.productList = productList;
        this.whichButton = whichButton;
        this.customerKey = customerKey;
    }

    public class CardHolder extends RecyclerView.ViewHolder{

        private TextView textView_cardViewProduct_productCode, textView_cardViewProduct_productName, textView_cardViewProduct_purchasePrice
                       , textView_cardViewProduct_sellingPrice, textView_cardViewProduct_howManyUnit;

        private CardView cardView_Product;

        public CardHolder(@NonNull View itemView) {
            super(itemView);

            textView_cardViewProduct_productCode = itemView.findViewById(R.id.textView_cardViewProduct_productCode);
            textView_cardViewProduct_productName = itemView.findViewById(R.id.textView_cardViewProduct_productName);
            textView_cardViewProduct_purchasePrice = itemView.findViewById(R.id.textView_cardViewProduct_purchasePrice);
            textView_cardViewProduct_sellingPrice = itemView.findViewById(R.id.textView_cardViewProduct_sellingPrice);
            textView_cardViewProduct_howManyUnit = itemView.findViewById(R.id.textView_cardViewProduct_howManyUnit);
            cardView_Product = itemView.findViewById(R.id.cardView_Product);



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

        final Product product = productList.get(position);

        setProductInfo(holder, product);


        if(whichButton.equals("addProduct")) { // eger add product fragmentindan  buraya gelindi ise ...
            // product detayina gecme kismi ...
            holder.cardView_Product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String productKey = product.getProductKey();
                    String productCode = product.getProductCode();

                    ProductDetailFragment productDetailFragment = new ProductDetailFragment();
                    StockUtils.gotoFragment(((AppCompatActivity) mContext), productDetailFragment, R.id.frameLayoutEntryActivity_holder, "productKey", productKey, "productCode", productCode, 1);

                }
            });
        }
        else{ // eger musteri listesinden sell product butonuna basilarak gelindi ise ...

            holder.cardView_Product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoSellProductFragment(product.getProductKey());
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    /**
     * Gorsel nesnelere bilgiler yerlestirilir ...
     * @param holder
     * @param product
     */
    public void setProductInfo(CardHolder holder, Product product){

        holder.textView_cardViewProduct_productCode.setText("Ürün Kodu : " + product.getProductCode());
        holder.textView_cardViewProduct_productName.setText("Ürün Adı : " + product.getProductName());
        holder.textView_cardViewProduct_purchasePrice.setText("Birim Alış Fiyatı : " + product.getPurchasePrice() + " TL");
        holder.textView_cardViewProduct_sellingPrice.setText("Birim Satış Fiyatı : " + product.getSellingPrice() + " TL");

        String productType = product.getTypeProduct();
        String type;
        if (productType.equals("Adet"))
            type = "Adet";
        else if (productType.equals("Ağırlık"))
            type = "Kilo";
        else
            type = "Litre";

        holder.textView_cardViewProduct_howManyUnit.setText("Stok Miktarı : " + product.getHowManyUnit() + " " + type);

    }


    /**
     * sell product fragment a gider .
     */
    public void gotoSellProductFragment(String productKey){

        SellProductFragment sellProductFragment = new SellProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString("whichFragment", whichButton);
        bundle.putString("customerOrSupplierKey", customerKey);
        bundle.putString("productKey", productKey);
        sellProductFragment.setArguments(bundle);
        ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutEntryActivity_holder, sellProductFragment).commit();

    }


}
