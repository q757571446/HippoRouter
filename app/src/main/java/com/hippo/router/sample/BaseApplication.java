package com.hippo.router.sample;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.hippo.router.RouterClient;
import com.hippo.router.factory.impl.ActivityRouterFactory;
import com.hippo.router.factory.impl.FragmentRouterFactory;
import com.hippo.router.sample.pager.activity.OneActivity;
import com.hippo.router.sample.pager.fragment.OneFragment;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kevin on 2016/11/26.
 */

public class BaseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        RouterClient.getSingleton().registerActivityRouter(new ActivityRouterFactory() {
            @Override
            public void initialize(Map<String, Class<? extends Activity>> tables) {
                tables.put("activity://one", OneActivity.class);
            }
        });

        RouterClient.getSingleton().registerFragmentRouter(new FragmentRouterFactory() {
            @Override
            public void initialize(Map<String, Class<? extends Fragment>> tables) {
                tables.put("fragment://one", OneFragment.class);
            }
        });
    }

}
