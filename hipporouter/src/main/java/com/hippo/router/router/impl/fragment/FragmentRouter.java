package com.hippo.router.router.impl.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.hippo.router.BuildConfig;
import com.hippo.router.exception.InvalidValueTypeException;
import com.hippo.router.router.impl.Router;
import com.hippo.router.router.impl.activity.ActivityRequest;

import java.util.List;
import java.util.Map;

import static com.hippo.router.utils.UrlUtils.getParameters;
import static com.hippo.router.utils.UrlUtils.getPathSegments;

/**
 * Created by Kevin on 2016/12/30.
 */

public class FragmentRouter extends Router<Fragment,FragmentRequest>{

    static final String TAG = "FragmentRouter";
    public static final String FRAGMENT_KEY_URL = "key_and_fragment_router_url";


    @Override
    protected boolean handle(FragmentRequest request, Map.Entry<String, Class<? extends Fragment>> entry) {
        try {
            int attachId = request.getAttachId();
            Fragment attachFragment = entry.getValue().newInstance();
            Bundle bundle = getBundle(request, entry);
            attachFragment.setArguments(bundle);
            request.getFragmentManager()
                    .beginTransaction()
//                    .setCustomAnimations(request.getAnimationIn(),request.getAnimationOut())
                    .add(attachId, attachFragment)
                    .commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected Bundle getBundle(FragmentRequest request, Map.Entry<String, Class<? extends Fragment>> entry) {
        Bundle bundle = new Bundle();
        bundle = setKeyValueInThePath(entry.getKey(), request.getUrl(), bundle);
        bundle = setOptionParams(request.getUrl(), bundle);
        bundle = setExtras(request.getBundle(), bundle);
        bundle.putString(FRAGMENT_KEY_URL, request.getUrl());
        return bundle;
    }

    protected Bundle setExtras(Bundle extras, Bundle bundle) {
        bundle.putAll(extras);
        return bundle;
    }

    protected Bundle setOptionParams(String url, Bundle bundle) {
        Map<String, String> queryParams = getParameters(url);
        for (String key : queryParams.keySet()) {
            bundle.putString(key, queryParams.get(key));
        }
        return bundle;
    }

    protected Bundle setKeyValueInThePath(String routeUrl, String givenUrl, Bundle bundle) {
        List<String> routePathSegs = getPathSegments(routeUrl);
        List<String> givenPathSegs = getPathSegments(givenUrl);
        for (int i = 0; i < routePathSegs.size(); i++) {
            String seg = routePathSegs.get(i);
            String inf = givenPathSegs.get(i);
            if (seg.startsWith(":")) {
                int indexOfLeft = seg.indexOf("{");
                int indexOfRight = seg.indexOf("}");
                String key = seg.substring(indexOfLeft + 1, indexOfRight);
                String val = inf;
                char typeChar = seg.charAt(1);
                switch (typeChar) {
                    //interger type
                    case 'i':
                        try {
                            int value = Integer.parseInt(val);
                            bundle.putInt(key, value);
                        } catch (Exception e) {
                            Log.e(TAG, "cannot parse integer " + val, e);
                            if (BuildConfig.DEBUG) {
                                throw new InvalidValueTypeException(givenUrl, val);
                            } else {
                                bundle.putInt(key, 0);
                            }
                        }
                        break;
                    case 'f':
                        //float type
                        try {
                            float value = Float.parseFloat(val);
                            bundle.putFloat(key, value);
                        } catch (Exception e) {
                            Log.e(TAG, "cannot parse float " + val, e);
                            if (BuildConfig.DEBUG) {
                                throw new InvalidValueTypeException(givenUrl, val);
                            } else {
                                bundle.putFloat(key, 0f);
                            }
                        }
                        break;
                    case 'l':
                        //long type
                        try {
                            long value = Long.parseLong(val);
                            bundle.putLong(key, value);
                        } catch (Exception e) {
                            Log.e(TAG, "cannot parse long " + val, e);
                            if (BuildConfig.DEBUG) {
                                throw new InvalidValueTypeException(givenUrl, val);
                            } else {
                                bundle.putLong(key, 0l);
                            }
                        }
                        break;
                    case 'd':
                        try {
                            double value = Double.parseDouble(val);
                            bundle.putDouble(key, value);
                        } catch (Exception e) {
                            Log.e(TAG, "cannot parse double " + val, e);
                            if (BuildConfig.DEBUG) {
                                throw new InvalidValueTypeException(givenUrl, val);
                            } else {
                                bundle.putDouble(key, 0d);
                            }
                        }
                        break;
                    case 'c':
                        try {
                            char value = val.charAt(0);
                            bundle.putChar(key, value);
                        } catch (Exception e) {
                            Log.e(TAG, "cannot parse character" + val, e);
                            if (BuildConfig.DEBUG) {
                                throw new InvalidValueTypeException(givenUrl, val);
                            } else {
                                bundle.putChar(key, ' ');
                            }
                        }
                        break;
                    case 's':
                    default:
                        bundle.putString(key, val);
                }
            }

        }
        return bundle;
    }
}
