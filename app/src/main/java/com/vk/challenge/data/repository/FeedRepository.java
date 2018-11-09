package com.vk.challenge.data.repository;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.vk.challenge.data.api.VkApiService;
import com.vk.challenge.data.api.response.TypedListResponse;
import com.vk.challenge.data.entity.Group;
import com.vk.challenge.data.entity.Post;
import com.vk.challenge.data.entity.PostItem;
import com.vk.challenge.data.entity.User;
import com.vk.challenge.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class FeedRepository {

    private VkApiService mVkApiService;

    @Inject
    public FeedRepository(VkApiService vkApiService) {
        mVkApiService = vkApiService;
    }

    public Single<List<PostItem>> getFeed(int from, int count) {
        return mVkApiService.feedGetDiscoverForContestant(count, from)
                .map(feedResponse -> feedResponse.getResponse().getItems())
                .flatMapObservable(Observable::fromIterable)
                .filter(post -> post.hasPhotoOrVideo() || !TextUtils.isEmpty(post.getText()))
                .toList()
                .map(items -> {
                    List<Long> ownerIds = ListUtils.map(items, Post::getSourceId);
                    List<Long> userIds = ListUtils.filter(ownerIds, it -> it > 0);
                    List<Long> groupIds = ListUtils.filter(ownerIds, it -> it < 0);
                    List<User> users = getUsers(userIds).blockingGet();
                    List<Group> groups = getGroups(groupIds).blockingGet();
                    return ListUtils.map(items, it -> new PostItem(it,
                            it.getSourceId() > 0 ? ListUtils.find(users, u -> u.getId() == it.getSourceId()) : null,
                            it.getSourceId() > 0 ? null : ListUtils.find(groups, g -> g.getId() == Math.abs(it.getSourceId()))));
                });
    }

    private Single<List<User>> getUsers(@Nullable List<Long> ownerIds) {
        if (ownerIds == null || ownerIds.size() == 0) return Single.just(new ArrayList<>());
        String ids = ListUtils.join(",", ownerIds);
        return mVkApiService.usersGet(ids, "photo_100").map(TypedListResponse::getResponse);
    }

    private Single<List<Group>> getGroups(@Nullable List<Long> ownerIds) {
        if (ownerIds == null || ownerIds.size() == 0) return Single.just(new ArrayList<>());
        List<Long> groupIds = ListUtils.map(ownerIds, Math::abs);
        String ids = ListUtils.join(",", groupIds);
        return mVkApiService.groupsGet(ids).map(TypedListResponse::getResponse);
    }


}
