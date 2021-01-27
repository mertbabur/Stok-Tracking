package com.example.stoktakip.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;

import com.example.stoktakip.Fragments.AppEntryFragment;
import com.example.stoktakip.Fragments.LoginFragment;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.StockUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mainActivity_fragmentHolder;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity_fragmentHolder = findViewById(R.id.mainActivity_fragmentHolder);

        appEntry();

        if (isLoginUser()){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, EntryActivity.class));
                    finish();
                }
            }, 1000);
        }
        else {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LoginFragment loginFragment = new LoginFragment();
                    StockUtils.gotoFragment(MainActivity.this, loginFragment, R.id.mainActivity_fragmentHolder, "whichButton", "noButton", 0);

                }
            }, 1000);

        }

    }


    /**
     * Kullanici daha once giris yapti mi ?
      * @return --> Yapti ise (true) , yapmadi ise (false) .
     */
    public boolean isLoginUser(){

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null)
            return true;

        return false;

    }

    /**
     * Giriş ekranını .
     */
    public void appEntry(){

        AppEntryFragment appEntryFragment = new AppEntryFragment();
        StockUtils.gotoFragment(MainActivity.this, appEntryFragment, R.id.mainActivity_fragmentHolder,0);

    }

}