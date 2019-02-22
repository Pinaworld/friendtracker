package com.example.pinbe.friendtracker;

import android.view.View;

import com.example.pinbe.friendtracker.Models.Appointment;
import com.example.pinbe.friendtracker.Models.Group;

public interface AppointmentCustomClickListener {
    public void onAppointmentItemClick(View v, Appointment appointment);
}
