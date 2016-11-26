package com.example.library.router.factory;

import com.example.library.router.router.Router;
import com.example.library.router.router.impl.ActivityRouter;

import java.util.Map;

/**
 * Created by kevin on 16-11-17.
 */

public abstract class ActivityRouterFactory implements RouterFactory<ActivityRouter> {
    @Override
    public final ActivityRouter buildInstance() {
        return new ActivityRouter();
    }
}
