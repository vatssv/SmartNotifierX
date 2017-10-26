package com.smartnotifierx.android.smartnotifierx;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import java.util.List;
import java.util.Random;

/**
 * Created by sv300_000 on 12-Jul-17.
 */

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.MyViewHolder> {

    private List<ContactDetails> logList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView nameView,dateView;
        public ImageView imageView;
        public MyViewHolder(View view){
            super(view);
            nameView = view.findViewById(R.id.name);
            dateView = view.findViewById(R.id.date);
            imageView = view.findViewById(R.id.image_view);
        }
    }

    public LogAdapter(List<ContactDetails> logList){
        this.logList = logList;
    }

    @Override
    public LogAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LogAdapter.MyViewHolder holder, int position) {
        ContactDetails log = logList.get(position);
        String contactImage;
        Random rand = new Random();
        int color;
        if(log.getContactName()!=null){
            holder.nameView.setText(log.getContactName());
            contactImage = Character.toString(log.getContactName().charAt(0));
            contactImage = contactImage.toUpperCase();
            color = Color.argb(255,rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));
        }

        else
        {
            holder.nameView.setText(log.getName());
            contactImage = "X";
            color = Color.argb(255,51,51,51);
        }
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(contactImage,color);
        holder.imageView.setImageDrawable(drawable);
        holder.dateView.setText(log.getDate());

    }

    @Override
    public int getItemCount() {
        return logList.size();
    }
}
