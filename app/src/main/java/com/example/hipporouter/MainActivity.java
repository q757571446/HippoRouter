package com.example.hipporouter;

import android.view.View;

import com.example.hipporouter.pager.base.BaseActivity;
import com.example.library.router.request.impl.RouterRequest;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        findViewById(R.id.btn_activity).setOnClickListener(this);
        findViewById(R.id.btn_fragment).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity:
                RouterRequest.from(this,"activity://content")
                        .open();
                break;
            case R.id.btn_fragment:
                RouterRequest.from(this,"fragment://content")
                        .open();
                break;
        }
    }
}
