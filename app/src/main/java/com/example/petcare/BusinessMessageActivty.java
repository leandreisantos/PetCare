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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BusinessMessageActivty extends AppCompatActivity {

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference,databaseReference2,rootref1,rootref2;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_message_activty);

        databaseReference2= database.getReference("All Message notification").child(currentUserId);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<MessageNotificationMember> options =
                new FirebaseRecyclerOptions.Builder<MessageNotificationMember>()
                        .setQuery(databaseReference2,MessageNotificationMember.class)
                        .build();

        FirebaseRecyclerAdapter<MessageNotificationMember, MessageNotifHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<MessageNotificationMember, MessageNotifHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MessageNotifHolder holder, int position, @NonNull MessageNotificationMember model) {

                       holder.setMessageNotif(getApplication(),model.getSenderid(),model.getTime(),model.getDate(),model.getMessage(),model.getReceiverid());
                        String id = getItem(position).getSenderid();

                       holder.cvholder.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               Intent intent = new Intent(BusinessMessageActivty.this,MessageActivity.class);
                               intent.putExtra("id",id);
                               intent.putExtra("need","customer");
                               startActivity(intent);
                           }
                       });


                    }

                    @NonNull
                    @Override
                    public MessageNotifHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.message_notifi_layout,parent,false);

                        return new MessageNotifHolder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }
}