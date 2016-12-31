package com.hippo.router.sample.pager;

import android.view.View;
import android.widget.TextView;

import com.hippo.router.router.impl.fragment.FragmentRequest;
import com.hippo.router.sample.R;
import com.hippo.router.sample.pager.base.BaseActivity;
import com.hippo.router.router.impl.activity.ActivityRequest;

import static com.hippo.router.router.impl.activity.ActivityRouter.ACTIVITY_KEY_URL;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initWidget() {
        findViewById(R.id.btn_open_login_pager).setOnClickListener(this);
        findViewById(R.id.btn_open_register_pager).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_login_pager:
                ActivityRequest.from(this,"activity://wrap/:LoginPager")
                        .withParams("content","fragment://login")
                        .withAnimation(R.anim.slide_in_bottom,0)
                        .open();
                break;
            case R.id.btn_open_register_pager:
                ActivityRequest.from(this,"activity://wrap/:RegisterPager")
                        .withParams("content","fragment://register")
                        .withAnimation(R.anim.slide_in_bottom,0)
                        .open();
                break;
        }
    }
}
