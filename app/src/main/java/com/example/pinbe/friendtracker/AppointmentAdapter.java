package com.example.pinbe.friendtracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pinbe.friendtracker.Models.Appointment;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder> {

    Context c;
    ArrayList<Appointment> appointments;
    AppointmentCustomClickListener listener;

    public AppointmentAdapter(Context c, ArrayList<Appointment> appointments, AppointmentCustomClickListener listener) {
        this.c = c;
        this.appointments = appointments;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.recycler_view_row_model,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Appointment appointment = appointments.get(position);
        holder.nameTxt.setText(appointment.getName() + ": " + appointment.getAppointmentDate() + " - " + appointment.getCity());

        holder.nameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAppointmentItemClick(v, appointment);
            }
        });
    }


    @Override
    public int getItemCount() {
        return appointments.size();
    }

    class MyViewHolder extends ViewHolder {
        Button nameTxt;

        public MyViewHolder(View itemView) {
            super(itemView);

            nameTxt= itemView.findViewById(R.id.nameTxt);
        }
    }
}
