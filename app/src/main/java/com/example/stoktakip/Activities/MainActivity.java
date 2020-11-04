package com.example.stoktakip.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.stoktakip.Fragments.LoginFragment;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.StockUtils;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mainActivity_fragmentHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity_fragmentHolder = findViewById(R.id.mainActivity_fragmentHolder);
        LoginFragment loginFragment = new LoginFragment();
        StockUtils.gotoFragment(MainActivity.this, loginFragment, R.id.mainActivity_fragmentHolder, "whichButton", "noButton");

    }
}