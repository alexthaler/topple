package com.thalsoft.topple.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ToppleResult {

    @JsonProperty
    String url;

    public ToppleResult() {
    }

    public ToppleResult(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
