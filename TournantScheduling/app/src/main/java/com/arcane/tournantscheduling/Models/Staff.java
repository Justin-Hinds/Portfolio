package com.arcane.tournantscheduling.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Staff implements Parcelable {
    String name;
    String id;
    String restaurantID;
    String address;
    String city;
    String state;
    public int zip;
    Date created;
    long phone;
    boolean manager;
    Map<String,Object> week;
    Map<String, Object> days;
    Map<String, Object> availability;
    public Staff(){}
    protected Staff(Parcel in) {
        name = in.readString();
        id = in.readString();
        restaurantID = in.readString();
        address = in.readString();
        city = in.readString();
        state = in.readString();
        zip = in.readInt();
        phone = in.readLong();
        manager = in.readByte() != 0;
    }

    public static final Creator<Staff> CREATOR = new Creator<Staff>() {
        @Override
        public Staff createFromParcel(Parcel in) {
            return new Staff(in);
        }

        @Override
        public Staff[] newArray(int size) {
            return new Staff[size];
        }
    };

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

    public Map<String, Object> getWeek() {
        return week;
    }

    public void setWeek(HashMap<String, Object> week) {
        this.week = week;
    }

    public Map<String, Object> getDays() {
        return days;
    }

    public void setDays(Map<String, Object> days) {
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

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    @Override
    public int describeContents() {
        return 0;
    }



    public Map<String, Object> getAvailability() {
        return availability;
    }

    public void setAvailability(Map<String, Object> availability) {
        this.availability = availability;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(id);
        parcel.writeString(restaurantID);
        parcel.writeString(address);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeInt(zip);
        parcel.writeLong(phone);
        parcel.writeByte((byte) (manager ? 1 : 0));
    }

    @Override
    public boolean equals(Object obj) {
        Staff newStaff = (Staff) obj;
        if(newStaff.getId() == id){
            return true;
        }
        return false;
    }
}
