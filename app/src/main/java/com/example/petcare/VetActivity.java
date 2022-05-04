package com.example.petcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class VetActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView back;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference;

    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet);

        databaseReference = database.getReference("All Business users vet");
        search = findViewById(R.id.et_search);

        recyclerView = findViewById(R.id.rv);
        back = findViewById(R.id.tv_back);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        back.setOnClickListener(view -> onBackPressed());

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                showRec("s");
            }
        });
    }

    private void showRec(String key) {

        FirebaseRecyclerOptions<AllOwnerUserMember> options = null;

        if (key.equals("n")) {
            options =
                    new FirebaseRecyclerOptions.Builder<AllOwnerUserMember>()
                            .setQuery(databaseReference, AllOwnerUserMember.class)
                            .build();
        } else if (key.equals("s")) {

            String text = search.getText().toString();
            Query search = databaseReference.orderByChild("name").startAt(text).endAt(text + "\uf0ff");
            options =
                    new FirebaseRecyclerOptions.Builder<AllOwnerUserMember>()
                            .setQuery(search, AllOwnerUserMember.class)
                            .build();
        }

        FirebaseRecyclerAdapter<AllOwnerUserMember, vetholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AllOwnerUserMember, vetholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull vetholder holder, int position, @NonNull AllOwnerUserMember model) {


                        holder.setvet(getApplication(),model.getUrl(),model.getName(),model.getMobile(),model.getLandline(),model.getEmail(),model.getWebsite(),
                                model.getAdd(),model.getIduser(),model.getStatus(),model.getStatusshop(),model.getDate(),model.getTime(),model.getPass());

                        String  id = getItem(position).getIduser();


                        holder.cv.setOnClickListener(view -> {
                            Intent intent = new Intent(VetActivity.this,ShowProfileActivity.class);
                            intent.putExtra("id",id);
                            startActivity(intent);
                        });

                    }

                    @NonNull
                    @Override
                    public vetholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.vet_item,parent,false);

                        return new vetholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showRec("n");
    }
}