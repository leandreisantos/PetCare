package com.example.petcare;

import android.app.Application;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class Petholder extends RecyclerView.ViewHolder {

    TextView nameholder,breedholder,ageholder,weightholder,genderholder;
    Switch swi;
    CheckBox cb;
    TextView editholder;


    public Petholder(@NonNull View itemView) {
        super(itemView);
    }


    public void setPet(FragmentActivity activity,String petname,String breed,String weight,String age,String status,String id,String ownerid){
        nameholder = itemView.findViewById(R.id.pet_name);
        breedholder = itemView.findViewById(R.id.pet_breed);
        ageholder = itemView.findViewById(R.id.tv_age);
        weightholder = itemView.findViewById(R.id.tv_weight);
        genderholder = itemView.findViewById(R.id.tv_gender);
        swi = itemView.findViewById(R.id.switch2);
        editholder = itemView.findViewById(R.id.tv_edit);


        nameholder.setText(petname);
        breedholder.setText(breed);
        ageholder.setText("Age:"+age);
        weightholder.setText("Weight:"+weight);


        if(status.equals("health")){
            swi.setChecked(true);
        }else
        {
            swi.setChecked(false);
        }


    }

    public void setPetAppointmet(Application application, String petname, String breed, String weight, String age, String status, String id, String ownerid){
        nameholder = itemView.findViewById(R.id.name);
        breedholder = itemView.findViewById(R.id.price);
        cb = itemView.findViewById(R.id.cb);


        nameholder.setText(petname);
        breedholder.setText(breed);

    }
}
