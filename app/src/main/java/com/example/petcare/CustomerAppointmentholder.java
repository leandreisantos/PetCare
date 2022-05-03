package com.example.petcare;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CustomerAppointmentholder extends RecyclerView.ViewHolder {

    TextView idholder,nameholder,descholder,moreholder;
    ImageView ivholder;
    CardView cv;
    CardView cvraccep,cvdecline;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference;



    public CustomerAppointmentholder(@NonNull View itemView) {
        super(itemView);
    }
    public void setAppointment(FragmentActivity activity,String id,String petid,String subtotal,String serviceid,String ownerid,
                               String businessid,String branchid,String selectedate,String opentime,String closetime,String time,String date,String day,
                               String year,String month,String status){


        ivholder = itemView.findViewById(R.id.iv);
        nameholder = itemView.findViewById(R.id.tv_name);
        descholder = itemView.findViewById(R.id.tv_serv);
        idholder = itemView.findViewById(R.id.tv_id);
        moreholder = itemView.findViewById(R.id.tv_more);




        databaseReference= database.getReference("All Customer users").child(ownerid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String url = snapshot.child("url").getValue(String.class);
                String id = snapshot.child("iduser").getValue(String.class);
                String branchid = snapshot.child("idbranch").getValue(String.class);


                Picasso.get().load(url).into(ivholder);
                nameholder.setText(name);
                descholder.setText(month+"/"+day+"/"+year+" * "+opentime+"-"+closetime);
                idholder.setText("Appointment ID:"+id);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setAppointmentB(FragmentActivity activity,String id,String petid,String subtotal,String serviceid,String ownerid,
                               String businessid,String branchid,String selectedate,String opentime,String closetime,String time,String date,String day,
                               String year,String month,String status){


        ivholder = itemView.findViewById(R.id.iv);
        nameholder = itemView.findViewById(R.id.tv_name);
        descholder = itemView.findViewById(R.id.tv_serv);
        idholder = itemView.findViewById(R.id.tv_id);
        moreholder = itemView.findViewById(R.id.tv_more);
        cvraccep = itemView.findViewById(R.id.cv1);
        cvdecline = itemView.findViewById(R.id.cv2);



        databaseReference= database.getReference("All Customer users").child(ownerid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String url = snapshot.child("url").getValue(String.class);
                String id = snapshot.child("iduser").getValue(String.class);
                String branchid = snapshot.child("idbranch").getValue(String.class);


                Picasso.get().load(url).into(ivholder);
                nameholder.setText(name);
                descholder.setText(month+"/"+day+"/"+year+" * "+opentime+"-"+closetime);
                idholder.setText("Appointment ID:"+id);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void setAppointmentaccept(FragmentActivity activity,String id,String petid,String subtotal,String serviceid,String ownerid,
                                String businessid,String branchid,String selectedate,String opentime,String closetime,String time,String date,String day,
                                String year,String month,String status){


        ivholder = itemView.findViewById(R.id.iv);
        nameholder = itemView.findViewById(R.id.tv_name);
        descholder = itemView.findViewById(R.id.tv_serv);
        idholder = itemView.findViewById(R.id.tv_id);
        moreholder = itemView.findViewById(R.id.tv_more);
        cvraccep = itemView.findViewById(R.id.cv1);
        cvdecline = itemView.findViewById(R.id.cv2);



        databaseReference= database.getReference("All Customer users").child(ownerid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String url = snapshot.child("url").getValue(String.class);
                String id = snapshot.child("iduser").getValue(String.class);
                String branchid = snapshot.child("idbranch").getValue(String.class);


                Picasso.get().load(url).into(ivholder);
                nameholder.setText(name);
                descholder.setText(month+"/"+day+"/"+year+" * "+opentime+"-"+closetime);
                idholder.setText("Appointment ID:"+id);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
