package com.example.stoktakip.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.stoktakip.R;
import com.example.stoktakip.Utils.FirebaseUtils;
import com.example.stoktakip.Utils.StockUtils;
import com.squareup.picasso.Picasso;

public class AddCustomerFragment extends Fragment {

    private ImageView imageView_fragmentAddCustomer_customerPP, imageView_fragmentAddCustomer_back;
    private TextView textView_fragmentAddCustomer_addCustomerPP;
    private EditText editText_fragmentAddCustomer__customerName, editText_fragmentAddCustomer_customerSurname
                     , editTextText_fragmentAddCustomer_companyName, editTextText_fragmentAddCustomer_customerNum
                     , editText_fragmentAddCustomer_customerAddress;
    private Button button_fragmentAddCustomer_save;

    private Uri getPhotoFromGalleryURI;

    private String WHICH_FRAGMENT;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_customer_design,  container, false);

        defineAttributes(rootView);
        actionAttributes();
        return rootView;

    }


    /**
     * Gorsel nesneler tanimlandi ve baglandi .
     */
    public void defineAttributes(View rootView){

        imageView_fragmentAddCustomer_customerPP = rootView.findViewById(R.id.imageView_fragmentAddCustomer_customerPP);
        textView_fragmentAddCustomer_addCustomerPP = rootView.findViewById(R.id.textView_fragmentAddCustomer_addCustomerPP);
        editText_fragmentAddCustomer__customerName = rootView.findViewById(R.id.editText_fragmentAddCustomer__customerName);
        editText_fragmentAddCustomer_customerSurname = rootView.findViewById(R.id.editText_fragmentAddCustomer_customerSurname);
        editTextText_fragmentAddCustomer_companyName = rootView.findViewById(R.id.editTextText_fragmentAddCustomer_companyName);
        editTextText_fragmentAddCustomer_customerNum = rootView.findViewById(R.id.editTextText_fragmentAddCustomer_customerNum);
        editText_fragmentAddCustomer_customerAddress = rootView.findViewById(R.id.editText_fragmentAddCustomer_customerAddress);
        button_fragmentAddCustomer_save = rootView.findViewById(R.id.button_fragmentAddCustomer_save);
        imageView_fragmentAddCustomer_back = rootView.findViewById(R.id.imageView_fragmentAddCustomer_back);

        WHICH_FRAGMENT = getArguments().getString("whichFragment", "bos fragment");

    }



    /**
     * Gorsel nesnelerin action lari tetiklenir .
     */
    public void actionAttributes(){

        // resim secme kismi ...
        textView_fragmentAddCustomer_addCustomerPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);

            }
        });

        // kaydet kismi ...
        button_fragmentAddCustomer_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFilled())
                        saveDB();
                else
                    Toast.makeText(getActivity(), "Lütfen bilgileri eksiksiz bir şekilde doldurunuz .", Toast.LENGTH_SHORT).show();

            }
        });

        // Customer Fragment a geri donme kismi ...
        imageView_fragmentAddCustomer_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomersFragment customersFragment = new CustomersFragment();
                if (WHICH_FRAGMENT.equals("customerFragment"))
                    StockUtils.gotoFragment(getActivity(), customersFragment, R.id.frameLayoutEntryActivity_holder, "whichFragment", "customerFragment", 1);
                else
                    StockUtils.gotoFragment(getActivity(), customersFragment, R.id.frameLayoutEntryActivity_holder, "whichFragment", "supplierFragment", 1);

            }
        });

    }


    /**
     * Galeriden seçilen fotoyu yakalar .
     * Sadece galeriden seçim olacagi icin request koda gore herhangi bir yakalama yapmadik .
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        getPhotoFromGalleryURI = data.getData();

        if(getPhotoFromGalleryURI != null){
            Toast.makeText(getActivity(), "Fotoğraf seçildi .", Toast.LENGTH_SHORT).show();
            Picasso.get().load(getPhotoFromGalleryURI).into(imageView_fragmentAddCustomer_customerPP);
        }


    }


    /**
     * Gorsel nesnelerden bilgileri alir  ve DB ye kaydeder .
     * Secilen photo null degilse icerideki metod fotoyu da storage a kaydeder .
     */
    public void saveDB(){

        String name = editText_fragmentAddCustomer__customerName.getText().toString().trim();
        String surname = editText_fragmentAddCustomer_customerSurname.getText().toString().trim();
        String companyName = editTextText_fragmentAddCustomer_companyName.getText().toString().trim();
        String num = editTextText_fragmentAddCustomer_customerNum.getText().toString().trim();
        String address = editText_fragmentAddCustomer_customerAddress.getText().toString().trim();

        if (WHICH_FRAGMENT.equals("customerFragment"))
            FirebaseUtils.addCustomerToDB(name, surname, companyName, num, address, getPhotoFromGalleryURI);
        else
            FirebaseUtils.addSupplierToDB(name, surname, companyName, num, address, getPhotoFromGalleryURI);


        editText_fragmentAddCustomer__customerName.setText("");
        editText_fragmentAddCustomer_customerSurname.setText("");
        editTextText_fragmentAddCustomer_companyName.setText("");
        editTextText_fragmentAddCustomer_customerNum.setText("");
        editText_fragmentAddCustomer_customerAddress.setText("");

        getPhotoFromGalleryURI = null;

        imageView_fragmentAddCustomer_customerPP.setImageDrawable(getResources().getDrawable(R.drawable.default_pp_icon));

    }


    /**
     * Gerekli bilgiler eksiksiz bir sekilde dolduruldu mu .
     * @return --> Eger dolu ise (true) , bos ise (false)
     */
    public boolean isFilled(){

        String name = editText_fragmentAddCustomer__customerName.getText().toString().trim();
        String surname = editText_fragmentAddCustomer_customerSurname.getText().toString().trim();
        String companyName = editTextText_fragmentAddCustomer_companyName.getText().toString().trim();
        String num = editTextText_fragmentAddCustomer_customerNum.getText().toString().trim();
        String address = editText_fragmentAddCustomer_customerAddress.getText().toString().trim();

        if (!name.equals("") && !surname.equals("") && !companyName.equals("") && !num.equals("") && !address.equals(""))
            return true;

        return false;

    }



}
