package com.example.pinbe.friendtracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pinbe.friendtracker.Models.Group;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {

    Context c;
    ArrayList<Group> groups;
    CustomClickListener listener;

    public GroupAdapter(Context c, ArrayList<Group> groups, CustomClickListener listener) {
        this.c = c;
        this.groups = groups;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.recycler_view_row_model,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Group group = groups.get(position);
        holder.nameTxt.setText(group.getName());
        holder.nameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, group);
            }
        });
    }


    @Override
    public int getItemCount() {
        return groups.size();
    }

    class MyViewHolder extends ViewHolder {
        Button nameTxt;

        public MyViewHolder(View itemView) {
            super(itemView);

            nameTxt= itemView.findViewById(R.id.nameTxt);
        }
    }
}
