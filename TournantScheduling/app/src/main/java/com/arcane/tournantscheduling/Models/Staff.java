package com.arcane.tournantscheduling.Models;

import java.util.Date;
import java.util.HashMap;



public class Staff {
    String name;
    String id;
    String address;
    String city;
    String state;
    int zip;
    Date created;
    long phone;
    boolean manager;
    HashMap<String,Object> week;
    HashMap<String,Object> days;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public boolean isManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

    public HashMap<String, Object> getWeek() {
        return week;
    }

    public void setWeek(HashMap<String, Object> week) {
        this.week = week;
    }

    public HashMap<String, Object> getDays() {
        return days;
    }

    public void setDays(HashMap<String, Object> days) {
        this.days = days;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
