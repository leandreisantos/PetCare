package com.example.petcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class homeFragment extends Fragment
{

    ConstraintLayout groomingholder;
    FirebaseAuth mAuth;

    CardView cvmessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment_layout,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        groomingholder = getActivity().findViewById(R.id.grooming);
        cvmessage = getActivity().findViewById(R.id.cv_message);

        groomingholder.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),GroomingActivity.class);
            startActivity(intent);
        });

        cvmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),BusinessMessageActivty.class);
                startActivity(intent);
            }
        });
    }
}
