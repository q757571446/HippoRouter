package com.example.library.router.router.impl;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.library.router.request.impl.Route;
import com.example.library.router.exception.RouteNotFoundException;
import com.example.library.router.router.Router;

import java.util.Map;

import timber.log.Timber;

import static com.example.library.router.utils.UrlUtils.getScheme;

/**
 * Created by kevin on 16-11-17.
 */

public class ActivityRouter extends Router {

    static final String SCHEME = "activity";

    @Override
    public boolean canHandle(String url) {
        return TextUtils.equals(getScheme(url),SCHEME);
    }

    @Override
    public boolean handle(Map.Entry<String, Class> entry) {
        return false;
    }



}
