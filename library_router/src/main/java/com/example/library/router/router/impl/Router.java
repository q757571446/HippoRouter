package com.example.library.router.router.impl;

import android.text.TextUtils;
import android.util.Log;

import com.example.library.router.exception.RouteNotFoundException;
import com.example.library.router.factory.RouterInitializer;
import com.example.library.router.router.IRouter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.example.library.router.utils.UrlUtils.getHost;
import static com.example.library.router.utils.UrlUtils.getPathSegments;
import static com.example.library.router.utils.UrlUtils.getScheme;
import static com.example.library.annotation.uri.RouterUtils.getGenerateClass;

public abstract class Router<T,P extends Request> implements IRouter<P> {

    protected abstract String canHandle();

    protected abstract boolean handle(P request, Map.Entry<String, Class<? extends T>> entry);

    public Router() {
        
    }

    public Map<String, Class<? extends T>> getRouterTables(Map<String,Class<?>> map) {
        Map<String, Class<? extends T>> tables = new HashMap<>();
        for (Map.Entry<String, Class<?>> entry : map.entrySet()) {
            String url = entry.getKey();
            Class<?> clazz = entry.getValue();
            if (canHandle(url)) {
                tables.put(url, (Class<? extends T>) clazz);
            }
        }
        return tables;
    }
    
    @Override
    public final boolean canHandle(String url) {
        return TextUtils.equals(getScheme(url), canHandle());
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

    protected final Map.Entry<String, Class<? extends T>> match(P request) throws  RouteNotFoundException {
        Map.Entry<String, Class<? extends T>> result;
        try {
            result = findFromMemory(request);
        } catch (RouteNotFoundException e) {
            result = findFromDisk(request);
        }
        return result;
    }

    private Map.Entry<String, Class<? extends T>> findFromMemory(P request) throws RouteNotFoundException {
        return findMatchRoute(request);
    }

    private Map.Entry<String, Class<? extends T>> findFromDisk(P request) throws RouteNotFoundException {
        findRouterInitializer(request).initialize(ROUTER_TABLE);
        return findMatchRoute(request);
    }

    protected Map.Entry<String, Class<? extends T>> findMatchRoute(P request) throws RouteNotFoundException {
        List<String> givenPathSegs = request.getPath();
        OutLoop:
        for (Map.Entry<String, Class<? extends T>> entry : getRouterTables(ROUTER_TABLE).entrySet()) {
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
        throw new RouteNotFoundException(request.getUrl());
    }

    private RouterInitializer findRouterInitializer(P request) throws RouteNotFoundException {
        try {
            return (RouterInitializer) getGenerateClass(request.getUrl()).newInstance();
        } catch (Exception e) {
            throw new RouteNotFoundException(request.getUrl());
        }
    }
}
