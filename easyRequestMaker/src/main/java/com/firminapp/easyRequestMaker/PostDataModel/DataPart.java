package com.firminapp.easyRequestMaker.PostDataModel;

import java.io.File;

import okhttp3.MediaType;

/**
 * Created by firmin on 12/26/2017.
 */

public class DataPart {

    private String key = "";
    private String value = "";


    private String description = "";

    public DataPart() {

    }

    public DataPart(String key, String value) {
        this.key = key;
        this.value = value;
    }
    public DataPart(String key, String value, File file, MediaType mediaType) {
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
