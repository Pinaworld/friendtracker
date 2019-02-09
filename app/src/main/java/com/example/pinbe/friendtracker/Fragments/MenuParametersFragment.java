package com.example.pinbe.friendtracker.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.pinbe.friendtracker.Activities.LoginActivity;
import com.example.pinbe.friendtracker.Activities.ParametersActivity;
import com.example.pinbe.friendtracker.Activities.ProfileActivity;
import com.example.pinbe.friendtracker.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuParametersFragment extends Fragment {

    private Button profileButton;
    private Button parametersButton;
    private ImageButton logoutButton;
    private ViewGroup inflatedView;

    public MenuParametersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflatedView = container;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_parameters, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

        profileButton = inflatedView.findViewById(R.id.profileButton);
        parametersButton = inflatedView.findViewById(R.id.parametersButton);
        logoutButton = inflatedView.findViewById(R.id.logoutButton);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfileActivity.class));
            }
        });

        parametersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ParametersActivity.class));
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
                startActivity( new Intent(getContext(), LoginActivity.class));
            }
        });

    }
}
