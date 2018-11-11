package com.vk.challenge.main;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.vk.challenge.GlideApp;
import com.vk.challenge.R;

import java.util.List;

public class AttachmentAdapter extends PagerAdapter {

    private List<String> mData;

    public AttachmentAdapter(List<String> data) {
        mData = data;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View v = inflater.inflate(R.layout.layout_attachement, container, false);
        bind(v, position);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        unbind((View)object);
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    private void bind(View v, int position) {
        String url = mData.get(position);
        ImageView imageView = v.findViewById(R.id.image_view);
        if (TextUtils.isEmpty(url)) return;
        GlideApp.with(v.getContext())
                .load(url)
                .apply(new RequestOptions().placeholder(R.drawable.bg_placeholder))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    private void unbind(View v){
        ImageView imageView = v.findViewById(R.id.image_view);
        GlideApp.with(v.getContext()).clear(imageView);
    }
}
