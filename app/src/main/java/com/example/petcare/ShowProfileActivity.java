package com.example.petcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class ShowProfileActivity extends AppCompatActivity {


    TextView nameholder,addholder,backholder;
    ImageView iv;
    RecyclerView recyclerView;
    CardView req;
    TextView message;

    String id_post;

    ConstraintLayout clmessage,clnotif;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference,databaseReference2;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id_post = extras.getString("id");
        }else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }

        databaseReference= database.getReference("All Business users").child(id_post);
        databaseReference2= database.getReference("All services").child(id_post);

        nameholder = findViewById(R.id.tv_name);
        addholder = findViewById(R.id.tv_add);
        backholder = findViewById(R.id.tv_back);
        message = findViewById(R.id.tv_message);
        clmessage = findViewById(R.id.cl_message);
        clnotif = findViewById(R.id.cl_notif);
        req = findViewById(R.id.cv_req);
        iv = findViewById(R.id.iv);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        backholder.setOnClickListener(view -> onBackPressed());


        clmessage.setOnClickListener(v -> {
            Intent intent = new Intent(ShowProfileActivity.this,BusinessMessageActivty.class);
            startActivity(intent);
        });

        clnotif.setOnClickListener(v -> {
            Intent intent = new Intent(ShowProfileActivity.this,BusinessNotificationActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String url = snapshot.child("url").getValue(String.class);
                String id = snapshot.child("iduser").getValue(String.class);
                String branchid = snapshot.child("idbranch").getValue(String.class);




                Picasso.get().load(url).into(iv);
                nameholder.setText(name);


                req.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ShowProfileActivity.this,RequestAppointmentActivity.class);
                        intent.putExtra("id",id);
                        intent.putExtra("idbranch",branchid);
                        startActivity(intent);
                    }
                });

                message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ShowProfileActivity.this,MessageActivity.class);
                        intent.putExtra("id",id);
                        intent.putExtra("need","owner");
                        startActivity(intent);
                    }
                });

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


                        holder.setServiceProfile(getApplication(),model.getId(),model.getOwenerid(),model.getServices(),model.getDesc(),
                                model.getMin(),model.getCapacity(),model.getAmount(),model.getDate(),model.getTime(),model.getTimeslotid());



                    }

                    @NonNull
                    @Override
                    public AllServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.serv_item,parent,false);

                        return new AllServicesHolder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(firebaseRecyclerAdapter);



    }
}