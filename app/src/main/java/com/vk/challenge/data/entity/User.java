package com.vk.challenge.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(Parcel.Serialization.BEAN)
public class User implements PostOwner {

    private long mId;

    private String mFirstName;

    private String mLastName;

    private String mPhoto;

    @ParcelConstructor
    public User(@JsonProperty("id") long id,
                @JsonProperty("first_name") String firstName,
                @JsonProperty("last_name") String lastName,
                @JsonProperty("photo_100") String photo) {
        mId = id;
        mFirstName = firstName;
        mLastName = lastName;
        mPhoto = photo;
    }

    @Override
    public long getId() {
        return mId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    @Override
    public String getPhoto() {
        return mPhoto;
    }

    @Override
    public String getDisplayName() {
        return mFirstName+" "+mLastName;
    }
}
