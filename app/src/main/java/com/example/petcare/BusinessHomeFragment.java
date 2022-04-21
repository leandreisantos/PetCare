package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class BusinessHomeFragment extends Fragment {

    CardView notif,message,add_walk;
    TextView search;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.business_home_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        notif = getActivity().findViewById(R.id.cv_notif);
        message = getActivity().findViewById(R.id.cv_message);
        search = getActivity().findViewById(R.id.tv_search);
        add_walk = getActivity().findViewById(R.id.cv_add_walk);

        notif.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),BusinessNotificationActivity.class);
            startActivity(intent);
        });

        message.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),BusinessMessageActivty.class);
            startActivity(intent);
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });

        add_walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddWalkinClientActivity.class);
                startActivity(intent);
            }
        });



    }
}
