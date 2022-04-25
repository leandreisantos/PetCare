package com.example.petcare;

import android.app.Application;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Messageholder extends RecyclerView.ViewHolder {


    CardView cvreceiver;
    CardView cvsender;
    TextView receivermessageholder;
    TextView sendermessageholder;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();



    public Messageholder(@NonNull View itemView) {
        super(itemView);
    }

    public void SetMessage(Application application,String message,String time,String date,String senderuid,String receiveruid){

        cvreceiver = itemView.findViewById(R.id.cv_receiver);
        cvsender = itemView.findViewById(R.id.cv_sender);
        receivermessageholder = itemView.findViewById(R.id.tv_message_receiver);
        sendermessageholder = itemView.findViewById(R.id.tv_message_sender);

        if(currentUserId.equals(senderuid)){
            cvreceiver.setVisibility(View.GONE);
            cvsender.setVisibility(View.VISIBLE);
            sendermessageholder.setText(message);
        }else{
            cvsender.setVisibility(View.GONE);
            cvreceiver.setVisibility(View.VISIBLE);
            receivermessageholder.setText(message);
        }
    }
}
