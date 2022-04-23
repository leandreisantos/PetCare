package com.example.petcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddWalkinClientActivity extends AppCompatActivity {

    TextView backholder,miinholder;
    CardView services,timeslot,done,selectiv;
    TextView servicestext,timetext;
    boolean checkservices=false,checktime=false;
    EditText nameholder,petnameholder,weightholder,ageholder;
    RadioButton healthy,unhealthy;
    String health;
    private static final int PICK_IMAGE=1;
    ImageView iv;
    TextView iconpic;
    Uri imageUridp;
    StorageReference storageReference;
    UploadTask uploadTask;
    String finalservices,finalminutes;
    String finalstart,finalend;
    ProgressBar pb;


    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference,databaseReference2,databaseReference3;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();


    AllWalkinMember walkinMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_walkin_client);

        walkinMember = new AllWalkinMember();

        databaseReference= database.getReference("All services").child(currentUserId);
        databaseReference3= database.getReference("All walkin").child(currentUserId);
        storageReference = FirebaseStorage.getInstance().getReference("Profile images");

        backholder = findViewById(R.id.tv_back);
        services = findViewById(R.id.cv_serv);
        timeslot = findViewById(R.id.cv_slot);
        done = findViewById(R.id.cv_done);
        servicestext = findViewById(R.id.servtext);
        timetext = findViewById(R.id.tv_time2);
        nameholder = findViewById(R.id.et_fullname);
        petnameholder = findViewById(R.id.et_petname);
        weightholder = findViewById(R.id.et_weigth);
        ageholder = findViewById(R.id.et_age);
        healthy = findViewById(R.id.rb_healthy);
        unhealthy = findViewById(R.id.rb_unhealthy);
        selectiv = findViewById(R.id.cv_iv);
        iv = findViewById(R.id.iv);
        iconpic = findViewById(R.id.icon);
        pb = findViewById(R.id.pb);
        miinholder = findViewById(R.id.minutes);

        backholder.setOnClickListener(v -> onBackPressed());

        healthy.setOnClickListener(view -> health = "Healthy");
        unhealthy.setOnClickListener(view -> health = "Unhealthy");


        services.setOnClickListener(view -> showServices());

        done.setOnClickListener(view -> submit());

        selectiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
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

    private void submit() {

        String tempname = nameholder.getText().toString();
        String temppetname = petnameholder.getText().toString();
        String tempweight = weightholder.getText().toString();
        String tempage = ageholder.getText().toString();

        if(checktime == true && checkservices==true && !TextUtils.isEmpty(tempname)&&!TextUtils.isEmpty(temppetname)&&!TextUtils.isEmpty(tempweight)&&
                !TextUtils.isEmpty(tempage)&&!TextUtils.isEmpty(health)){

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

                Calendar cdate = Calendar.getInstance();
                SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyy");
                final String savedate = currentdate.format(cdate.getTime());

                Calendar ctime = Calendar.getInstance();
                SimpleDateFormat currenttime =new SimpleDateFormat("HH-mm");
                final String savetime = currenttime.format(ctime.getTime());

                String id1 = databaseReference3.push().getKey();
                walkinMember.setId(id1);
                walkinMember.setIdowner(currentUserId);
                walkinMember.setName(tempname);
                walkinMember.setName(tempname);
                walkinMember.setPetname(temppetname);
                walkinMember.setWeight(tempweight);
                walkinMember.setAge(tempage);
                walkinMember.setHealth(health);
                walkinMember.setServices(finalservices);
                walkinMember.setMinutes(finalminutes);
                walkinMember.setTime(savetime);
                walkinMember.setDate(savedate);
                walkinMember.setUrl(downloadUri.toString());
                walkinMember.setStart(finalstart);
                walkinMember.setEnd(finalend);


                databaseReference3.child(id1).setValue(walkinMember);
                Toast.makeText(this, "Walkin Added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddWalkinClientActivity.this,MainActivity.class);
                startActivity(intent);

            });



        }else{
            Toast.makeText(this, "Please fill up all requirements", Toast.LENGTH_SHORT).show();
        }


    }

    private void showServices() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.shopservices_layout,null);

        TextView back =  view.findViewById(R.id.tv_back);
        RecyclerView recyclerView = view.findViewById(R.id.rv);
         recyclerView.setHasFixedSize(false);
         recyclerView.setLayoutManager(new LinearLayoutManager(this));


        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();


        FirebaseRecyclerOptions<AllServicesMember> options =
                new FirebaseRecyclerOptions.Builder<AllServicesMember>()
                        .setQuery(databaseReference,AllServicesMember.class)
                        .build();

        FirebaseRecyclerAdapter<AllServicesMember, AllServicesHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AllServicesMember, AllServicesHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AllServicesHolder holder, int position, @NonNull AllServicesMember model) {


                        holder.setService(getApplication(),model.getId(),model.getOwenerid(),model.getServices(),model.getDesc(),
                                model.getMin(),model.getCapacity(),model.getAmount(),model.getDate(),model.getTime(),model.getTimeslotid());

                        String  services = getItem(position).getServices();
                        String  timeid = getItem(position).getTimeslotid();
                        String  min = getItem(position).getMin();


                        holder.titleholder.setOnClickListener(view12 -> {
                            timeslot.setVisibility(View.VISIBLE);
                            done.setVisibility(View.VISIBLE);
                            finalservices = services;
                            finalminutes = min;
                            miinholder.setText(min);
                            servicestext.setText(services);
                            timeslot.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showSlotAvail(timeid);
                                }
                            });
                            checkservices = true;
                            alertDialog.dismiss();
                        });




                    }

                    @NonNull
                    @Override
                    public AllServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.shopservice_item,parent,false);

                        return new AllServicesHolder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);






        back.setOnClickListener(view1 -> alertDialog.dismiss());

    }

    private void showSlotAvail(String timeid) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.shopservices_layout,null);

        TextView back =  view.findViewById(R.id.tv_back);
        RecyclerView recyclerView = view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference2= database.getReference("All timeslot").child(currentUserId).child(timeid);


        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        FirebaseRecyclerOptions<TimeSlotMember> options =
                new FirebaseRecyclerOptions.Builder<TimeSlotMember>()
                        .setQuery(databaseReference2,TimeSlotMember.class)
                        .build();

        FirebaseRecyclerAdapter<TimeSlotMember, Timeholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<TimeSlotMember, Timeholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Timeholder holder, int position, @NonNull TimeSlotMember model) {


                        holder.setTimewalk(getApplication(),model.getId(),model.getOpen(),model.getClose(),model.getAvail(),model.getTime());
                        String time = getItem(position).getTime();
                        String id = getItem(position).getId();
                        String opentime = getItem(position).getOpen();
                        String closetime = getItem(position).getClose();

                        holder.timecons.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                timetext.setText(opentime+" - "+closetime);
                                finalstart = opentime;
                                finalend = closetime;
                                alertDialog.dismiss();
                                checktime = true;
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public Timeholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.timeitemwalkin_layout,parent,false);

                        return new Timeholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();






    }
}