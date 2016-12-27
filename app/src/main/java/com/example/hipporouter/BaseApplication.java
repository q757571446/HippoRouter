package com.example.hipporouter;

import android.app.Application;

import com.example.hipporouter.extend.MyRouter;
import com.example.hipporouter.pager.ContentActivity;
import com.example.hipporouter.pager.ContentFragment;
import com.example.library.router.RouterEngine;
import com.example.library.router.RouterManager;
import com.example.library.router.factory.ActivityRouterFactory;
import com.example.library.router.factory.FragmentRouterFactory;
import com.example.library.router.factory.RouterFactory;
import com.example.library.router.router.IRouter;
import com.example.library.router.router.Router;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 2016/11/26.
 */

public class BaseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        RouterEngine.getSingleton().registerRouter(new ActivityRouterFactory() {
            @Override
            public void put(Map<String, Class> tables) {

            }
        });
    }
}
