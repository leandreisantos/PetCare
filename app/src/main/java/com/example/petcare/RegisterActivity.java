package com.example.petcare;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked()){
                    confpassholder.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passholder.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    confpassholder.setInputType(129);
                    passholder.setInputType(129);
                }
            }
        });
    }

    private void registeruser() {
        String email = emailholder.getText().toString();
        String pass = passholder.getText().toString();
        String conf = confpassholder.getText().toString();

        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(pass)||TextUtils.isEmpty(conf)){
            Toast.makeText(this, "Please fill up all requirements!", Toast.LENGTH_SHORT).show();
        }else{
            if(pass.equals(conf)){
                pbholder.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this,"Registered successfully.Please check your email for verification", Toast.LENGTH_SHORT).show();
                                    pbholder.setVisibility(View.INVISIBLE);
                                    emailholder.setText("");
                                    passholder.setText("");
                                    confpassholder.setText("");
                                }else{
                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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