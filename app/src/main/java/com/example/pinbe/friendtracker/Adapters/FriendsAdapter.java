package com.example.pinbe.friendtracker.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pinbe.friendtracker.Interfaces.UserCustomClickListener;
import com.example.pinbe.friendtracker.Models.User;
import com.example.pinbe.friendtracker.R;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.MyViewHolder> {

    Context c;
    ArrayList<User> users;
    UserCustomClickListener listener;

    public FriendsAdapter(Context c, ArrayList<User> users, UserCustomClickListener listener) {
        this.c = c;
        this.users = users;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.recycler_view_row_model,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final User user = users.get(position);
        holder.nameTxt.setText(user.getLastname() + " " +  user.getFirstname());
        holder.nameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUserItemClick(v, user);
            }
        });
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    class MyViewHolder extends ViewHolder {
        Button nameTxt;

        public MyViewHolder(View itemView) {
            super(itemView);

            nameTxt= itemView.findViewById(R.id.nameTxt);
        }
    }
}
