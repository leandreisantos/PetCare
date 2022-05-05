package com.example.petcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddServicesActivity extends AppCompatActivity {

    CardView addservices,done,addtime;
    TextView serviceslbl;
    String finalservices;
    TextView addholder,minusholder,capholder;
    TextView open,close;
    TextView back;
    String houropen,hourclose;
    EditText desc,min,amount;
    int hour,minute;

    int capacity;
    boolean newvheck=false;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference,databaseReference2,databaseReference3;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();

    TimeSlotMember timemember;
    String id1;

    RecyclerView recyclerView;
    int countex = 0;

    AllServicesMember servmember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services);

        timemember = new TimeSlotMember();
        servmember = new AllServicesMember();
        databaseReference = database.getReference("All timeslot").child(currentUserId);
        databaseReference2 = database.getReference("All services").child(currentUserId);
        databaseReference3= database.getReference("All Business users").child(currentUserId);

        addservices = findViewById(R.id.cv1);
        serviceslbl = findViewById(R.id.lbl_serv);
        addholder = findViewById(R.id.add);
        minusholder = findViewById(R.id.minus);
        capholder = findViewById(R.id.amount);
        done = findViewById(R.id.cv_done);
        addtime = findViewById(R.id.cv_timeslot);
        desc = findViewById(R.id.et_desc);
        min = findViewById(R.id.et_min);
        amount = findViewById(R.id.et_amount);
        back = findViewById(R.id.back);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        addservices.setOnClickListener(v -> showServices());

        addtime.setOnClickListener(v -> showTimeSlot());

        back.setOnClickListener(view -> onBackPressed());

        addholder.setOnClickListener(v -> {
            capacity ++ ;
            capholder.setText(String.valueOf(capacity));
        });
        minusholder.setOnClickListener(v -> {
            if(capacity>0){
                capacity -- ;
                capholder.setText(String.valueOf(capacity));
            }
        });

        done.setOnClickListener(v -> submit());

    }

    private void showTimeSlot() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.timeslot_layout,null);

        open = view.findViewById(R.id.et_open);
        close = view.findViewById(R.id.et_close);
        CardView addtime = view.findViewById(R.id.cv_add);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        open.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    AddServicesActivity.this,R.style.TimePickerTheme,
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
        close.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    AddServicesActivity.this,R.style.TimePickerTheme,
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
        });

        addtime.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(hourclose)&&!TextUtils.isEmpty(houropen)){
                Calendar ctime = Calendar.getInstance();
                SimpleDateFormat currenttime =new SimpleDateFormat("HH-mm-ss");
                final String savetime = currenttime.format(ctime.getTime());

                if(newvheck){
                    databaseReference = database.getReference("All timeslot").child(currentUserId).child(id1);
                }else{
                    id1 = databaseReference.push().getKey();
                }

                timemember.setId(id1);
                timemember.setOpen(houropen);
                timemember.setClose(hourclose);
                timemember.setAvail("true");
                timemember.setTime(savetime);

                if(newvheck){
                    databaseReference.child(id1+savetime).setValue(timemember);
                }else{
                    databaseReference.child(id1).child(id1+savetime).setValue(timemember);
                    databaseReference = database.getReference("All timeslot").child(currentUserId).child(id1);
                    newvheck = true;
//                        result.setVisibility(View.VISIBLE);
                }
                Toast.makeText(AddServicesActivity.this, "Time Added", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                showRec();
//                    databaseReference.child(id1).setValue(timemember);
                countex ++;


            }else{
                Toast.makeText(AddServicesActivity.this, "Please select time", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showRec() {
        FirebaseRecyclerOptions<TimeSlotMember> options =
                new FirebaseRecyclerOptions.Builder<TimeSlotMember>()
                        .setQuery(databaseReference,TimeSlotMember.class)
                        .build();

        FirebaseRecyclerAdapter<TimeSlotMember, Timeholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<TimeSlotMember, Timeholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Timeholder holder, int position, @NonNull TimeSlotMember model) {


                        holder.setTime(getApplication(),model.getId(),model.getOpen(),model.getClose(),model.getAvail(),model.getTime());
                        String time = getItem(position).getTime();
                        String id = getItem(position).getId();


                        holder.delete.setOnClickListener(v -> {
                            databaseReference.child(id+time).removeValue();
                            countex -- ;
                        });


                    }

                    @NonNull
                    @Override
                    public Timeholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.timeitem_layout,parent,false);

                        return new Timeholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void submit() {
        String tempdesc = desc.getText().toString();
        String tempmin = min.getText().toString();
        String tempamount = amount.getText().toString();


        if(!TextUtils.isEmpty(finalservices)&&!TextUtils.isEmpty(tempdesc)&&!TextUtils.isEmpty(tempmin)&&!TextUtils.isEmpty(tempamount)&&
            capacity != 0 && countex>0){

            String id2 = databaseReference2.push().getKey();
            Calendar cdate = Calendar.getInstance();
            SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyy");
            final String savedate = currentdate.format(cdate.getTime());

            Calendar ctime = Calendar.getInstance();
            SimpleDateFormat currenttime =new SimpleDateFormat("HH-mm");
            final String savetime = currenttime.format(ctime.getTime());

            servmember.setId(id2);
            servmember.setOwenerid(currentUserId);
            servmember.setServices(finalservices);
            servmember.setDesc(tempdesc);
            servmember.setMin(tempmin);
            servmember.setCapacity(String.valueOf(capacity));
            servmember.setAmount(tempamount);
            servmember.setTime(savetime);
            servmember.setDate(savedate);
            servmember.setTimeslotid(id1);

            databaseReference2.child(id2).setValue(servmember);
            Toast.makeText(this, "services added", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddServicesActivity.this,MainActivity.class);
            startActivity(intent);

        }else{
            Toast.makeText(this, "Please fill up all requirements", Toast.LENGTH_SHORT).show();
        }

    }

    private void showServices() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.service_layout,null);
        
        TextView consul =  view.findViewById(R.id.tv_consultation);
        TextView vacc =  view.findViewById(R.id.tv_Vaccination);
        TextView groom =  view.findViewById(R.id.tv_fullGroom);
        TextView nail =  view.findViewById(R.id.tv_nailTriming);
        TextView eye =  view.findViewById(R.id.tv_eye);
        TextView bath =  view.findViewById(R.id.tv_bathing);
        TextView teeth =  view.findViewById(R.id.tv_teeth);
        TextView anal =  view.findViewById(R.id.tv_anal);

        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String statusshop = snapshot.child("statusshop").getValue(String.class);

                if(statusshop.equals("vet")){
                    groom.setVisibility(View.GONE);
                    nail.setVisibility(View.GONE);
                    eye.setVisibility(View.GONE);
                    bath.setVisibility(View.GONE);
                    teeth.setVisibility(View.GONE);
                    anal.setVisibility(View.GONE);
                }else if(statusshop.equals("groom")){
                    consul.setVisibility(View.GONE);
                    vacc.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        alertDialog.show();

        consul.setOnClickListener(v -> {
            finalservices = "Consultation";
            serviceslbl.setText("Consultation");
            alertDialog.dismiss();
        });
        vacc.setOnClickListener(v -> {
            finalservices = "Vaccination";
            serviceslbl.setText("Vaccination");
            alertDialog.dismiss();
        });
        groom.setOnClickListener(v -> {
            finalservices = "Full Groom";
            serviceslbl.setText("Full Groom");
            alertDialog.dismiss();
        });
        nail.setOnClickListener(v -> {
            finalservices = "Nail Trimming";
            serviceslbl.setText("Nail Trimming");
            alertDialog.dismiss();
        });
        eye.setOnClickListener(v -> {
            finalservices = "Eye and Ear Clean";
            serviceslbl.setText("Eye and Ear Clean");
            alertDialog.dismiss();
        });

        bath.setOnClickListener(v -> {
            finalservices = "Bathing";
            serviceslbl.setText("Bathing");
            alertDialog.dismiss();
        });

        teeth.setOnClickListener(v -> {
            finalservices = "Teeth Brushing";
            serviceslbl.setText("Teeth Brushing");
            alertDialog.dismiss();
        });

        anal.setOnClickListener(v -> {
            finalservices = "Anal Gland Expression";
            serviceslbl.setText("Anal Gland Expression");
            alertDialog.dismiss();
        });
        
    }
}