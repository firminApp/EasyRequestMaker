package com.firminapp.myokhttprequestmaker.PostDataModel;

/**
 * Created by firmin on 1/1/2018.
 */

public class Get {
    public Get()
    {

    }
    public Get(String key, String value)
    {
        this.key=key;
        this.value=value;
    }
    private String key = "";
    private String value = "";

    private String description = "";

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
