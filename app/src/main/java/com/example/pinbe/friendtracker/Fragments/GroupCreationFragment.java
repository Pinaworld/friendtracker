package com.example.pinbe.friendtracker.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pinbe.friendtracker.Models.Group;
import com.example.pinbe.friendtracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.pinbe.friendtracker.Database.Database.getDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupCreationFragment extends Fragment {

    private Button createGroupButton;
    private Button deleteGroupButton;
    private EditText groupName;
    private EditText groupeDescription;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth auth;

    private Boolean saved=null;
    private ViewGroup inflatedView;
    private Group group;
    private Boolean viewForModification;
    private String userId;
    private FragmentManager fragmentManager;


    public GroupCreationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflatedView = container;

        return inflater.inflate(R.layout.fragment_group_creation, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentManager = getActivity().getSupportFragmentManager();
    }

    @Override
    public void onStart() {
        super.onStart();

        createGroupButton = inflatedView.findViewById(R.id.createGroupButton);
        deleteGroupButton = inflatedView.findViewById(R.id.deleteGroupButton);
        deleteGroupButton.setVisibility(View.GONE);
        groupName = inflatedView.findViewById(R.id.groupName);
        groupeDescription = inflatedView.findViewById(R.id.groupDescription);

        auth = FirebaseAuth.getInstance();
        mFirebaseInstance = getDatabase(getContext());
        mFirebaseDatabase =  mFirebaseInstance.getReference().child("Group");

        userId = auth.getCurrentUser().getUid();
        
        if(viewForModification == null){
            createGroupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createGroup();
                }
            });
        }
        else{
            deleteGroupButton.setVisibility(View.VISIBLE);
            createGroupButton.setText("Modifier le groupe");
            createGroupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modififyGroup();
                }
            });
            deleteGroupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteGroup();
                }
            });
            setTextViews();
        }
    }

    private void modififyGroup() {
        final String name = groupName.getText().toString();
        final String description = groupeDescription.getText().toString();

        Boolean inputsAreValid = validateInputs(name);

        if(inputsAreValid) {
            group.setName(name);
            group.setDescription(description);

            mFirebaseDatabase.child(group.getId()).setValue(group);
            removeFragment();
            Toast.makeText(getContext(), "Groupe modifié.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteGroup() {
        mFirebaseDatabase.child(group.getId()).removeValue();
        removeFragment();
        Toast.makeText(getContext(), "Groupe supprimé.",
                Toast.LENGTH_SHORT).show();
    }

    private void setTextViews(){
        groupName.setText(group.getName());
        groupeDescription.setText(group.getDescription());
    }

    private void createGroup(){
        String name = groupName.getText().toString();
        String description = groupeDescription.getText().toString();
        ArrayList<String> membersList = new ArrayList<String>();

        membersList.add("");

        if(validateInputs(name)){
            Boolean result =  save(name, description, membersList, userId);
            if(result){
                Toast.makeText(getContext(), "Groupe Créé.",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), "Error during group creation.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean validateInputs(String name){

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "Veuillez indiquer le nom du groupe", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public Boolean save(String name, String description, ArrayList<String> membersList, String userId)
    {
        try
        {
            String groupId = mFirebaseDatabase.push().getKey();

            Group group = new Group(name, description, membersList, userId, groupId);

            ArrayList<String> membersID = new ArrayList<>();

            membersID.add(group.getOwnerId());
            group.setMembersId(membersID);

            mFirebaseDatabase.child(groupId).setValue(group);
            addGroupChangeListener(group);

            saved=true;

            removeFragment();
        }catch (DatabaseException e)
        {
            e.printStackTrace();
            saved=false;
        }
        return saved;
    }

    private void removeFragment() {
        final Fragment fragment = GroupCreationFragment.this;
        fragmentManager.beginTransaction().remove(fragment).commit();
    }

    private void addGroupChangeListener(Group group) {
        // User data change listener
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Group group = dataSnapshot.getValue(Group.class);

                // Check for null
                if (group == null) {
                    Log.e("GROUP_CREATION", "Group data is null!");
                    return;
                }

                Log.e("GROUP_CREATION", "Group data is changed: " + group.getName() + ", " + group.getDescription());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("GROUP_CREATION", "Failed to read user", error.toException());
            }
        });
    }

    public void getGroupForModification(Group group){
        this.group = group;
        viewForModification = true;
    }

}
