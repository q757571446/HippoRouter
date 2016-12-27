package com.example.library.router;

import com.example.library.router.factory.ActivityRouterFactory;
import com.example.library.router.factory.FragmentRouterFactory;
import com.example.library.router.router.Router;
import com.example.library.router.factory.RouterFactory;
import com.example.library.router.router.impl.ActivityRouter;
import com.example.library.router.router.impl.FragmentRouter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by kevin on 16-11-16.
 * deal the manipulation of Route with the help of IRouter
 */

public class RouterManager {
    private static final RouterManager singleton = new RouterManager();

    static List<Router> mRouters ;

    private RouterManager(){
        mRouters = new LinkedList<>();
        addRouter(new ActivityRouter());//Add default router
        addRouter(new FragmentRouter());//Add default router
    }

    public static RouterManager getSingleton(){
        return singleton;
    }

    private synchronized void addRouter(Router router){
        if(router != null){
            //first remove all the duplicate routers
            List<Router> duplicateRouters = new ArrayList<>();
            for(Router r : mRouters){
                if(r.getClass().equals(router.getClass())){
                    duplicateRouters.add(r);
                }
            }
            mRouters.removeAll(duplicateRouters);
            mRouters.add(router);
        } else {
            Timber.e(new NullPointerException("The Route" +
                    "is null" +
                    ""), "");
        }
    }

    public synchronized void registerActivityRouter(ActivityRouterFactory factory, String... schemes) {
        registerRouter(factory,schemes);
    }

    public synchronized void registerFragmentRouter(FragmentRouterFactory factory, String... schemes) {
        registerRouter(factory,schemes);
    }


    public synchronized void registerRouter(RouterFactory factory, String... schemes) {
        Router router = factory.buildInstance();
        Map table = factory.getRouterTable();
        if(schemes != null && schemes.length > 0){
            router.setMatchSchemes(schemes);
        }
        router.ROUTER_TABLE.putAll(table);
        addRouter(router);
    }

    public Router getRouter(String url) {
        for (Router router : mRouters) {
            if (router.canOpenTheUrl(url)) {
                return router;
            }
        }
        throw new IllegalArgumentException("cannot find router to resolve "+url);
    }
}
