package com.example.library.router.factory;

import com.example.library.router.router.impl.FragmentRouter;

import java.util.Map;

/**
 * Created by kevin on 16-11-17.
 */

public abstract class FragmentRouterFactory implements RouterFactory<FragmentRouter>{


    @Override
    public final FragmentRouter buildInstance() {
        return new FragmentRouter();
    }
}
