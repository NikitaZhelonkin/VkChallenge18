package com.vk.challenge.main;

import android.support.annotation.NonNull;

import com.vk.challenge.data.api.response.FeedResponse;
import com.vk.challenge.data.entity.Post;
import com.vk.challenge.data.entity.PostItem;
import com.vk.challenge.data.repository.FeedRepository;
import com.vk.challenge.utils.AppSchedulers;
import com.vk.sdk.VKAccessToken;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nikitazhelonkin.mvp.MvpPresenterBase;

public class MainPresenter extends MvpPresenterBase<MainView> {

    private FeedRepository mFeedRepository;

    private List<PostItem> mData = new ArrayList<>();

    private Disposable mSubscription;

    private int mCurrentPage = -1;
    private int mPageSize = 10;

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

//        loadFeed(firstAttach);
    }

    public void onEnd(){
        loadFeed();
    }

    public void onIgnoreClick(){
        //TODO
    }

    public void onLikeClick(){
        //TODO
    }

    private void loadFeed() {
        if (getView() != null) {
            getView().setEmptyViewVisible(false);
            getView().setErrorViewVisible(false);
            getView().setProgressVisible(true);
        }

        mSubscription = mFeedRepository.getFeed(mCurrentPage+1, mPageSize)
                .compose(AppSchedulers.ioToMainTransformer())
                .subscribe(this::onFeedResult, this::onFeedError);

    }

    private void onFeedResult(List<PostItem> posts) {
        mCurrentPage++;
        mData.addAll(posts);
        if (getView() == null) return;
        getView().setFeed(mData);
        getView().setProgressVisible(false);
        getView().setEmptyViewVisible(mData.size() == 0);
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
