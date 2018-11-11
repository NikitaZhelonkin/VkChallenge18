package com.vk.challenge.main;

import com.vk.challenge.data.entity.Post;
import com.vk.challenge.data.entity.PostItem;
import com.vk.challenge.data.entity.State;

import java.util.List;

import ru.nikitazhelonkin.mvp.MvpView;

public interface MainView extends MvpView {

    void navigateToLogin();

    void setFeed(List<PostItem> posts, State state);

    void swipeLeft();

    void swipeRight();

}
