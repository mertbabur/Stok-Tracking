package com.example.stoktakip.Adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.Fragments.DetailCustomerFragment;
import com.example.stoktakip.Models.Customer;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.StockUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.CardHolder>{

    private Context mContex;
    private List<Customer> customerList;


    public CustomerListAdapter(Context mContex, List<Customer> customerList) {
        this.mContex = mContex;
        this.customerList = customerList;
    }

    public class CardHolder extends RecyclerView.ViewHolder{

        ImageView imageView_cardViewCustomer_customerPP, imageView_cardViewCustomer_phoneCall, imageView_cardViewCustomer_sendMessage
                  , imageView_cardViewCustomer_deleteCustomer;
        TextView textView_cardViewCustomer_customerName, textView_cardViewCustomer_companyName;
        CardView cardView_cardViewCustomer;

        public CardHolder(@NonNull View itemView) {
            super(itemView);

            imageView_cardViewCustomer_customerPP = itemView.findViewById(R.id.imageView_cardViewCustomer_customerPP);
            imageView_cardViewCustomer_phoneCall = itemView.findViewById(R.id.imageView_cardViewCustomer_phoneCall);
            imageView_cardViewCustomer_sendMessage = itemView.findViewById(R.id.imageView_cardViewCustomer_sendMessage);
            textView_cardViewCustomer_customerName = itemView.findViewById(R.id.textView_cardViewCustomer_customerName);
            cardView_cardViewCustomer = itemView.findViewById(R.id.cardView_cardViewCustomer);
            textView_cardViewCustomer_companyName = itemView.findViewById(R.id.textView_cardViewCustomer_companyName);
            imageView_cardViewCustomer_deleteCustomer = itemView.findViewById(R.id.imageView_cardViewCustomer_deleteCustomer);

        }
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_customer_design, parent, false);

        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

        Customer customer = customerList.get(position);

        setCustomerInfo(holder, customer);
        actionHolder(holder, customer);




    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }


    /**
     * Holder in action u tetiklenir .
     * @param holder
     * @param customer
     */
    public void actionHolder(final CardHolder holder, final Customer customer){

        // Customer ayrintilari sayfasini acar ...
        holder.cardView_cardViewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DetailCustomerFragment detailCustomerFragment = new DetailCustomerFragment();
                StockUtils.gotoFragment(mContex, detailCustomerFragment, R.id.frameLayoutEntryActivity_holder, "customerKey", customer.getCustomerKey(), 1);

            }
        });

        // arama yapma kismi ...
        holder.imageView_cardViewCustomer_phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone(customer.getCustomerNum());
            }
        });

        // mesaj gonderme kismi ...
        holder.imageView_cardViewCustomer_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // customer silme kismi ...
        holder.imageView_cardViewCustomer_deleteCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }


    /**
     * Gorsel nesnelere customer bilgilerini yerlestirir .
     * @param holder
     */
    public void setCustomerInfo(CardHolder holder, Customer customer){

        holder.textView_cardViewCustomer_companyName.setText(customer.getCompanyName());
        holder.textView_cardViewCustomer_customerName.setText(customer.getCustomerName() + " " + customer.getCustomerSurname());

        if(!customer.getCustomerPhoto().equals("null"))
            setCustomerPP(holder, customer.getCustomerPhoto());

    }


    /**
     * Fotokeyi ile customer pp sini storage dan bulup gerekli gorsel nesneye yerlestirir .
     * @param holder
     * @param photoKey
     */
    public void setCustomerPP(final CardHolder holder, String photoKey){

        String userUID = FirebaseAuth.getInstance().getUid();

        FirebaseStorage.getInstance().getReference().child("CustomersPictures").child(userUID).child(photoKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).into(holder.imageView_cardViewCustomer_customerPP);

            }
        });

    }


    /**
     * Customer i arar ...
     */
    public void callPhone(String phone){


        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));

        if (ActivityCompat.checkSelfPermission(mContex, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(((AppCompatActivity)mContex), new String[] {Manifest.permission.CALL_PHONE}, 101 );
        else
            mContex.startActivity(callIntent);

    }



}
