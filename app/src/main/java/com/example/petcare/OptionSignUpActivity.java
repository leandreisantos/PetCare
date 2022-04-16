package com.example.petcare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OptionSignUpActivity extends AppCompatActivity {

    CardView owner,bussiness;
    TextView back;
    private static final int PICK_IMAGE=1;
    ImageView iv;
    TextView iconpic;
    Uri imageUridp;
    StorageReference storageReference;
    UploadTask uploadTask;
    String currentUserId;

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
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference,databaseReference2,databaseReference3;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;

    AllOwnerUserMember ownerMember;
    AllUserMember member;
    AllBranchMember branchMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_sign_up);

        ownerMember = new AllOwnerUserMember();
        member = new AllUserMember();
        branchMember = new AllBranchMember();

        storageReference = FirebaseStorage.getInstance().getReference("Profile images");
        databaseReference = database.getReference("All users");
        databaseReference2 = database.getReference("All Business users");

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
        CardView cv_i = view.findViewById(R.id.cv_iv);
        iv = view.findViewById(R.id.iv);
        iconpic = view.findViewById(R.id.tv_icon);

        CardView next = view.findViewById(R.id.cv3);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        cv_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

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
                    mAuth.createUserWithEmailAndPassword(emailholder,passholder).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            currentUserId = user.getUid();
                            mAuth.signInWithEmailAndPassword(emailholder,passholder).addOnCompleteListener(task2 -> {
                                if(task2.isSuccessful()){
                                    documentReference = db.collection("user").document(currentUserId);
                                    databaseReference3 = database.getReference("All Branch").child(currentUserId);
                                    showdialog2();
                                    alertDialog.dismiss();
                                }else{
                                    String error = Objects.requireNonNull(task2.getException()).getMessage();
                                    Toast.makeText(OptionSignUpActivity.this, "Error:"+error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            String error = Objects.requireNonNull(task.getException()).getMessage();
                            Toast.makeText(OptionSignUpActivity.this, "Error:"+error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(OptionSignUpActivity.this, "Password not match", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(OptionSignUpActivity.this, "Please fill up all requirements", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData()!=null){
                imageUridp = data.getData();
                iconpic.setVisibility(View.GONE);
                Picasso.get().load(imageUridp).into(iv);
            }

        }catch (Exception e){
            Toast.makeText(this, "Error"+e, Toast.LENGTH_SHORT).show();
        }
    }
    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
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
        ProgressBar pb = view.findViewById(R.id.pb);


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
                if(sunclick == 0 &&monclick == 0&&tueclick==0&&wedclick==0&&thuclick==0&&friclick==0&&satclick==0){
                    Toast.makeText(OptionSignUpActivity.this, "Please select day", Toast.LENGTH_SHORT).show();
                }else{
                    String tempopen = open.getText().toString();
                    String tempclose = close.getText().toString();
                    if(!TextUtils.isEmpty(tempopen)&&!TextUtils.isEmpty(tempclose)){
                        pb.setVisibility(View.VISIBLE);
                        final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExt(imageUridp));
                        uploadTask = reference.putFile(imageUridp);
                        Task<Uri> urlTask = uploadTask.continueWithTask((Task<UploadTask.TaskSnapshot> task3) -> {
                            if(!task3.isSuccessful()){
                                throw task3.getException();
                            }
                            return reference.getDownloadUrl();
                        }).addOnCompleteListener(task3 -> {
                            Uri downloadUri = task3.getResult();

                            Map<String, String> profile = new HashMap<>();
                            profile.put("name",bussnameholder);
                            profile.put("mobile",mobileholder);
                            profile.put("url",downloadUri.toString());
                            profile.put("landline",landlineholder);
                            profile.put("email",emailholder);
                            profile.put("web",webholder);
                            profile.put("uid",currentUserId);
                            profile.put("status","Bussiness");

                            Calendar cdate = Calendar.getInstance();
                            SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyy");
                            final String savedate = currentdate.format(cdate.getTime());

                            Calendar ctime = Calendar.getInstance();
                            SimpleDateFormat currenttime =new SimpleDateFormat("HH-mm");
                            final String savetime = currenttime.format(ctime.getTime());

                            ownerMember.setUrl(downloadUri.toString());
                            ownerMember.setName(bussnameholder);
                            ownerMember.setMobile(mobileholder);
                            ownerMember.setLandline(landlineholder);
                            ownerMember.setEmail(emailholder);
                            ownerMember.setWebsite(webholder);
                            ownerMember.setIduser(currentUserId);
                            ownerMember.setStatus("Business");
                            ownerMember.setStatusshop(statusposition);
                            ownerMember.setDate(savedate);
                            ownerMember.setTime(savetime);
                            ownerMember.setPass(passholder);

                            member.setUserid(currentUserId);

                            databaseReference2.child(currentUserId).setValue(ownerMember);
                            databaseReference.child(currentUserId).setValue(member);

                            documentReference.set(profile)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(OptionSignUpActivity.this, "Profile Created", Toast.LENGTH_SHORT).show();

                                        Handler handler = new Handler();
                                        handler.postDelayed(() -> {
                                            Intent intent = new Intent(OptionSignUpActivity.this,MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        },2000);
                                    });
                        });

                    }else{
                        Toast.makeText(OptionSignUpActivity.this, "Please fill up all requirements", Toast.LENGTH_SHORT).show();
                    }
                }
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