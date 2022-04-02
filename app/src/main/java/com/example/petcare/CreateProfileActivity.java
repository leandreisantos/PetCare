package com.example.petcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CreateProfileActivity extends AppCompatActivity {

    CardView cvowner,cvcustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        cvowner = findViewById(R.id.cv1);
        cvcustomer = findViewById(R.id.cv2);

        cvowner.setOnClickListener(v -> showShopOptions());
        cvcustomer.setOnClickListener(v -> {
            Intent intent = new Intent(CreateProfileActivity.this,CustomerCreateProfile.class);
            startActivity(intent);
        });
    }

    private void showShopOptions() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.shopoption_layout,null);
        CardView groom = view.findViewById(R.id.cv1);
        CardView clinic = view.findViewById(R.id.cv2);
        TextView close = view.findViewById(R.id.close_tv);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        groom.setOnClickListener(v -> {
            gotoOwnerProfile("groom");
            alertDialog.dismiss();
        });
        clinic.setOnClickListener(v -> {
            gotoOwnerProfile("clinic");
            alertDialog.dismiss();
        });

        close.setOnClickListener(v -> alertDialog.dismiss());
    }

    private void gotoOwnerProfile(String groom) {
        Intent intent = new Intent(CreateProfileActivity.this,OwnerCreateProfile.class);
        intent.putExtra("status",groom);
        startActivity(intent);
    }


}