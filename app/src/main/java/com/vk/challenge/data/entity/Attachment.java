package com.vk.challenge.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(Parcel.Serialization.BEAN)
public class Attachment {

    private final String mType;

    private final Photo mPhoto;

    private final Link mLink;
    private final Video mVideo;

    @ParcelConstructor
    public Attachment(@JsonProperty("type") String type,
                      @JsonProperty("photo") Photo photo,
                      @JsonProperty("link") Link link,
                      @JsonProperty("video") Video video) {
        mType = type;
        mPhoto = photo;
        mLink = link;
        mVideo = video;
    }

    public String getType() {
        return mType;
    }

    public Photo getPhoto() {
        return mPhoto;
    }

    public Link getLink() {
        return mLink;
    }

    public Video getVideo() {
        return mVideo;
    }
}
