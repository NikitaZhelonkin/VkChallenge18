package com.vk.challenge.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.vk.challenge.data.entity.PostItem;
import com.vk.challenge.data.entity.State;

import java.util.List;

public class FeedAdapter extends FragmentStatePagerAdapter {

    private List<PostItem> mData;
    private State mState;
    private Fragment mCurrent;

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

    public PostItem getDataItem(int position) {
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
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (mCurrent != object) {
            mCurrent = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    public Fragment getCurrent() {
        return mCurrent;
    }


    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

}
