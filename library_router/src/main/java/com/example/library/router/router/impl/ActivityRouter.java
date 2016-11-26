package com.example.library.router.router.impl;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.library.router.request.impl.RouterRequest;
import com.example.library.router.exception.RouteNotFoundException;
import com.example.library.router.router.Router;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static com.example.library.router.utils.UrlUtils.getHost;
import static com.example.library.router.utils.UrlUtils.getPathSegments;

/**
 * Created by kevin on 16-11-17.
 */

public class ActivityRouter extends Router {
    public static final String ACTIVITY_KEY_URL = "key_and_activity_router_url";

    public static final String DEFAULT_ACTIVITY_SCHEME = "activity";

    @Override
    public String getDefaultScheme() {
        return DEFAULT_ACTIVITY_SCHEME;
    }

    @Override
    public boolean open(RouterRequest request) {
        try {
            open(request.getContext(), request);
            return true;
        } catch (Exception e) {
            Timber.e(e, "Url route not specified: %s", request.getUrl());
        }
        return false;
    }

    protected void open(Context context, RouterRequest request) throws RouteNotFoundException {
        Intent intent = match(context,request);
        if (intent == null) {
            throw new RouteNotFoundException(request.getUrl());
        }
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | request.getFlags());
            context.startActivity(intent);
        } else {
            intent.setFlags(request.getFlags());
            context.startActivity(intent);
        }

        if (request.getAnimationIn() != -1 && request.getAnimationOut() != -1 && context instanceof Activity) {
            ((Activity) context).overridePendingTransition(request.getAnimationIn(),request.getAnimationOut());
        }
    }

    private Intent match(Context context, RouterRequest request) {
        Class matchedActivity = findMatchClass(request);
        Intent intent = new Intent(context, matchedActivity);
        //find the key value in the path
        intent = setKeyValueInThePath(matchedRoute, request.getUrl(), intent);
        intent = setOptionParams(request.getUrl(), intent);
        intent = setExtras(request.getBundle(), intent);
        intent.putExtra(ACTIVITY_KEY_URL, request.getUrl());
        return intent;
    }
}
