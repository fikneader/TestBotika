package com.ngeartstudio.testbotika.testbotika;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ngeartstudio.testbotika.testbotika.adapter.HobiListAdapter;
import com.ngeartstudio.testbotika.testbotika.object.DataHobi;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    private View popupInputDialogView;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private FirebaseUser user;
    Firebase firebase;
    DatabaseReference rootReff, userReff, myReff;
    TextView username,email,notelp,asal,jabatan,bio;
    ImageView plushobi,deletehobi,dp;
    RecyclerView listview;
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager;
    LinearLayout mainLayout;
    ArrayList<DataHobi> items;
    EditText hobi;
    Button tambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (TextView) findViewById(R.id.username);
        email = (TextView) findViewById(R.id.email);
        notelp = (TextView) findViewById(R.id.notelp);
        asal = (TextView) findViewById(R.id.asal);
        jabatan = (TextView) findViewById(R.id.jabatan);
        bio = (TextView) findViewById(R.id.bio);
        plushobi = (ImageView) findViewById(R.id.plushobi);
        deletehobi = (ImageView) findViewById(R.id.deletehobi);
        dp = (ImageView) findViewById(R.id.dp);

        Firebase.setAndroidContext(this);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();

        rootReff = FirebaseDatabase.getInstance().getReference();
        userReff = rootReff.child("User");

        myReff = userReff.child("hobi").push();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        listview = (RecyclerView) findViewById(R.id.listhobi);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        listview.setHasFixedSize(false);
        listview.setLayoutManager(linearLayoutManager);

        userReff.child(user.getUid()).child("hobi").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items = new ArrayList<DataHobi>();

                mainLayout.setVisibility(View.VISIBLE);

                int number = 0;
                for(DataSnapshot object : dataSnapshot.getChildren()){
                    items.add(new DataHobi(object.getValue(String.class)));
                    number++;
                }
                //set Listview
                HobiListAdapter adapter = new HobiListAdapter(getApplicationContext(), items);
                listview.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        String emailku = user.getEmail();
        String usernameku = user.getDisplayName();
        username.setText(usernameku);
        email.setText(emailku);
        Glide.with(this).load(user.getPhotoUrl()).into(dp);

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

        plushobi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a AlertDialog Builder.
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                // Set title, icon, can not cancel properties.
                alertDialogBuilder.setTitle("Hobi Pegawai");
                alertDialogBuilder.setIcon(R.drawable.logobotika);
                alertDialogBuilder.setCancelable(true);

                // Init popup dialog view and it's ui controls.
                initPopupViewControls();

                // Set the inflated layout view object to the AlertDialog builder.
                alertDialogBuilder.setView(popupInputDialogView);

                // Create AlertDialog and show.
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                tambah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       String hobiku = hobi.getText().toString().trim();
                        if (TextUtils.isEmpty(hobiku)) {
                            Toast.makeText(getApplicationContext(), "Masukan hobi anda!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        userReff.child(user.getUid()).child("hobi").push().setValue(hobiku);
                        hobi.getText().clear();
                        alertDialog.dismiss();
                    }
                });


            }
        });

        deletehobi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Pemberitahuan");
                alertDialog.setMessage("Hapus Hobi ?");
                alertDialog.setCancelable(false);
                alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE, "Ya",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                userReff.child(user.getUid()).child("hobi").removeValue();
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE, "Tidak",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    private void initPopupViewControls()
    {
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);

        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.input_hobi, null);

        // Get user input edittext and button ui controls in the popup dialog.
        hobi = (EditText) popupInputDialogView.findViewById(R.id.inHobi);
        tambah = (Button) popupInputDialogView.findViewById(R.id.butHobi);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dotmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_logout){
            // do something
            auth.signOut();
        }if(id == R.id.menu_editprofil){
            // do something
            startActivity(new Intent(MainActivity.this, EditProfilActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
