package com.example.library.router;

import com.example.library.router.factory.RouterFactory;
import com.example.library.router.router.impl.Request;
import com.example.library.router.router.IRouter;
import com.example.library.router.router.impl.activity.ActivityRouter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.library.router.router.IRouter.ROUTER_TABLE;

/**
 * Created by Kevin on 2016/12/27.
 */

public class RouterClient {

    static final RouterClient singleton = new RouterClient();

    static List<IRouter> mRouters ;

    private RouterClient(){
        mRouters = new LinkedList<>();
        //default add activity router
        addRouter(new ActivityRouter());
    }

    public static RouterClient getSingleton(){
        return singleton;
    }

    private synchronized void addRouter(IRouter router){
        if(router == null)
            throw new NullPointerException("the router you register is null");
        //first remove all the duplicate routers
        List<IRouter> duplicateRouters = new ArrayList<>();
        for(IRouter r : mRouters){
            if(r.getClass().equals(router.getClass())){
                duplicateRouters.add(r);
            }
        }
        mRouters.removeAll(duplicateRouters);
        mRouters.add(router);
    }

    public synchronized void registerRouter(RouterFactory factory) {
        IRouter router = factory.buildInstance();
        factory.initialize(ROUTER_TABLE);
        addRouter(router);
    }

    public synchronized boolean process(Request request) {
        IRouter router = getRouter(request.getUrl());
        return router.handle(request);
    }

    private synchronized IRouter getRouter(String url) {
        for (IRouter router : mRouters) {
            if (router.canHandle(url)) {
                return router;
            }
        }
        throw new IllegalArgumentException(String.format("cannot find router to resolve: %s", url));
    }
}
