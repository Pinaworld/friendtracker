package com.example.pinbe.friendtracker.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pinbe.friendtracker.Activities.MapsActivity;
import com.example.pinbe.friendtracker.Constants;
import com.example.pinbe.friendtracker.Models.Appointment;
import com.example.pinbe.friendtracker.Models.Group;
import com.example.pinbe.friendtracker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.pinbe.friendtracker.Database.Database.getDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends Fragment {

    private TextView appointmentName;
    private TextView appointmentGroupName;
    private TextView appointmentAddress;
    private TextView appointmentDateTime;
    private Button buttonFindItinerary;

    private ViewGroup inflatedView;

    private Group group;
    private String groupId;
    private Appointment appointment;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    public AppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFirebaseInstance = getDatabase(inflater.getContext());
        mFirebaseDatabase =  mFirebaseInstance.getReference();

        // Inflate the layout for this fragment
        this.inflatedView = container;
        return inflater.inflate(R.layout.fragment_appointment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        appointmentName = inflatedView.findViewById(R.id.fragAppointmentName);
        appointmentGroupName = inflatedView.findViewById(R.id.fragAppointmentGroupName);
        appointmentAddress = inflatedView.findViewById(R.id.fragAppointmentAddress);
        appointmentDateTime = inflatedView.findViewById(R.id.fragAppointmentDateTime);
        buttonFindItinerary = inflatedView.findViewById(R.id.buttonFindItinerary);

        Query query = mFirebaseDatabase.child("Group").orderByChild("id").equalTo(groupId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        group = ds.getValue(Group.class);

                    }
                }catch(Exception e){
                    Log.i("ERROR", e.getMessage());
                }

                setTextViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonFindItinerary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = appointment.getAddress() + ", " + appointment.getPostalCode() + " " + appointment.getCity() + ", France\n";

                Intent intent = new Intent(Constants.ITINERARY_TASK);
                intent.putExtra("destination", address);
                getContext().sendBroadcast(intent);

            }
        });
    }

    private void setTextViews() {
        appointmentName.setText(appointment.getName());
        appointmentGroupName.setText(group.getName());
        appointmentAddress.setText(appointment.getAddress() + " " + appointment.getPostalCode() + " " + appointment.getCity());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = appointment.getAppointmentDate();
        String datStr = dateFormat.format(date);
        appointmentDateTime.setText(datStr);
    }

    public void getAppointment(Appointment appointment){
        this.appointment = appointment;
        this.groupId = appointment.getGroupId();
    }

}
