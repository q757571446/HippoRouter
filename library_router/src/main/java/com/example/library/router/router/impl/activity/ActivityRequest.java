package com.example.library.router.router.impl.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import com.example.library.router.RouterClient;
import com.example.library.router.router.impl.Request;

/**
 * Created by kevin on 16-11-16.
 */

public class ActivityRequest extends Request {

    private int mFlags;
    private Bundle mBundle;
    private int mAnimationIn;
    private int mAnimationOut;
    private Context mContext;

    private ActivityRequest(Context context, int flags, Bundle bundle, int animationIn, int animationOut, String url) {
        super(url);
        this.mContext = context;
        this.mBundle = bundle;
        this.mAnimationIn = animationIn;
        this.mAnimationOut = animationOut;
        this.mFlags = flags;
    }

    public int getFlags() {
        return mFlags;
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

    public Context getContext() {
        return mContext;
    }

    public static Builder from(Context from, String to) {
        if (from == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        if (TextUtils.isEmpty(to)) {
            throw new IllegalArgumentException("you must give a url to jump");
        }
        return new Builder(from,to);
    }

    public static class Builder{
        Context mContext;
        Bundle mBundle;
        String mUrl;
        int mInAnimation = -1;
        int mOutAnimation = -1;
        int mFlags = 0;

        public Builder(Context context, String url) {
            mContext = context;
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

        public Builder withFlags(int flags){
            mFlags = flags;
            return this;
        }

        /**
         * set the animation of transform
         * @param inAnimation
         * @param outAnimation
         * @return
         */
        public Builder withAnimation(int inAnimation, int outAnimation){
            mInAnimation = inAnimation;
            mOutAnimation = outAnimation;
            return this;
        }

        public boolean open() {
            ActivityRequest route = new ActivityRequest(mContext, mFlags, mBundle, mInAnimation, mOutAnimation, mUrl);
            return RouterClient.getSingleton().process(route);
        }
    }
}
