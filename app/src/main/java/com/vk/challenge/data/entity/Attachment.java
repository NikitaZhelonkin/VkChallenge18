package com.vk.challenge.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vk.challenge.utils.ListUtils;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

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
            List<Photo.Size> sizes = mPhoto.getSizes();
            Photo.Size size = ListUtils.find(sizes, s -> "x".equals(s.getType()));
            if (size == null) size = ListUtils.find(sizes, s -> "q".equals(s.getType()));
            if (size == null) size = ListUtils.find(sizes, s -> "p".equals(s.getType()));
            if (size == null) size = sizes != null && sizes.size() > 0 ? sizes.get(0) : null;
            return size == null ? null : size.getUrl();
        } else if (mVideo != null) {
            return mVideo.getPhoto();
        }
        return null;
    }
}
