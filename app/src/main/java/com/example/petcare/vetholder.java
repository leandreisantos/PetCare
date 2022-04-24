package com.example.petcare;

import android.app.Application;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class vetholder extends RecyclerView.ViewHolder {

    TextView nameholder,locationholder;
    CardView cv;

    public vetholder(@NonNull View itemView) {
        super(itemView);
    }

    public void setvet(Application application, String url, String name, String mobile, String landline, String email, String website, String add,
                       String iduser, String status, String stastusshop, String date, String time, String pass){

        nameholder = itemView.findViewById(R.id.vet_item);
        locationholder = itemView.findViewById(R.id.tv_add);
        cv = itemView.findViewById(R.id.cv1);

        nameholder.setText(name);
        locationholder.setText(add);

    }
}
