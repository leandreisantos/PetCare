package com.example.petcare;

import android.app.Application;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MessageNotifHolder extends RecyclerView.ViewHolder{

    TextView nameholder,messageholder;
    ImageView ivholder;
    CardView cvholder;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference;

    public MessageNotifHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setMessageNotif(Application application,String senderid,String time,String date,String message,String receiverid){

        databaseReference= database.getReference("All Customer users").child(senderid);

        nameholder = itemView.findViewById(R.id.name);
        messageholder = itemView.findViewById(R.id.message);
        cvholder = itemView.findViewById(R.id.cv);
        ivholder = itemView.findViewById(R.id.iv);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String url = snapshot.child("url").getValue(String.class);
                String id = snapshot.child("iduser").getValue(String.class);




                Picasso.get().load(url).into(ivholder);
                nameholder.setText(name);
                messageholder.setText(message);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    public void setMessageNotifCustomer(Application application,String senderid,String time,String date,String message,String receiverid){

        databaseReference= database.getReference("All Business users").child(senderid);

        nameholder = itemView.findViewById(R.id.name);
        messageholder = itemView.findViewById(R.id.message);
        cvholder = itemView.findViewById(R.id.cv);
        ivholder = itemView.findViewById(R.id.iv);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String url = snapshot.child("url").getValue(String.class);
                String id = snapshot.child("iduser").getValue(String.class);




                Picasso.get().load(url).into(ivholder);
                nameholder.setText(name);
                messageholder.setText(message);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
