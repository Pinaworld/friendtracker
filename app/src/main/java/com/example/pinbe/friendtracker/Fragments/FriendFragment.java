package com.example.pinbe.friendtracker.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pinbe.friendtracker.Activities.FriendsActivity;
import com.example.pinbe.friendtracker.Models.Appointment;
import com.example.pinbe.friendtracker.Models.Group;
import com.example.pinbe.friendtracker.Models.User;
import com.example.pinbe.friendtracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.pinbe.friendtracker.Database.Database.getDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends Fragment {

    private Button addButton;
    private TextView userInfo;
    private ViewGroup inflatedView;
    private User user;
    private Group group;
    private User currentUser;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;

    public FriendFragment() {
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
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        addButton = inflatedView.findViewById(R.id.addButton);
        userInfo = inflatedView.findViewById(R.id.userInfo);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(group == null){
                    addFriend();
                }
                else{
                    addMember();
                }

            }
        });

        userInfo.setText(user.getFirstname() + " " + user.getLastname());
    }

    private void addMember() {
        ArrayList<String> groupMembersId = group.getMembersId();
        if(groupMembersId == null){
            groupMembersId = new ArrayList<>();
        }
        if(groupMembersId.contains(user.getId())){
            Toast.makeText(getContext(), "Déja membre du groupe !", Toast.LENGTH_SHORT).show();
            return;
        }

        groupMembersId.add(user.getId());
        group.setMembersId(groupMembersId);

        mFirebaseDatabase.child("Group").child(group.getId()).setValue(group);
        ((FriendsActivity)getActivity()).updateGroup(group);
        Toast.makeText(getContext(), "Membre Ajouté", Toast.LENGTH_SHORT).show();
    }

    private void addFriend() {
        ArrayList<String> usersId = currentUser.getFriendsId();
        if(usersId == null){
            usersId = new ArrayList<>();
        }
        if(usersId.contains(user.getId())){
            Toast.makeText(getContext(), "Déja votre amis !", Toast.LENGTH_SHORT).show();
            return;
        }
        usersId.add(user.getId());
        currentUser.setFriendsId(usersId);
        mFirebaseDatabase.child("User").child(userId).setValue(currentUser);
        Toast.makeText(getContext(), "Amis Ajouté", Toast.LENGTH_SHORT).show();
    }

    public void getFriend(User user, Group group, User currentUser){
        this.user = user;
        this.group = group;
        this.currentUser = currentUser;
    }
}
