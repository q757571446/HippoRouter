package com.hippo.router.sample;

import android.app.Activity;
import android.app.Application;

import com.hippo.router.sample.pager.MainActivity;
import com.hippo.router.RouterClient;
import com.hippo.router.factory.impl.ActivityRouterFactory;


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
