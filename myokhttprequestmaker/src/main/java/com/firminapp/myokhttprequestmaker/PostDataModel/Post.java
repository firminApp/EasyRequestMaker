package com.firminapp.myokhttprequestmaker.PostDataModel;

/**
 * Created by firmin on 12/26/2017.
 */

public class Post {

    private String key = "";
    private String value = "";
    private Boolean code;
    private String description = "";

    public Post() {

    }

    public Post(String key, String value) {
        this.key = key;
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
