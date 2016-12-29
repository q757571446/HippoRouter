package com.example.hipporouter.extend;

import android.support.v4.app.Fragment;

import com.example.library.router.router.impl.Router;

import java.util.Map;

/**
 * Created by Kevin on 2016/12/28.
 */

public class FragmentRouter extends Router<Fragment,FragmentRequest>{


    @Override
    protected String canHandle() {
        return "fragment";
    }

    @Override
    protected boolean handle(FragmentRequest request, Map.Entry<String, Class<? extends Fragment>> entry) {
        return false;
    }
}
