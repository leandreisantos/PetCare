package com.example.petcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class OptionSignUpActivity extends AppCompatActivity {

    CardView owner,bussiness;
    TextView back;

    String bussnameholder,emailholder,passholder,confpassholder; // dialog 1
    String statusposition; // dialog 2
    String mobileholder,landlineholder,webholder; //dialog 3
    String branch; // dialog4
    int sunclick = 0;
    int monclick = 0;
    int tueclick = 0;
    int wedclick = 0;
    int thuclick = 0;
    int friclick = 0;
    int satclick = 0;
    int hour,minute;
    String houropen,hourclose;

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

        EditText namebuss = view.findViewById(R.id.et_name);
        EditText email = view.findViewById(R.id.et_email);
        EditText pass = view.findViewById(R.id.et_pass);
        EditText confpass = view.findViewById(R.id.et_confpass);

        CardView next = view.findViewById(R.id.cv3);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        next.setOnClickListener(v -> {
            String tempname = namebuss.getText().toString();
            String tempemail = email.getText().toString();
            String temppass = pass.getText().toString();
            String tempconfpass = confpass.getText().toString();

            if(!TextUtils.isEmpty(tempname)&&!TextUtils.isEmpty(tempemail)&&!TextUtils.isEmpty(temppass)&&!TextUtils.isEmpty(tempconfpass)){
                if(temppass.equals(tempconfpass)){
                    bussnameholder  = tempname;
                    emailholder = tempemail;
                    passholder = temppass;
                    confpassholder = tempconfpass;
                    showdialog2();
                    alertDialog.dismiss();
                }else{
                    Toast.makeText(OptionSignUpActivity.this, "Password not match", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(OptionSignUpActivity.this, "Please fill up all requirements", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void showdialog2() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.owner_signin_dialog2,null);

        CardView next = view.findViewById(R.id.cv3);
        RadioButton vet = view.findViewById(R.id.vet);
        RadioButton groom = view.findViewById(R.id.groom);
        RadioButton both = view.findViewById(R.id.both);


        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        next.setOnClickListener(v -> {

            if(vet.isChecked()){
                statusposition = "vet";
                showdialog3();
                alertDialog.dismiss();
            }
            if(groom.isChecked()){
                statusposition = "groom";
                showdialog3();
                alertDialog.dismiss();
            }
            if(both.isChecked()){
                statusposition = "both";
                showdialog3();
                alertDialog.dismiss();
            }else{
                Toast.makeText(OptionSignUpActivity.this, "Please select Business", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showdialog3() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.owner_signin_dialog3,null);

        CardView next = view.findViewById(R.id.cv3);
        EditText mobile = view.findViewById(R.id.et_mnumber);
        EditText landline = view.findViewById(R.id.et_landline);
        EditText web = view.findViewById(R.id.et_web);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempmobile = mobile.getText().toString();
                String templandline = landline.getText().toString();
                String tempweb = web.getText().toString();

                if(!TextUtils.isEmpty(tempmobile)&&!TextUtils.isEmpty(templandline)&&!TextUtils.isEmpty(tempweb)){
                    mobileholder = tempmobile;
                    landlineholder = templandline;
                    webholder = tempweb;

                    showdialog4();
                    alertDialog.dismiss();
                }else{
                    Toast.makeText(OptionSignUpActivity.this, "Please fill up all requirements", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void showdialog4() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.owner_signin_dialog4,null);

        CardView next = view.findViewById(R.id.cv3);
        EditText b1 = view.findViewById(R.id.et_b1);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempb1 = b1.getText().toString();

                if(!TextUtils.isEmpty(tempb1)){
                    branch = tempb1;
                    showdialog5();
                    alertDialog.dismiss();
                }else{
                    Toast.makeText(OptionSignUpActivity.this, "Please fill up branch", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showdialog5() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.owner_signin_dialog5,null);

        boolean ischeck = false;
        String sunholder,monholder,tuesholder,wedholder,thuholder,friholder,satholder;
        String openholder,closeholder;

        CardView next = view.findViewById(R.id.cv3);
        CardView sun = view.findViewById(R.id.sun);
        CardView mon = view.findViewById(R.id.mon);
        CardView tues = view.findViewById(R.id.tues);
        CardView wed = view.findViewById(R.id.wed);
        CardView thu = view.findViewById(R.id.thu);
        CardView fri = view.findViewById(R.id.fri);
        CardView sat = view.findViewById(R.id.sat);
        TextView bname = view.findViewById(R.id.bname);
        TextView open = view.findViewById(R.id.et_open);
        TextView close = view.findViewById(R.id.et_close);


        bname.setText(branch);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        sun.setOnClickListener(v -> {
            if(sunclick == 0){
                sun.setCardBackgroundColor(ContextCompat.getColor(OptionSignUpActivity.this, R.color.green2));
                sunclick = 1;
            }else{
                sun.setCardBackgroundColor(ContextCompat.getColor(OptionSignUpActivity.this, R.color.white));
                sunclick = 0;
            }
        });
        mon.setOnClickListener(v -> {
            if(monclick == 0){
                mon.setCardBackgroundColor(ContextCompat.getColor(OptionSignUpActivity.this, R.color.green2));
                monclick = 1;
            }else{
                mon.setCardBackgroundColor(ContextCompat.getColor(OptionSignUpActivity.this, R.color.white));
                monclick = 0;
            }
        });
        tues.setOnClickListener(v -> {
            if(tueclick == 0){
                tues.setCardBackgroundColor(ContextCompat.getColor(OptionSignUpActivity.this, R.color.green2));
                tueclick = 1;
            }else{
                tues.setCardBackgroundColor(ContextCompat.getColor(OptionSignUpActivity.this, R.color.white));
                tueclick = 0;
            }
        });
        wed.setOnClickListener(v -> {
            if(wedclick == 0){
                wed.setCardBackgroundColor(ContextCompat.getColor(OptionSignUpActivity.this, R.color.green2));
                wedclick = 1;
            }else{
                wed.setCardBackgroundColor(ContextCompat.getColor(OptionSignUpActivity.this, R.color.white));
                wedclick = 0;
            }
        });
        thu.setOnClickListener(v -> {
            if(thuclick == 0){
                thu.setCardBackgroundColor(ContextCompat.getColor(OptionSignUpActivity.this, R.color.green2));
                thuclick = 1;
            }else{
                thu.setCardBackgroundColor(ContextCompat.getColor(OptionSignUpActivity.this, R.color.white));
                thuclick = 0;
            }
        });
        fri.setOnClickListener(v -> {
            if(friclick == 0){
                fri.setCardBackgroundColor(ContextCompat.getColor(OptionSignUpActivity.this, R.color.green2));
                friclick = 1;
            }else{
                fri.setCardBackgroundColor(ContextCompat.getColor(OptionSignUpActivity.this, R.color.white));
                friclick = 0;
            }
        });
        sat.setOnClickListener(v -> {
            if(satclick == 0){
                sat.setCardBackgroundColor(ContextCompat.getColor(OptionSignUpActivity.this, R.color.green2));
                satclick = 1;
            }else{
                sat.setCardBackgroundColor(ContextCompat.getColor(OptionSignUpActivity.this, R.color.white));
                satclick = 0;
            }
        });

        open.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    OptionSignUpActivity.this,R.style.TimePickerTheme,
                    (TimePicker view1, int hourOfDay, int minute) -> {

                        hour = hourOfDay;
                        minute = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0,hour,minute);

                        open.setText(DateFormat.format("hh:mm aa",calendar));
                        houropen = DateFormat.format("HH:mm",calendar).toString();
//                        stime.setText(Integer.toString(hourOfDay)+Integer.toString(minute));

                    },12,0,false
            );

            timePickerDialog.updateTime(hour,minute);
            timePickerDialog.show();
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        OptionSignUpActivity.this,R.style.TimePickerTheme,
                        (TimePicker view1, int hourOfDay, int minute) -> {

                            hour = hourOfDay;
                            minute = minute;
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(0,0,0,hour,minute);

                            close.setText(DateFormat.format("hh:mm aa",calendar));
                            hourclose = DateFormat.format("HH:mm",calendar).toString();
//                        stime.setText(Integer.toString(hourOfDay)+Integer.toString(minute));

                        },12,0,false
                );

                timePickerDialog.updateTime(hour,minute);
                timePickerDialog.show();
            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





//                showdialog5();
//                alertDialog.dismiss();
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