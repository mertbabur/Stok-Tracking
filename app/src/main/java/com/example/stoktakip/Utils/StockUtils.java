package com.example.stoktakip.Utils;

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
    public static void gotoFragment(FragmentActivity activity, Fragment whichFragment, int whichHolder){

        activity.getSupportFragmentManager().beginTransaction().replace(whichHolder, whichFragment).commit();

    }


}
