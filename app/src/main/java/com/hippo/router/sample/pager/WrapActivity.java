package com.hippo.router.sample.pager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import com.hippo.router.compile.Route;
import com.hippo.router.router.impl.fragment.FragmentRequest;
import com.hippo.router.sample.R;
import com.hippo.router.sample.pager.base.BaseActivity;

/**
 * Created by Kevin on 2016/12/31.
 */
@Route("activity://wrap")
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

        mContentPager = getIntent().getStringExtra("content");
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        int viewId = getIntent().getIntExtra("viewId",-1);
        switch (viewId){
            case R.id.one:
                FragmentRequest.from(getSupportFragmentManager(), mContentPager)
                        .attach(R.id.container);
                break;
            case R.id.two:
                FragmentRequest.from(getSupportFragmentManager(), mContentPager)
                        .attach(R.id.container);
                break;
            case R.id.three:
                FragmentRequest.from(getSupportFragmentManager(), mContentPager)
                        .attach(R.id.container);
                break;
            case R.id.four:
                FragmentRequest.from(getSupportFragmentManager(), mContentPager)
                        .attach(R.id.container);
                break;
            case R.id.five:
                Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.xiaoxing);
                FragmentRequest.from(getSupportFragmentManager(), mContentPager)
                        .withParams("bitmap",bitmap)
                        .attach(R.id.container);
                break;
            case R.id.six:
                FragmentRequest.from(getSupportFragmentManager(), mContentPager)
                        .attach(R.id.container);
                break;
            case R.id.seven:
                FragmentRequest.from(getSupportFragmentManager(), mContentPager)
                        .attach(R.id.container);
                break;
        }
    }
}
