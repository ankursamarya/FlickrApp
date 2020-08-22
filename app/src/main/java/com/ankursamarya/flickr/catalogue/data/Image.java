package com.ankursamarya.flickr.catalogue.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("owner")
    @Expose
    public String owner;
    @SerializedName("secret")
    @Expose
    public String secret;
    @SerializedName("server")
    @Expose
    public String server;
    @SerializedName("farm")
    @Expose
    public int farm;
    @SerializedName("title")
    @Expose
    public String  title;

    public String getUrl(){
        return String.format("https://farm%d.staticflickr.com/%s/%s_%s.jpg", farm, server,id, secret);
    }

}