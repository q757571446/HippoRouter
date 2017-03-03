package com.hippo.router.sample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.TextView;

import com.hippo.router.router.impl.activity.ActivityRequest;
import com.hippo.router.sample.R;
import com.hippo.router.sample.pager.base.BaseFragment;

/**
 * Created by kevinhao on 2017/3/3.
 */

public class FragmentTab_2 extends TabFragment{
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_2;
    }

    @Override
    protected void widgetClick(View view) {
        //attach and detatch in WrapActivity
        ActivityRequest.from(getActivity(),"activity://wrap")
                .withParams("viewId",view.getId())
                .withParams("content",((TextView) view).getText().toString().trim())
                .open();

    }
}
