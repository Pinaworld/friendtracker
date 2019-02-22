package com.example.pinbe.friendtracker.Models;

import java.io.Serializable;
import java.util.Date;

public class Appointment implements Serializable {

    private String name;
    private String address;
    private int postalCode;
    private String city;
    private String description;
    private String groupId;
    private Date appointmentDate;

    public Appointment() {
    }

    public Appointment(String name, String address, int postalCode, String city, String description, Date appointmentDate, String groupId) {
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.description = description;
        this.groupId = groupId;
        this.appointmentDate = appointmentDate;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
