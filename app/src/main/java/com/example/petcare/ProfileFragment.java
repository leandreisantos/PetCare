package com.example.petcare;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    TextView logoutholder;
    FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentid = user.getUid();

    ImageView ivholder;
    TextView namholder,contactholder,addressholder,emailholder;
    ImageView pet,addpet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment_layout,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        logoutholder = getActivity().findViewById(R.id.tv_logout_pf);
        namholder = getActivity().findViewById(R.id.tv_name_p);
        ivholder = getActivity().findViewById(R.id.iv_pic);
        contactholder = getActivity().findViewById(R.id.tv_contact_p);
        addressholder = getActivity().findViewById(R.id.tv_address_p);
        emailholder = getActivity().findViewById(R.id.tv_email_p);
        pet = getActivity().findViewById(R.id.tv_pet);
        addpet = getActivity().findViewById(R.id.tv_addpet);

        mAuth = FirebaseAuth.getInstance();


        logoutholder.setOnClickListener(v -> showlogout());

        pet.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),AllPetActivity.class);
            startActivity(intent);
        });
        addpet.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),CreateNewPetActivity.class);
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

    @Override
    public void onStart() {
        super.onStart();

        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        reference = firestore.collection("user").document(currentid);

        reference.get()
                .addOnCompleteListener(task -> {
                    if(task.getResult().exists()){

                        String nameResult = task.getResult().getString("name");
                        String contacResult = task.getResult().getString("mobile");
                        String addreesResult = task.getResult().getString("add");
                        String urlResult = task.getResult().getString("url");
                        String emailResult = task.getResult().getString("email");

                        Picasso.get().load(urlResult).into(ivholder);
                        namholder.setText(nameResult);
                        contactholder.setText(contacResult);
                        addressholder.setText(addreesResult);
                        emailholder.setText(emailResult);

                    }else{
                        Intent intent = new Intent(getActivity(),CreateProfileActivity.class);
                        startActivity(intent);
                    }
                });

    }
}
