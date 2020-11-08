package com.example.stoktakip.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stoktakip.Models.CustomerOrSupplier;
import com.example.stoktakip.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SupplierListAdapter extends RecyclerView.Adapter<SupplierListAdapter.CardHolder>{

    private Context mContext;
    private List<CustomerOrSupplier> supplierList;

    public SupplierListAdapter(Context mContext, List<CustomerOrSupplier> supplierList) {
        this.mContext = mContext;
        this.supplierList = supplierList;
    }

    public class CardHolder extends RecyclerView.ViewHolder{

        ImageView imageView_cardView_supplierList_supplierPP;
        TextView textView_cardView_supplierList_nameSurname, textView_cardView_supplierList_companyName;
        CardView cardView_supplierList;

        public CardHolder(@NonNull View itemView) {
            super(itemView);

            textView_cardView_supplierList_companyName = itemView.findViewById(R.id.textView_cardView_supplierList_companyName);
            textView_cardView_supplierList_nameSurname = itemView.findViewById(R.id.textView_cardView_supplierList_nameSurname);
            imageView_cardView_supplierList_supplierPP = itemView.findViewById(R.id.imageView_cardView_supplierList_supplierPP);
            cardView_supplierList = itemView.findViewById(R.id.cardView_supplierList);

        }
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_supplier_list_design, parent, false);

        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

        CustomerOrSupplier supplier = supplierList.get(position);
        Log.e("asssdf", "girdi "+supplier.getName());
        setInfoSupplier(holder, supplier);

    }

    @Override
    public int getItemCount() {
        return supplierList.size();
    }


    public void setInfoSupplier(final CardHolder holder, CustomerOrSupplier supplier){

        holder.textView_cardView_supplierList_nameSurname.setText(supplier.getName() + " " + supplier.getSurname());
        holder.textView_cardView_supplierList_companyName.setText(supplier.getCompanyName());

        String userUID = FirebaseAuth.getInstance().getUid();

        String photoKey = supplier.getPhoto();
        FirebaseStorage.getInstance().getReference().child("SuppliersPictures").child(userUID).child(photoKey).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.imageView_cardView_supplierList_supplierPP);
            }
        });

    }


}
