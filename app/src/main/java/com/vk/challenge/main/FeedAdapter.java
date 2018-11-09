package com.vk.challenge.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vk.challenge.data.entity.Post;
import com.vk.challenge.data.entity.PostItem;

import java.util.List;

public class FeedAdapter extends FragmentPagerAdapter {

    private List<PostItem> mData;

    public FeedAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(List<PostItem> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    private PostItem getDataItem(int position){
        return mData.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return FeedItemFragment.create(getDataItem(position));
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

}
