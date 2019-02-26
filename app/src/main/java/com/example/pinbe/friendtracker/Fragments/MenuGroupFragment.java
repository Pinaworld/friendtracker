package com.example.pinbe.friendtracker.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pinbe.friendtracker.Activities.AppointmentsActivity;
import com.example.pinbe.friendtracker.Activities.GroupsActivity;
import com.example.pinbe.friendtracker.R;


public class MenuGroupFragment extends Fragment {

    private Button groupsButton;
    private Button appointmentsButton;
    private ViewGroup inflatedView;

    public MenuGroupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflatedView = container;

        return inflater.inflate(R.layout.fragment_menu_group, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        groupsButton = inflatedView.findViewById(R.id.groupsButton);
        appointmentsButton = inflatedView.findViewById(R.id.appointmentsButton);

        groupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), GroupsActivity.class));
            }
        });

        appointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AppointmentsActivity.class));
            }
        });
    }
}
