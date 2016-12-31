package com.hippo.router.factory.impl;

import android.app.Activity;

import com.hippo.router.factory.RouterFactory;
import com.hippo.router.router.impl.activity.ActivityRequest;
import com.hippo.router.router.impl.activity.ActivityRouter;

/**
 * Created by kevin on 16-11-17.
 */

public abstract class ActivityRouterFactory implements RouterFactory<Activity,ActivityRequest,ActivityRouter> {

    @Override
    public ActivityRouter buildInstance() {
        return new ActivityRouter();
    }

}
