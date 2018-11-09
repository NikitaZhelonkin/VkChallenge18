package com.vk.challenge.main;

import android.support.annotation.NonNull;

import com.vk.challenge.data.api.response.FeedResponse;
import com.vk.challenge.data.entity.Post;
import com.vk.challenge.data.entity.PostItem;
import com.vk.challenge.data.repository.FeedRepository;
import com.vk.challenge.utils.AppSchedulers;
import com.vk.sdk.VKAccessToken;

import java.util.List;

import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nikitazhelonkin.mvp.MvpPresenterBase;

public class MainPresenter extends MvpPresenterBase<MainView> {

    private FeedRepository mFeedRepository;

    private List<PostItem> mData;

    private Disposable mSubscription;

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

        loadFeed(firstAttach);
    }

    public void onIgnoreClick(){
        //TODO
    }

    public void onLikeClick(){
        //TODO
    }

    private void loadFeed(boolean force) {
        if (mData != null) {
            onFeedResult(mData);
        }
        if (force || mData == null) {
            if (getView() != null) {
                getView().setEmptyViewVisible(false);
                getView().setErrorViewVisible(false);
                getView().setProgressVisible(true);
            }

            mSubscription = mFeedRepository.getFeed(0, 20)
                    .compose(AppSchedulers.ioToMainTransformer())
                    .subscribe(this::onFeedResult, this::onFeedError);
        }

    }

    private void onFeedResult(List<PostItem> posts) {
        mData = posts;
        if (getView() == null) return;
        getView().setFeed(posts);
        getView().setProgressVisible(false);
        getView().setEmptyViewVisible(posts.size() == 0);
    }

    private void onFeedError(Throwable e) {
        if (getView() == null) return;
        getView().setProgressVisible(false);
        getView().setErrorViewVisible(true);
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
