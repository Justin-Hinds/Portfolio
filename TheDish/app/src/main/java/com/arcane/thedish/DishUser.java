package com.arcane.thedish;


import java.io.Serializable;
import java.util.HashMap;

public class DishUser implements Serializable {
    private String name;
    private String id;
    private String gamerTag;
    private String psnID;
    private String profilePicURL;
    private String preferredConsole;
    private HashMap<String,Object> fellowPlayers = new HashMap<>();

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

    public String getGamerTag() {
        return gamerTag;
    }

    public void setGamerTag(String gamerTag) {
        this.gamerTag = gamerTag;
    }

    public String getPsnID() {
        return psnID;
    }

    public void setPsnID(String psnID) {
        this.psnID = psnID;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public String getPreferredConsole() {
        return preferredConsole;
    }

    public void setPreferredConsole(String preferredConsole) {
        this.preferredConsole = preferredConsole;
    }

    public HashMap<String, Object> getFellowPlayers() {
        return fellowPlayers;
    }

    public void setFellowPlayers(HashMap<String, Object> fellowPlayers) {
        this.fellowPlayers = fellowPlayers;
    }
}
