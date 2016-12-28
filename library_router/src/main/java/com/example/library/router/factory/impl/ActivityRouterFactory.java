package com.example.library.router.factory.impl;

import android.app.Activity;

import com.example.library.router.factory.RouterFactory;
import com.example.library.router.router.impl.activity.ActivityRequest;
import com.example.library.router.router.impl.activity.ActivityRouter;

/**
 * Created by kevin on 16-11-17.
 */

public abstract class ActivityRouterFactory implements RouterFactory<Activity,ActivityRequest,ActivityRouter> {

    @Override
    public ActivityRouter buildInstance() {
        return new ActivityRouter();
    }

}
