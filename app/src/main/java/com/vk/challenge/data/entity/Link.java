package com.vk.challenge.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(Parcel.Serialization.BEAN)
public class Link {

    private final String mUrl;
    private final String mTitle;
    private final String mDescription;
    private final Photo mPhoto;


    @ParcelConstructor
    public Link(@JsonProperty("url") String url,
                @JsonProperty("title") String title,
                @JsonProperty("description") String description,
                @JsonProperty("photo") Photo photo) {
        mUrl = url;
        mTitle = title;
        mDescription = description;
        mPhoto = photo;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public Photo getPhoto() {
        return mPhoto;
    }
}
