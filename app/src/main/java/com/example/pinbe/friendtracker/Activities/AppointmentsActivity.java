package com.example.pinbe.friendtracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.pinbe.friendtracker.AppointmentAdapter;
import com.example.pinbe.friendtracker.AppointmentCustomClickListener;
import com.example.pinbe.friendtracker.Fragments.AppointmentCreationFragment;
import com.example.pinbe.friendtracker.Fragments.AppointmentFragment;
import com.example.pinbe.friendtracker.GroupAdapter;
import com.example.pinbe.friendtracker.GroupCustomClickListener;
import com.example.pinbe.friendtracker.Fragments.GroupCreationFragment;
import com.example.pinbe.friendtracker.Fragments.GroupFragment;
import com.example.pinbe.friendtracker.Models.Appointment;
import com.example.pinbe.friendtracker.Models.Group;
import com.example.pinbe.friendtracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.pinbe.friendtracker.Database.Database.getDatabase;

public class AppointmentsActivity extends AppCompatActivity {

    private Button createAppointmentButton;
    private RecyclerView appointmentRecyclerView;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;
    private GroupCustomClickListener clickListener;
    private Fragment currentFragment;
    private Query query;
    private Group group;
    private Boolean haveInitialGroup;
    private  ArrayList<Appointment> appointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appointmentRecyclerView = findViewById(R.id.appointmentRecyclerView);
        appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        createAppointmentButton = findViewById(R.id.goToCreateAppointmentButton);

        Intent intent = getIntent();

        group = (Group) intent.getSerializableExtra("Group");
        appointments = new ArrayList<>();

        mFirebaseInstance = getDatabase();
        mFirebaseDatabase =  mFirebaseInstance.getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(group == null) {
            createAppointmentButton.setVisibility(View.GONE);
            haveInitialGroup = false;
            getAllappointments();
        }
        else{
            haveInitialGroup = true;
            setGroupAppointments(group.getId());
        }

        createAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFragment = new AppointmentCreationFragment();
                ((AppointmentCreationFragment) currentFragment).getGroupAndUserId(group.getId(), userId);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.appointmentFrameLayout, currentFragment)
                        .commit();
            }
        });
    }

    private void setAppointmentFragment(Appointment appointment) {
        currentFragment = new AppointmentFragment();
        ((AppointmentFragment) currentFragment).getAppointment(appointment);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.appointmentFrameLayout, currentFragment)
                .commit();

    }

    private void getAllappointments() {

        Query query = mFirebaseDatabase.child("Group").orderByChild("members");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                try{
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Group group = ds.getValue(Group.class);
                        if(group.getMembersId().contains(userId)){
                            setGroupAppointments(group.getId());
                        }
                    }
                }catch(Exception e){
                    Log.i("ERROR", e.getMessage());
                }

                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setAdapter(){
        appointmentRecyclerView.setAdapter(new AppointmentAdapter(getApplicationContext(), appointments, new AppointmentCustomClickListener() {
            @Override
            public void onAppointmentItemClick(View v, Appointment appointment) {
                setAppointmentFragment(appointment);
            }
        }));
    }

    private void setGroupAppointments(String groupId) {
        Query query = mFirebaseDatabase.child("Appointment").orderByChild("groupId").equalTo(groupId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                try{
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Appointment appointment = ds.getValue(Appointment.class);
                        appointments.add(appointment);
                    }
                }catch(Exception e){
                    Log.i("ERROR", e.getMessage());
                }

                setAdapter();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}