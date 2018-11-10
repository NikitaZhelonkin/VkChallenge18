package com.vk.challenge.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

public class StackViewPager extends ViewPager {

    public static final int DIRECTION_NONE = -1;
    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;

    private static final int PAGE_OFFSET = 0; //dips


    private static final int MAX_ROTATION = 4; //degrees

    private static final float PAGE_SCALE = 0.90f;

    private static final int OFFSCREEN_PAGE_LIMIT = 3;

    private static final int VISIBLE_COUNT = 3;

    private static final int SCROLL_DURATION = 200;

    private int mPageOffset;

    private float mInitialMotionX;
    private float mInitialMotionY;
    private float mTouchSlop;
    private boolean mDragging;

    private int mDragDirection = DIRECTION_NONE;

    private int mScrollState;
    private float mPositionOffsetPixels;

    private boolean mFirstLayout;

    public StackViewPager(Context context) {
        super(context);
        init(context, null);
    }

    public StackViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);

        setPageTransformer(true, new StackPageTransformer());

        final float density = context.getResources().getDisplayMetrics().density;
        mPageOffset = (int) (PAGE_OFFSET * density);

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledPagingTouchSlop();

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPositionOffsetPixels = positionOffsetPixels;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mScrollState = state;
                if (mScrollState == SCROLL_STATE_IDLE) {
                    mDragDirection = DIRECTION_NONE;
                }
            }

            @Override
            public void onPageSelected(int position) {
                //do nothing
            }
        });

    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        mFirstLayout = true;
    }

    @Override
    public void draw(Canvas canvas) {
        invalidateTransformationIfNeeded();
        super.draw(canvas);
    }

    private void invalidateTransformationIfNeeded() {
        if (mFirstLayout && getAdapter() != null && getChildCount() != 0) {
            mFirstLayout = false;
            onPageScrolled(0, 0, 0);
        }
    }

    public void smoothScrollToNext(int direction) {
        if (isFakeDragging()) {
            return;
        }
        mDragDirection = direction;

        ValueAnimator animator = ValueAnimator.ofInt(0, getWidth() / 2);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                if (isFakeDragging()) {
                    endFakeDrag();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isFakeDragging()) {
                    endFakeDrag();
                }
            }
        });

        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            private int oldValue = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (isFakeDragging()) {
                    int value = (Integer) animation.getAnimatedValue();
                    fakeDragBy(oldValue - value);
                    oldValue = value;
                }

            }
        });

        animator.setDuration(SCROLL_DURATION);
        beginFakeDrag();
        animator.start();
    }

    public void setScroller(Scroller scroller) {
        setScroller(this, scroller);
    }

    public boolean isDragging() {
        return mDragging;
    }

    public int getDragDirection() {
        return mDragDirection;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (disableDrag()) {
            return super.onInterceptTouchEvent(ev);
        }
        final int action = ev.getAction();
        if (action != MotionEvent.ACTION_DOWN) {
            if (mDragging) {
                return super.onInterceptTouchEvent(ev);
            }
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mInitialMotionX = ev.getX();
                mInitialMotionY = ev.getY();

                if (mScrollState == SCROLL_STATE_SETTLING) {
                    if (!isPageCloseEnough()) {
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float x = ev.getX();
                final float dx = x - mInitialMotionX;
                final float xDiff = Math.abs(dx);
                final float y = ev.getY();
                final float dy = y - mInitialMotionY;
                final float yDiff = Math.abs(dy);

                if (!mDragging) {
                    if (xDiff > mTouchSlop && xDiff * 0.5f > yDiff) {
                        mDragging = true;
                    }
                }
                break;
        }
        mirrorTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (disableDrag()) {
            return true;
        }
        final int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mInitialMotionX = ev.getX();
                mInitialMotionY = ev.getY();

                if (mScrollState == SCROLL_STATE_SETTLING) {
                    if (!isPageCloseEnough()) {
                        return false;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float x = ev.getX();
                final float xDiff = Math.abs(x - mInitialMotionX);
                final float y = ev.getY();
                final float yDiff = Math.abs(y - mInitialMotionY);

                if (!mDragging) {
                    if (xDiff > mTouchSlop && xDiff > yDiff) {
                        mDragging = true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mDragging) {
                    mDragging = false;
                }
                break;

        }
        mirrorTouchEvent(ev);
        return super.onTouchEvent(ev);

    }

    private MotionEvent mirrorTouchEvent(MotionEvent event) {
        mDragDirection = detectDragDirection(event);
        float mirroredX = mirrorX(event.getX());
        event.setLocation(mirroredX, event.getY());
        return event;
    }

    private float mirrorX(float x) {
        float diff = x - mInitialMotionX;
        float mirroredX = mInitialMotionX - Math.abs(diff);
        if (mDragging) {
            mirroredX = Math.min(mirroredX, mInitialMotionX - mTouchSlop);
        }
        return mirroredX;
    }

    private int detectDragDirection(MotionEvent event) {
        float diff = event.getX() - mInitialMotionX;
        return diff > 0 ? DIRECTION_RIGHT : DIRECTION_LEFT;
    }


    private boolean isPageCloseEnough() {
        return mPositionOffsetPixels < mTouchSlop ||
                getWidth() - (Math.abs(mPositionOffsetPixels)) < mTouchSlop;
    }

    private boolean disableDrag() {
        return getAdapter() == null || getCurrentItem() == getAdapter().getCount() - 1;
    }


    //  ------- PAGE TRANSFORMER -----------  //
    private class StackPageTransformer implements PageTransformer {

        @Override
        public void transformPage(View view, float position) {
            position = viewPosition(view);

            view.setTranslationX(translationXForPosition(view, position));
            view.setTranslationY(translationYForPosition(position));
            view.setRotation(rotateForPosition(position));
            setScale(view, scaleForPosition(position));
            setAlpha(view, alphaForPosition(position));
        }

        private float alphaForPosition(float position) {
            int visibleItems = getVisibleItemsCount(VISIBLE_COUNT);
            if (position > 0 && visibleItems != 0) {
                return -(position - visibleItems) * 1 / visibleItems;
            } else {
                return 1;
            }
        }

        private void setAlpha(View view, float alpha) {
            view.setAlpha(alpha);
        }

        private void setScale(View v, float value) {
            v.setPivotX(v.getWidth() / 2);
            v.setPivotY(v.getHeight() /2);
            v.setScaleX(value);
            v.setScaleY(value);
        }


        private float translationXForPosition(View view, float position) {
            if (position < -1) {
                return 0;
            } else if (position < 0) {
                return mDragDirection == DIRECTION_RIGHT ?
                        2 * (getScrollX() % pageWidth(view)) : 0;
            } else {
                return (-pageWidth(view)) * position;
            }
        }

        private float rotateForPosition(float position) {
            if (position < 0 && position > -1) {
                int sign = mDragDirection == DIRECTION_RIGHT ? 1 : -1;
                return sign * MAX_ROTATION * Math.abs(position);
            } else {
                return 0;
            }
        }

        private float translationYForPosition(float position) {
            if (position < 0) {
                return 0;
            } else {
                return -mPageOffset * position;
            }
        }

        private float scaleForPosition(float position) {
            return (position < 0 ? 1 : 1 - (1 - PAGE_SCALE) * position);
        }


        private float viewPosition(View view) {
            return (float) (view.getLeft() - getPaddingLeft() - getScrollX())
                    / (getMeasuredWidth() + getPageMargin() - getPaddingLeft() - getPaddingRight());
        }

        private int getVisibleItemsCount(int maxItems) {
            if (getAdapter() == null) {
                return 0;
            }
            return Math.min(maxItems, getAdapter().getCount());
        }

        private float pageWidth(View v) {
            return v.getWidth() + getPageMargin();
        }

    }

    public static void setScroller(ViewPager viewPager, Scroller scroller) {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            scrollerField.set(viewPager, scroller);
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            //do nothing
        }
    }

}
