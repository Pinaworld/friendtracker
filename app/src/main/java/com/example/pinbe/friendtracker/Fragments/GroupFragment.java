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

import com.example.pinbe.friendtracker.Activities.AppointmentsActivity;
import com.example.pinbe.friendtracker.Models.Appointment;
import com.example.pinbe.friendtracker.Models.Group;
import com.example.pinbe.friendtracker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupFragment extends Fragment {

    private TextView textView;
    private Button addMembersButton;
    private Button groupAppointmentButton;
    private Button groupMapButton;
    private Button buttonGroupParameters;
    private ArrayList<Appointment> appointments;
    private ViewGroup inflatedView;
    private Group group;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;


    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase =  mFirebaseInstance.getReference();
        // Inflate the layout for this fragment
        this.inflatedView = container;
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();


        textView = inflatedView.findViewById(R.id.fragGroupName);
        addMembersButton = inflatedView.findViewById(R.id.buttonGroupAddMembers);
        groupAppointmentButton = inflatedView.findViewById(R.id.buttonNextAppointments);
        groupMapButton = inflatedView.findViewById(R.id.buttonViewGroupMap);
        groupMapButton = inflatedView.findViewById(R.id.buttonGroupParameters);

        setButtonsOnClickLIsteners();

        textView.setText(group.getName());
    }

    public void getGroup(Group group){
        this.group = group;

    }

    private void setButtonsOnClickLIsteners() {
        addMembersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        groupAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), AppointmentsActivity.class);
                intent.putExtra("Group", group);

                startActivity(intent);
            }
        });

        groupMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        groupMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
