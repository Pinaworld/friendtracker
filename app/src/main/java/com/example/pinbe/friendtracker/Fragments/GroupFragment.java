package com.example.pinbe.friendtracker.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pinbe.friendtracker.Models.Group;
import com.example.pinbe.friendtracker.R;

public class GroupFragment extends Fragment {

    private TextView textView;
    private ViewGroup inflatedView;
    private Group group;


    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflatedView = container;
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        textView = inflatedView.findViewById(R.id.fragGroupName);
        textView.setText(group.getName());
    }

    public void getGroup(Group group){
        this.group = group;

    }
}
