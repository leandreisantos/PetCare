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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.admin.v1.Index;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class BusinessCalendarFragment extends Fragment {

    TextView request,accept;
    CardView notifholder;

    RecyclerView recyclerView;
    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference,databaseReference2,databaseReference3,databaseReference4,databaseReference5;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();
    TableRow tb1,tb2;

    NotificationMember notifmember;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.business_calendar_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        notifmember = new NotificationMember();

        databaseReference = database.getReference("All Appointment owner").child(currentUserId);
        databaseReference5 = database.getReference("All Business users").child(currentUserId);
        databaseReference3 = database.getReference("All Notification");

        request = getActivity().findViewById(R.id.tv_request);
        accept = getActivity().findViewById(R.id.tv_accepted);
        notifholder = getActivity().findViewById(R.id.cv_notif);
        tb1 = getActivity().findViewById(R.id.tb_1);
        tb2 = getActivity().findViewById(R.id.tb_2);
        recyclerView = getActivity().findViewById(R.id.rv);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        notifholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "notif", Toast.LENGTH_SHORT).show();
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tb1.setVisibility(View.VISIBLE);
                tb2.setVisibility(View.INVISIBLE);
                showRec();
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tb1.setVisibility(View.INVISIBLE);
                tb2.setVisibility(View.VISIBLE);
                showRecAccept();
            }
        });

    }

    private void showRecAccept() {
        Query query = databaseReference.orderByChild("status").equalTo("Accept");
        FirebaseRecyclerOptions<AppointmentMember> options =
                new FirebaseRecyclerOptions.Builder<AppointmentMember>()
                        .setQuery(query,AppointmentMember.class)
                        .build();

        FirebaseRecyclerAdapter<AppointmentMember, CustomerAppointmentholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AppointmentMember, CustomerAppointmentholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CustomerAppointmentholder holder, int position, @NonNull AppointmentMember model) {

                        holder.setAppointmentB(getActivity(),model.getId(),model.getPetid(),model.getSubtotal(),model.getServicesid(),model.getOwnerid(),
                                model.getBusinessid(),model.getBrachid(),model.getSelecteddate(),model.getOpentime(),model.getClosetime(),model.getTime(),model.getDate(),
                                model.getDay(),model.getYear(),model.getMonth(),model.getStatus());

                        String businessid = getItem(position).getBusinessid();
                        String ownerId = getItem(position).getOwnerid();
                        String subtotal = getItem(position).getSubtotal();
                        String month = getItem(position).getMonth();
                        String year = getItem(position).getYear();
                        String day = getItem(position).getDay();
                        String servicesid = getItem(position).getServicesid();
                        String id = getItem(position).getId();
                        String opentime = getItem(position).getOpentime();
                        String closetime = getItem(position).getClosetime();


                        holder.moreholder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showDialogmore(businessid,subtotal,month,year,day,servicesid);
                            }
                        });

                        holder.cvraccep.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                databaseReference2 = database.getReference("All Appointment owner").child(currentUserId).child(id);
                                databaseReference2.child("status").setValue("Accept");

                                databaseReference4 = database.getReference("All Appointment customer").child(ownerId).child(id);
                                databaseReference4.child("status").setValue("Accept");

                                databaseReference5.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String name = snapshot.child("name").getValue(String.class);

                                        String id1 = databaseReference3.push().getKey();
                                        notifmember.setId(id1);
                                        notifmember.setFrom(businessid);
                                        notifmember.setTo(ownerId);
                                        notifmember.setAppointmentid(id);
                                        notifmember.setMessage("Your appointment V/G "+year+"-"+month+"-"+day+" at "+name+" at "+opentime+"-"+closetime+" has been approved");

                                        databaseReference3.child(ownerId).child(id1).setValue(notifmember);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });
                        holder.cvdecline.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                databaseReference5.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String name = snapshot.child("name").getValue(String.class);

                                        String id1 = databaseReference3.push().getKey();
                                        notifmember.setId(id1);
                                        notifmember.setFrom(businessid);
                                        notifmember.setTo(ownerId);
                                        notifmember.setAppointmentid(id);
                                        notifmember.setMessage("The "+name+" was unable to give you an appointment for "+opentime+"-"+closetime+" on "+month+"/"+day+"/"+year);

                                        databaseReference3.child(ownerId).child(id1).setValue(notifmember);
                                        databaseReference2 = database.getReference("All Appointment owner").child(currentUserId).child(id);
                                        databaseReference2.removeValue();
                                        databaseReference2 = database.getReference("All Appointment customer").child(ownerId).child(id);
                                        databaseReference2.removeValue();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CustomerAppointmentholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.appointment_item_owner,parent,false);

                        return new CustomerAppointmentholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        showRec();
    }

    private void showRec() {
        Query query = databaseReference.orderByChild("status").equalTo("req");
        FirebaseRecyclerOptions<AppointmentMember> options =
                new FirebaseRecyclerOptions.Builder<AppointmentMember>()
                        .setQuery(query,AppointmentMember.class)
                        .build();

        FirebaseRecyclerAdapter<AppointmentMember, CustomerAppointmentholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AppointmentMember, CustomerAppointmentholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CustomerAppointmentholder holder, int position, @NonNull AppointmentMember model) {

                        holder.setAppointmentB(getActivity(),model.getId(),model.getPetid(),model.getSubtotal(),model.getServicesid(),model.getOwnerid(),
                                model.getBusinessid(),model.getBrachid(),model.getSelecteddate(),model.getOpentime(),model.getClosetime(),model.getTime(),model.getDate(),
                                model.getDay(),model.getYear(),model.getMonth(),model.getStatus());

                        String businessid = getItem(position).getBusinessid();
                        String subtotal = getItem(position).getSubtotal();
                        String month = getItem(position).getMonth();
                        String year = getItem(position).getYear();
                        String day = getItem(position).getDay();
                        String servicesid = getItem(position).getServicesid();
                        String id = getItem(position).getId();
                        String ownerId = getItem(position).getOwnerid();
                        String opentime = getItem(position).getOpentime();
                        String closetime = getItem(position).getClosetime();


                        holder.moreholder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showDialogmore(businessid,subtotal,month,year,day,servicesid);
                            }
                        });

                        holder.cvraccep.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                databaseReference2 = database.getReference("All Appointment owner").child(currentUserId).child(id);
                                databaseReference2.child("status").setValue("Accept");

                                databaseReference4 = database.getReference("All Appointment customer").child(ownerId).child(id);
                                databaseReference4.child("status").setValue("Accept");
                                databaseReference5.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String name = snapshot.child("name").getValue(String.class);

                                        String id1 = databaseReference3.push().getKey();
                                        notifmember.setId(id1);
                                        notifmember.setFrom(businessid);
                                        notifmember.setTo(ownerId);
                                        notifmember.setAppointmentid(id);
                                        notifmember.setMessage("Your appointment V/G "+year+"-"+month+"-"+day+" at "+name+" at "+opentime+"-"+closetime+" has been approved");

                                        databaseReference3.child(ownerId).child(id1).setValue(notifmember);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                        holder.cvdecline.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                databaseReference5.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String name = snapshot.child("name").getValue(String.class);

                                        String id1 = databaseReference3.push().getKey();
                                        notifmember.setId(id1);
                                        notifmember.setFrom(businessid);
                                        notifmember.setTo(ownerId);
                                        notifmember.setAppointmentid(id);
                                        notifmember.setMessage("The "+name+" was unable to give you an appointment for "+opentime+"-"+closetime+" on "+month+"/"+day+"/"+year);

                                        databaseReference3.child(ownerId).child(id1).setValue(notifmember);
                                        databaseReference2 = database.getReference("All Appointment owner").child(currentUserId).child(id);
                                        databaseReference2.removeValue();
                                        databaseReference2 = database.getReference("All Appointment customer").child(ownerId).child(id);
                                        databaseReference2.removeValue();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CustomerAppointmentholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.appointment_item_owner,parent,false);

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
