package com.ankursamarya.flickr.catalogue.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("photos")
    @Expose
    public Images photos;
    @SerializedName("stat")
    @Expose
    public String stat;

}