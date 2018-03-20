package com.arcane.tournantscheduling.Models;

import java.util.HashMap;



public class Staff {
    String name;
    String id;
    String address;
    int phone;
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

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
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
}
