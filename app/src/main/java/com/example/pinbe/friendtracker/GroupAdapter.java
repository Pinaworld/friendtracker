package com.example.pinbe.friendtracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {

    Context c;
    ArrayList<String> groups;

    public GroupAdapter(Context c, ArrayList<String> groups) {
        this.c = c;
        this.groups = groups;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.recycler_view_row_model,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.nameTxt.setText(groups.get(position));
    }


    @Override
    public int getItemCount() {
        return groups.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        Button nameTxt;

        public MyViewHolder(View itemView) {
            super(itemView);

            nameTxt= itemView.findViewById(R.id.nameTxt);
        }
    }
}
