package com.example.stoktakip.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.Models.AdditionalExpense;
import com.example.stoktakip.Models.CashDesk;
import com.example.stoktakip.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.CardHolder>{

    private Context mContext;
    private List<AdditionalExpense> expenseList;
    private String userUID;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public ExpenseAdapter(Context mContext, List<AdditionalExpense> expenseList, String userUID) {
        this.mContext = mContext;
        this.expenseList = expenseList;
        this.userUID = userUID;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

    }

    public class CardHolder extends RecyclerView.ViewHolder{

        TextView textView_cardview_expense_expenseType, textView_cardview_expense_expenseDate, textView_cardview_expense_expenseQuantity
                , textView_cardview_expense_expenseAbout;
        private ImageView imageView_cardView_Expense_deleteExpense, imageView_cardView_Expense_updateExpense;

        public CardHolder(@NonNull View itemView) {
            super(itemView);

            textView_cardview_expense_expenseType = itemView.findViewById(R.id.textView_cardview_expense_expenseType);
            textView_cardview_expense_expenseDate = itemView.findViewById(R.id.textView_cardview_expense_expenseDate);
            textView_cardview_expense_expenseQuantity = itemView.findViewById(R.id.textView_cardview_expense_expenseQuantity);
            textView_cardview_expense_expenseAbout = itemView.findViewById(R.id.textView_cardview_expense_expenseAbout);
            imageView_cardView_Expense_deleteExpense = itemView.findViewById(R.id.imageView_cardView_Expense_deleteExpense);
            imageView_cardView_Expense_updateExpense = itemView.findViewById(R.id.imageView_cardView_Expense_updateExpense);

        }
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_expense_design, parent, false);

        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardHolder holder, int position) {

        final AdditionalExpense additionalExpense = expenseList.get(position);

        setExpenseInfo(holder, additionalExpense);

        // ek gider silme .
        holder.imageView_cardView_Expense_deleteExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteAdditionalExpense(holder, additionalExpense.getExpenseKey(), Float.valueOf(additionalExpense.getExpenseQuantity()), additionalExpense.getExpenseType());

            }
        });

        // ek gider guncelleme .
        holder.imageView_cardView_Expense_updateExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return expenseList.size();
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


    /**
     *
     * @param holder
     * @param expenseKey
     * @param expenseQuantity
     */
    public void deleteAdditionalExpense(CardHolder holder, final String expenseKey, final Float expenseQuantity, final String expenseType){

        myRef.child("CashDesk").child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CashDesk cashDesk = snapshot.getValue(CashDesk.class);

                Float totalAdditionalExpense = Float.valueOf(cashDesk.getTotalAdditionalExpense());
                Float totalExpense = Float.valueOf(cashDesk.getTotalExpense());

                totalAdditionalExpense -= expenseQuantity;
                totalExpense -= expenseQuantity;

                Map map = new HashMap();
                if (expenseType.equals("taxExpense")) {
                    Float taxExpense = Float.valueOf(cashDesk.getTotalTaxExpense());
                    taxExpense -= expenseQuantity;
                    map.put("totalTaxExpense", String.valueOf(taxExpense));
                    map.put("totalAdditionalExpense", String.valueOf(totalAdditionalExpense));
                    map.put("totalExpense", String.valueOf(totalExpense));
                }
                else if (expenseType.equals("other")) {
                    Float otherExpense = Float.valueOf(cashDesk.getTotalTaxExpense());
                    otherExpense -= expenseQuantity;
                    map.put("totalTaxExpense", String.valueOf(totalAdditionalExpense));
                    map.put("totalOtherExpense", String.valueOf(otherExpense));
                    map.put("totalExpense", String.valueOf(totalExpense));
                }
                else if (expenseType.equals("rentExpense")) {
                    Float rentExpense = Float.valueOf(cashDesk.getTotalTaxExpense());
                    rentExpense -= expenseQuantity;
                    map.put("totalAdditionalExpense", String.valueOf(totalAdditionalExpense));
                    map.put("totalRentExpense", String.valueOf(rentExpense));
                    map.put("totalExpense", String.valueOf(totalExpense));
                }
                else if (expenseType.equals("fuelExpense")) {
                    Float fuelExpense = Float.valueOf(cashDesk.getTotalTaxExpense());
                    fuelExpense -= expenseQuantity;
                    map.put("totalAdditionalExpense", String.valueOf(totalAdditionalExpense));
                    map.put("totalFuelExpense", String.valueOf(fuelExpense));
                    map.put("totalExpense", String.valueOf(totalExpense));;
                }
                else {
                    Float employeeExpense = Float.valueOf(cashDesk.getTotalTaxExpense());
                    employeeExpense -= expenseQuantity;
                    map.put("totalAdditionalExpense", String.valueOf(totalAdditionalExpense));
                    map.put("totalEmployeeCost", String.valueOf(employeeExpense));
                    map.put("totalExpense", String.valueOf(totalExpense));
                }


                myRef.child("CashDesk").child(userUID).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            myRef.child("UserExpenses").child(userUID).child(expenseKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(mContext, "Silme işlem başarılı .", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            Toast.makeText(mContext, "Silme işlemi başarısız .", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



}
