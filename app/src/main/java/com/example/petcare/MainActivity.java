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

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new homeFragment()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener onNav = item -> {
        Fragment selected = null;
        switch(item.getItemId()){
            case R.id.home_bottom:
                selected = new homeFragment();
                break;
            case R.id.profile_bottom:
                selected = new ProfileFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,selected).commit();
        return true;
    };

    @Override
    protected void onStart() {
        super.onStart();

        DocumentReference reference1;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        reference1 = firestore.collection("user").document(currentuid);
        reference1.get()
                .addOnCompleteListener(task -> {
                    if(!task.getResult().exists()){
                        Intent intent = new Intent(MainActivity.this, CreateProfileActivity.class);
                        startActivity(intent);
                    }
                });
    }
}