package com.example.library.router.router;
import android.text.TextUtils;

import com.example.library.router.exception.RouteNotFoundException;
import com.example.library.router.factory.RouterInitializer;
import com.example.library.router.request.impl.Route;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.library.router.utils.UrlUtils.getHost;
import static com.example.library.router.utils.UrlUtils.getPathSegments;

public abstract class Router implements IRouter {

    protected Map<String, Class> mRouterTables = new HashMap<>();


    public Map<String, Class> getRouterTables() {
        return mRouterTables;
    }

    @Override
    public void init(RouterInitializer initializer) {
        //添加至全局唯一路由表
        initializer.put(ROUTER_TABLE);
        //将路由表分成对应类别的路由表
        for (Map.Entry<String, Class> entry : ROUTER_TABLE.entrySet()) {
            String url = entry.getKey();
            Class<?> clazz = entry.getValue();
            if (canHandle(url)) {
                mRouterTables.put(url, clazz);
            }
        }
    }

    @Override
    public final Map.Entry<String, Class> match(Route route) {
        Map.Entry<String, Class> fromMemory = findFromMemory(route);
        if (fromMemory != null) {
            return fromMemory;
        }
        Map.Entry<String, Class> fromDisk = findFromDisk(route);
        if (fromDisk != null) {
            return fromDisk;
        }
        throw new RouteNotFoundException(route);
    }

    private Map.Entry<String, Class> findFromMemory(Route route) {
        return findMatchRoute(route);
    }

    private Map.Entry<String, Class> findFromDisk(Route route) {
        //加载进内存
        RouterInitializer initializer = findRouterBinder(route);
        init(initializer);
        return findMatchRoute(route);
    }

    protected Map.Entry<String, Class> findMatchRoute(Route route) {
        List<String> givenPathSegs = route.getPath();
        OutLoop:
        for (Map.Entry<String, Class> entry : mRouterTables.entrySet()) {
            String routeUrl = entry.getKey();
            List<String> routePathSegs = getPathSegments(routeUrl);
            if (!TextUtils.equals(getHost(routeUrl), route.getHost())) {
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

    private RouterInitializer findRouterBinder(Route route) {

        return new RouterInitializer() {
            @Override
            public void put(Map<String, Class> tables) {

            }
        };
    }

}
