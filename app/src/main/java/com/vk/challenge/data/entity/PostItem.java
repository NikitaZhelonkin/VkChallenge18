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


}
