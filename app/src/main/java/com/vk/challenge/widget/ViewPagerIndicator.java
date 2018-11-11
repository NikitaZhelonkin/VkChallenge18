package com.vk.challenge.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.vk.challenge.R;

public class ViewPagerIndicator extends View {

    private Drawable mDrawable;

    private Drawable mSelectedDrawable;

    private ViewPager mViewPager;

    private int mSpacing;

    private int mCurrentPosition = 0;

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator, 0, 0);
            mDrawable = a.getDrawable(R.styleable.ViewPagerIndicator_indicatorDrawable);
            mSelectedDrawable = a.getDrawable(R.styleable.ViewPagerIndicator_indicatorSelectedDrawable);
            mSpacing = a.getDimensionPixelSize(R.styleable.ViewPagerIndicator_indicatorSpacing, 0);
            a.recycle();
        }

        if (mDrawable == null || mSelectedDrawable == null) {
            throw new IllegalArgumentException("Indicators drawables wasn't defined");
        }
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //do nothing
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                invalidate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //do nothing
            }
        });
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = Math.max(mSelectedDrawable.getMinimumHeight(), mDrawable.getMinimumHeight());
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int dotsCount = getItemCount();
        if (dotsCount < 2) return;

        float width = getWidth() - getPaddingLeft() - getPaddingRight();
        float itemWidth = (width - (dotsCount - 1) * mSpacing) / dotsCount;

        for (int i = 0; i < dotsCount; ++i) {
            Drawable drawable = i == mCurrentPosition ? mSelectedDrawable : mDrawable;
            int left = (int) (i * (itemWidth + mSpacing));
            drawable.setBounds(left, 0, (int) (left + itemWidth), drawable.getMinimumHeight());
            drawable.draw(canvas);
        }
    }


    private int getItemCount() {
        if (mViewPager == null || mViewPager.getAdapter() == null) {
            return 0;
        }
        return mViewPager.getAdapter().getCount();
    }
}