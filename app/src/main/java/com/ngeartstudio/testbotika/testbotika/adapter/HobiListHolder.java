package com.ngeartstudio.testbotika.testbotika.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngeartstudio.testbotika.testbotika.R;

public class HobiListHolder extends RecyclerView.ViewHolder {
    TextView hobi;
    Button delhobi;
    public HobiListHolder(View itemView) {
        super(itemView);
        hobi = (TextView)itemView.findViewById(R.id.hobi);
        //delhobi = (Button) itemView.findViewById(R.id.delhobi);
    }
}
