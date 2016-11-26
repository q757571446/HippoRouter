package com.example.library.router.router;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.library.router.BuildConfig;
import com.example.library.router.exception.InvalidValueTypeException;
import com.example.library.router.exception.RouteNotFoundException;
import com.example.library.router.factory.RouterBinder;
import com.example.library.router.factory.RouterFactory;
import com.example.library.router.request.impl.RouterRequest;
import com.example.library.router.utils.UrlUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import timber.log.Timber;

import static android.content.ContentValues.TAG;
import static com.example.library.router.utils.UrlUtils.getHost;
import static com.example.library.router.utils.UrlUtils.getPathSegments;

/**
 * Created by kevin on 16-11-16.
 */

public abstract class Router implements IRouter{

    protected List<String> MATCH_SCHEMES = new ArrayList<>();

    public Router() {
        MATCH_SCHEMES.add(getDefaultScheme());
    }
    protected String matchedRoute;

    @Override
    public boolean canOpenTheUrl(String url) {
        for(String scheme : MATCH_SCHEMES) {
            if(TextUtils.equals(scheme, UrlUtils.getScheme(url))){
                return true;
            }
        }
        return false;
    }

    public void setMatchScheme(String scheme) {
        MATCH_SCHEMES.clear();
        MATCH_SCHEMES.add(scheme);
    }

    public void setMatchSchemes(String... schemes){
        MATCH_SCHEMES.clear();
        List<String> list = Arrays.asList(schemes);
        list.remove("");
        list.remove(null);
        MATCH_SCHEMES.addAll(list);
    }

    public abstract String getDefaultScheme();

    private Map<String, Class> getRouterTable(Map<String,Class> map) {
        Map<String, Class> table = new HashMap<>();
        for (Map.Entry<String, Class> entry : map.entrySet()) {
            String url = entry.getKey();
            Class<?> clazz = entry.getValue();
            if (canOpenTheUrl(url)) {
                table.put(url, clazz);
            }
        }
        return table;
    }

    @Override
    public Map<String, Class> getRouterTableFromMemory() {
        return getRouterTable(ROUTER_TABLE);
    }

    protected Map<String, Class> getRouterTableFromDisk(RouterRequest request) throws Exception {
        Map<String, Class> generateTable = findGenerateClass(request.getScheme(),request.getHost()).getRouterTable();
        return getRouterTable(generateTable);
    }

    protected RouterBinder findGenerateClass(String scheme,String host) throws Exception {
        String className = new StringBuffer("com.example.router.")
                .append(toUpperCaseFirstOne(scheme))
                .append(toUpperCaseFirstOne(host))
                .append("_RouterBinding")
                .toString();
        Timber.d("router", "load generate router class>>>"+className);
        return (RouterBinder) Class.forName(className).newInstance();
    }


    protected Intent setExtras(Bundle bundle, Intent intent) {
        intent.putExtras(bundle);
        return intent;
    }

    protected Intent setOptionParams(String url, Intent intent) {
        Map<String, String> queryParams = UrlUtils.getParameters(url);
        for (String key : queryParams.keySet()) {
            intent.putExtra(key, queryParams.get(key));
        }
        return intent;
    }

    protected Class findMatchClass(RouterRequest request) {
        Class matchClassFromMemory = findMatchClassFromMemory(request);
        if (matchClassFromMemory != null) {
            return matchClassFromMemory;
        }
        Class matchClassFromDisk = findMatchClassFromDisk(request);
        if (matchClassFromDisk != null) {
            return matchClassFromDisk;
        }
        throw new RuntimeException("cannot find router url"+request.getUrl());
    }

    protected Class findMatchClassFromMemory(RouterRequest request) {
        Map<String, Class> routerTableFromMemory = getRouterTableFromMemory();
        matchedRoute = findMatchRequest(routerTableFromMemory,request);
        return routerTableFromMemory.get(matchedRoute);
    }


    protected Class findMatchClassFromDisk(RouterRequest request) {
        try {
            Map<String, Class> routerTableFromDisk = getRouterTableFromDisk(request);
            ROUTER_TABLE.putAll(routerTableFromDisk);// add disk map to memeory
            matchedRoute = findMatchRequest(routerTableFromDisk,request);
            return routerTableFromDisk.get(matchedRoute);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * match host and path
     *
     * @param routerTable
     * @param request
     * @return
     */
    protected String findMatchRequest(Map<String, Class> routerTable, RouterRequest request) {
        //find in memeroy
        List<String> givenPathSegs = request.getPath();
        OutLoop:
        for (String routeUrl : routerTable.keySet()) {
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
            //find the match route
            return routeUrl;
        }
        return null;
    }

    /**
     * find the key value in the path and set them in the intent
     *
     * @param routeUrl the matched route path
     * @param givenUrl the given path
     * @param intent   the intent
     * @return the intent
     */
    protected Intent setKeyValueInThePath(String routeUrl, String givenUrl, Intent intent) {
        List<String> routePathSegs = getPathSegments(routeUrl);
        List<String> givenPathSegs = getPathSegments(givenUrl);
        for (int i = 0; i < routePathSegs.size(); i++) {
            String seg = routePathSegs.get(i);
            if (seg.startsWith(":")) {
                int indexOfLeft = seg.indexOf("{");
                int indexOfRight = seg.indexOf("}");
                String key = seg.substring(indexOfLeft + 1, indexOfRight);
                char typeChar = seg.charAt(1);
                switch (typeChar) {
                    //interger type
                    case 'i':
                        try {
                            int value = Integer.parseInt(givenPathSegs.get(i));
                            intent.putExtra(key, value);
                        } catch (Exception e) {
                            Log.e(TAG, "解析整形类型失败 " + givenPathSegs.get(i), e);
                            if (BuildConfig.DEBUG) {
                                throw new InvalidValueTypeException(givenUrl, givenPathSegs.get(i));
                            } else {
                                //如果是在release情况下则给一个默认值
                                intent.putExtra(key, 0);
                            }
                        }
                        break;
                    case 'f':
                        //float type
                        try {
                            float value = Float.parseFloat(givenPathSegs.get(i));
                            intent.putExtra(key, value);
                        } catch (Exception e) {
                            Log.e(TAG, "解析浮点类型失败 " + givenPathSegs.get(i), e);
                            if (BuildConfig.DEBUG) {
                                throw new InvalidValueTypeException(givenUrl, givenPathSegs.get(i));
                            } else {
                                intent.putExtra(key, 0f);
                            }
                        }
                        break;
                    case 'l':
                        //long type
                        try {
                            long value = Long.parseLong(givenPathSegs.get(i));
                            intent.putExtra(key, value);
                        } catch (Exception e) {
                            Log.e(TAG, "解析长整形失败 " + givenPathSegs.get(i), e);
                            if (BuildConfig.DEBUG) {
                                throw new InvalidValueTypeException(givenUrl, givenPathSegs.get(i));
                            } else {
                                intent.putExtra(key, 0l);
                            }
                        }
                        break;
                    case 'd':
                        try {
                            double value = Double.parseDouble(givenPathSegs.get(i));
                            intent.putExtra(key, value);
                        } catch (Exception e) {
                            Log.e(TAG, "解析double类型失败 " + givenPathSegs.get(i), e);
                            if (BuildConfig.DEBUG) {
                                throw new InvalidValueTypeException(givenUrl, givenPathSegs.get(i));
                            } else {
                                intent.putExtra(key, 0d);
                            }
                        }
                        break;
                    case 'c':
                        try {
                            char value = givenPathSegs.get(i).charAt(0);
                        } catch (Exception e) {
                            Log.e(TAG, "解析Character类型失败" + givenPathSegs.get(i), e);
                            if (BuildConfig.DEBUG) {
                                throw new InvalidValueTypeException(givenUrl, givenPathSegs.get(i));
                            } else {
                                intent.putExtra(key, ' ');
                            }
                        }
                        break;
                    case 's':
                    default:
                        intent.putExtra(key, givenPathSegs.get(i));
                }
            }

        }
        return intent;
    }


    //首字母转大写
    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
}
