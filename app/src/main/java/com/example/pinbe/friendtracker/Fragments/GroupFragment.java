package com.example.pinbe.friendtracker.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pinbe.friendtracker.Activities.AppointmentsActivity;
import com.example.pinbe.friendtracker.Activities.FriendsActivity;
import com.example.pinbe.friendtracker.Activities.GroupsActivity;
import com.example.pinbe.friendtracker.Models.Appointment;
import com.example.pinbe.friendtracker.Models.Group;
import com.example.pinbe.friendtracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.pinbe.friendtracker.Database.Database.getDatabase;

public class GroupFragment extends Fragment {

    private TextView textView;
    private Button addMembersButton;
    private Button viewMembersButton;
    private Button groupAppointmentButton;
    private Button groupMapButton;
    private Button buttonGroupParameters;
    private ArrayList<Appointment> appointments;
    private ViewGroup inflatedView;
    private Group group;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;
    private FragmentManager fragmentManager;


    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFirebaseInstance = getDatabase(getContext());
        mFirebaseDatabase =  mFirebaseInstance.getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Inflate the layout for this fragment
        this.inflatedView = container;
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();


        textView = inflatedView.findViewById(R.id.fragGroupName);
        addMembersButton = inflatedView.findViewById(R.id.buttonGroupAddMembers);
        viewMembersButton = inflatedView.findViewById(R.id.buttonGroupViewMembers);
        groupAppointmentButton = inflatedView.findViewById(R.id.buttonNextAppointments);
        groupMapButton = inflatedView.findViewById(R.id.buttonViewGroupMap);
        buttonGroupParameters = inflatedView.findViewById(R.id.buttonGroupParameters);

        if(!group.getOwnerId().equals(userId)){
            buttonGroupParameters.setVisibility(View.GONE);
        }
        setButtonsOnClickLIsteners();

        textView.setText(group.getName());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentManager = getActivity().getSupportFragmentManager();
    }

    public void getGroup(Group group){
        this.group = group;

    }


    private void setButtonsOnClickLIsteners() {
        addMembersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FriendsActivity.class);
                intent.putExtra("Group", group);
                intent.putExtra("Type", "Ajout de membres");
                intent.putExtra("ViewType", "Search");
                removeFragment();
                startActivity(intent);
            }
        });

        viewMembersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FriendsActivity.class);
                intent.putExtra("Group", group);
                intent.putExtra("Type", "Membres du groupe");
                intent.putExtra("ViewType", "View");
                removeFragment();
                startActivity(intent);
            }
        });

        groupAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), AppointmentsActivity.class);
                intent.putExtra("Group", group);
                removeFragment();
                startActivity(intent);
            }
        });

        buttonGroupParameters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GroupsActivity)getActivity()).setFragmentForModification(group);
            }
        });

        groupMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void removeFragment() {
        final Fragment fragment = GroupFragment.this;
        fragmentManager.beginTransaction().remove(fragment).commit();

    }

}
