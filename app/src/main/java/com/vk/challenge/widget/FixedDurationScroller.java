package com.vk.challenge.widget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class FixedDurationScroller extends Scroller {

    private int mDuration;

    public FixedDurationScroller(Context context, int duration) {
        super(context);
        mDuration = duration;
    }

    public FixedDurationScroller(Context context, Interpolator interpolator, int duration) {
        super(context, interpolator);
        mDuration = duration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}

