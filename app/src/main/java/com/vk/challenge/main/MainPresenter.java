package com.vk.challenge.main;

import android.support.annotation.NonNull;
import android.util.Log;

import com.vk.challenge.data.entity.PostItem;
import com.vk.challenge.data.entity.State;
import com.vk.challenge.data.repository.FeedRepository;
import com.vk.challenge.utils.AppSchedulers;
import com.vk.sdk.VKAccessToken;

import java.util.List;

import io.reactivex.disposables.Disposable;
import ru.nikitazhelonkin.mvp.MvpPresenterBase;

public class MainPresenter extends MvpPresenterBase<MainView> {

    private static final int PAGE_SIZE = 10;

    private FeedRepository mFeedRepository;

    private Disposable mSubscription;

    private int mCurrentPage = -1;

    private State mState;

    public MainPresenter(FeedRepository feedRepository) {
        mFeedRepository = feedRepository;
    }

    @Override
    public void attachView(@NonNull MainView view, boolean firstAttach) {
        super.attachView(view, firstAttach);

        VKAccessToken token = VKAccessToken.currentToken();
        if (token == null || token.isExpired()) {
            view.navigateToLogin();
            return;
        }
        loadFeed();
    }

    public void onReachEnd() {
        loadFeed();
    }

    public void onIgnoreClick(){
        if (getView() != null) getView().swipeLeft();
    }

    public void onLikeClick(){
        if (getView() != null) getView().swipeRight();
    }

    public void onRepeatClick() {
        loadFeed();
    }

    public void like(PostItem postItem) {
        Log.e("TAG", "like " + postItem.getPostOwner().getDisplayName());
    }

    public void ignore(PostItem postItem) {
        Log.e("TAG", "ignore " + postItem.getPostOwner().getDisplayName());
    }

    private void loadFeed() {
        if (getView() != null && mState != State.LOADING) {
            getView().setFeed(null, mState = State.LOADING);
        }
        mSubscription = mFeedRepository.getFeed((mCurrentPage + 1) * PAGE_SIZE, PAGE_SIZE)
                .compose(AppSchedulers.ioToMainTransformer())
                .subscribe(this::onFeedResult, this::onFeedError);

    }

    private void onFeedResult(List<PostItem> posts) {
        mCurrentPage++;
        if (getView() == null) return;
        getView().setFeed(posts, mState = State.LOADING);
        Log.e("TAG", "OK");
    }

    private void onFeedError(Throwable e) {
        if (getView() == null) return;
        getView().setFeed(null, mState = State.ERROR);
        Log.e("TAG", "ERROR" + e);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(mSubscription!=null && !mSubscription.isDisposed()) mSubscription.dispose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
