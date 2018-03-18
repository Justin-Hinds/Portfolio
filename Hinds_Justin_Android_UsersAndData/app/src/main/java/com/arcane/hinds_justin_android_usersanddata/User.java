package com.arcane.hinds_justin_android_usersanddata;


import java.io.Serializable;

class User implements Serializable {
    private String name;
    private String id;
    private int age;



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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


}
