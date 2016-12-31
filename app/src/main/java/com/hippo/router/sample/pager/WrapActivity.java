package com.hippo.router.sample.pager;

import android.widget.TextView;
import com.hippo.router.annotation.RouterMap;
import com.hippo.router.router.impl.fragment.FragmentRequest;
import com.hippo.router.sample.R;
import com.hippo.router.sample.pager.base.BaseActivity;

/**
 * Created by Kevin on 2016/12/31.
 */
@RouterMap("activity://wrap/:s{title}")
public class WrapActivity extends BaseActivity{

    private String mContentPager;
    private String mContentTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wrap;
    }

    @Override
    protected void initData() {
        super.initData();

        mContentTitle = getIntent().getStringExtra("title");
        mContentPager = getIntent().getStringExtra("content");
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        ((TextView) findViewById(R.id.txt_title)).setText(mContentTitle);
        FragmentRequest.from(getFragmentManager(), mContentPager)
                .withParams("username","kevin")
                .withParams("password","123456")
                .attach(R.id.container);
    }
}
