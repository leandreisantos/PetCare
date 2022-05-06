package com.example.petcare;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AllServicesHolder extends RecyclerView.ViewHolder{

    TextView titleholder,descholder;
    CardView viewall,bg,cvmain;

    TextView iconholder;
    CheckBox cb;
    TextView priceholder;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference;


    public AllServicesHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setService(FragmentActivity activity,String id,String ownerId,String services,String desc,String min,String capacity,
                           String amount,String date,String time,String timeslotid){

        titleholder = itemView.findViewById(R.id.lbl_title);
        descholder = itemView.findViewById(R.id.lbl_desc);
        viewall = itemView.findViewById(R.id.cv_view);


        titleholder.setText(services);
        descholder.setText(desc);


    }

    public void setService(Application application, String id, String ownerId, String services, String desc, String min, String capacity,
                           String amount, String date, String time, String timeslotid){

        titleholder = itemView.findViewById(R.id.tv_title);


        titleholder.setText(services);


    }

    public void setServiceProfile(Application application, String id, String ownerId, String services, String desc, String min, String capacity,
                           String amount, String date, String time, String timeslotid){

        titleholder = itemView.findViewById(R.id.tv_name);
        iconholder = itemView.findViewById(R.id.icon);
        bg = itemView.findViewById(R.id.cv_bg);


        titleholder.setText(services);

        if(services.equals("Consultation")){
            iconholder.setBackgroundResource(R.drawable.ic_consul_icon);
            bg.setBackgroundColor(Color.parseColor("#85DBD9"));
        }else if(services.equals("Vaccination")){
            iconholder.setBackgroundResource(R.drawable.ic_vaccine_icon);
            bg.setBackgroundColor(Color.parseColor("#85DBD9"));
        }else if(services.equals("Nail Trimming")){
            iconholder.setBackgroundResource(R.drawable.ic_nailtrim_icon);
            bg.setBackgroundColor(Color.parseColor("#eda2b2"));
        }else if(services.equals("Eye and Ear Clean")){
            iconholder.setBackgroundResource(R.drawable.ic_nailtrim_icon);
            bg.setBackgroundColor(Color.parseColor("#eda2b2"));
        }else if(services.equals("Bathing")){
            iconholder.setBackgroundResource(R.drawable.ic_bathing_icon);
            bg.setBackgroundColor(Color.parseColor("#eda2b2"));
        }else if(services.equals("Full Groom")){
            iconholder.setBackgroundResource(R.drawable.ic_full_grooming_icon);
            bg.setBackgroundColor(Color.parseColor("#eda2b2"));
        }else if(services.equals("Teeth Brushing")){
            iconholder.setBackgroundResource(R.drawable.ic_teeth_brushing_icon);
            bg.setBackgroundColor(Color.parseColor("#eda2b2"));
        }
        else if(services.equals("Anal Gland Expression")){
            iconholder.setBackgroundResource(R.drawable.ic_anal_expres_icon);
            bg.setBackgroundColor(Color.parseColor("#eda2b2"));
        }
        else{
            iconholder.setBackgroundResource(R.drawable.ic_vaccine_icon);
        }


    }

    public void setServiceAppointment(Application application, String id, String ownerId, String services, String desc, String min, String capacity,
                                  String amount, String date, String time, String timeslotid){

        cb = itemView.findViewById(R.id.cb);
        titleholder = itemView.findViewById(R.id.name);
        priceholder = itemView.findViewById(R.id.price);
        iconholder = itemView.findViewById(R.id.icon);
        bg = itemView.findViewById(R.id.cv);


        titleholder.setText(services);
        priceholder.setText(amount);

        if(services.equals("Consultation")){
            iconholder.setBackgroundResource(R.drawable.ic_consul_icon);
            bg.setBackgroundColor(Color.parseColor("#4AB2AF"));
        }else if(services.equals("Vaccination")){
            iconholder.setBackgroundResource(R.drawable.ic_vaccine_icon);
            bg.setBackgroundColor(Color.parseColor("#4AB2AF"));
        }else if(services.equals("Nail Trimming")){
            iconholder.setBackgroundResource(R.drawable.ic_nailtrim_icon);
            bg.setBackgroundColor(Color.parseColor("#eda2b2"));
        }else if(services.equals("Eye and Ear Clean")){
            iconholder.setBackgroundResource(R.drawable.ic_nailtrim_icon);
            bg.setBackgroundColor(Color.parseColor("#eda2b2"));
        }else if(services.equals("Bathing")){
            iconholder.setBackgroundResource(R.drawable.ic_bathing_icon);
            bg.setBackgroundColor(Color.parseColor("#eda2b2"));
        }else if(services.equals("Full Groom")){
            iconholder.setBackgroundResource(R.drawable.ic_full_grooming_icon);
            bg.setBackgroundColor(Color.parseColor("#eda2b2"));
        }else if(services.equals("Teeth Brushing")){
            iconholder.setBackgroundResource(R.drawable.ic_teeth_brushing_icon);
            bg.setBackgroundColor(Color.parseColor("#eda2b2"));
        }
        else if(services.equals("Anal Gland Expression")){
            iconholder.setBackgroundResource(R.drawable.ic_anal_expres_icon);
            bg.setBackgroundColor(Color.parseColor("#eda2b2"));
        }
        else{
            iconholder.setBackgroundResource(R.drawable.ic_vaccine_icon);
        }


    }

    public void setSelectedServices(Application application,String id,String idowner){

        databaseReference= database.getReference("All services").child(idowner).child(id);

        cb = itemView.findViewById(R.id.cb);
        titleholder = itemView.findViewById(R.id.name);
        priceholder = itemView.findViewById(R.id.price);
        iconholder = itemView.findViewById(R.id.icon);
        bg = itemView.findViewById(R.id.cv);
        cvmain = itemView.findViewById(R.id.cv_main);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String idholder = snapshot.child("id").getValue(String.class);
                String amountholder = snapshot.child("amount").getValue(String.class);
                String serviceholder = snapshot.child("services").getValue(String.class);

                titleholder.setText(serviceholder);
                priceholder.setText(amountholder);
                cb.setChecked(true);

                if(serviceholder.equals("Consultation")){
                    iconholder.setBackgroundResource(R.drawable.ic_consul_icon);
                    bg.setBackgroundColor(Color.parseColor("#4AB2AF"));
                }else if(serviceholder.equals("Vaccination")){
                    iconholder.setBackgroundResource(R.drawable.ic_vaccine_icon);
                    bg.setBackgroundColor(Color.parseColor("#4AB2AF"));
                }else if(serviceholder.equals("Nail Trimming")){
                    iconholder.setBackgroundResource(R.drawable.ic_nailtrim_icon);
                    bg.setBackgroundColor(Color.parseColor("#eda2b2"));
                }else if(serviceholder.equals("Eye and Ear Clean")){
                    iconholder.setBackgroundResource(R.drawable.ic_nailtrim_icon);
                    bg.setBackgroundColor(Color.parseColor("#eda2b2"));
                }else if(serviceholder.equals("Bathing")){
                    iconholder.setBackgroundResource(R.drawable.ic_bathing_icon);
                    bg.setBackgroundColor(Color.parseColor("#eda2b2"));
                }else if(serviceholder.equals("Full Groom")){
                    iconholder.setBackgroundResource(R.drawable.ic_full_grooming_icon);
                    bg.setBackgroundColor(Color.parseColor("#eda2b2"));
                }else if(serviceholder.equals("Teeth Brushing")){
                    iconholder.setBackgroundResource(R.drawable.ic_teeth_brushing_icon);
                    bg.setBackgroundColor(Color.parseColor("#eda2b2"));
                }
                else if(serviceholder.equals("Anal Gland Expression")){
                    iconholder.setBackgroundResource(R.drawable.ic_anal_expres_icon);
                    bg.setBackgroundColor(Color.parseColor("#eda2b2"));
                }
                else{
                    iconholder.setBackgroundResource(R.drawable.ic_vaccine_icon);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
