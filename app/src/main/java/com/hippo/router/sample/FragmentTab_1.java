package com.hippo.router.sample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hippo.router.router.impl.activity.ActivityRequest;
import com.hippo.router.sample.R;
import com.hippo.router.sample.pager.base.BaseFragment;

/**
 * Created by kevinhao on 2017/3/3.
 */

public class FragmentTab_1 extends TabFragment  {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_1;
    }


    @Override
    protected void widgetClick(View view) {
        switch (view.getId()){
            case R.id.one:
                ActivityRequest.from(getActivity(),((TextView) view).getText().toString().trim())
                        .open();
                break;
            case R.id.two:
                ActivityRequest.from(getActivity(),((TextView) view).getText().toString().trim())
                        .open();
                break;
            case R.id.three:
                ActivityRequest.from(getActivity(),((TextView) view).getText().toString().trim())
                        .open();
                break;
            case R.id.four:
                ActivityRequest.from(getActivity(),((TextView) view).getText().toString().trim())
                        .open();
                break;
            case R.id.five:
                Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.xiaoxing);
                ActivityRequest.from(getActivity(),((TextView) view).getText().toString().trim())
                        .withParams("bitmap",bitmap)
                        .open();
                break;
            case R.id.six:
                ActivityRequest.from(getActivity(),((TextView) view).getText().toString().trim())
                        .open();
                break;
            case R.id.seven:
                ActivityRequest.from(getActivity(),((TextView) view).getText().toString().trim())
                        .open();
                break;
            case R.id.eight:
                ActivityRequest.from(getActivity(),((TextView) view).getText().toString().trim())
                        .withAnimation(R.anim.slide_in_bottom,0)
                        .open();
                break;
            case R.id.nine:
                ActivityRequest.from(getActivity(),((TextView) view).getText().toString().trim())
                        .withAnimation(0,R.anim.slide_out_bottom)
                        .open();
                break;
        }
    }
}
