package com.example.petcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class DateAppointmentActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();

    RecyclerView recyclerView;
    CardView cv,next;
    CalendarView calendar;
    boolean checkdate = false;
    int dayIndex;
    String openTime,closeTime;
    String dayholder,monthholder,yearholder;
    TextView timeavailable;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference,databaseReference2;

    String ownerbundle,serviceidbundle,subtotalbundle,bundleidbranch,petidbundle;

    String days[] = {"sun", "mon", "tues", "wed", "thurs", "fri", "sat"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_appointment);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            ownerbundle = extras.getString("owner");
            serviceidbundle = extras.getString("idservices");
            subtotalbundle = extras.getString("subtotal");
            bundleidbranch = extras.getString("branchid");
            petidbundle = extras.getString("petid");
        }else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }


        databaseReference= database.getReference("All pet").child(currentUserId);
        databaseReference2= database.getReference("All Branch").child(ownerbundle).child(bundleidbranch);
        recyclerView = findViewById(R.id.rv);
        next = findViewById(R.id.cv_next);
        cv = findViewById(R.id.cv_next);
        timeavailable = findViewById(R.id.tv_time);
        calendar = findViewById(R.id.cview);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DateAppointmentActivity.this,SummaryAppointmentActivity.class);
                startActivity(intent);
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.DAY_OF_YEAR,dayOfMonth);

                dayholder = String.valueOf(dayOfMonth);
                monthholder = String.valueOf(month);
                yearholder = String.valueOf(year);

                dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
                if(dayIndex == 7){
                    dayIndex = 0;
                }

                databaseReference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String day = snapshot.child(days[dayIndex]).getValue(String.class);
                         openTime = snapshot.child(days[dayIndex]+"open").getValue(String.class);
                         closeTime = snapshot.child(days[dayIndex]+"close").getValue(String.class);

                        timeavailable.setText(openTime+"-"+closeTime);

                        if(day.equals("true")){
                            Toast.makeText(DateAppointmentActivity.this, "selected day", Toast.LENGTH_SHORT).show();
                            checkdate = true;
                        }else{
                            Toast.makeText(DateAppointmentActivity.this, "Please select available date", Toast.LENGTH_SHORT).show();
                            checkdate = false;
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkdate){
                    Intent intent = new Intent(DateAppointmentActivity.this,SummaryAppointmentActivity.class);
                    intent.putExtra("ownerid",ownerbundle);
                    intent.putExtra("serviceid",serviceidbundle);
                    intent.putExtra("branchid",bundleidbranch);
                    intent.putExtra("petid",petidbundle);
                    intent.putExtra("opentime",openTime);
                    intent.putExtra("closetime",closeTime);
                    intent.putExtra("subtotal",subtotalbundle);
                    intent.putExtra("day",dayholder);
                    intent.putExtra("month",monthholder);
                    intent.putExtra("year",yearholder);
                    startActivity(intent);


                }else{
                    Toast.makeText(DateAppointmentActivity.this, "Please select available date", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void showAvaibleTime() {

    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}