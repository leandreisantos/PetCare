package com.example.petcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

        petnameholder = findViewById(R.id.tv_for);
        paymentholder = findViewById(R.id.tv_total);
        schedholder = findViewById(R.id.tv_appoint);
        set = findViewById(R.id.cv_next);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });


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


        Intent intent = new Intent(SummaryAppointmentActivity.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        paymentholder.setText(subtotalbundle);
        schedholder.setText(monthbundle+"/"+daybundle+"/"+yearbundle+" "+opentimebundle+"-"+closeidbundle);

    }
}