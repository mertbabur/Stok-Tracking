package com.example.stoktakip.Utils;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.stoktakip.R;

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


}
