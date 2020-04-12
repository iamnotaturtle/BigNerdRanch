package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public abstract class SingleFragmentActivity extends FragmentActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inflate the activity's view
        setContentView(R.layout.activity_fragment);

        //Get the fragment manager
        FragmentManager fm = getSupportFragmentManager();

        //Add a crime fragment
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        //Create a new fragment transaction, add and commit it
        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

    }

}
