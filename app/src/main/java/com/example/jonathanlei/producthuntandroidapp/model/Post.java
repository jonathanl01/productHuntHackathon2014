package com.example.jonathanlei.producthuntandroidapp.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by anthony on 10/27/14.
 */
public class Post implements Serializable {
    private int  comments_count, votes_count;
    private String id,
            name,
            tagline,
            created_at,
            day,
            discussion_url,
            redirect_url,
            screenshot_url_300px,
            screenshot_url_850px;
    private boolean maker_inside;
    private User user;
    private ArrayList<User> makers;

    public Post(JSONObject post) throws JSONException {
        id = post.getString("id");
        name = post.getString("name");
        tagline = post.getString("tagline");
        created_at = post.getString("created_at");
        day = post.getString("day");
        comments_count = post.getInt("comments_count");
        votes_count = post.getInt("votes_count");
        discussion_url = post.getString("discussion_url");
        redirect_url = post.getString("redirect_url");
        setScreenShotUrls(post.getJSONObject("screenshot_url"));
        maker_inside = post.getBoolean("maker_inside");
        user = new User(post.getJSONObject("user"));
        setMakers(post.getJSONArray("makers"));
    }

    private void setScreenShotUrls(JSONObject screenshot_url) throws JSONException {
        screenshot_url_300px = screenshot_url.getString("300px");
        screenshot_url_850px = screenshot_url.getString("850px");
    }

    private void setMakers(JSONArray makers) throws JSONException {
        this.makers = new ArrayList<User>();
        for (int i = 0; i < makers.length(); ++i) {
            this.makers.add(new User(makers.getJSONObject(i)));
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTagline() {
        return tagline;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getDay() {
        return day;
    }

    public String getDiscussion_url() {
        return discussion_url;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public boolean isMaker_inside() {
        return maker_inside;
    }

    public int getComments_count() {
        return comments_count;
    }

    public int getVotes_count() {
        return votes_count;
    }

    public String getScreenshot_url_300px() {
        return screenshot_url_300px;
    }

    public String getScreenshot_url_850px() {
        return screenshot_url_850px;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<User> getMakers() {
        return makers;
    }
}


