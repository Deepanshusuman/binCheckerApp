package com.assets.binfinder.util;

import androidx.annotation.NonNull;

import com.assets.binfinder.BinOuterClass;

public class SaveState {
    @NonNull
    public BinOuterClass.SearchRequest.Builder searchRequest;


    public SaveState() {
        this.searchRequest = BinOuterClass.SearchRequest.newBuilder();
    }

    @NonNull
    public BinOuterClass.SearchRequest.Builder getSearchRequest() {
        return searchRequest;
    }

    public void setSearchRequest(@NonNull BinOuterClass.SearchRequest.Builder searchRequest) {
        this.searchRequest = searchRequest;
    }


}
