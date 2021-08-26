package com.augurit.agmobile.patrolcore.search.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.augurit.agmobile.patrolcore.R;
import com.augurit.am.cmpt.permission.PermissionsUtil2;


public class SearchActivity extends AppCompatActivity {

    private SearchFragmentWithoutMap fragmentWithoutMap;
    private ViewGroup container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        container = (ViewGroup) findViewById(R.id.frament_container);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        fragmentWithoutMap = SearchFragmentWithoutMap.newInstance("",true);
        transaction.add(R.id.frament_container,fragmentWithoutMap);
        transaction.commit();
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsUtil2.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/
}
