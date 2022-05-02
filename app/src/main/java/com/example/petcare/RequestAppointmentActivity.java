package com.example.petcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class RequestAppointmentActivity extends AppCompatActivity {

    TextView nameholder,totalholder;

    String id_post,branchidbundle;

    RecyclerView recyclerView;
    CardView next;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference,databaseReference2,databaseReference3;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();

    ArrayList<String> mylist = new ArrayList<String>();

    AllservicesSelectedMember member;

    int subtotal=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_appointment);

        member = new AllservicesSelectedMember();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id_post = extras.getString("id");
            branchidbundle = extras.getString("idbranch");
        }else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }

        databaseReference= database.getReference("All Business users").child(id_post);
        databaseReference2= database.getReference("All services").child(id_post);
        databaseReference3= database.getReference("All selected services").child(currentUserId);


        nameholder = findViewById(R.id.tV_nambussiness);
        next = findViewById(R.id.cv_next);
        totalholder = findViewById(R.id.total);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        next.setOnClickListener(view -> {

            if(!mylist.isEmpty()){

                String id1 = databaseReference3.push().getKey();


                for(int i=0;i<mylist.size();i++){
//                    Toast.makeText(this, mylist.get(i), Toast.LENGTH_SHORT).show();
                    member.setId(mylist.get(i));
                    databaseReference3.child(id1).child(mylist.get(i)).setValue(member);
                }

                Intent intent = new Intent(RequestAppointmentActivity.this,AppoinmentPetActivity.class);
                intent.putExtra("owner",id_post);
                intent.putExtra("idservices",id1);
                intent.putExtra("subtotal",String.valueOf(subtotal));
                intent.putExtra("branchid",branchidbundle);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Select services", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                nameholder.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseRecyclerOptions<AllServicesMember> options =
                new FirebaseRecyclerOptions.Builder<AllServicesMember>()
                        .setQuery(databaseReference2,AllServicesMember.class)
                        .build();

        FirebaseRecyclerAdapter<AllServicesMember, AllServicesHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AllServicesMember, AllServicesHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AllServicesHolder holder, int position, @NonNull AllServicesMember model) {


                        holder.setServiceAppointment(getApplication(),model.getId(),model.getOwenerid(),model.getServices(),model.getDesc(),
                                model.getMin(),model.getCapacity(),model.getAmount(),model.getDate(),model.getTime(),model.getTimeslotid());

                        String  id = getItem(position).getId();
                        String  amount = getItem(position).getAmount();

                        holder.cb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(holder.cb.isChecked()){
                                    mylist.add(id);
                                    subtotal+=Integer.parseInt(amount);
                                    totalholder.setText("Subtotal:"+subtotal);
                                }else{
                                    mylist.remove(id);
                                    subtotal-=Integer.parseInt(amount);
                                    totalholder.setText("Subtotal:"+subtotal);
                                }

//                                mylist.add(id);
//                                Toast.makeText(RequestAppointmentActivity.this, Arrays.toString(mylist.toArray()), Toast.LENGTH_SHORT).show();
                            }
                        });

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