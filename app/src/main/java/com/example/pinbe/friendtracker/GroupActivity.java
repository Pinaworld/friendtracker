package com.example.pinbe.friendtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class GroupActivity extends AppCompatActivity {

    public GroupActivity() {

    }

    public GroupActivity(String groupeName) {
        TextView groupTitle = findViewById(R.id.GroupTitle);
        groupTitle.setText(groupTitle.getText() + " " + groupeName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
    }

    public void sendModification(View button) {
        final EditText groupNameField = (EditText) findViewById(R.id.Group_name);
        String name = groupNameField.getText().toString();

        final EditText descriptionField = (EditText) findViewById(R.id.Group_description);
        String description = descriptionField.getText().toString();


        // TODO Send modif
    }
}
