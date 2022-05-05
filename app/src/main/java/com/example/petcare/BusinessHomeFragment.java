package com.example.petcare;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BusinessHomeFragment extends Fragment {

    CardView notif,message,add_walk;
    TextView search,dateholder;
    RecyclerView recyclerView;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();

    CardView check;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.business_home_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        databaseReference = database.getReference("All walkin").child(currentUserId);

        notif = getActivity().findViewById(R.id.cv_notif);
        message = getActivity().findViewById(R.id.cv_message);
        search = getActivity().findViewById(R.id.tv_search);
        add_walk = getActivity().findViewById(R.id.cv_add_walk);
        dateholder = getActivity().findViewById(R.id.datenow);
        check = getActivity().findViewById(R.id.check_cv);

        recyclerView = getActivity().findViewById(R.id.rv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        notif.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),BusinessNotificationActivity.class);
            startActivity(intent);
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
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

    private void show() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.terms_condition_layout,null);
        CheckBox cb = view.findViewById(R.id.cb_agree);
        TextView create = view.findViewById(R.id.tv_create);


        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        alertDialog.show();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb.isChecked()){
                    Toast.makeText(getActivity(), "yes", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "no", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        Calendar cdate = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyy");
        final String savedate = currentdate.format(cdate.getTime());


        dateholder.setText(savedate);

        FirebaseRecyclerOptions<AllWalkinMember> options =
                new FirebaseRecyclerOptions.Builder<AllWalkinMember>()
                        .setQuery(databaseReference,AllWalkinMember.class)
                        .build();

        FirebaseRecyclerAdapter<AllWalkinMember, Walkinholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AllWalkinMember, Walkinholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Walkinholder holder, int position, @NonNull AllWalkinMember model) {


                        holder.setwalkin(getActivity(),model.getId(),model.getIdowner(),model.getName(),model.getPetname(),model.getWeight(),model.getAge(),
                                model.getHealth(),model.getServices(),model.getMinutes(),model.getTime(),model.getDate(),model.getUrl(),model.getStart(),model.getEnd());





                    }

                    @NonNull
                    @Override
                    public Walkinholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.walkinsched_item,parent,false);

                        return new Walkinholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);





    }
}
