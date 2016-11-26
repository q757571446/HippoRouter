package com.example.library.router.router.impl;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.example.library.router.exception.RouteNotFoundException;
import com.example.library.router.request.impl.RouterRequest;
import com.example.library.router.router.Router;
import com.example.library.router.utils.UrlUtils;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static com.example.library.router.router.impl.ActivityRouter.DEFAULT_ACTIVITY_SCHEME;
import static com.example.library.router.utils.UrlUtils.getHost;
import static com.example.library.router.utils.UrlUtils.getPathSegments;

/**
 * Created by kevin on 16-11-17.
 */

public class FragmentRouter extends Router{
    public static final String FRAGMENT_KEY_URL = "key_and_fragment_router_url";

    public static final String DEFAULT_FRAGMENT_SCHEME = "fragment";

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


    private void open(Context context, RouterRequest request) throws RouteNotFoundException {
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
        Class matchedFragment = findMatchClass(request);
        Intent intent = new Intent(context, getWrapActivity(matchedRoute));
        intent = setKeyValueInThePath(matchedRoute, request.getUrl(), intent);
        intent = setOptionParams(request.getUrl(), intent);
        intent = setExtras(request.getBundle(), intent);
        intent.putExtra(FRAGMENT_KEY_URL, matchedFragment.getName());
        return intent;
    }

    @Override
    public String getDefaultScheme() {
        return DEFAULT_FRAGMENT_SCHEME;
    }

    protected Class<?> getWrapActivity(String matchedRoute) {
        String hostActivity = UrlUtils.getFragment(matchedRoute);
        Class wrapActivityFromMemory = findWrapActivityFromMemory(hostActivity);
        if (wrapActivityFromMemory != null) {
            Log.d("router", "Get WrapActivity from memory!");
            return wrapActivityFromMemory;
        }

        Class wrapActivityFromDisk = findWrapActivityFromDisk(hostActivity);
        if (wrapActivityFromDisk != null) {
            Log.d("router", "Get WrapActivity from disk!");
            return wrapActivityFromDisk;
        }
        throw new IllegalArgumentException("cannot find wrap activity about"+matchedRoute);
    }

    private Class findWrapActivityFromMemory(String hostActivity) {
        return findWrapActivity(hostActivity, ROUTER_TABLE);
    }

    private Class findWrapActivityFromDisk(String hostActivity) {
        try {
            Map<String, Class> table = findGenerateClass(DEFAULT_ACTIVITY_SCHEME, hostActivity).getRouterTable();
            //add to memory
            ROUTER_TABLE.putAll(table);
            return findWrapActivity(hostActivity, table);
        } catch (Exception e) {
        }
        return null;
    }


    private Class findWrapActivity(String hostActivity,Map<String,Class> findMap) {
        for (Map.Entry<String, Class> entry : findMap.entrySet()) {
            String key = entry.getKey();
            if (UrlUtils.getScheme(key).equals(DEFAULT_ACTIVITY_SCHEME)&& UrlUtils.getHost(key).equals(hostActivity)) {//find cannot open url
                return entry.getValue();
            }
        }
        return null;
    }

}
