package com.example.maddi.telerestaurant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.HashMap;

/**
 * Created by maddi on 3/21/2016.
 */
public class RecyclerFrag_Main extends AppCompatActivity {
    //private boolean mSidePanel;
    private Toolbar mToolbar;
    private android.support.v7.app.ActionBar mActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_frag_change_main);
        //Load common fragment
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rcfrag_main,RecyclerView_Main.newInstance()).commit();
        }
        mToolbar = (Toolbar) findViewById(R.id.recycler_toolbar);
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);


    }
}
