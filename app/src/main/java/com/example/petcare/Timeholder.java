package com.example.petcare;

import android.app.Application;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class Timeholder extends  RecyclerView.ViewHolder{

    TextView time;
    TextView delete;
    ConstraintLayout timecons;

    public Timeholder(@NonNull View itemView) {
        super(itemView);
    }

    public void setTime(Application application, String id,String open,String close,String avail,String timex){

        time = itemView.findViewById(R.id.time);
        delete = itemView.findViewById(R.id.delete);


        time.setText(open+" - "+close);
    }

    public void setTimewalk(Application application, String id,String open,String close,String avail,String timex){

        time = itemView.findViewById(R.id.time);
        timecons = itemView.findViewById(R.id.cltime);


        time.setText(open+" - "+close);
    }

}
