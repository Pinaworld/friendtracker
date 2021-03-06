package com.example.pinbe.friendtracker.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {

    public String name;
    public String description;
    public ArrayList<String> membersId;
    public String ownerId;
    public String id;

    public Group(){
    }

    public Group(String name, String description, ArrayList<String> membersId, String ownerId, String id) {
        this.name = name;
        this.description = description;
        this.membersId = membersId;
        this.ownerId = ownerId;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getMembersId() {
        return membersId;
    }

    public void setMembersId(ArrayList<String> membersId) {
        this.membersId = membersId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
