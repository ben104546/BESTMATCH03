package com.example.a5dayappchat2.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.a5dayappchat2.Activity.ActivityDetailAndSetting.MainActivityDetailFragment;
import com.example.a5dayappchat2.Activity.ActivityDetailAndSetting.MemberActivityFragment;
import com.example.a5dayappchat2.Activity.ActivityDetailAndSetting.PlanActivityFragment;
import com.example.a5dayappchat2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ActivityDeailAndSetting extends AppCompatActivity {

    String DoingToken;
    String GroupToken;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deail_and_setting);

        GroupToken = getIntent().getStringExtra("GroupKey");
        DoingToken = getIntent().getStringExtra("DoingKey");


       replaceFragment(new MainActivityDetailFragment());

        /*Bundle bundle = new Bundle();
        bundle.putString("GroupKey", GroupToken);
        bundle.putString("DoingKey", DoingToken);

        MainActivityDetailFragment mainActivityDetailFragment = new MainActivityDetailFragment();
        mainActivityDetailFragment.setArguments(bundle);*/

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.DeActivity:
                        replaceFragment(new MainActivityDetailFragment());
                        break;
                    case R.id.MeActivity:
                        replaceFragment(new MemberActivityFragment());
                        break;

                    case R.id.PlActivity:
                        replaceFragment(new PlanActivityFragment());
                        break;

                }
                return true;
            }
        });

        getDataDoingToken();
        getDataGroupToken();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    public  String getDataGroupToken(){
            return GroupToken ;
    }

    public  String getDataDoingToken(){
        return DoingToken ;
    }
}