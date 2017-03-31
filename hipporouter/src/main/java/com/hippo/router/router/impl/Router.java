package com.hippo.router.router.impl;


import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.hippo.router.exception.RouteNotFoundException;
import com.hippo.router.router.IRouter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
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
    Class clazz;
    List<String> mSchemes = new ArrayList<>();


    protected abstract boolean handle(P request, Map.Entry<String, Class<? extends T>> entry);

    public Router(){
        Type genType = getClass().getGenericSuperclass();
        clazz = (Class) ((ParameterizedType) genType).getActualTypeArguments()[0];
        mSchemes.add(clazz.getSimpleName().toLowerCase());
    }

    private  Map<String, Class<? extends T>> getRouterTables(){
        // ensure that activity://login/username is rank top over activity://login/:username
        Map<String, Class<? extends T>> tables = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String s1, String t1) {
                return s1.compareTo(t1)*-1;
            }
        });
        for (Map.Entry<String, Class<?>> entry : ROUTER_TABLE.entrySet()) {
            String url = entry.getKey();
            Class<?> value = entry.getValue();
            //filter url in router table
            if (canHandle(getScheme(url))&& clazz.isAssignableFrom(value)) {
                tables.put(getWrapperUrl(url), (Class<? extends T>) value);
            }
        }
        return tables;
    }

    private String getWrapperUrl(String oldRoute){
        StringBuffer newPath = new StringBuffer();
        for(String oldSeg : getPathSegments(oldRoute)){
            if(oldSeg.startsWith(":")){
                int indexOfColo = oldSeg.indexOf(":");
                int indexOfLeft = oldSeg.indexOf("{");
                int indexOfRight = oldSeg.indexOf("}");
                if(indexOfLeft == -1 ^ indexOfRight == -1){
                    throw new IllegalArgumentException("the router you registered"+oldRoute+"lack {}");
                }else if(indexOfLeft == -1 && indexOfRight == -1){
                    String newSeg = new StringBuffer()
                            .append(":")
                            .append("{")
                            .append(oldSeg.substring(indexOfColo + 1).trim())
                            .append("}")
                            .toString();
                    newPath.append("/")
                            .append(newSeg);
                }else{
                    newPath.append("/")
                            .append(oldSeg);
                }
            }else{
                newPath.append("/")
                        .append(oldSeg);
            }
        }
        String newRoute = Uri.parse(oldRoute).buildUpon()
                .path(newPath.toString())
                .build().toString();
        return Uri.decode(newRoute);
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
            Map.Entry<String, Class<? extends T>> result = match(request,getRouterTables());
            return handle(request, result);
        } catch (RouteNotFoundException e) {
            Log.e("Router", e.getLocalizedMessage());
        }
        return false;
    }


    protected final Map.Entry<String, Class<? extends T>> match(P request,Map<String, Class<? extends T>> tables) throws  RouteNotFoundException {
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
                String routePathSeg = routePathSegs.get(i);
                String givenPathSeg = givenPathSegs.get(i);

                if(routePathSeg.startsWith(":")){

                }else if (!TextUtils.equals(routePathSeg, givenPathSeg)) {
                    continue;
                }
            }
            return entry;
        }
        throw new RouteNotFoundException(request.getUrl());
    }
}
