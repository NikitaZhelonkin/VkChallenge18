package com.vk.challenge.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import com.vk.challenge.App;
import com.vk.challenge.R;
import com.vk.challenge.data.entity.Post;
import com.vk.challenge.data.entity.PostItem;
import com.vk.challenge.login.LoginActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nikitazhelonkin.mvp.android.MvpActivity;

public class MainActivity extends MvpActivity<MainPresenter> implements MainView {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.empty_view)
    View mEmptyView;
    @BindView(R.id.error_view)
    View mErrorView;

    private FeedAdapter mFeedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mFeedAdapter = new FeedAdapter(getSupportFragmentManager());

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
    }

    @Override
    public void setProgressVisible(boolean visible) {
        mProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setEmptyViewVisible(boolean visible) {
        mEmptyView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setErrorViewVisible(boolean visible) {
        mErrorView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

}
