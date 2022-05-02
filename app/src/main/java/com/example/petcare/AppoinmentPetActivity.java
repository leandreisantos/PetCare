package com.example.petcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AppoinmentPetActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();

    RecyclerView recyclerView;
    CardView cv;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference,databaseReference3;

    String ownerbundle,serviceidbundle,subtotalbundle,bundleidbranch;
    ArrayList<String> mylist = new ArrayList<String>();

    AllpetSelectedMember member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoinment_pet);

        member = new AllpetSelectedMember();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            ownerbundle = extras.getString("owner");
            serviceidbundle = extras.getString("idservices");
            subtotalbundle = extras.getString("subtotal");
            bundleidbranch = extras.getString("branchid");
        }else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }


        databaseReference= database.getReference("All pet").child(currentUserId);
        databaseReference3= database.getReference("All selected pet").child(currentUserId);
        recyclerView = findViewById(R.id.rv);
        cv = findViewById(R.id.cv_next);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!mylist.isEmpty()){

                    String id1 = databaseReference3.push().getKey();


                    for(int i=0;i<mylist.size();i++){
//                    Toast.makeText(this, mylist.get(i), Toast.LENGTH_SHORT).show();
                        member.setId(mylist.get(i));
                        databaseReference3.child(serviceidbundle).child(mylist.get(i)).setValue(member);
                    }

                    Intent intent = new Intent(AppoinmentPetActivity.this,DateAppointmentActivity.class);
                    intent.putExtra("owner",ownerbundle);
                    intent.putExtra("idservices",serviceidbundle);
                    intent.putExtra("subtotal",subtotalbundle);
                    intent.putExtra("petid",id1);
                    intent.putExtra("branchid",bundleidbranch);
                    startActivity(intent);
                }else{
                    Toast.makeText(AppoinmentPetActivity.this, "Select pet", Toast.LENGTH_SHORT).show();
                }


//
//                Intent intent = new Intent(AppoinmentPetActivity.this,DateAppointmentActivity.class);
//                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AllPetMember> options =
                new FirebaseRecyclerOptions.Builder<AllPetMember>()
                        .setQuery(databaseReference,AllPetMember.class)
                        .build();

        FirebaseRecyclerAdapter<AllPetMember, Petholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AllPetMember, Petholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Petholder holder, int position, @NonNull AllPetMember model) {


                        holder.setPetAppointmet(getApplication(),model.getPetname(),model.getBreed(),model.getWeight(),model.getAge(),model.getStatus(),model.getId(),model.getOwnerid());
                        String  id = getItem(position).getId();
                        holder.cb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(holder.cb.isChecked()){
                                    mylist.add(id);
                                }else{
                                    mylist.remove(id);
                                }

//                                mylist.add(id);
//                                Toast.makeText(RequestAppointmentActivity.this, Arrays.toString(mylist.toArray()), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public Petholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.pet_appoinment_item,parent,false);

                        return new Petholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}