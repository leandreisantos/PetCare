package com.example.petcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BusinessNotificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();

    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_notification);

        databaseReference = database.getReference("All Notification").child(currentUserId);

        recyclerView = findViewById(R.id.rv);
        back = findViewById(R.id.tv_back);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        back.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<NotificationMember> options =
                new FirebaseRecyclerOptions.Builder<NotificationMember>()
                        .setQuery(databaseReference,NotificationMember.class)
                        .build();

        FirebaseRecyclerAdapter<NotificationMember, notificationholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<NotificationMember, notificationholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull notificationholder holder, int position, @NonNull NotificationMember model) {

                        holder.setNotification(getApplication(),model.getId(),model.getFrom(),model.getTo(),model.getAppointmentid(),model.getMessage());

                    }

                    @NonNull
                    @Override
                    public notificationholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.notification_item,parent,false);

                        return new notificationholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}