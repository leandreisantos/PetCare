package com.example.petcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GroomingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView back;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference,databaseReference2,databaseReference3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grooming);


        databaseReference = database.getReference("All Business users");


        recyclerView = findViewById(R.id.rv);
        back = findViewById(R.id.tv_back);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        back.setOnClickListener(view -> onBackPressed());


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AllOwnerUserMember> options =
                new FirebaseRecyclerOptions.Builder<AllOwnerUserMember>()
                        .setQuery(databaseReference,AllOwnerUserMember.class)
                        .build();

        FirebaseRecyclerAdapter<AllOwnerUserMember, vetholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AllOwnerUserMember, vetholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull vetholder holder, int position, @NonNull AllOwnerUserMember model) {


                        holder.setvet(getApplication(),model.getUrl(),model.getName(),model.getMobile(),model.getLandline(),model.getEmail(),model.getWebsite(),
                                model.getAdd(),model.getIduser(),model.getStatus(),model.getStatusshop(),model.getDate(),model.getTime(),model.getPass());

                        String  id = getItem(position).getIduser();


                        holder.cv.setOnClickListener(view -> {
                            Intent intent = new Intent(GroomingActivity.this,ShowProfileActivity.class);
                            intent.putExtra("id",id);
                            startActivity(intent);
                        });

                    }

                    @NonNull
                    @Override
                    public vetholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.vet_item,parent,false);

                        return new vetholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}