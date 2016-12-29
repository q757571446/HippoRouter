package com.example.library.router.router.impl.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.library.router.BuildConfig;
import com.example.library.router.exception.InvalidValueTypeException;
import com.example.library.router.router.impl.Router;

import java.util.List;
import java.util.Map;

import static com.example.library.router.utils.UrlUtils.getParameters;
import static com.example.library.router.utils.UrlUtils.getPathSegments;
import static com.example.library.router.utils.UrlUtils.getScheme;

/**
 * Created by kevin on 16-11-17.
 */

public class ActivityRouter extends Router<Activity,ActivityRequest> {

    static final String TAG = "ActivityRouter";
    public static final String ACTIVITY_KEY_URL = "key_and_activity_router_url";
    protected Context mContext;

    @Override
    protected String canHandle() {
        return "activity";
    }

    @Override
    protected boolean handle(ActivityRequest route, Map.Entry<String, Class<? extends Activity>> entry) {
        mContext = route.getContext();
        Intent intent = getIntent(route, entry);
        mContext.startActivity(intent);
        if (route.getAnimationIn() != -1 && route.getAnimationOut() != -1 && mContext instanceof Activity) {
            ((Activity) mContext).overridePendingTransition(route.getAnimationIn(),route.getAnimationOut());
        }
        return false;
    }

    protected Intent getIntent(ActivityRequest route, Map.Entry<String, Class<? extends Activity>> entry) {
        Intent intent = new Intent(mContext, entry.getValue());
        //find the key value in the path
        intent = setKeyValueInThePath(entry.getKey(), route.getUrl(), intent);
        intent = setOptionParams(route.getUrl(), intent);
        intent = setExtras(route.getBundle(), intent);
        intent.putExtra(ACTIVITY_KEY_URL, route.getUrl());
        if (mContext instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | route.getFlags());
        } else {
            intent.setFlags(route.getFlags());
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
                int indexOfValue = inf.indexOf(":");
                String key = seg.substring(indexOfLeft + 1, indexOfRight);
                String val = inf.substring(indexOfValue+1);
                char typeChar = seg.charAt(1);
                switch (typeChar) {
                    //interger type
                    case 'i':
                        try {
                            int value = Integer.parseInt(val);
                            intent.putExtra(key, value);
                        } catch (Exception e) {
                            Log.e(TAG, "解析整形类型失败 " + val, e);
                            if (BuildConfig.DEBUG) {
                                throw new InvalidValueTypeException(givenUrl, val);
                            } else {
                                //如果是在release情况下则给一个默认值
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
                            Log.e(TAG, "解析浮点类型失败 " + val, e);
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
                            Log.e(TAG, "解析长整形失败 " + val, e);
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
                            Log.e(TAG, "解析double类型失败 " + val, e);
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
                            Log.e(TAG, "解析Character类型失败" + val, e);
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
