package com.example.petcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MessageActivity extends AppCompatActivity {

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference,databaseReference2,rootref1,rootref2;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();

    TextView name;
    ImageView ivpic;
    String id_post,needbundle;

    EditText messageet;
    TextView send;

    MessageMember member;
    MessageNotificationMember mnotifi;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        member = new MessageMember();
        mnotifi = new MessageNotificationMember();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id_post = extras.getString("id");
            needbundle = extras.getString("need");
        }else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }

        if(needbundle.equals("customer"))databaseReference= database.getReference("All Customer users").child(id_post);
        else databaseReference= database.getReference("All Business users").child(id_post);
        databaseReference2= database.getReference("All Message notification").child(id_post).child(currentUserId);
        rootref1= database.getReference("Message").child(currentUserId).child(id_post);
        rootref2= database.getReference("Message").child(id_post).child(currentUserId);

        name = findViewById(R.id.tv_name);
        ivpic = findViewById(R.id.iv);
        messageet = findViewById(R.id.et_message);
        send = findViewById(R.id.tv_send);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempmessage = messageet.getText().toString();

                if(!TextUtils.isEmpty(tempmessage)){
                    Calendar cdate = Calendar.getInstance();
                    SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyy");
                    final String savedate = currentdate.format(cdate.getTime());

                    Calendar ctime = Calendar.getInstance();
                    SimpleDateFormat currenttime =new SimpleDateFormat("HH-mm-ss");
                    final String savetime = currenttime.format(ctime.getTime());

                    mnotifi.setSenderid(currentUserId);
                    mnotifi.setTime(savetime);
                    mnotifi.setDate(savedate);
                    mnotifi.setMessage(tempmessage);
                    mnotifi.setReceiverid(id_post);

                    databaseReference2.setValue(mnotifi);



                    member.setMessage(tempmessage);
                    member.setTime(savetime);
                    member.setSenderuid(currentUserId);
                    member.setReceiveruid(id_post);

                    String id = rootref1.push().getKey();
                    rootref1.child(id).setValue(member);

                    String id1 = rootref2.push().getKey();
                    rootref2.child(id1).setValue(member);

                    messageet.setText("");
                }else{
                    Toast.makeText(MessageActivity.this, "Input Some Message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nameholder = snapshot.child("name").getValue(String.class);
                String url = snapshot.child("url").getValue(String.class);
                String id = snapshot.child("iduser").getValue(String.class);

                name.setText(nameholder);
                Picasso.get().load(url).into(ivpic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseRecyclerOptions<MessageMember> options =
                new FirebaseRecyclerOptions.Builder<MessageMember>()
                        .setQuery(rootref1,MessageMember.class)
                        .build();

        FirebaseRecyclerAdapter<MessageMember, Messageholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<MessageMember, Messageholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Messageholder holder, int position, @NonNull MessageMember model) {

                        holder.SetMessage(getApplication(),model.getMessage(),model.getTime(),model.getDate(),model.getSenderuid(),model.getReceiveruid());


                    }

                    @NonNull
                    @Override
                    public Messageholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.message_item_layout,parent,false);

                        return new Messageholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);





    }
}