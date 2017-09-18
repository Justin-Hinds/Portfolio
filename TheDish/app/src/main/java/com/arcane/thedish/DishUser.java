package com.arcane.thedish;


import java.io.Serializable;
import java.util.HashMap;

public class DishUser implements Serializable {
    private String name;
    private String id;
    private String favoriteRestaurant;
    private String favoriteFood;
    private String profilePicURL;

    public String getFavoriteFood() {
        return favoriteFood;
    }

    public void setFavoriteFood(String favoriteFood) {
        this.favoriteFood = favoriteFood;
    }

    private String favoriteDrink;
    private HashMap<String,Object> friends = new HashMap<>();

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

    public String getFavoriteRestaurant() {
        return favoriteRestaurant;
    }

    public void setFavoriteRestaurant(String favoriteRestaurant) {
        this.favoriteRestaurant = favoriteRestaurant;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public String getFavoriteDrink() {
        return favoriteDrink;
    }

    public void setFavoriteDrink(String favoriteDrink) {
        this.favoriteDrink = favoriteDrink;
    }

    public HashMap<String, Object> getFriends() {
        return friends;
    }

    public void setFriends(HashMap<String, Object> friends) {
        this.friends = friends;
    }
}
