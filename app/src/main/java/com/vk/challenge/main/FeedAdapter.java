package com.vk.challenge.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vk.challenge.data.entity.PostItem;
import com.vk.challenge.data.entity.State;

import java.util.List;

public class FeedAdapter extends FragmentPagerAdapter {

    private List<PostItem> mData;
    private State mState;

    public FeedAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(List<PostItem> data, State state) {
        mData = data;
        mState = state;
    }

    @Override
    public int getCount() {
        return (mData != null ? mData.size() : 0) + 1;
    }

    public PostItem getDataItem(int position){
        return mData.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == getCount() - 1) {
            return LoadingFragment.create(mState == null ? State.LOADING : mState);
        }
        return FeedItemFragment.create(getDataItem(position));
    }

    @Override
    public long getItemId(int position) {
        if (position == getCount() - 1) return -mState.ordinal();
        return getDataItem(position).getPost().getPostId();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

}
