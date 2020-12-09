package com.example.stoktakip.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.stoktakip.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class StockUtils {

    /**
     * Cesitli sayfalara gecisleri saglar .
     * @param whichFragment --> hangi fragment a gidilecegini belirler .
     * @param activity --> getActivity .
     * @param whichHolder --> hangi frameLayout un tutacagini belirler .
     */
    public static void gotoFragment(FragmentActivity activity, Fragment whichFragment, int whichHolder, int val){

        if (val == 0) // backstack e atilmayacak ...
            activity.getSupportFragmentManager().beginTransaction().replace(whichHolder, whichFragment).commit();
        else // backstack e atilacak ...
            activity.getSupportFragmentManager().beginTransaction().replace(whichHolder, whichFragment).addToBackStack(null).commit();

    }


    public static void gotoFragment(Context mContext, Fragment whichFragment, int whichHolder, int val){

        if (val == 0) // backstack e atilmayacak ...
            ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction().replace(whichHolder, whichFragment).commit();
        else // backstack e atilacak ...
            ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction().replace(whichHolder, whichFragment).addToBackStack(null).commit();



    }


    public static void gotoFragment(FragmentActivity activity, Fragment whichFragment, int whichHolder, String bundleKey, String bundleString, int val){

        Bundle bundle = new Bundle();
        bundle.putString(bundleKey, bundleString);
        whichFragment.setArguments(bundle);

        if (val == 0) // backstack e atilmayacak ...
            activity.getSupportFragmentManager().beginTransaction().replace(whichHolder, whichFragment).commit();
        else // backstack e atilacak ...
            activity.getSupportFragmentManager().beginTransaction().replace(whichHolder, whichFragment).addToBackStack(null).commit();

    }


    public static void gotoFragment(FragmentActivity activity, Fragment whichFragment, int whichHolder, String bundleKey1, String bundleString1, String bundleKey2, String bundleString2, int val){

        Bundle bundle = new Bundle();
        bundle.putString(bundleKey1, bundleString1);
        bundle.putString(bundleKey2, bundleString2);
        whichFragment.setArguments(bundle);

        if (val == 0) // backstack e atilmayacak ...
            activity.getSupportFragmentManager().beginTransaction().replace(whichHolder, whichFragment).commit();
        else // backstack e atilacak ...
            activity.getSupportFragmentManager().beginTransaction().replace(whichHolder, whichFragment).addToBackStack(null).commit();

    }


    public static void gotoFragment(Context mContext, Fragment whichFragment, int whichHolder, String bundleKey1, String bundleString1, String bundleKey2, String bundleString2, int val){

        Bundle bundle = new Bundle();
        bundle.putString(bundleKey1, bundleString1);
        bundle.putString(bundleKey2, bundleString2);
        whichFragment.setArguments(bundle);

        if (val == 0) // backstack e atilmayacak ...
            ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction().replace(whichHolder, whichFragment).commit();
        else // backstack e atilacak ...
            ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction().replace(whichHolder, whichFragment).addToBackStack(null).commit();

    }


    /**
     * AlertView olusturur .
     * getPaidFromCustomer metodunu cagirir . ---> odeme alir .
     * @param activity
     * @param paidQuantity --> odeme miktari girme yeri .
     * @param getPaidClick --> odeme al tusu .
     * @param USER_UID
     * @param CUSTOMER_OR_SUPPLIER_KEY
     */
    public static void createAlertViewForGetPaid(final FragmentActivity activity, TextInputEditText paidQuantity, final TextView getPaidClick, final TextView kalanBorcText, final String USER_UID, final String CUSTOMER_OR_SUPPLIER_KEY){

        View desing = activity.getLayoutInflater().inflate(R.layout.alertview_get_paid_design, null);
        paidQuantity = desing.findViewById(R.id.textInputEditText_alertView_getPaid_paidQuantity);

        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(activity);

        alertDialogbuilder.setTitle("ÖDEME AL");
        alertDialogbuilder.setMessage("Lütfen alınacak miktarı geçmeyecek şekilde tutarı giriniz ." );
        alertDialogbuilder.setIcon(R.drawable.get_paid_icon);

        alertDialogbuilder.setView(desing);

        final TextInputEditText finalPaidQuantity = paidQuantity;
        alertDialogbuilder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FirebaseUtils.getPaidFromCustomer(activity, finalPaidQuantity, getPaidClick, kalanBorcText, USER_UID, CUSTOMER_OR_SUPPLIER_KEY );

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
     * totalDebt 0 ise yazi rengi yesil olur ve odeme al butonu not enabled olur .
     * @param getPaidClick --> odeme al tusu .
     */
    public static void controlTotalDebt(TextView getPaidClick){

        getPaidClick.setTextColor(Color.rgb(13,192,0));
        getPaidClick.setEnabled(false);

    }


}
