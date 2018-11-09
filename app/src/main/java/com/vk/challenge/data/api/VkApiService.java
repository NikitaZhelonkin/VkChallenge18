package com.vk.challenge.data.api;

import com.vk.challenge.data.api.response.FeedResponse;
import com.vk.challenge.data.api.response.GroupsResponse;
import com.vk.challenge.data.api.response.UsersResponse;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface VkApiService {

    @GET("method/newsfeed.getDiscoverForContestant")
    Single<FeedResponse> feedGetDiscoverForContestant(@Query("count") int count, @Query("start_from") int startFrom);

    @POST("method/likes.add")
    @FormUrlEncoded
    Single<ResponseBody> likesAdd(@Field("type") String type, @Field("owner_id") int ownerId, @Field("item_id") int itemId);

    @POST("method/newsfeed.ignoreItem")
    @FormUrlEncoded
    Single<ResponseBody> feedIgnoreItem(@Field("type") String type, @Field("owner_id") int ownerId, @Field("item_id") int itemId);

    @GET("method/users.get")
    Single<UsersResponse> usersGet(@Query("user_ids") String userIds, @Query("fields") String fields);

    @GET("method/groups.getById")
    Single<GroupsResponse> groupsGet(@Query("group_ids") String groupIds);

}
