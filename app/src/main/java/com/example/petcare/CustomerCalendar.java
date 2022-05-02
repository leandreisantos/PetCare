package com.example.petcare;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerCalendar extends Fragment {

    RecyclerView recyclerView;
    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference,databaseReference2;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();

    TableRow tb1,tb2;
    TextView request,accept;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.customer_calendar_layout,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        databaseReference = database.getReference("All Appointment customer").child(currentUserId);

        recyclerView = getActivity().findViewById(R.id.rv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        tb1 = getActivity().findViewById(R.id.tb_1);
        tb2 = getActivity().findViewById(R.id.tb_2);
        request = getActivity().findViewById(R.id.tv_request);
        accept = getActivity().findViewById(R.id.tv_accepted);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecRequest();
                tb1.setVisibility(View.VISIBLE);
                tb2.setVisibility(View.INVISIBLE);
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tb1.setVisibility(View.INVISIBLE);
                tb2.setVisibility(View.VISIBLE);
            }
        });

    }

    private void showRecRequest() {
        FirebaseRecyclerOptions<AppointmentMember> options =
                new FirebaseRecyclerOptions.Builder<AppointmentMember>()
                        .setQuery(databaseReference,AppointmentMember.class)
                        .build();

        FirebaseRecyclerAdapter<AppointmentMember, CustomerAppointmentholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AppointmentMember, CustomerAppointmentholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CustomerAppointmentholder holder, int position, @NonNull AppointmentMember model) {

                        holder.setAppointment(getActivity(),model.getId(),model.getPetid(),model.getSubtotal(),model.getServicesid(),model.getOwnerid(),
                                model.getBusinessid(),model.getBrachid(),model.getSelecteddate(),model.getOpentime(),model.getClosetime(),model.getTime(),model.getDate(),
                                model.getDay(),model.getYear(),model.getMonth(),model.getStatus());

                        String businessid = getItem(position).getBusinessid();
                        String subtotal = getItem(position).getSubtotal();
                        String month = getItem(position).getMonth();
                        String year = getItem(position).getYear();
                        String day = getItem(position).getDay();
                        String servicesid = getItem(position).getServicesid();


                        holder.moreholder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showDialogmore(businessid,subtotal,month,year,day,servicesid);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CustomerAppointmentholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.appointment_item,parent,false);

                        return new CustomerAppointmentholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AppointmentMember> options =
                new FirebaseRecyclerOptions.Builder<AppointmentMember>()
                        .setQuery(databaseReference,AppointmentMember.class)
                        .build();

        FirebaseRecyclerAdapter<AppointmentMember, CustomerAppointmentholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AppointmentMember, CustomerAppointmentholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CustomerAppointmentholder holder, int position, @NonNull AppointmentMember model) {

                        holder.setAppointment(getActivity(),model.getId(),model.getPetid(),model.getSubtotal(),model.getServicesid(),model.getOwnerid(),
                                model.getBusinessid(),model.getBrachid(),model.getSelecteddate(),model.getOpentime(),model.getClosetime(),model.getTime(),model.getDate(),
                                model.getDay(),model.getYear(),model.getMonth(),model.getStatus());

                        String businessid = getItem(position).getBusinessid();
                        String subtotal = getItem(position).getSubtotal();
                        String month = getItem(position).getMonth();
                        String year = getItem(position).getYear();
                        String day = getItem(position).getDay();
                        String servicesid = getItem(position).getServicesid();


                        holder.moreholder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showDialogmore(businessid,subtotal,month,year,day,servicesid);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CustomerAppointmentholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.appointment_item,parent,false);

                        return new CustomerAppointmentholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void showDialogmore(String businessid, String subtotal, String month, String year, String day, String servicesid) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.more_appointment_dialog,null);
        TextView nameholder = view.findViewById(R.id.tv_name);
        ImageView ivholder = view.findViewById(R.id.iv);
        ImageView addholder = view.findViewById(R.id.tv_add);
        ImageView numberholder = view.findViewById(R.id.tv_number);

        databaseReference2= database.getReference("All Business users").child(businessid);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
        alertDialog.show();


    }
}
