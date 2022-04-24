package com.example.petcare;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class AllServicesHolder extends RecyclerView.ViewHolder{

    TextView titleholder,descholder;
    CardView viewall,bg;

    TextView iconholder;


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
        }
        else{
            iconholder.setBackgroundResource(R.drawable.ic_vaccine_icon);
        }




    }
}
