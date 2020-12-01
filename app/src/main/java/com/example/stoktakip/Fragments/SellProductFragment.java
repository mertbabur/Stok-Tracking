package com.example.stoktakip.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.R;

public class SellProductFragment extends Fragment {

    private ImageView imageView_sellProductFragment_customerPP;
    private CardView cardView_sellProductFragment_productInfo, cardView_sellProductFragment_productQuantitySelect;
    private EditText editText_sellProductFragment_productName, editText_sellProductFragment_productQuantity;
    private TextView imageView_sellProductFragment_companyName, imageView_sellProductFragment_customerName, textView_sellProductFragment_productCode
            , textView_sellProductFragment_productName, textView_sellProductFragment_purchasePrice, textView_sellProductFragment_sellingPrice
            , textView_sellProductFragment_howManyQuantity, textView_sellProductFragment_selectProductClick;
    private Button button_sellProductFragment_sellProduct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sell_product_design, container, false);

        defineAttributes(rootView);


        return rootView;

    }


    /**
     * Gorsel nesneler tanimlanir .
     * @param rootView
     */
    public void defineAttributes(View rootView){

        imageView_sellProductFragment_customerPP = rootView.findViewById(R.id.imageView_sellProductFragment_customerPP);
        cardView_sellProductFragment_productInfo = rootView.findViewById(R.id.cardView_sellProductFragment_productInfo);
        cardView_sellProductFragment_productQuantitySelect = rootView.findViewById(R.id.cardView_sellProductFragment_productQuantitySelect);
        editText_sellProductFragment_productName = rootView.findViewById(R.id.editText_sellProductFragment_productName);
        editText_sellProductFragment_productQuantity = rootView.findViewById(R.id.editText_sellProductFragment_productQuantity);
        imageView_sellProductFragment_companyName = rootView.findViewById(R.id.imageView_sellProductFragment_companyName);
        imageView_sellProductFragment_customerName = rootView.findViewById(R.id.imageView_sellProductFragment_customerName);
        textView_sellProductFragment_productCode = rootView.findViewById(R.id.textView_sellProductFragment_productCode);
        textView_sellProductFragment_purchasePrice = rootView.findViewById(R.id.textView_sellProductFragment_purchasePrice);
        textView_sellProductFragment_sellingPrice = rootView.findViewById(R.id.textView_sellProductFragment_sellingPrice);
        textView_sellProductFragment_productName = rootView.findViewById(R.id.textView_sellProductFragment_productName);
        textView_sellProductFragment_howManyQuantity = rootView.findViewById(R.id.textView_sellProductFragment_howManyQuantity);
        textView_sellProductFragment_selectProductClick = rootView.findViewById(R.id.textView_sellProductFragment_selectProductClick);
        button_sellProductFragment_sellProduct = rootView.findViewById(R.id.button_sellProductFragment_sellProduct);


    }



}
