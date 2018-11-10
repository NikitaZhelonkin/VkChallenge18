package com.vk.challenge.data.entity;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.Transient;

@Parcel(Parcel.Serialization.BEAN)
public class PostItem {

    private Post mPost;

    private Group mGroup;

    private User mUser;

    @ParcelConstructor
    public PostItem(Post post, User user, Group group) {
        mPost = post;
        mUser = user;
        mGroup = group;
    }

    public Post getPost() {
        return mPost;
    }

    public User getUser() {
        return mUser;
    }

    public Group getGroup() {
        return mGroup;
    }

    @Transient
    public PostOwner getPostOwner() {
        return mUser == null ? mGroup : mUser;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostItem postItem = (PostItem) o;

        return mPost != null ? mPost.equals(postItem.mPost) : postItem.mPost == null;
    }

    @Override
    public int hashCode() {
        return mPost != null ? mPost.hashCode() : 0;
    }
}
