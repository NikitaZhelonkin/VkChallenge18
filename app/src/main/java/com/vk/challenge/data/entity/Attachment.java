package com.vk.challenge.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vk.challenge.utils.ListUtils;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(Parcel.Serialization.BEAN)
public class Attachment {

    private final String mType;

    private final Photo mPhoto;

    private final Video mVideo;

    @ParcelConstructor
    public Attachment(@JsonProperty("type") String type,
                      @JsonProperty("photo") Photo photo,
                      @JsonProperty("video") Video video) {
        mType = type;
        mPhoto = photo;
        mVideo = video;
    }

    public String getType() {
        return mType;
    }

    public Photo getPhoto() {
        return mPhoto;
    }

    public Video getVideo() {
        return mVideo;
    }

    public String getDisplayPhoto() {
        //TODO fix retrieving photo
        if (mPhoto != null) {
            Photo.Size size = ListUtils.find(mPhoto.getSizes(), s -> "x".equals(s.getType()));
            size = size == null ? mPhoto.getSizes().get(0) : size;
            return size.getUrl();
        } else if (mVideo != null) {
            return mVideo.getPhoto();
        }
        return null;
    }
}
