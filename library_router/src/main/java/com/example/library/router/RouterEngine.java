package com.example.library.router;

import com.example.library.router.factory.ActivityRouterFactory;
import com.example.library.router.factory.RouterFactory;
import com.example.library.router.request.impl.Route;
import com.example.library.router.router.Router;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.library.router.router.IRouter.ROUTER_TABLE;

/**
 * Created by Kevin on 2016/12/27.
 */

public class RouterEngine {

    static final RouterEngine singleton = new RouterEngine();

    static List<Router> mRouters ;

    private RouterEngine(){
        mRouters = new LinkedList<>();
    }

    public static RouterEngine getSingleton(){
        return singleton;
    }

    private synchronized void addRouter(Router router){
        if(router == null)
            throw new NullPointerException("the router you register is null");
        //first remove all the duplicate routers
        List<Router> duplicateRouters = new ArrayList<>();
        for(Router r : mRouters){
            if(r.getClass().equals(router.getClass())){
                duplicateRouters.add(r);
            }
        }
        mRouters.removeAll(duplicateRouters);
        mRouters.add(router);
    }

    public synchronized void registerActivityRouter(ActivityRouterFactory factory) {
        registerRouter(factory);
    }

    public synchronized void registerRouter(RouterFactory factory) {
        Router router = factory.buildInstance();
        router.init(factory);
        addRouter(router);
    }

    public synchronized boolean process(Route route) {
        Router router = getRouter(route.getUrl());
        Map.Entry<String, Class> match = router.match(route);
        return router.handle(match);
    }

    private synchronized Router getRouter(String url) {
        for (Router router : mRouters) {
            if (router.canHandle(url)) {
                return router;
            }
        }
        throw new IllegalArgumentException(String.format("cannot find router to resolve: %s", url));
    }
}
