package com.example.hipporouter;

import android.view.View;

import com.example.hipporouter.pager.base.BaseActivity;
import com.example.library.router.router.impl.activity.ActivityRequest;

/**
 * Created by Kevin on 2016/12/29.
 */

public class SplashActivity extends BaseActivity{
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initWidget() {
        super.initWidget();


        findViewById(R.id.btn_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityRequest.from(SplashActivity.this, "activity://main")
                        .withParams("token", "this is token")
                        .open();
            }
        });
    }
}
