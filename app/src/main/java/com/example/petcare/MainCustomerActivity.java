package com.example.petcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainCustomerActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_customer);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNav);


        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new homeFragment()).commit();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener onNav = item -> {

        Fragment selected = null;

        switch(item.getItemId()) {
            case R.id.home_bottom:
                selected = new homeFragment();
                break;
            case R.id.profile_bottom:
                selected = new ProfileUserFragment();
                break;
            case R.id.calendar_bottom:
                selected = new CustomerCalendar();
                break;
            case R.id.fav_botoom:
                selected = new PetFragment();
                break;
        }
         getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,selected).commit();
        return true;
    };
}