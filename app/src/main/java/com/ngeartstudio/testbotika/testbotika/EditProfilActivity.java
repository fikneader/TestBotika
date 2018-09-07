package com.ngeartstudio.testbotika.testbotika;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.utilities.ParsedUrl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

public class EditProfilActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private DatabaseReference rootReff, userReff;
    private FirebaseUser user;
    private TextView username,email;
    private EditText notelp,asal,jabatan,bio;
    private Button simpan;
    private ImageView dpfoto;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;

    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);


        username = (TextView) findViewById(R.id.username);
        email = (TextView) findViewById(R.id.email);
        notelp = (EditText) findViewById(R.id.notelp);
        asal = (EditText) findViewById(R.id.asal);
        jabatan = (EditText) findViewById(R.id.jabatan);
        bio = (EditText) findViewById(R.id.bio);
        simpan = (Button) findViewById(R.id.simpanprofil);
        dpfoto = (ImageView) findViewById(R.id.dpfoto);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();

        rootReff = FirebaseDatabase.getInstance().getReference();
        userReff = rootReff.child("User");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        String emailku = user.getEmail();
        String usernameku = user.getDisplayName();
        username.setText(usernameku);
        email.setText(emailku);

        //Glide.with(this).load(user.getPhotoUrl()).into(dpfoto);

        userReff.child(user.getUid()).child("notelp").addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                String notelpku = dataSnapshot.getValue(String.class);
                notelp.setText(notelpku);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userReff.child(user.getUid()).child("asal").addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                String asalku = dataSnapshot.getValue(String.class);
                asal.setText(asalku);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userReff.child(user.getUid()).child("jabatan").addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                String jabatanku = dataSnapshot.getValue(String.class);
                jabatan.setText(jabatanku);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userReff.child(user.getUid()).child("bio").addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                String bioku = dataSnapshot.getValue(String.class);
                bio.setText(bioku);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dpfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notelpkuu = notelp.getText().toString().trim();
                userReff.child(user.getUid()).child("notelp").setValue(notelpkuu);

                String asalkuu = asal.getText().toString().trim();
                userReff.child(user.getUid()).child("asal").setValue(asalkuu);

                String pekerjaankuu = jabatan.getText().toString().trim();
                userReff.child(user.getUid()).child("jabatan").setValue(pekerjaankuu);

                String biokuu = bio.getText().toString().trim();
                userReff.child(user.getUid()).child("bio").setValue(biokuu);

                uploadImage();

                startActivity(new Intent(EditProfilActivity.this, MainActivity.class));
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih gambar"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                dpfoto.setImageBitmap(bitmap);
                //Toast.makeText(this, "URI : " + filePath, Toast.LENGTH_SHORT).show();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(filePath)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "User profile updated.");
                                }
                            }
                        });

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Proses mengirim gambar...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/").child(user.getUid());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfilActivity.this, "Gambar berhasil diupload", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfilActivity.this, "Gambar gagal diupload", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Proses upload "+(int)progress+"%");
                        }
                    });
        }
    }
}
