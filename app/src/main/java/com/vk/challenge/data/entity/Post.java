package com.vk.challenge.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vk.challenge.utils.ListUtils;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.Transient;

import java.util.List;

@Parcel(Parcel.Serialization.BEAN)
public class Post {

    private String mType;
    private long mSourceId;
    private long mDate;
    private long mPostId;
    private String mPostType;
    private String mText;
    private List<Attachment> mAttachments;

    @ParcelConstructor
    public Post(@JsonProperty("type") String type,
                @JsonProperty("source_id") long sourceId,
                @JsonProperty("date") long date,
                @JsonProperty("post_id") long postId,
                @JsonProperty("post_type") String postType,
                @JsonProperty("text") String text,
                @JsonProperty("attachments") List<Attachment> attachments) {
        mType = type;
        mSourceId = sourceId;
        mDate = date;
        mPostId = postId;
        mPostType = postType;
        mText = text;
        mAttachments = attachments;
    }

    public String getType() {
        return mType;
    }

    public long getSourceId() {
        return mSourceId;
    }

    public long getDate() {
        return mDate;
    }

    public long getPostId() {
        return mPostId;
    }

    public String getPostType() {
        return mPostType;
    }

    public String getText() {
        return mText;
    }

    public List<Attachment> getAttachments() {
        return mAttachments;
    }

    public boolean hasPhotoOrVideo() {
        List<Attachment> attachments = ListUtils.filter(mAttachments,
                it -> "photo".equals(it.getType()) || "video".equals(it.getType())
        );
        return attachments != null && attachments.size() > 0;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        return mPostId == post.mPostId;
    }

    @Override
    public int hashCode() {
        return (int) (mPostId ^ (mPostId >>> 32));
    }
}
