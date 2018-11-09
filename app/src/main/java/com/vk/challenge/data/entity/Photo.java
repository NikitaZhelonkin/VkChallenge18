package com.vk.challenge.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

@Parcel(Parcel.Serialization.BEAN)
public class Photo {

    private long mId;
    private long mOwnerId;
    private List<Size> mSizes;
    private String mText;
    private long mDate;

    @ParcelConstructor
    public Photo(@JsonProperty("id") long id,
                 @JsonProperty("owner_id") long ownerId,
                 @JsonProperty("sizes") List<Size> sizes,
                 @JsonProperty("text") String text,
                 @JsonProperty("date") long date) {
        mId = id;
        mOwnerId = ownerId;
        mSizes = sizes;
        mText = text;
        mDate = date;
    }

    public long getId() {
        return mId;
    }

    public long getOwnerId() {
        return mOwnerId;
    }

    public List<Size> getSizes() {
        return mSizes;
    }

    public String getText() {
        return mText;
    }

    public long getDate() {
        return mDate;
    }

    @Parcel(Parcel.Serialization.BEAN)
    public static class Size {

        private final String mType;
        private final String mUrl;
        private final int mWidth;
        private final int mHeight;

        @ParcelConstructor
        public Size(@JsonProperty("type") String type,
                    @JsonProperty("url") String url,
                    @JsonProperty("width") int width,
                    @JsonProperty("height") int height) {
            mType = type;
            mUrl = url;
            mWidth = width;
            mHeight = height;
        }

        public String getType() {
            return mType;
        }


        public String getUrl() {
            return mUrl;
        }


        public int getWidth() {
            return mWidth;
        }


        public int getHeight() {
            return mHeight;
        }

    }
}
