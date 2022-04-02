package com.example.petcare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OwnerCreateProfile extends AppCompatActivity {

    String statusbundle;
    TextView backholder;
    CardView cviv,cvupload;
    TextView piclogo;
    ImageView imageView;
    Uri imageUridp;
    EditText nameholder,mobileholder,landlineholder,emailholder,webholder,addholder;
    UploadTask uploadTask;
    StorageReference storageReference;

    private static final int PICK_IMAGE=1;

    databaseReference dbr = new databaseReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance(dbr.keyDb());
    DatabaseReference databaseReference,databaseReference2;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();


    AllOwnerUserMember ownerMember;
    AllUserMember member;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_create_profile);

        ownerMember = new AllOwnerUserMember();
        member = new AllUserMember();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            statusbundle = extras.getString("status");
        }else {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }

        documentReference = db.collection("user").document(currentUserId);
        storageReference = FirebaseStorage.getInstance().getReference("Profile images");
        databaseReference = database.getReference("All users");
        databaseReference2 = database.getReference("All Owner users");

        backholder = findViewById(R.id.tv_back);
        cviv = findViewById(R.id.cv1);
        piclogo = findViewById(R.id.pic_logo);
        imageView = findViewById(R.id.iv_pic);
        cvupload = findViewById(R.id.cv2);
        nameholder = findViewById(R.id.et_name);
        mobileholder = findViewById(R.id.et_mobile);
        landlineholder = findViewById(R.id.et_landlinenumber);
        emailholder = findViewById(R.id.et_email);
        webholder = findViewById(R.id.et_website);
        addholder = findViewById(R.id.et_add);

        backholder.setOnClickListener(v -> onBackPressed());

        cviv.setOnClickListener(v -> chooseImage());

        cvupload.setOnClickListener(v -> uploadProfile());

    }

    private void uploadProfile() {
        String name = nameholder.getText().toString();
        String mobile = mobileholder.getText().toString();
        String landline = landlineholder.getText().toString();
        String email = emailholder.getText().toString();
        String web = webholder.getText().toString();
        String add = addholder.getText().toString();

        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(mobile)&&!TextUtils.isEmpty(landline)
            &&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(web)&&!TextUtils.isEmpty(add)&&imageUridp != null){

            final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExt(imageUridp));
            uploadTask = reference.putFile(imageUridp);

            Task<Uri> urlTask = uploadTask.continueWithTask((Task<UploadTask.TaskSnapshot> task) -> {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return reference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                Uri downloadUri = task.getResult();

                Map<String, String> profile = new HashMap<>();
                profile.put("name",name);
                profile.put("mobile",mobile);
                profile.put("url",downloadUri.toString());
                profile.put("landline",landline);
                profile.put("email",email);
                profile.put("web",web);
                profile.put("add",add);
                profile.put("uid",currentUserId);
                profile.put("status","Owner");

                Calendar cdate = Calendar.getInstance();
                SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyy");
                final String savedate = currentdate.format(cdate.getTime());

                Calendar ctime = Calendar.getInstance();
                SimpleDateFormat currenttime =new SimpleDateFormat("HH-mm");
                final String savetime = currenttime.format(ctime.getTime());

                ownerMember.setUrl(downloadUri.toString());
                ownerMember.setName(name);
                ownerMember.setMobile(mobile);
                ownerMember.setLandline(landline);
                ownerMember.setEmail(email);
                ownerMember.setWebsite(web);
                ownerMember.setAdd(add);
                ownerMember.setIduser(currentUserId);
                ownerMember.setStatus("Shop Owner");
                ownerMember.setStatusshop(statusbundle);
                ownerMember.setDate(savedate);
                ownerMember.setTime(savetime);

                member.setUserid(currentUserId);

                databaseReference2.child(currentUserId).setValue(ownerMember);
                databaseReference.child(currentUserId).setValue(member);

                documentReference.set(profile)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(OwnerCreateProfile.this, "Profile Created", Toast.LENGTH_SHORT).show();

                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                Intent intent = new Intent(OwnerCreateProfile.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            },2000);
                        });
            });

        }else{
            Toast.makeText(OwnerCreateProfile.this, "Fill up All Requirements!", Toast.LENGTH_SHORT).show();
        }

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData()!=null){
                imageUridp = data.getData();
                piclogo.setVisibility(View.GONE);
                Picasso.get().load(imageUridp).into(imageView);
            }

        }catch (Exception e){
            Toast.makeText(this, "Error"+e, Toast.LENGTH_SHORT).show();
        }
    }
    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }



}