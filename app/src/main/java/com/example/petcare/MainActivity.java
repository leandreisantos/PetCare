package com.example.petcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(user != null&&user.isEmailVerified()){
            DocumentReference reference1;
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            reference1 = firestore.collection("user").document(currentuid);
            reference1.get()
                    .addOnCompleteListener(task -> {
                        if(!task.getResult().exists()){
                            Intent intent = new Intent(MainActivity.this, OptionSignUpActivity.class);
                            startActivity(intent);
                        }else{
                            showMain();
                        }
                    });
        }else{
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }

    }

    private void showMain() {
        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        reference = firestore.collection("user").document(currentuid);

        reference.get()
                .addOnCompleteListener(task -> {
                    if(task.getResult().exists()){

                        String statusResult = task.getResult().getString("status");

                        if(statusResult.equals("Bussiness")){
                            Intent intent = new Intent(MainActivity.this,Main_Business_Activity.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(MainActivity.this,MainCustomerActivity.class);
                            startActivity(intent);
                        }
                    }else{
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}