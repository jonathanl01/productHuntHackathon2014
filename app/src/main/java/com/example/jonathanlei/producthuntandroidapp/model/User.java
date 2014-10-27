package com.example.jonathanlei.producthuntandroidapp.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by anthony on 10/27/14.
 */
public class User implements Serializable {
    private int id;
    private String
            name,
            headline,
            created_at,
            username,
            profile_url,
            image_url_48px,
            image_url_73px,
            image_url_original;

    public User(JSONObject user) throws JSONException {
        id = user.getInt("id");
        name = user.getString("name");
        headline = user.getString("headline");
        created_at = user.getString("created_at");
        username = user.getString("username");
        profile_url = user.getString("profile_url");
        getImageUrls(user.getJSONObject("image_url"));
    }

    private void getImageUrls(JSONObject image_url) throws JSONException {
        image_url_48px = image_url.getString("48px");
        image_url_73px = image_url.getString("73px");
        image_url_original = image_url.getString("original");
        
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHeadline() {
        return headline;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUsername() {
        return username;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public String getImage_url_48px() {
        return image_url_48px;
    }

    public String getImage_url_73px() {
        return image_url_73px;
    }

    public String getImage_url_original() {
        return image_url_original;
    }
}
