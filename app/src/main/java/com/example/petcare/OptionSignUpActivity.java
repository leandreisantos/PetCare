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
        bussiness = findViewById(R.id.et_pass_login);
        back = findViewById(R.id.tv_back);

        owner.setOnClickListener(v -> showdialogowner());
        bussiness.setOnClickListener(v -> showdialogBuss());

        back.setOnClickListener(v -> onBackPressed());
    }

    private void showdialogBuss() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.owner_signin_dialog1,null);

        CardView next = view.findViewById(R.id.cv3);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog2();
                alertDialog.dismiss();
            }
        });

    }

    private void showdialog2() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.owner_signin_dialog2,null);

        CardView next = view.findViewById(R.id.cv3);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog3();
                alertDialog.dismiss();
            }
        });
    }

    private void showdialog3() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.owner_signin_dialog3,null);

        CardView next = view.findViewById(R.id.cv3);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog4();
                alertDialog.dismiss();
            }
        });
    }

    private void showdialog4() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.owner_signin_dialog4,null);

        CardView next = view.findViewById(R.id.cv3);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog4();
                alertDialog.dismiss();
            }
        });
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