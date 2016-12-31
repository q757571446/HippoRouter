package com.hippo.router.sample.pager.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Kevin on 2016/11/26.
 */

public abstract class BaseActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initData();
        initWidget();
    }
    protected abstract int getLayoutId();
    protected  void initData(){};
    protected void initWidget(){};

}
