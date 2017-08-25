package com.arcane.sticks.models;


import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Post implements Serializable {
    public Post(){

    }
    private String postText;
    private String user;
    public String imgURL;
    private URL hyperLink;
    private String id;
    private long time;
    private int upCount;
    private int downCount;
    private Map<String, Boolean> ups = new HashMap<>();
    private Map<String, Boolean> downs  = new HashMap<>();
    private Map<String, Boolean>  comments = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUpCount() {
        return upCount;
    }

    public void setUpCount(int upCount) {
        this.upCount = upCount;
    }

    public int getDownCount() {
        return downCount;
    }

    public void setDownCount(int downCount) {
        this.downCount = downCount;
    }


    public String getPostText() {
        return postText;
    }
    public void setPostText(String postText) {
        this.postText = postText;
    }
    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public URL getHyperLink() {
        return hyperLink;
    }

    public void setHyperLink(URL hyperLink) {
        this.hyperLink = hyperLink;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Map<String, Boolean> getUps() {
        return ups;
    }

    public void setUps(Map<String, Boolean> ups) {
        this.ups = ups;
    }

    public Map<String, Boolean> getDowns() {
        return downs;
    }

    public void setDowns(Map<String, Boolean> downs) {
        this.downs = downs;
    }

    public Map<String, Boolean> getComments() {
        return comments;
    }

    public void setComments(Map<String, Boolean> comments) {
        this.comments = comments;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return postText;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("time", time);
        result.put("postText", postText);
        result.put("imgURL", imgURL);
        result.put("hyperLink", hyperLink);
        result.put("upCount", upCount);
        result.put("downCount", downCount);
        result.put("ups", ups);
        result.put("downs", downs);
        result.put("comments", comments);
        result.put("id",id);

        return result;
    }
}
