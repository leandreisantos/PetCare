package com.example.petcare;

import android.app.Application;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class notificationholder extends RecyclerView.ViewHolder {

    TextView messageholder;

    public notificationholder(@NonNull View itemView) {
        super(itemView);
    }

    public void setNotification(Application application,String id,String from,String to,String appointmentid,String message){

        messageholder = itemView.findViewById(R.id.text_message);

        messageholder.setText(message);
    }
}
