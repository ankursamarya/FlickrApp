package com.ankursamarya.flickr.catalogue.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Images {
    @SerializedName("photo")
    @Expose
    public List<Image> photo = new ArrayList<>();

}