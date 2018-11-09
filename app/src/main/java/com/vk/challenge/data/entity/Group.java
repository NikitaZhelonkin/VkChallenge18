package com.vk.challenge.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(Parcel.Serialization.BEAN)
public class Group implements PostOwner {

    private long mId;

    private String mName;

    private String mScreenName;

    private String mPhoto;

    @ParcelConstructor
    public Group(@JsonProperty("id") long id,
                 @JsonProperty("name") String name,
                 @JsonProperty("screen_name") String screenName,
                 @JsonProperty("photo_100") String photo) {
        mId = id;
        mName = name;
        mScreenName = screenName;
        mPhoto = photo;
    }

    @Override
    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getScreenName() {
        return mScreenName;
    }

    @Override
    public String getPhoto() {
        return mPhoto;
    }

    @Override
    public String getDisplayName() {
        return mName;
    }
}
