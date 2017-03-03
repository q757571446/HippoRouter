package com.hippo.router.sample.pager.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kevin on 2016/11/26.
 */

public abstract class BaseActivity extends AppCompatActivity{
    View mRootView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootView = LayoutInflater.from(this).inflate(getLayoutId(), null);
        setContentView(mRootView);
        initData();
        initWidget();
    }
    protected View getRootView(){
        return mRootView;
    }
    protected abstract int getLayoutId();
    protected  void initData(){};
    protected void initWidget(){};

}
