package com.hippo.router.factory.impl;


import android.app.Fragment;
import com.hippo.router.factory.RouterFactory;
import com.hippo.router.router.impl.fragment.FragmentRequest;
import com.hippo.router.router.impl.fragment.FragmentRouter;

/**
 * Created by Kevin on 2016/12/30.
 */

public abstract class FragmentRouterFactory implements RouterFactory<Fragment,FragmentRequest,FragmentRouter> {
    @Override
    public FragmentRouter buildInstance() {
        return new FragmentRouter();
    }
}
