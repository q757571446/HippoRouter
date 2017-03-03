package com.hippo.router.router.impl;


import android.text.TextUtils;
import android.util.Log;

import com.hippo.router.exception.RouteNotFoundException;
import com.hippo.router.router.IRouter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.hippo.router.utils.UrlUtils.getHost;
import static com.hippo.router.utils.UrlUtils.getPathSegments;
import static com.hippo.router.utils.UrlUtils.getScheme;

public abstract class Router<T,P extends Request> implements IRouter<P> {
    public static final String TAG = "Router";
    List<String> mSchemes = new ArrayList<>();

    // ensure that activity://login/username is rank top over activity://login/:username
    Map<String, Class<? extends T>> tables = new TreeMap<>(new Comparator<String>() {
        @Override
        public int compare(String s1, String t1) {
            return s1.compareTo(t1)*-1;
        }
    });
    protected abstract boolean handle(P request, Map.Entry<String, Class<? extends T>> entry);

    public Router(){
        Type genType = getClass().getGenericSuperclass();
        Class clazz = (Class) ((ParameterizedType) genType).getActualTypeArguments()[0];
        mSchemes.add(clazz.getSimpleName().toLowerCase());

        for (Map.Entry<String, Class<?>> entry : ROUTER_TABLE.entrySet()) {
            String url = entry.getKey();
            //filter url in router table
            if (canHandle(getScheme(url))) {
                tables.put(url, (Class<? extends T>) entry.getValue());
            }
        }
    }


    private boolean canHandle(String givenScheme){
        for(String requireScheme : mSchemes){
            if(requireScheme.endsWith(givenScheme)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canHandle(P request) {
        return canHandle(request.getScheme());
    }

    @Override
    public final boolean handle(P request) {
        try {
            Map.Entry<String, Class<? extends T>> result = match(request);
            return handle(request, result);
        } catch (RouteNotFoundException e) {
            Log.e("Router", e.getLocalizedMessage());
        }
        return false;
    }


    protected final Map.Entry<String, Class<? extends T>> match(P request) throws  RouteNotFoundException {
        List<String> givenPathSegs = request.getPath();
        OutLoop:
        for (Map.Entry<String, Class<? extends T>> entry : tables.entrySet()) {
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
}
