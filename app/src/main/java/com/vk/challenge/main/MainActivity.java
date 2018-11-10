package com.vk.challenge.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.widget.ProgressBar;

import com.vk.challenge.App;
import com.vk.challenge.R;
import com.vk.challenge.data.entity.PostItem;
import com.vk.challenge.login.LoginActivity;
import com.vk.challenge.widget.FixedDurationScroller;
import com.vk.challenge.widget.StackViewPager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nikitazhelonkin.mvp.android.MvpActivity;

public class MainActivity extends MvpActivity<MainPresenter> implements MainView {

    @BindView(R.id.view_pager)
    StackViewPager mViewPager;

    private FeedAdapter mFeedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mFeedAdapter = new FeedAdapter(getSupportFragmentManager());

        mViewPager.setScroller(new FixedDurationScroller(this, new FastOutSlowInInterpolator(), 500));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i==mFeedAdapter.getCount()-1){
                    getPresenter().onEnd();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        mViewPager.setAdapter(mFeedAdapter);
    }

    @Override
    protected MainPresenter onCreatePresenter() {
        return new MainPresenter(App.getAppComponent().feedrepository());
    }

    @OnClick(R.id.fab_ignore)
    public void onIgnoreClick(){
        getPresenter().onIgnoreClick();
    }

    @OnClick(R.id.fab_like)
    public void onLikeClick(){
        getPresenter().onLikeClick();
    }

    @Override
    public void navigateToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void setFeed(List<PostItem> posts) {
        mFeedAdapter.setData(posts);
        mViewPager.setAdapter(mFeedAdapter);
    }


}
