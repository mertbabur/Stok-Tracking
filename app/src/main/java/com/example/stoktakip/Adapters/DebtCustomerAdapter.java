package com.example.stoktakip.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.Fragments.SoldProductlistFragment;
import com.example.stoktakip.Models.CustomerOrSupplier;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.StockUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DebtCustomerAdapter extends RecyclerView.Adapter<DebtCustomerAdapter.CardHolder> {

    private Context mContext;
    private List<String> debtCustomerList;
    private String userUID;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private FirebaseStorage storage;
    private StorageReference stRef;

    private FirebaseAuth mAuth;

    public DebtCustomerAdapter(Context mContext, List<String> debtCustomerList, String userUID) {
        this.mContext = mContext;
        this.debtCustomerList = debtCustomerList;
        this.userUID = userUID;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        storage = FirebaseStorage.getInstance();
        stRef = storage.getReference();

    }

    public class CardHolder extends RecyclerView.ViewHolder{

        ImageView imageView_cardView_debtCustomer_customerPP, imageView_cardView_debtCustomer_isPaid;
        TextView textView_cardView_debtCustomer_companyName, textView_cardView_debtCustomer_customerName, textView_cardView_debtCustomer_totalDebt
                , textView_cardView_debtCustomer_getPaid;
        CardView cardView_debtCustomer_customerClick;
        TextInputEditText textInputEditText_alertView_getPaid_paidQuantity;

        public CardHolder(@NonNull View itemView) {
            super(itemView);

            imageView_cardView_debtCustomer_customerPP = itemView.findViewById(R.id.imageView_cardView_debtCustomer_customerPP);
            imageView_cardView_debtCustomer_isPaid = itemView.findViewById(R.id.imageView_cardView_debtCustomer_isPaid);
            textView_cardView_debtCustomer_companyName = itemView.findViewById(R.id.textView_cardView_debtCustomer_companyName);
            textView_cardView_debtCustomer_customerName = itemView.findViewById(R.id.textView_cardView_debtCustomer_customerName);
            textView_cardView_debtCustomer_totalDebt = itemView.findViewById(R.id.textView_cardView_debtCustomer_totalDebt);
            cardView_debtCustomer_customerClick = itemView.findViewById(R.id.cardView_debtCustomer_customerClick);
            textView_cardView_debtCustomer_getPaid = itemView.findViewById(R.id.textView_cardView_debtCustomer_getPaid);

        }
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_debt_customer_design, parent, false);

        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardHolder holder, int position) {

        final String debtCustomerKey = debtCustomerList.get(position);

        setInfoDebtCustomer(holder, debtCustomerKey);

        // Musteriye satilan urunleri listeleme kismina gitme .
        holder.cardView_debtCustomer_customerClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SoldProductlistFragment soldProductlistFragment = new SoldProductlistFragment();
                StockUtils.gotoFragment(((AppCompatActivity)mContext), soldProductlistFragment, R.id.frameLayoutEntryActivity_holder, "debtCustomerKey", debtCustomerKey, 1);

            }
        });

        holder.textView_cardView_debtCustomer_getPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAlertViewForGetPaid(holder, debtCustomerKey);

            }
        });

    }

    @Override
    public int getItemCount() {
        return debtCustomerList.size();
    }


    /**
     * DB den customer bilgilerini getirip gorsel nesnelere yerlestirir .
     * setDebtCustomerPP metodunu cagirir .
     * @param holder
     * @param debtCustomerKey
     */
    public void setInfoDebtCustomer(final CardHolder holder, String debtCustomerKey){

        myRef.child("Customers").child(userUID).child(debtCustomerKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CustomerOrSupplier customer = snapshot.getValue(CustomerOrSupplier.class);

                holder.textView_cardView_debtCustomer_companyName.setText(customer.getCompanyName());
                holder.textView_cardView_debtCustomer_customerName.setText(customer.getName() + " " + customer.getSurname());
                holder.textView_cardView_debtCustomer_totalDebt.setText("Ödenmesi Gereken : " + customer.getTotalDebt() + " TL");

                String photoKey = customer.getPhoto();
                if (photoKey != "null"){
                    setDebtCustomerPP(holder, photoKey);
                }

                String totalDebt = customer.getTotalDebt();
                changeImageViewContent(holder, totalDebt);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /**
     * Customer pp sini storage dan alip gorsel nesneye yerlestirir .
     * @param holder
     * @param photoKey --> foto keyi --> storage da erismek icin .
     */
    public void setDebtCustomerPP(final CardHolder holder, String photoKey){

        stRef.child("CustomersPictures").child(userUID).child(photoKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).into(holder.imageView_cardView_debtCustomer_customerPP);

            }
        });

    }


    /**
     * Musterinin odemsini tamamlamasina gore imageView un ici degisir .
     * @param holder
     * @param totalDebt --> toplam musteri borcu .
     */
    public void changeImageViewContent(CardHolder holder, String totalDebt){

        Float debt = Float.valueOf(totalDebt);

        if (debt == 0){
            changeColorImageAndSetEnabledFalseText(holder);
        }

    }

    /**
     * AlertView olusturur .
     * getPaidFromCustomer metodunu cagirir . ---> odeme alir .
     */
    public void createAlertViewForGetPaid(final CardHolder holder, final String customerId){

        View desing = ((AppCompatActivity)mContext).getLayoutInflater().inflate(R.layout.alertview_get_paid_design, null);

        holder.textInputEditText_alertView_getPaid_paidQuantity = desing.findViewById(R.id.textInputEditText_alertView_getPaid_paidQuantity);

        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(mContext);

        alertDialogbuilder.setTitle("ÖDEME AL");
        alertDialogbuilder.setMessage("Lütfen alınacak miktarı geçmeyecek şekilde tutarı giriniz ." );
        alertDialogbuilder.setIcon(R.drawable.get_paid_icon);

        alertDialogbuilder.setView(desing);

        alertDialogbuilder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                getPaidFromCustomer(holder, customerId);

            }
        });

        alertDialogbuilder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });


        alertDialogbuilder.create().show();

    }


    /**
     * Musteriden odeme alir.
     * Customers DB sindeki totalDebt i duzenler.
     */
    public void getPaidFromCustomer(final CardHolder holder, final String customerId){

        final Float paidQuantity = Float.valueOf(holder.textInputEditText_alertView_getPaid_paidQuantity.getText().toString());

        myRef.child("Customers").child(userUID).child(customerId).child("totalDebt").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Float totalDebt = Float.valueOf(snapshot.getValue().toString());
                if (totalDebt >= paidQuantity) {
                    totalDebt -= paidQuantity;

                    myRef.child("Customers").child(userUID).child(customerId).child("totalDebt").setValue(String.valueOf(totalDebt));

                    // kasaya alinan parayi aktar ... *** devam edielecek

                    holder.textView_cardView_debtCustomer_totalDebt.setText(String.valueOf(totalDebt));

                    if (totalDebt == 0.0)
                        changeColorImageAndSetEnabledFalseText(holder);

                }
                else
                    Toast.makeText(((AppCompatActivity)mContext), "Alınacak miktardan büyük değer girilemez .", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void changeColorImageAndSetEnabledFalseText(CardHolder holder){

        holder.imageView_cardView_debtCustomer_isPaid.setImageDrawable(mContext.getResources().getDrawable(R.drawable.tik_icon));
        holder.textView_cardView_debtCustomer_getPaid.setTextColor(Color.rgb(13,192,0));
        holder.textView_cardView_debtCustomer_getPaid.setEnabled(false);

    }

}

