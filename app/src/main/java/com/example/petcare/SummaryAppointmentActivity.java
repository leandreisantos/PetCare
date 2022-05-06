package com.example.petcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SummaryAppointmentActivity extends AppCompatActivity {

    String owneridbundle,serviceidbundle,branchidbundle,petidbundle,opentimebundle,closeidbundle,subtotalbundle,daybundle,yearbundle,monthbundle;

    TextView petnameholder,paymentholder,schedholder;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference,databaseReference2,databaseReference3;
    DatabaseReference databaseReference4,databaseReference5;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();

    CardView set;
    RecyclerView recyclerView;

    AppointmentMember member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_appointment);

        member = new AppointmentMember();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            owneridbundle = extras.getString("ownerid");
            serviceidbundle = extras.getString("serviceid");
            branchidbundle = extras.getString("branchid");
            subtotalbundle = extras.getString("subtotal");
            petidbundle = extras.getString("petid");
            opentimebundle = extras.getString("opentime");
            closeidbundle = extras.getString("closetime");
            daybundle = extras.getString("day");
            monthbundle = extras.getString("month");
            yearbundle = extras.getString("year");
        }else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }

        databaseReference = database.getReference("All Appointment owner").child(owneridbundle);
        databaseReference2 = database.getReference("All Appointment customer").child(currentUserId);
        databaseReference3 = database.getReference("All Appointment customer and owner").child(currentUserId).child(owneridbundle);
        databaseReference4= database.getReference("All selected services").child(currentUserId).child(serviceidbundle);


        petnameholder = findViewById(R.id.tv_for);
        paymentholder = findViewById(R.id.tv_total);
        schedholder = findViewById(R.id.tv_appoint);
        set = findViewById(R.id.cv_next);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        set.setOnClickListener(v -> showdia());


    }

    private void showdia() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.no_changes_dialog,null);
        CardView yes = view.findViewById(R.id.cv_yes);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        yes.setOnClickListener(v -> submit());

    }

    private void submit() {

        String id1 = databaseReference.push().getKey();
        member.setId(id1);
        member.setPetid(petidbundle);
        member.setSubtotal(subtotalbundle);
        member.setServicesid(serviceidbundle);
        member.setOwnerid(currentUserId);
        member.setBusinessid(owneridbundle);
        member.setBrachid(branchidbundle);
        member.setOpentime(opentimebundle);
        member.setClosetime(closeidbundle);
        member.setDay(daybundle);
        member.setYear(yearbundle);
        member.setMonth(monthbundle);
        member.setStatus("req");

        databaseReference.child(id1).setValue(member);
        databaseReference2.child(id1).setValue(member);
        databaseReference3.child(id1).setValue(member);


        showdiaok();

    }

    private void showdiaok() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.successful_dialog,null);
        CardView yes = view.findViewById(R.id.ok);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        yes.setOnClickListener(v -> {
            Intent intent = new Intent(SummaryAppointmentActivity.this,MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        paymentholder.setText(subtotalbundle);
        schedholder.setText(monthbundle+"/"+daybundle+"/"+yearbundle+" "+opentimebundle+"-"+closeidbundle);

        FirebaseRecyclerOptions<AllservicesSelectedMember> options =
                new FirebaseRecyclerOptions.Builder<AllservicesSelectedMember>()
                        .setQuery(databaseReference4,AllservicesSelectedMember.class)
                        .build();

        FirebaseRecyclerAdapter<AllservicesSelectedMember, AllServicesHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AllservicesSelectedMember, AllServicesHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AllServicesHolder holder, int position, @NonNull AllservicesSelectedMember model) {


                        holder.setSelectedServices(getApplication(),model.getId(),model.getIdowner());

                        String  id = getItem(position).getId();


                    }

                    @NonNull
                    @Override
                    public AllServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.service_appointment_item,parent,false);

                        return new AllServicesHolder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);



    }
}