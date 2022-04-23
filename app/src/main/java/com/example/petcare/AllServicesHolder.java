package com.example.petcare;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class AllServicesHolder extends RecyclerView.ViewHolder{

    TextView titleholder,descholder;
    CardView viewall;

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
}
