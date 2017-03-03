package com.hippo.router.router.impl.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hippo.router.BuildConfig;
import com.hippo.router.exception.InvalidValueTypeException;
import com.hippo.router.router.impl.Router;

import java.util.List;
import java.util.Map;

import static com.hippo.router.utils.UrlUtils.getParameters;
import static com.hippo.router.utils.UrlUtils.getPathSegments;

/**
 * Created by kevin on 16-11-17.
 */

public class ActivityRouter extends Router<Activity,ActivityRequest> {

    static final String TAG = "ActivityRouter";
    public static final String ACTIVITY_KEY_URL = "key_and_activity_router_url";


    @Override
    protected boolean handle(ActivityRequest request, Map.Entry<String, Class<? extends Activity>> entry) {
        Intent intent = getIntent(request, entry);
        request.getContext().startActivity(intent);
        if (request.getAnimationIn() != -1 && request.getAnimationOut() != -1 && request.getContext() instanceof Activity) {
            ((Activity) request.getContext()).overridePendingTransition(request.getAnimationIn(),request.getAnimationOut());
        }
        return true;
    }

    protected Intent getIntent(ActivityRequest request, Map.Entry<String, Class<? extends Activity>> entry) {
        Intent intent = new Intent(request.getContext(), entry.getValue());
        //find the key value in the path
        intent = setKeyValueInThePath(entry.getKey(), request.getUrl(), intent);
        intent = setOptionParams(request.getUrl(), intent);
        intent = setExtras(request.getBundle(), intent);
        intent.putExtra(ACTIVITY_KEY_URL, request.getUrl());
        if (request.getContext() instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | request.getFlags());
        } else {
            intent.setFlags(request.getFlags());
        }
        return intent;
    }

    protected Intent setExtras(Bundle bundle, Intent intent) {
        intent.putExtras(bundle);
        return intent;
    }

    protected Intent setOptionParams(String url, Intent intent) {
        Map<String, String> queryParams = getParameters(url);
        for (String key : queryParams.keySet()) {
            intent.putExtra(key, queryParams.get(key));
        }
        return intent;
    }

    protected Intent setKeyValueInThePath(String routeUrl, String givenUrl, Intent intent) {
        List<String> routePathSegs = getPathSegments(routeUrl);
        List<String> givenPathSegs = getPathSegments(givenUrl);
        for (int i = 0; i < routePathSegs.size(); i++) {
            String seg = routePathSegs.get(i);
            String inf = givenPathSegs.get(i);
            if (seg.startsWith(":")) {
                int indexOfLeft = seg.indexOf("{");
                int indexOfRight = seg.indexOf("}");

                String key = seg.substring(indexOfLeft + 1, indexOfRight).trim();
                String val = inf;

                char typeChar = seg.charAt(1);
                switch (typeChar) {
                    //interger type
                    case 'i':
                        try {
                            int value = Integer.parseInt(val);
                            intent.putExtra(key, value);
                        } catch (Exception e) {
                            Log.e(TAG, "cannot parse integer " + val, e);
                            if (BuildConfig.DEBUG) {
                                throw new InvalidValueTypeException(givenUrl, val);
                            } else {
                                intent.putExtra(key, 0);
                            }
                        }
                        break;
                    case 'f':
                        //float type
                        try {
                            float value = Float.parseFloat(val);
                            intent.putExtra(key, value);
                        } catch (Exception e) {
                            Log.e(TAG, "cannot parse float " + val, e);
                            if (BuildConfig.DEBUG) {
                                throw new InvalidValueTypeException(givenUrl, val);
                            } else {
                                intent.putExtra(key, 0f);
                            }
                        }
                        break;
                    case 'l':
                        //long type
                        try {
                            long value = Long.parseLong(val);
                            intent.putExtra(key, value);
                        } catch (Exception e) {
                            Log.e(TAG, "cannot parse long " + val, e);
                            if (BuildConfig.DEBUG) {
                                throw new InvalidValueTypeException(givenUrl, val);
                            } else {
                                intent.putExtra(key, 0l);
                            }
                        }
                        break;
                    case 'd':
                        try {
                            double value = Double.parseDouble(val);
                            intent.putExtra(key, value);
                        } catch (Exception e) {
                            Log.e(TAG, "cannot parse double " + val, e);
                            if (BuildConfig.DEBUG) {
                                throw new InvalidValueTypeException(givenUrl, val);
                            } else {
                                intent.putExtra(key, 0d);
                            }
                        }
                        break;
                    case 'c':
                        try {
                            char value = val.charAt(0);
                            intent.putExtra(key, value);
                        } catch (Exception e) {
                            Log.e(TAG, "cannot parse character" + val, e);
                            if (BuildConfig.DEBUG) {
                                throw new InvalidValueTypeException(givenUrl, val);
                            } else {
                                intent.putExtra(key, ' ');
                            }
                        }
                        break;
                    case 's':
                    default:
                        intent.putExtra(key, val);
                }
            }

        }
        return intent;
    }


}
