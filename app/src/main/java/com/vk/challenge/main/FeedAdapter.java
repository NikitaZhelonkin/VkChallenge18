package com.vk.challenge.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.vk.challenge.data.entity.PostItem;

import org.parceler.Parcels;

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
        return (mData != null ? mData.size() : 0) + 1;
    }

    private PostItem getDataItem(int position){
        return mData.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == getCount() - 1) {
            return new LoadingFragment();
        }
        return FeedItemFragment.create(getDataItem(position));
    }

    @Override
    public long getItemId(int position) {
        return position == getCount() - 1 ? -1 : getDataItem(position).getPost().getPostId();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
//        Fragment fragment = ((Fragment) object);
//        Bundle args = fragment.getArguments();
//        PostItem postItem = args == null ? null : Parcels.unwrap(args.getParcelable("extra_item"));
//        if (postItem == null) {
//            return getCount() - 1;
//        } else {
//            int idx =  mData.indexOf(postItem);
//            if (idx != -1) {
//                return idx;
//            } else {
                return PagerAdapter.POSITION_NONE;
//            }
//        }
    }

}
