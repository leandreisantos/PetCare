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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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

public class BusinessRegisterActivity extends AppCompatActivity {

    CardView iv_picholder,cv_submit;
    EditText businessnameholder,mobileholder,landlineholder,websiteholder,branchaddholder;
    TextView open,close;
    CardView sun,mon,tues,wed,thu,fri,sat;
    RadioButton vet,pet,both;
    ImageView iv;
    TextView iconpic;
    Uri imageUridp;
    StorageReference storageReference;
    UploadTask uploadTask;
    String statuposition;
    boolean checkrd=false;
    private static final int PICK_IMAGE=1;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();

    FirebaseAuth mAuth;

    //latest update
    CheckBox sun_cb,mon_cb,tues_cb,wed_cb,thurs_cb,fri_cb,sat_cb;
    TextView sun_open,sun_close,mon_open,mon_close,tues_open,tues_close,wed_open,wed_close,thurs_open,thurs_close,fri_open,fri_close,sat_open,sat_close;
    String sunOpenHour,sunCloseHour,monOpenHour,monCloseHour,tuesOpenHour,tuesClosehour,wedOpenHour,wedCloseHour,thurOpenHour,thurCloseHour,friOpenHour,friCloseHour,satOpenHour,satCloseHour;

    int numselecyday = 0,numselectime = 0;

    int sunclick = 0;
    int monclick = 0;
    int tueclick = 0;
    int wedclick = 0;
    int thuclick = 0;
    int friclick = 0;
    int satclick = 0;

    String houropen,hourclose;
    int hour,minute;

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
        setContentView(R.layout.activity_business_register);

        documentReference = db.collection("user").document(currentUserId);
        databaseReference3 = database.getReference("All Branch").child(currentUserId);

        ownerMember = new AllOwnerUserMember();
        member = new AllUserMember();
        branchMember = new AllBranchMember();

        storageReference = FirebaseStorage.getInstance().getReference("Profile images");
        databaseReference = database.getReference("All users");
        databaseReference2 = database.getReference("All Business users");

        mAuth = FirebaseAuth.getInstance();



        iv_picholder = findViewById(R.id.cv1);
        iv = findViewById(R.id.iv_pic);
        iconpic = findViewById(R.id.icon);
        cv_submit = findViewById(R.id.cv_submit);
        vet = findViewById(R.id.vet);
        pet = findViewById(R.id.groom);
        both = findViewById(R.id.both);
        businessnameholder = findViewById(R.id.et_name);
        mobileholder = findViewById(R.id.et_mnumber);
        landlineholder = findViewById(R.id.et_landline);
        websiteholder = findViewById(R.id.et_web);
        branchaddholder = findViewById(R.id.et_b1);
        sun = findViewById(R.id.sun);
        mon = findViewById(R.id.mon);
         tues = findViewById(R.id.tues);
         wed = findViewById(R.id.wed);
         thu = findViewById(R.id.thu);
        fri = findViewById(R.id.fri);
        sat = findViewById(R.id.sat);
         open = findViewById(R.id.et_open);
        close = findViewById(R.id.et_close);

        //latest update
        sun_cb = findViewById(R.id.cb_sun);
        mon_cb = findViewById(R.id.cb_mon);
        tues_cb = findViewById(R.id.cb_tues);
        wed_cb = findViewById(R.id.cb_wed);
        thurs_cb = findViewById(R.id.cb_thur);
        fri_cb = findViewById(R.id.cb_fri);
        sat_cb = findViewById(R.id.cb_sat);

        sun_open = findViewById(R.id.sun_open);
        sun_close = findViewById(R.id.sun_close);
        mon_open= findViewById(R.id.mon_open);
        mon_close= findViewById(R.id.mon_close);
        tues_open= findViewById(R.id.tues_open);
        tues_close= findViewById(R.id.tues_close);
        wed_open= findViewById(R.id.wed_open);
        wed_close= findViewById(R.id.wed_close);
        thurs_open= findViewById(R.id.thur_open);
        thurs_close= findViewById(R.id.thur_close);
        fri_open= findViewById(R.id.fri_open);
        fri_close= findViewById(R.id.fri_close);
        sat_open= findViewById(R.id.sat_open);
        sat_close= findViewById(R.id.sat_close);

        sun_cb.setOnClickListener(v -> {
            if(sunclick == 0) {
                sunclick = 1;
                sun_open.setVisibility(View.VISIBLE);
                sun_close.setVisibility(View.VISIBLE);
                numselecyday ++;
            }
            else {
                sunclick = 0;
                sun_open.setVisibility(View.GONE);
                sun_close.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(sunOpenHour)){
                    numselectime --;
                }
                if(!TextUtils.isEmpty(sunCloseHour)){
                    numselectime --;
                }
                numselecyday--;
            }
        });
        mon_cb.setOnClickListener(v -> {
            if(monclick == 0){
                monclick = 1;
                mon_open.setVisibility(View.VISIBLE);
                mon_close.setVisibility(View.VISIBLE);
                numselecyday ++;
            }
            else {
                monclick = 0;
                mon_open.setVisibility(View.GONE);
                mon_close.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(monOpenHour)){
                    numselectime --;
                }
                if(!TextUtils.isEmpty(monCloseHour)){
                    numselectime --;
                }
                numselecyday--;
            }
        });
        tues_cb.setOnClickListener(v -> {
            if(tueclick == 0) {
                tueclick = 1;
                tues_open.setVisibility(View.VISIBLE);
                tues_close.setVisibility(View.VISIBLE);
                numselecyday ++;
            }
            else{
                tueclick = 0;
                tues_open.setVisibility(View.GONE);
                tues_close.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(tuesOpenHour)){
                    numselectime --;
                }
                if(!TextUtils.isEmpty(tuesClosehour)){
                    numselectime --;
                }
                numselecyday--;
            }
        });
        wed_cb.setOnClickListener(v -> {
            if(wedclick == 0) {
                wedclick = 1;
                wed_open.setVisibility(View.VISIBLE);
                wed_close.setVisibility(View.VISIBLE);
                numselecyday ++;
            }
            else {
                wedclick = 0;
                wed_open.setVisibility(View.GONE);
                wed_close.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(wedOpenHour)){
                    numselectime --;
                }
                if(!TextUtils.isEmpty(wedCloseHour)){
                    numselectime --;
                }
                numselecyday--;
            }
        });
        thurs_cb.setOnClickListener(v -> {
            if(thuclick == 0) {
                thuclick = 1;
                thurs_open.setVisibility(View.VISIBLE);
                thurs_close.setVisibility(View.VISIBLE);
                numselecyday ++;
            }
            else {
                thuclick = 0;
                thurs_open.setVisibility(View.GONE);
                thurs_open.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(thurOpenHour)){
                    numselectime --;
                }
                if(!TextUtils.isEmpty(thurCloseHour)){
                    numselectime --;
                }
                numselecyday--;
            }
        });
        fri_cb.setOnClickListener(v -> {
            if(friclick == 0) {
                friclick = 1;
                fri_open.setVisibility(View.VISIBLE);
                fri_close.setVisibility(View.VISIBLE);
                numselecyday ++;
            }
            else {
                friclick = 0;
                fri_open.setVisibility(View.GONE);
                fri_open.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(friOpenHour)){
                    numselectime --;
                }
                if(!TextUtils.isEmpty(friCloseHour)){
                    numselectime --;
                }
                numselecyday--;
            }
        });
        sat_cb.setOnClickListener(v -> {
            if(satclick == 0) {
                satclick = 1;
                sat_open.setVisibility(View.VISIBLE);
                sat_close.setVisibility(View.VISIBLE);
                numselecyday ++;
            }
            else {
                satclick = 0;
                sat_open.setVisibility(View.GONE);
                sat_open.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(satOpenHour)){
                    numselectime --;
                }
                if(!TextUtils.isEmpty(satCloseHour)){
                    numselectime --;
                }
                numselecyday--;
            }
        });

        sun_open.setOnClickListener(v -> selectTime("sun","open"));
        sun_close.setOnClickListener(v -> selectTime("sun","close"));
        mon_open.setOnClickListener(v -> selectTime("mon","open"));
        mon_close.setOnClickListener(v -> selectTime("mon","close"));
        tues_open.setOnClickListener(v -> selectTime("tues","open"));
        tues_close.setOnClickListener(v -> selectTime("tues","close"));
        wed_open.setOnClickListener(v -> selectTime("wed","open"));
        wed_close.setOnClickListener(v -> selectTime("wed","close"));
        thurs_open.setOnClickListener(v -> selectTime("thurs","open"));
        thurs_close.setOnClickListener(v -> selectTime("thurs","close"));
        fri_open.setOnClickListener(v -> selectTime("fri","open"));
        fri_close.setOnClickListener(v -> selectTime("fri","close"));
        sat_open.setOnClickListener(v -> selectTime("sat","open"));
        sat_close.setOnClickListener(v -> selectTime("sat","close"));

        iv_picholder.setOnClickListener(v -> chooseImage());
        cv_submit.setOnClickListener(v -> submit());

        vet.setOnClickListener(v -> {
            statuposition = "vet";
            checkrd = true;
        });
        pet.setOnClickListener(v -> {
            statuposition = "groom";
            checkrd = true;
        });
        both.setOnClickListener(v -> {
            statuposition = "both";
            checkrd = true;
        });

        sun.setOnClickListener(v -> {
            if(sunclick == 0){
                sun.setCardBackgroundColor(ContextCompat.getColor(BusinessRegisterActivity.this, R.color.green2));
                sunclick = 1;
            }else{
                sun.setCardBackgroundColor(ContextCompat.getColor(BusinessRegisterActivity.this, R.color.white));
                sunclick = 0;
            }
        });
        mon.setOnClickListener(v -> {
            if(monclick == 0){
                mon.setCardBackgroundColor(ContextCompat.getColor(BusinessRegisterActivity.this, R.color.green2));
                monclick = 1;
            }else{
                mon.setCardBackgroundColor(ContextCompat.getColor(BusinessRegisterActivity.this, R.color.white));
                monclick = 0;
            }
        });
        tues.setOnClickListener(v -> {
            if(tueclick == 0){
                tues.setCardBackgroundColor(ContextCompat.getColor(BusinessRegisterActivity.this, R.color.green2));
                tueclick = 1;
            }else{
                tues.setCardBackgroundColor(ContextCompat.getColor(BusinessRegisterActivity.this, R.color.white));
                tueclick = 0;
            }
        });
        wed.setOnClickListener(v -> {
            if(wedclick == 0){
                wed.setCardBackgroundColor(ContextCompat.getColor(BusinessRegisterActivity.this, R.color.green2));
                wedclick = 1;
            }else{
                wed.setCardBackgroundColor(ContextCompat.getColor(BusinessRegisterActivity.this, R.color.white));
                wedclick = 0;
            }
        });
        thu.setOnClickListener(v -> {
            if(thuclick == 0){
                thu.setCardBackgroundColor(ContextCompat.getColor(BusinessRegisterActivity.this, R.color.green2));
                thuclick = 1;
            }else{
                thu.setCardBackgroundColor(ContextCompat.getColor(BusinessRegisterActivity.this, R.color.white));
                thuclick = 0;
            }
        });
        fri.setOnClickListener(v -> {
            if(friclick == 0){
                fri.setCardBackgroundColor(ContextCompat.getColor(BusinessRegisterActivity.this, R.color.green2));
                friclick = 1;
            }else{
                fri.setCardBackgroundColor(ContextCompat.getColor(BusinessRegisterActivity.this, R.color.white));
                friclick = 0;
            }
        });
        sat.setOnClickListener(v -> {
            if(satclick == 0){
                sat.setCardBackgroundColor(ContextCompat.getColor(BusinessRegisterActivity.this, R.color.green2));
                satclick = 1;
            }else{
                sat.setCardBackgroundColor(ContextCompat.getColor(BusinessRegisterActivity.this, R.color.white));
                satclick = 0;
            }
        });

        open.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    BusinessRegisterActivity.this,R.style.TimePickerTheme,
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
                        BusinessRegisterActivity.this,R.style.TimePickerTheme,
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

    }

    private void selectTime(String day, String status) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                BusinessRegisterActivity.this,R.style.TimePickerTheme,
                (TimePicker view1, int hourOfDay, int minute) -> {

                    hour = hourOfDay;
                    minute = minute;
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(0,0,0,hour,minute);


                    if(day.equals("sun")&&status.equals("open")){
                        sun_open.setText(DateFormat.format("hh:mm aa",calendar));
                        sunOpenHour = DateFormat.format("HH:mm",calendar).toString();
                        numselectime ++;
                    }
                    if(day.equals("sun")&&status.equals("close")){
                        sun_close.setText(DateFormat.format("hh:mm aa",calendar));
                        sunCloseHour = DateFormat.format("HH:mm",calendar).toString();
                        numselectime ++;
                    }

                    if(day.equals("mon")&&status.equals("open")){
                        mon_open.setText(DateFormat.format("hh:mm aa",calendar));
                        monOpenHour = DateFormat.format("HH:mm",calendar).toString();
                        numselectime ++;
                    }
                    if(day.equals("mon")&&status.equals("close")){
                        mon_close.setText(DateFormat.format("hh:mm aa",calendar));
                        monCloseHour = DateFormat.format("HH:mm",calendar).toString();
                        numselectime ++;
                    }

                    if(day.equals("tues")&&status.equals("open")){
                        tues_open.setText(DateFormat.format("hh:mm aa",calendar));
                        tuesOpenHour = DateFormat.format("HH:mm",calendar).toString();
                        numselectime ++;
                    }
                    if(day.equals("tues")&&status.equals("close")){
                        tues_close.setText(DateFormat.format("hh:mm aa",calendar));
                        tuesClosehour = DateFormat.format("HH:mm",calendar).toString();
                        numselectime ++;
                    }

                    if(day.equals("wed")&&status.equals("open")){
                        wed_open.setText(DateFormat.format("hh:mm aa",calendar));
                        wedOpenHour = DateFormat.format("HH:mm",calendar).toString();
                        numselectime ++;
                    }
                    if(day.equals("wed")&&status.equals("close")){
                        wed_close.setText(DateFormat.format("hh:mm aa",calendar));
                        wedCloseHour = DateFormat.format("HH:mm",calendar).toString();
                        numselectime ++;
                    }

                    if(day.equals("thurs")&&status.equals("open")){
                        thurs_open.setText(DateFormat.format("hh:mm aa",calendar));
                        thurOpenHour = DateFormat.format("HH:mm",calendar).toString();
                        numselectime ++;
                    }
                    if(day.equals("thurs")&&status.equals("close")){
                        thurs_close.setText(DateFormat.format("hh:mm aa",calendar));
                        thurCloseHour = DateFormat.format("HH:mm",calendar).toString();
                        numselectime ++;
                    }

                    if(day.equals("fri")&&status.equals("open")){
                        fri_open.setText(DateFormat.format("hh:mm aa",calendar));
                        friOpenHour = DateFormat.format("HH:mm",calendar).toString();
                        numselectime ++;
                    }
                    if(day.equals("fri")&&status.equals("close")){
                        fri_close.setText(DateFormat.format("hh:mm aa",calendar));
                        friCloseHour = DateFormat.format("HH:mm",calendar).toString();
                        numselectime ++;
                    }

                    if(day.equals("sat")&&status.equals("open")){
                        sat_open.setText(DateFormat.format("hh:mm aa",calendar));
                        satOpenHour = DateFormat.format("HH:mm",calendar).toString();
                        numselectime ++;
                    }
                    if(day.equals("sat")&&status.equals("close")){
                        sat_close.setText(DateFormat.format("hh:mm aa",calendar));
                        satCloseHour = DateFormat.format("HH:mm",calendar).toString();
                        numselectime ++;
                    }
                },12,0,false
        );

        timePickerDialog.updateTime(hour,minute);
        timePickerDialog.show();
    }

    private void submit() {
        String tempbname = businessnameholder.getText().toString();
        String tempmobile = mobileholder.getText().toString();
        String tempweb =websiteholder.getText().toString();
        String tempbranchadd = branchaddholder.getText().toString();
        String tempopen = open.getText().toString();
        String tempclose = close.getText().toString();
        String templandline = landlineholder.getText().toString();

        if(!TextUtils.isEmpty(tempbname)&&!TextUtils.isEmpty(templandline)&&!TextUtils.isEmpty(tempmobile)&&!TextUtils.isEmpty(tempweb)&&imageUridp != null&&checkrd==true
            &&!TextUtils.isEmpty(tempbranchadd)){
            if(sunclick == 0 &&monclick == 0&&tueclick==0&&wedclick==0&&thuclick==0&&friclick==0&&satclick==0){
                Toast.makeText(BusinessRegisterActivity.this, "Please select day", Toast.LENGTH_SHORT).show();
            }else {
                if((numselecyday*2)==numselectime){
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
                        profile.put("name",tempbname);
                        profile.put("mobile",tempmobile);
                        profile.put("url",downloadUri.toString());
                        profile.put("landline",templandline);
                        profile.put("web",tempweb);
                        profile.put("uid",currentUserId);
                        profile.put("status","Bussiness");

                        Calendar cdate = Calendar.getInstance();
                        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyy");
                        final String savedate = currentdate.format(cdate.getTime());

                        Calendar ctime = Calendar.getInstance();
                        SimpleDateFormat currenttime =new SimpleDateFormat("HH-mm");
                        final String savetime = currenttime.format(ctime.getTime());

                        String id1 = databaseReference3.push().getKey();

                        ownerMember.setUrl(downloadUri.toString());
                        ownerMember.setName(tempbname);
                        ownerMember.setMobile(tempmobile);
                        ownerMember.setLandline(templandline);
                        ownerMember.setEmail("");
                        ownerMember.setWebsite(tempweb);
                        ownerMember.setAdd(tempbranchadd);
                        ownerMember.setIduser(currentUserId);
                        ownerMember.setStatus("Business");
                        ownerMember.setStatusshop(statuposition);
                        ownerMember.setDate(savedate);
                        ownerMember.setTime(savetime);
                        ownerMember.setPass("");

                        member.setUserid(currentUserId);

                        branchMember.setId(id1);
                        branchMember.setIdowner(currentUserId);
                        branchMember.setName(tempbname);
                        branchMember.setLocation(tempbranchadd);
                        branchMember.setBusinessName(tempbname);
                        if(sunclick == 1){
                            branchMember.setSun("true");
                            branchMember.setSunopen(sunOpenHour);
                            branchMember.setSunclose(sunCloseHour);
                        }
                        else branchMember.setSun("false");

                        if(monclick == 1){
                            branchMember.setMon("true");
                            branchMember.setMonopen(monOpenHour);
                            branchMember.setMonclose(monCloseHour);
                        }
                        else branchMember.setMon("false");

                        if(tueclick == 1) {
                            branchMember.setTues("true");
                            branchMember.setTuesopen(tuesOpenHour);
                            branchMember.setTuesclose(tuesClosehour);
                        }
                        else branchMember.setTues("false");

                        if(wedclick == 1) {
                            branchMember.setWed("true");
                            branchMember.setWedopen(wedOpenHour);
                            branchMember.setWedclose(wedCloseHour);
                        }
                        else branchMember.setWed("false");

                        if(thuclick == 1) {
                            branchMember.setThu("true");
                            branchMember.setThursopen(thurOpenHour);
                            branchMember.setThursclose(thurCloseHour);
                        }
                        else branchMember.setThu("false");

                        if(friclick == 1){
                            branchMember.setFri("true");
                            branchMember.setFriopen(friOpenHour);
                            branchMember.setFriclose(friCloseHour);
                        }
                        else branchMember.setFri("false");

                        if(satclick == 1) {
                            branchMember.setSat("true");
                            branchMember.setSatopen(satOpenHour);
                            branchMember.setSatclose(satCloseHour);
                        }
                        else branchMember.setSat("false");


                        databaseReference2.child(currentUserId).setValue(ownerMember);
                        databaseReference.child(currentUserId).setValue(member);
                        databaseReference3.child(id1).setValue(branchMember);

                        documentReference.set(profile)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(BusinessRegisterActivity.this, "Profile Created", Toast.LENGTH_SHORT).show();

                                    Handler handler = new Handler();
                                    handler.postDelayed(() -> {
                                        Intent intent = new Intent(BusinessRegisterActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    },2000);
                                });
                    });
                }else{
                    Toast.makeText(this, "Please fill up all selected time", Toast.LENGTH_SHORT).show();
                }

            }
        }else{
            Toast.makeText(BusinessRegisterActivity.this, "Please fill up requirements", Toast.LENGTH_SHORT).show();
        }
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

}