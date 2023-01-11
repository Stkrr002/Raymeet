package com.example.jitsidemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterTime extends RecyclerView.Adapter<AdapterTime.TimeViewHolder> {
    private ArrayList<ClassTime> timeList;

    public AdapterTime(ArrayList<ClassTime> timeList) {
        this.timeList = timeList;

    }


    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.adrv_layout_time, parent, false);
        TimeViewHolder viewHolder = new TimeViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {

        holder.tvTime.setText(timeList.get(position).getTime());
        holder.tvEmail.setText(timeList.get(position).getEmail());
        holder.tvMeetName.setText(timeList.get(position).getMeetName());


    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    public class TimeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime,tvEmail,tvMeetName;

        public TimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime=itemView.findViewById(R.id.tv_time);
            tvEmail=itemView.findViewById(R.id.tv_email);
            tvMeetName=itemView.findViewById(R.id.tv_meet_name);

        }
    }
}
