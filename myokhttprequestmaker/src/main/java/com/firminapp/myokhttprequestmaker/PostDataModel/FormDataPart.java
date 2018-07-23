package com.firminapp.myokhttprequestmaker.PostDataModel;

import okhttp3.MediaType;

/**
 * Created by firmin on 08/06/18.
 */

public class FormDataPart {
    private String key;
    private String value;
    private MediaType mediatype;
    FormDataPart(String key,String value)
    {
        this.key=key;
        this.value=value;
    }
    FormDataPart(String key,String value, MediaType mediatype)
    {
        this.key=key;
        this.value=value;
        this.mediatype=mediatype;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public MediaType getMediatype() {
        return mediatype;
    }

    public void setMediatype(MediaType mediatype) {
        this.mediatype = mediatype;
    }
}
