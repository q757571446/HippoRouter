package com.hippo.router;

import com.hippo.router.factory.RouterFactory;
import com.hippo.router.factory.impl.ActivityRouterFactory;
import com.hippo.router.factory.impl.FragmentRouterFactory;
import com.hippo.router.router.IRequest;
import com.hippo.router.router.impl.Request;
import com.hippo.router.router.IRouter;
import com.hippo.router.router.impl.activity.ActivityRouter;
import com.hippo.router.router.impl.fragment.FragmentRouter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.hippo.router.router.IRouter.ROUTER_TABLE;

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
        addRouter(new FragmentRouter());
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

    public synchronized void registerFragmentRouter(FragmentRouterFactory factory) {
        registerRouter(factory);
    }

    public synchronized void registerActivityRouter(ActivityRouterFactory factory) {
        registerRouter(factory);
    }

    public synchronized void registerRouter(RouterFactory factory) {
        IRouter router = factory.buildInstance();
        factory.initialize(ROUTER_TABLE);
        addRouter(router);
    }

    public synchronized boolean process(IRequest request) {
        IRouter router = getRouter(request);
        return router.handle(request);
    }

    private synchronized IRouter getRouter(IRequest request) {
        for (IRouter router : mRouters) {
            if (router.canHandle(request)) {
                return router;
            }
        }
        throw new IllegalArgumentException(String.format("cannot find router to resolve: %s", request.getUrl()));
    }
}
