package com.example.pinbe.friendtracker.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pinbe.friendtracker.Activities.AppointmentsActivity;
import com.example.pinbe.friendtracker.Activities.FriendsActivity;
import com.example.pinbe.friendtracker.R;

import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFriendsFragment extends Fragment {

    private Button friendsButton;
    private Button addFriendButton;
    private ViewGroup inflatedView;

    public MenuFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflatedView = container;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_friends, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        friendsButton = inflatedView.findViewById(R.id.friendsButton);
        addFriendButton = inflatedView.findViewById(R.id.friendAddingButton);

        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FriendsActivity.class);
                intent.putExtra("Type", "Mes amis");
                intent.putExtra("ViewType", "View");
                startActivity(intent);
            }
        });

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FriendsActivity.class);
                intent.putExtra("Type", "Recherche d'amis");
                intent.putExtra("ViewType", "Search");
                startActivity(intent);
            }
        });
    }
}
