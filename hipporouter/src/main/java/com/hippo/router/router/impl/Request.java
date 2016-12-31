package com.hippo.router.router.impl;

import com.hippo.router.router.IRequest;
import com.hippo.router.utils.UrlUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by kevin on 16-11-16.
 */

public class Request implements IRequest {
    String mUrl;
    String mScheme;
    String mHost;

    int mPort;
    List<String> mPath;
    Map<String, String> mQueryParameters;

    public Request(String url) {
        mUrl = url;
        mScheme = UrlUtils.getScheme(url);
        mHost = UrlUtils.getHost(url);
        mPort = UrlUtils.getPort(url);
        mPath = UrlUtils.getPathSegments(url);
        mQueryParameters = UrlUtils.getParameters(url);
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public String getScheme() {
        return mScheme;
    }

    @Override
    public String getHost() {
        return mHost;
    }

    @Override
    public int getPort() {
        return mPort;
    }

    @Override
    public List<String> getPath() {
        return mPath;
    }

    @Override
    public Map<String, String> getParameters(){
        return mQueryParameters;
    }

}
