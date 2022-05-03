package com.example.petcare;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PetFragment extends Fragment {

    CardView addpet;
    boolean checkchoose = false;
    String status;

    ConstraintLayout clmessage,clnotif;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference;

    AllPetMember petMember;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();

    RecyclerView recyclerView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.petfragment_layout,container,false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        databaseReference= database.getReference("All pet").child(currentUserId);


        petMember = new AllPetMember();

        addpet = getActivity().findViewById(R.id.cv_addpet);
        clmessage = getActivity().findViewById(R.id.cl_message);
        clnotif = getActivity().findViewById(R.id.cl_notif);

        addpet = getActivity().findViewById(R.id.cv_addpet);
        recyclerView = getActivity().findViewById(R.id.rv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        addpet.setOnClickListener(view -> showaddPet());

        clmessage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),BusinessMessageActivty.class);
            startActivity(intent);
        });
        clnotif.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),BusinessNotificationActivity.class);
            startActivity(intent);
        });


    }

    private void showaddPet() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.addpet_dialog,null);


        EditText name = view.findViewById(R.id.et_name);
        EditText breed = view.findViewById(R.id.et_breed);
        EditText weight = view.findViewById(R.id.et_weight);
        EditText age = view.findViewById(R.id.et_age);
        RadioButton health = view.findViewById(R.id.rb_healthy);
        RadioButton unhealth = view.findViewById(R.id.rb_unhealthy);
        CardView done = view.findViewById(R.id.cv);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        alertDialog.show();


        health.setOnClickListener(view1 -> {
            status = "health";
            checkchoose = true;
        });
        unhealth.setOnClickListener(view12 -> {
            status = "unhealthy";
            checkchoose = true;
        });





        done.setOnClickListener(view13 -> {
            String tempname = name.getText().toString();
            String tempbreed = breed.getText().toString();
            String tempweight = weight.getText().toString();
            String tempage = age.getText().toString();

            if(!TextUtils.isEmpty(tempname)&&!TextUtils.isEmpty(tempbreed)&&!TextUtils.isEmpty(tempweight)&&!TextUtils.isEmpty(tempage)&&checkchoose==true){

                String id1 = databaseReference.push().getKey();
                petMember.setPetname(tempname);
                petMember.setBreed(tempbreed);
                petMember.setWeight(tempweight);
                petMember.setAge(tempage);
                petMember.setStatus(status);
                petMember.setId(id1);
                petMember.setOwnerid(currentUserId);

                databaseReference.child(id1).setValue(petMember);
                Toast.makeText(getActivity(), "Pet Added", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }else{
                Toast.makeText(getActivity(), "Please fill up all requirements", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<AllPetMember> options =
                new FirebaseRecyclerOptions.Builder<AllPetMember>()
                        .setQuery(databaseReference,AllPetMember.class)
                        .build();

        FirebaseRecyclerAdapter<AllPetMember, Petholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AllPetMember, Petholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Petholder holder, int position, @NonNull AllPetMember model) {


                        holder.setPet(getActivity(),model.getPetname(),model.getBreed(),model.getWeight(),model.getAge(),model.getStatus(),model.getId(),model.getOwnerid());

                    }

                    @NonNull
                    @Override
                    public Petholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.petitem,parent,false);

                        return new Petholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);



    }
}
