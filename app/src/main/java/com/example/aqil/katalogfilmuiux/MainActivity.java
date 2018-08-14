package com.example.aqil.katalogfilmuiux;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    FragmentManager manager = getSupportFragmentManager();
    @BindView(R.id.navigation)
    BottomNavigationView navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Fragment currentFragment = manager.findFragmentById(R.id.main_container);
        if(currentFragment==null){
            navigation.setSelectedItemId(R.id.now_playing);
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment currentFragment;
            FragmentTransaction transaction = manager.beginTransaction();
            Log.d("TAG", "onNavigationItemSelected: " + item.getItemId());
            Log.d("TAG", "onNavigationItemSelected:2 " + item.getItemId());
            switch (item.getItemId()) {
                case R.id.now_playing:
                    currentFragment = manager.findFragmentByTag("FRAGMENT_NOW_PLAYING");
                    if (currentFragment != null) {
                        transaction.replace(R.id.main_container, currentFragment);
                    } else {
                        transaction.replace(R.id.main_container, new NowPlayingFragment(), "FRAGMENT_NOW_PLAYING");
                    }
                    break;


                case R.id.up_coming:
                    currentFragment = manager.findFragmentByTag("FRAGMENT_UP_COMING");
                    if (currentFragment != null) {
                        transaction.replace(R.id.main_container, currentFragment);
                    } else {
                        transaction.replace(R.id.main_container, new UpcomingFragment(), "FRAGMENT_UP_COMING");
                    }

                    break;
                case R.id.favorite:
                    currentFragment = manager.findFragmentByTag("FRAGMENT_FAVORITE");
                    if (currentFragment != null) {
                        transaction.replace(R.id.main_container, currentFragment);
                    } else {
                        transaction.replace(R.id.main_container, new FavoritesFragment(), "FRAGMENT_FAVORITE");
                    }
                    break;
                case R.id.search:
                    currentFragment = manager.findFragmentByTag("FRAGMENT_SEARCH");
                    if (currentFragment != null) {
                        transaction.replace(R.id.main_container, currentFragment);
                    } else {
                        transaction.replace(R.id.main_container, new SearchFragment(), "FRAGMENT_SEARCH");
                    }
                    break;

                case R.id.setting:
                    Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                    startActivityForResult(intent,0);
                    break;
            }
            transaction.commit();
            transaction.addToBackStack(null);
            return true;
        }
    };


    @Override
    public void onBackPressed() {

        Fragment currentFragment = manager.findFragmentById(R.id.main_container);
        if (currentFragment.getTag() != null) {
            if (!currentFragment.getTag().equals("FRAGMENT_NOW_PLAYING")) {
                navigation.setSelectedItemId(R.id.now_playing);
            } else {
                manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                super.onBackPressed();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        navigation.setSelectedItemId(R.id.now_playing);
    }
}