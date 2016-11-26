package com.example.hipporouter;

import android.app.Application;

import com.example.hipporouter.extend.MyRouter;
import com.example.hipporouter.pager.ContentActivity;
import com.example.hipporouter.pager.ContentFragment;
import com.example.library.router.RouterManager;
import com.example.library.router.factory.ActivityRouterFactory;
import com.example.library.router.factory.FragmentRouterFactory;
import com.example.library.router.factory.RouterFactory;
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

        RouterManager.getSingleton().registerActivityRouter(new ActivityRouterFactory() {
            @Override
            public Map<String, Class> getRouterTable() {
                Map<String, Class> map = new HashMap<>();
                map.put("activity://content", ContentActivity.class);
                map.put("activity://wrap", WrapActivity.class);
                return map;
            }
        });

        RouterManager.getSingleton().registerFragmentRouter(new FragmentRouterFactory() {
            @Override
            public Map<String, Class> getRouterTable() {
                Map<String, Class> map = new HashMap<>();
                map.put("fragment://content#wrap", ContentFragment.class);
                return map;
            }
        });

        RouterManager.getSingleton().registerRouter(new RouterFactory() {
            @Override
            public Router buildInstance() {
                return new MyRouter();
            }

            @Override
            public Map<String, Class> getRouterTable() {
                Map<String, Class> map = new HashMap<>();
                // add router table
                return map;
            }
        });
    }
}
