package com.example.library.router.router.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.example.library.router.exception.RouteNotFoundException;
import com.example.library.router.factory.RouterInitializer;
import com.example.library.router.router.IRouter;
import com.example.library.router.router.impl.activity.ActivityRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.example.library.router.utils.UrlUtils.getHost;
import static com.example.library.router.utils.UrlUtils.getPathSegments;
import static com.example.library.router.utils.UrlUtils.getScheme;

public abstract class Router<T,P extends Request> implements IRouter<P> {

    protected Map<String, Class<? extends T>> mRouterTables = new HashMap<>();

    public Map<String, Class<? extends T>> getRouterTables() {
        return mRouterTables;
    }

    protected abstract String getHandleScheme();

    protected abstract boolean handle(P request, Map.Entry<String, Class<? extends T>> entry);

    @Override
    public final void init(RouterInitializer initializer) {
        initializer.initialize(ROUTER_TABLE);
        for (Map.Entry<String, Class<?>> entry : ROUTER_TABLE.entrySet()) {
            String url = entry.getKey();
            Class<?> clazz = entry.getValue();
            if (canHandle(url)) {
                mRouterTables.put(url, (Class<? extends T>) clazz);
            }
        }
    }

    @Override
    public final boolean canHandle(String url) {
        return TextUtils.equals(getScheme(url),getHandleScheme());
    }

    @Override
    public final boolean handle(P request) {
        try {
            Map.Entry<String, Class<? extends T>> match = match(request);
            return handle(request, match);
        } catch (RouteNotFoundException e) {
            Log.e("Router", e.getLocalizedMessage());
        }
        return false;
    }

    protected final Map.Entry<String, Class<? extends T>> match(P request) throws RouteNotFoundException {
        Map.Entry<String, Class<? extends T>> fromMemory = findFromMemory(request);
        if (fromMemory != null) {
            return fromMemory;
        }
//        Map.Entry<String, Class<? extends T>> fromDisk = findFromDisk(request);
//        if (fromDisk != null) {
//            return fromDisk;
//        }
        throw new RouteNotFoundException(request.getUrl());
    }

    private Map.Entry<String, Class<? extends T>> findFromMemory(P request) {
        return findMatchRoute(request);
    }

    private Map.Entry<String, Class<? extends T>> findFromDisk(P request) {
        RouterInitializer initializer = findRouterBinder(request);
        init(initializer);
        return findMatchRoute(request);
    }

    protected Map.Entry<String, Class<? extends T>> findMatchRoute(P request) {
        List<String> givenPathSegs = request.getPath();
        OutLoop:
        for (Map.Entry<String, Class<? extends T>> entry : mRouterTables.entrySet()) {
            String routeUrl = entry.getKey();
            List<String> routePathSegs = getPathSegments(routeUrl);
            if (!TextUtils.equals(getHost(routeUrl), request.getHost())) {
                continue;
            }
            if (givenPathSegs.size() != routePathSegs.size()) {
                continue;
            }
            for (int i = 0; i < routePathSegs.size(); i++) {
                if (!routePathSegs.get(i).startsWith(":")
                        && !TextUtils.equals(routePathSegs.get(i), givenPathSegs.get(i))) {
                    continue OutLoop;
                }
            }
            return entry;
        }
        return null;
    }

    private RouterInitializer<T> findRouterBinder(P request) {

        return new RouterInitializer<T>() {

            @Override
            public void initialize(Map<String, Class<? extends T>> tables) {

            }
        };
    }

}
