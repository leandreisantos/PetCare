package com.example.petcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class OptionSignUpActivity extends AppCompatActivity {

    CardView owner,bussiness;
    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_sign_up);

        owner = findViewById(R.id.et_email_login);

        owner.setOnClickListener(v -> showdialogowner());

        back.setOnClickListener(v -> onBackPressed());
    }

    private void showdialogowner() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.customer_signin_dialog,null);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

    }
}