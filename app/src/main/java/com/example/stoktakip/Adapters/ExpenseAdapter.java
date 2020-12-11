package com.example.stoktakip.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.Models.AdditionalExpense;
import com.example.stoktakip.R;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.CardHolder>{

    private Context mContext;
    private List<AdditionalExpense> expenseList;
    private String userUID;

    public ExpenseAdapter(Context mContext, List<AdditionalExpense> expenseList, String userUID) {
        this.mContext = mContext;
        this.expenseList = expenseList;
        this.userUID = userUID;
    }

    public class CardHolder extends RecyclerView.ViewHolder{

        TextView textView_cardview_expense_expenseType, textView_cardview_expense_expenseDate, textView_cardview_expense_expenseQuantity
                , textView_cardview_expense_expenseAbout;

        public CardHolder(@NonNull View itemView) {
            super(itemView);

            textView_cardview_expense_expenseType = itemView.findViewById(R.id.textView_cardview_expense_expenseType);
            textView_cardview_expense_expenseDate = itemView.findViewById(R.id.textView_cardview_expense_expenseDate);
            textView_cardview_expense_expenseQuantity = itemView.findViewById(R.id.textView_cardview_expense_expenseQuantity);
            textView_cardview_expense_expenseAbout = itemView.findViewById(R.id.textView_cardview_expense_expenseAbout);

        }
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_expense_design, parent, false);

        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

        AdditionalExpense additionalExpense = expenseList.get(position);

        setExpenseInfo(holder, additionalExpense);


    }

    @Override
    public int getItemCount() {
        return 3;
    }


    /**
     * Gider bilgilerini gerekli gorsel nesnelere yerlestirir .
     * setExpenseType metodunu cagirir .
     * @param holder
     * @param additionalExpense
     */
    public void setExpenseInfo(CardHolder holder, AdditionalExpense additionalExpense){

        setExpenseType(holder, additionalExpense.getExpenseType());

        holder.textView_cardview_expense_expenseDate.setText("TARİH : " + additionalExpense.getExpenseDate());
        holder.textView_cardview_expense_expenseAbout.setText("GİDER NOTU : " + additionalExpense.getWhatExpense());
        holder.textView_cardview_expense_expenseQuantity.setText("TUTAR : " + additionalExpense.getExpenseQuantity() + " TL");

    }


    /**
     * Gider türünü gorsel nesneye yerlestirir .
     * @param holder
     * @param expenseType --> gider turu .
     */
    public void setExpenseType(CardHolder holder, String expenseType){

        String type;
        if (expenseType.equals("taxExpense"))
            type = "Vergi";
        else if (expenseType.equals("other"))
            type = "Diğer";
        else if (expenseType.equals("rentExpense"))
            type = "Kira";
        else if (expenseType.equals("fuelExpense"))
            type = "Yakıt";
        else
            type = "Çalışan Maaliyeti";

        holder.textView_cardview_expense_expenseType.setText("GİDER TÜRÜ : " + type);

    }


}
