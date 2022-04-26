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

public class LoginActivity extends AppCompatActivity {

    TextView loginholder,registerholder,forgotpass;
    EditText emailholder,passholder;
    CheckBox checkpass;

    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    ProgressBar pbholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginholder = findViewById(R.id.tv_login_l);
        registerholder = findViewById(R.id.tv_register_l);
        emailholder = findViewById(R.id.et_email_login);
        passholder = findViewById(R.id.et_pass_login);
        pbholder = findViewById(R.id.pv_login);
        checkpass = findViewById(R.id.cb_pass);
        forgotpass = findViewById(R.id.tv_forgot);

        forgotpass.setOnClickListener(v -> {
            String email = emailholder.getText().toString();
            if(!TextUtils.isEmpty(email)){
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Email sent!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(LoginActivity.this, "Input Email to forgot password verify", Toast.LENGTH_SHORT).show();
            }
        });


        loginholder.setOnClickListener(view -> loginuser());

        registerholder.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        });

        checkpass.setOnClickListener(v -> {
            if(checkpass.isChecked()){
                passholder.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }else{
                passholder.setInputType(129);
            }
        });


    }

    private void loginuser() {
        String email = emailholder.getText().toString();
        String password = passholder.getText().toString();

        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
            pbholder.setVisibility(View.VISIBLE);
            enabledElement(false);

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    if(mAuth.getCurrentUser().isEmailVerified()){
                        Intent intent = new Intent(LoginActivity.this, OptionSignUpActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        pbholder.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Please verify your email address", Toast.LENGTH_SHORT).show();
                        enabledElement(true);
                    }
                }else{
                    pbholder.setVisibility(View.GONE);
                    String error = Objects.requireNonNull(task.getException()).getMessage();
                    Toast.makeText(LoginActivity.this, "Error:"+error, Toast.LENGTH_SHORT).show();
                    enabledElement(true);
                }
            });
        }else Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
    }

    private void enabledElement(Boolean x){
        emailholder.setEnabled(x);
        passholder.setEnabled(x);
        registerholder.setEnabled(x);
        loginholder.setEnabled(x);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}