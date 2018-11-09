package com.vk.challenge.data.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TypedListResponse <T> {

    private List<T> mResponse;

    public void setResponse(List<T> response) {
        mResponse = response;
    }

    public List<T> getResponse() {
        return mResponse;
    }
}