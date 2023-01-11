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

import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {
    private List<ClassHistory> classHistoryList;

    public AdapterHistory(List<ClassHistory> classHistoryList) {
        this.classHistoryList = classHistoryList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.rv_layout_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

       holder.tvDate.setText(classHistoryList.get(position).getDates());
       holder.btnDate.setText(position+"");
       holder.llHistory.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(v.getContext(), ""+position+"", Toast.LENGTH_SHORT).show();
           }
       });

        AdapterTime adapter = new AdapterTime(classHistoryList.get(position).getTimeList());
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.recyclerView.getContext()));
        holder.recyclerView.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return classHistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         TextView tvDate;
         Button btnDate;
         LinearLayout llHistory;
          RecyclerView recyclerView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=itemView.findViewById(R.id.tv_date);
            btnDate=itemView.findViewById(R.id.btn_date);
            llHistory=itemView.findViewById(R.id.ll_history);
            recyclerView=itemView.findViewById(R.id.rv_time);
        }
    }
}
