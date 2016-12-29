package com.example.hipporouter;

import android.app.Activity;
import android.app.Application;

import com.example.hipporouter.pager.MainActivity;
import com.example.library.router.RouterClient;
import com.example.library.router.factory.impl.ActivityRouterFactory;


import java.util.Map;

/**
 * Created by Kevin on 2016/11/26.
 */

public class BaseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        RouterClient.getSingleton().registerRouter(new ActivityRouterFactory() {
            @Override
            public void initialize(Map<String, Class<? extends Activity>> tables) {
                tables.put("activity://main", MainActivity.class);
            }
        });

    }
}
