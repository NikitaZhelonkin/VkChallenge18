package com.vk.challenge.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;

import com.vk.challenge.App;
import com.vk.challenge.R;
import com.vk.challenge.data.entity.PostItem;
import com.vk.challenge.data.entity.State;
import com.vk.challenge.login.LoginActivity;
import com.vk.challenge.utils.ViewUtils;
import com.vk.challenge.widget.FixedDurationScroller;
import com.vk.challenge.widget.OnStackScrollListener;
import com.vk.challenge.widget.PageChangeListenerAdapter;
import com.vk.challenge.widget.StackViewPager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nikitazhelonkin.mvp.android.MvpActivity;

public class MainActivity extends MvpActivity<MainPresenter> implements MainView,
        LoadingFragment.Callback,
        FeedItemFragment.Callback{

    @BindView(R.id.view_pager)
    StackViewPager mViewPager;
    @BindView(R.id.fab_container)
    View mFabContainer;

    private FeedAdapter mFeedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mFeedAdapter = new FeedAdapter(getSupportFragmentManager());

        mViewPager.setScroller(new FixedDurationScroller(this, new FastOutSlowInInterpolator(), 400));

        mViewPager.addOnPageChangeListener(new PageChangeListenerAdapter() {

            private boolean mUserDragging;

            @Override
            public void onPageScrolled(int i, float v, int i1) {
                super.onPageScrolled(i, v, i1);
                Fragment fragment = mFeedAdapter.getCurrent();
                if (fragment instanceof OnStackScrollListener) {
                    ((OnStackScrollListener) fragment).onStackScrolled(v, mViewPager.getDragDirection(), mUserDragging);
                }
            }

            @Override
            public void onPageSelected(int i) {
                if (i == mFeedAdapter.getCount() - 1) {
                    getPresenter().onReachEnd();
                }

                int direction = mViewPager.getDragDirection();
                PostItem postItem = i > 0 ? mFeedAdapter.getDataItem(i - 1) : null;
                if (postItem != null && direction == StackViewPager.DIRECTION_LEFT) {
                    getPresenter().ignore(postItem);
                } else if (postItem != null && direction == StackViewPager.DIRECTION_RIGHT) {
                    getPresenter().like(postItem);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                super.onPageScrollStateChanged(i);
                if (mViewPager.getScrollState() == ViewPager.SCROLL_STATE_DRAGGING) {
                    mUserDragging = true;
                }
                if (mViewPager.getScrollState() == ViewPager.SCROLL_STATE_IDLE) {
                    mUserDragging = false;
                }
            }
        });
    }

    @Override
    protected MainPresenter onCreatePresenter() {
        return new MainPresenter(App.getAppComponent().feedrepository());
    }

    @OnClick(R.id.fab_ignore)
    public void onIgnoreClick() {
        getPresenter().onIgnoreClick();
    }

    @OnClick(R.id.fab_like)
    public void onLikeClick() {
        getPresenter().onLikeClick();
    }

    @Override
    public void onRepeatClick() {
        getPresenter().onRepeatClick();
    }

    @Override
    public void onCardExpanded() {
        mViewPager.setDragEnabled(false);
    }

    @Override
    public void navigateToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void setFeed(List<PostItem> posts, State state) {
        mFeedAdapter.setData(posts, state);
        mViewPager.setAdapter(mFeedAdapter);
    }

    @Override
    public void swipeLeft() {
        if (canDoAutoSwipe()){
            mViewPager.setDragEnabled(true);
            mViewPager.smoothScrollToNext(StackViewPager.DIRECTION_LEFT);
        }
    }

    @Override
    public void swipeRight() {
        if (canDoAutoSwipe()){
            mViewPager.setDragEnabled(true);
            mViewPager.smoothScrollToNext(StackViewPager.DIRECTION_RIGHT);
        }
    }

    private boolean canDoAutoSwipe() {
        return mViewPager.getCurrentItem() < mFeedAdapter.getCount() - 1 &&
                mViewPager.getScrollState() == ViewPager.SCROLL_STATE_IDLE;
    }

    @Override
    public void showFabs() {
        mFabContainer.setVisibility(View.VISIBLE);
        ViewUtils.onPreDraw(mViewPager, () -> {
            mFabContainer.setTranslationY(mFabContainer.getHeight());
            mFabContainer.animate()
                    .translationY(0)
                    .setDuration(300)
                    .setInterpolator(new FastOutSlowInInterpolator()).start();
        });
    }
}
