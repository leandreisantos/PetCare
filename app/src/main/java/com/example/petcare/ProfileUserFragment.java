package com.example.petcare;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileUserFragment extends Fragment {

    TextView logout;
    FirebaseAuth mAuth;
    TextView profile,contact,password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profiile_user_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();


        logout = getActivity().findViewById(R.id.logout);
        profile = getActivity().findViewById(R.id.profile);
        contact = getActivity().findViewById(R.id.contact);
        password = getActivity().findViewById(R.id.password);


        logout.setOnClickListener(view -> showlogout());

        profile.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),ProfileSettingsActivity.class);
            startActivity(intent);
        });
        contact.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),ContactActivity.class);
            startActivity(intent);
        });
        password.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(),PasswordActivity.class);
            startActivity(intent);
        });

    }

    private void showlogout() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.logout_layout,null);
        TextView logout_tv = view.findViewById(R.id.logout_tv_ll);
        TextView cancel_tv = view.findViewById(R.id.cancel_tv_ll);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        alertDialog.show();
        logout_tv.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getActivity(),LoginActivity.class));
        });
        cancel_tv.setOnClickListener(v -> alertDialog.dismiss());
    }
}
