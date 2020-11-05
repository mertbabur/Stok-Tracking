package com.example.stoktakip.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.stoktakip.Fragments.MainMenuFragment;
import com.example.stoktakip.R;
import com.example.stoktakip.Utils.StockUtils;

public class EntryActivity extends AppCompatActivity {

    private FrameLayout frameLayoutEntryActivity_holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        frameLayoutEntryActivity_holder = findViewById(R.id.frameLayoutEntryActivity_holder);

        MainMenuFragment mainMenuFragment = new MainMenuFragment();
        StockUtils.gotoFragment(EntryActivity.this, mainMenuFragment, R.id.frameLayoutEntryActivity_holder);

    }
}