package com.example.petcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNav);

        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        reference = firestore.collection("user").document(currentuid);

        reference.get()
                .addOnCompleteListener(task -> {
                    if(task.getResult().exists()){

                        String statusResult = task.getResult().getString("status");

                        if(statusResult.equals("Bussiness")){
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new BusinessHomeFragment()).commit();
                        }

                    }else{
                        Intent intent = new Intent(MainActivity.this,CreateProfileActivity.class);
                        startActivity(intent);
                    }
                });


//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new homeFragment()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener onNav = item -> {

        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        reference = firestore.collection("user").document(currentuid);

        reference.get()
                .addOnCompleteListener(task -> {
                    if(task.getResult().exists()){

                        String statusResult = task.getResult().getString("status");
                        Fragment selected = null;

                        if(statusResult.equals("Bussiness")){
                            switch(item.getItemId()){
                                case R.id.home_bottom:
                                    selected = new BusinessHomeFragment();
                                    break;
                                case R.id.profile_bottom:
                                    selected = new ProfileFragment();
                                    break;
                                case R.id.calendar_bottom:
                                    selected = new BusinessCalendarFragment();
                                    break;
                            }
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,selected).commit();
                        }

                    }else{
                        Intent intent = new Intent(MainActivity.this,CreateProfileActivity.class);
                        startActivity(intent);
                    }
                });
        return true;
    };

    @Override
    protected void onStart() {
        super.onStart();

//        DocumentReference reference1;
//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//
//        reference1 = firestore.collection("user").document(currentuid);
//        reference1.get()
//                .addOnCompleteListener(task -> {
//                    if(!task.getResult().exists()){
//                        Intent intent = new Intent(MainActivity.this, CreateProfileActivity.class);
//                        startActivity(intent);
//                    }
//                });
    }
}