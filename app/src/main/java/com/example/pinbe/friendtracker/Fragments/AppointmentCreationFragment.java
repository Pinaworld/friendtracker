package com.example.pinbe.friendtracker.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pinbe.friendtracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentCreationFragment extends Fragment {


    public AppointmentCreationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointment_creation, container, false);
    }

}
