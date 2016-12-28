package com.example.hipporouter.extend;

import android.support.v4.app.Fragment;

import com.example.library.router.factory.RouterFactory;

import java.util.Map;

/**
 * Created by Kevin on 2016/12/28.
 */

public class FragmentRouterFactory implements RouterFactory<Fragment,FragmentRequest,FragmentRouter> {

    @Override
    public FragmentRouter buildInstance() {
        return null;
    }

    @Override
    public void initialize(Map<String, Class<? extends Fragment>> tables) {

    }
}
