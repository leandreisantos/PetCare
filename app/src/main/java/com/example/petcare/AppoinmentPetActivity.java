package com.example.petcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppoinmentPetActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();

    RecyclerView recyclerView;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoinment_pet);


        databaseReference= database.getReference("All pet").child(currentUserId);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AllPetMember> options =
                new FirebaseRecyclerOptions.Builder<AllPetMember>()
                        .setQuery(databaseReference,AllPetMember.class)
                        .build();

        FirebaseRecyclerAdapter<AllPetMember, Petholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AllPetMember, Petholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Petholder holder, int position, @NonNull AllPetMember model) {


                        holder.setPetAppointmet(getApplication(),model.getPetname(),model.getBreed(),model.getWeight(),model.getAge(),model.getStatus(),model.getId(),model.getOwnerid());

                    }

                    @NonNull
                    @Override
                    public Petholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.pet_appoinment_item,parent,false);

                        return new Petholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}