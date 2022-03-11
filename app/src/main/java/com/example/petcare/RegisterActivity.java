package com.example.petcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    TextView backholder,submit;
    EditText emailholder,passholder,confpassholder;
    ProgressBar pbholder;
    CheckBox cb;

    //firebase
    FirebaseAuth mAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        backholder = findViewById(R.id.tv_back_r);
        emailholder = findViewById(R.id.et_email_r);
        passholder = findViewById(R.id.et_pass_r);
        confpassholder = findViewById(R.id.et_confpass_r);
        submit = findViewById(R.id.tv_register_r);
        pbholder = findViewById(R.id.pv_login);
        cb = findViewById(R.id.cb_pass);

        backholder.setOnClickListener(view -> onBackPressed());

        submit.setOnClickListener(view -> registeruser());


        if(cb.isChecked()){
            confpassholder.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passholder.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }else{
            confpassholder.setInputType(129);
            passholder.setInputType(129);
        }


    }

    private void registeruser() {
        String email = emailholder.getText().toString();
        String pass = passholder.getText().toString();
        String conf = confpassholder.getText().toString();

        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(pass)||TextUtils.isEmpty(conf)){
            Toast.makeText(this, "Please fill up all requirements!", Toast.LENGTH_SHORT).show();
        }else{
            if(pass.equals(conf)){
                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(RegisterActivity.this,"Successfully created your account enjoy!", Toast.LENGTH_SHORT).show();
                        pbholder.setVisibility(View.INVISIBLE);
                    }else{
                        process(false);
                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(RegisterActivity.this, "Error:"+error, Toast.LENGTH_SHORT).show();
                        pbholder.setVisibility(View.INVISIBLE);
                    }
                });
            }else{
                Toast.makeText(this, "Password and Confirm Password not match!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void process(boolean b) {
        if(b) {
            pbholder.setVisibility(View.VISIBLE);
            emailholder.setEnabled(false);
            passholder.setEnabled(false);
            confpassholder.setEnabled(false);
            backholder.setEnabled(false);
            submit.setEnabled(false);
        }else{
            pbholder.setVisibility(View.GONE);
            emailholder.setEnabled(true);
            passholder.setEnabled(true);
            confpassholder.setEnabled(true);
            backholder.setEnabled(true);
            submit.setEnabled(true);
        }
    }
}