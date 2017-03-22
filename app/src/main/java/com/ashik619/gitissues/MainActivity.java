package com.ashik619.gitissues;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ashik619.gitissues.fragments.HomeFragment;


public class MainActivity extends AppCompatActivity {

    HomeFragment homeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inflateHomeFragment();
    }
    void inflateHomeFragment(){
        homeFragment = new HomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction homeTransaction = manager.beginTransaction();
        homeTransaction.replace(R.id.container, homeFragment);
        homeTransaction.commit();
    }



}
