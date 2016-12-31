package com.hippo.router.router.impl.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import com.hippo.router.RouterClient;
import com.hippo.router.router.impl.Request;

/**
 * Created by Kevin on 2016/12/30.
 */
public class FragmentRequest extends Request{
    private Bundle mBundle;
    private int mAnimationIn;
    private int mAnimationOut;
    private int mAttachId;
    private FragmentManager mFragmentManager;

    public FragmentRequest(int containerId, FragmentManager manager, Bundle bundle, int animationIn, int animationOut, String url) {
        super(url);
        this.mAttachId = containerId;
        this.mFragmentManager = manager;
        this.mBundle = bundle;
        this.mAnimationIn = animationIn;
        this.mAnimationOut = animationOut;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public int getAnimationIn() {
        return mAnimationIn;
    }

    public int getAnimationOut() {
        return mAnimationOut;
    }

    public int getAttachId() {
        return mAttachId;
    }

    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    public static Builder from(FragmentManager manager, String fragment) {
        if (manager == null) {
            throw new IllegalArgumentException("Activity cannot be null");
        }
        if (TextUtils.isEmpty(fragment)) {
            throw new IllegalArgumentException("you must give a url to attach");
        }
        return new Builder(manager,fragment);
    }

    public static class Builder{
        FragmentManager mFragmentManager;
        Bundle mBundle;
        String mUrl;
        int mInAnimation = -1;
        int mOutAnimation = -1;

        public Builder(FragmentManager manager, String url) {
            mFragmentManager =  manager;
            mBundle = new Bundle();
            mUrl = url;
        }

        public Builder withParams(String key, Parcelable value){
            mBundle.putParcelable(key, value);
            return this;
        }

        public Builder withParams(String key, int value){
            mBundle.putInt(key, value);
            return this;
        }

        public Builder withParams(String key, double value){
            mBundle.putDouble(key, value);
            return this;
        }

        public Builder withParams(String key, float value){
            mBundle.putFloat(key, value);
            return this;
        }

        public Builder withParams(String key, char value){
            mBundle.putChar(key, value);
            return this;
        }

        public Builder withParams(String key, CharSequence value){
            mBundle.putCharSequence(key, value);
            return this;
        }

        public Builder withParams(String key, String value){
            mBundle.putString(key, value);
            return this;
        }

        public Builder withParams(String key, long value){
            mBundle.putLong(key, value);
            return this;
        }

        public Builder withAnimation(int inAnimation, int outAnimation){
            mInAnimation = inAnimation;
            mOutAnimation = outAnimation;
            return this;
        }

        public boolean attach(int attachViewId) {
            FragmentRequest request = new FragmentRequest(attachViewId,mFragmentManager, mBundle, mInAnimation, mOutAnimation, mUrl);
            return RouterClient.getSingleton().process(request);
        }
    }
}
