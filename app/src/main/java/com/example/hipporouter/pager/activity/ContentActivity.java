package com.example.hipporouter.pager.activity;

import android.view.View;

import com.example.hipporouter.R;
import com.example.hipporouter.pager.base.BaseActivity;
import com.example.library.router.router.impl.activity.ActivityRequest;
import com.example.library.annotation.RouterMap;

import java.util.UUID;

/**
 * Created by Kevin on 2016/11/26.
 */
@RouterMap("activity://content")
public class ContentActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_content;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        findViewById(R.id.btn_animation_open).setOnClickListener(this);
        findViewById(R.id.btn_static_params_open).setOnClickListener(this);
        findViewById(R.id.btn_dynamic_params_open).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_static_params_open:
                ActivityRequest.from(this,"activity://content/annotated/:kevin/:123456")
                        .open();
                break;
            case R.id.btn_dynamic_params_open:
                ActivityRequest.from(this,"activity://content/annotated/:kevin/:123456")
                        .withParams("extra_id", UUID.randomUUID().toString())
                        .open();
                break;
            case R.id.btn_animation_open:
                ActivityRequest.from(this,"activity://content/annotated/:kevin/:123456")
                        .withParams("extra_id", UUID.randomUUID().toString())
                        .withAnimation(R.anim.slide_in_bottom,0)
                        .open();
                break;
        }
    }
}
