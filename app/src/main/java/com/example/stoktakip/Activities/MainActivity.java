package com.example.stoktakip.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.stoktakip.Fragments.LoginFragment;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.StockUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mainActivity_fragmentHolder;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isLoginUser()){
            startActivity(new Intent(MainActivity.this, EntryActivity.class));
            finish();
        }
        else {
            mainActivity_fragmentHolder = findViewById(R.id.mainActivity_fragmentHolder);
            LoginFragment loginFragment = new LoginFragment();
            StockUtils.gotoFragment(MainActivity.this, loginFragment, R.id.mainActivity_fragmentHolder, "whichButton", "noButton", 0);
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



}