package com.hippo.router.sample.pager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hippo.router.router.impl.activity.ActivityRequest;
import com.hippo.router.sample.R;
import com.hippo.router.sample.pager.base.BaseActivity;

public class LaunchActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch;
    }


    @Override
    protected void initWidget() {
        super.initWidget();

        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        for (int i = 0; i < container.getChildCount(); i++) {
            final View view = container.getChildAt(i);
            if (view instanceof TextView) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityRequest.from(LaunchActivity.this,((TextView) view).getText().toString().trim())
                                .open();
                    }
                });
            }
        }
    }


}
