package com.example.petcare;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class Walkinholder extends RecyclerView.ViewHolder {

    ImageView ivholder;
    TextView nameholder,serviceholder,idholder;


    public Walkinholder(@NonNull View itemView) {
        super(itemView);
    }
    public void setwalkin(FragmentActivity activity,String id,String idowner,String name,String petname,String weight,String age,String health,String services,
                          String minutes,String time,String date,String url,String start,String end){


        ivholder = itemView.findViewById(R.id.iv);
        nameholder = itemView.findViewById(R.id.tv_name);
        serviceholder = itemView.findViewById(R.id.tv_serv);
        idholder = itemView.findViewById(R.id.tv_id);


        Picasso.get().load(url).into(ivholder);
        nameholder.setText(name);
        serviceholder.setText(services);
        idholder.setText("Appointment ID: "+id);

    }
}
