package com.arcane.tournantscheduling.Models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.HashMap;

public class Restaurant implements Parcelable{
    String name;
    String state;
    String address;
    String city;
    int zip;
    long phone;
    Date created;
    HashMap<String,Object> staff;

    public Restaurant(){}
    protected Restaurant(Parcel in) {
        name = in.readString();
        state = in.readString();
        address = in.readString();
        city = in.readString();
        zip = in.readInt();
        phone = in.readInt();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public HashMap<String, Object> getStaff() {
        return staff;
    }

    public void setStaff(HashMap<String, Object> staff) {
        this.staff = staff;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(state);
        parcel.writeString(address);
        parcel.writeString(city);
        parcel.writeInt(zip);
        parcel.writeLong(phone);
    }
}
