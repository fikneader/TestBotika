package com.ngeartstudio.testbotika.testbotika.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ngeartstudio.testbotika.testbotika.LoginActivity;
import com.ngeartstudio.testbotika.testbotika.R;
import com.ngeartstudio.testbotika.testbotika.object.DataHobi;

import java.util.ArrayList;

public class HobiListAdapter extends RecyclerView.Adapter<HobiListAdapter.ViewHolder> {
    private Context context;
    private FirebaseUser user;
    private ArrayList<DataHobi> items;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    DatabaseReference myRef;

    public HobiListAdapter(Context context, ArrayList<DataHobi> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hobi, null);
        ViewHolder view = new ViewHolder(layoutView,context);
        return view;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataHobi dataHobi = items.get(position);
        holder.hobi.setText(dataHobi.getHobi());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public Context context;
        public TextView hobi;
        public Button delhobi;

        public ViewHolder(View itemView, final Context context) {
            super(itemView);
            this.context = context;
            hobi = itemView.findViewById(R.id.hobi);
            //delhobi = itemView.findViewById(R.id.delhobi);

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            //get current user
            user = FirebaseAuth.getInstance().getCurrentUser();

            myRef = database.getReference().child("User");
//            DatabaseReference newDatabaseReference = myRef.child(user.getUid()).child("hobi").push();
//            final String dell = newDatabaseReference.getKey();
//            delhobi.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    DataHobi dataHobi = items.get(position);
//                    //final String key2 = myRef.child(user.getUid()).child("hobi").push().getKey();
//                    final String key = myRef.child(user.getUid()).child("hobi").child(dataHobi.getHobi()).toString().trim();
//                    //int lenght = key.length(); //Note this should be function.
//                    String numbers = key.substring(key.lastIndexOf("/")+1);
//                    //Toast.makeText(context, key, Toast.LENGTH_SHORT).show();
//                    myRef.child(user.getUid()).child("hobi").orderByValue().equalTo(numbers).getRef();
//
//                    myRef.child(user.getUid()).child("hobi").getRef().orderByValue().equalTo("test")
//                            .addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                   dataSnapshot.getRef().removeValue(); //used this
//                                    //String tes = dataSnapshot.getRef().getKey().toString().trim();
//                                    //Toast.makeText(context, tes, Toast.LENGTH_SHORT).show();
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//                                }
//                            });
//                }
//            });
        }

    }
}
