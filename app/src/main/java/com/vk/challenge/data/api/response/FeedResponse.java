package com.vk.challenge.data.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vk.challenge.data.entity.Post;

import java.util.List;

public class FeedResponse {

    private final Response mResponse;

    public FeedResponse(@JsonProperty("response") Response response) {
        mResponse = response;
    }

    public Response getResponse() {
        return mResponse;
    }

    public static class Response {

        private final List<Post> mItems;

        public Response(@JsonProperty("items") List<Post> items) {
            mItems = items;
        }

        public List<Post> getItems() {
            return mItems;
        }
    }
}
