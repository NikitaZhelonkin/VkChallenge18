package com.vk.challenge.data.entity;


import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(Parcel.Serialization.BEAN)
public class Video {

    private long mId;
    private long mOwnerId;
    private String mTitle;
    private String mDescription;
    private long mDate;
    private String mPhoto;

    @ParcelConstructor
    public Video(@JsonProperty("id") long id,
                 @JsonProperty("owner_id") long ownerId,
                 @JsonProperty("title") String title,
                 @JsonProperty("description") String description,
                 @JsonProperty("date") long date,
                 @JsonProperty("photo_320") String photo) {
        mId = id;
        mOwnerId = ownerId;
        mTitle = title;
        mDescription = description;
        mDate = date;
        mPhoto = photo;
    }

    public long getId() {
        return mId;
    }

    public long getOwnerId() {
        return mOwnerId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public long getDate() {
        return mDate;
    }

    public String getPhoto() {
        return mPhoto;
    }
}
