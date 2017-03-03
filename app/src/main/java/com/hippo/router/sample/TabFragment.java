package com.hippo.router.sample;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hippo.router.sample.pager.base.BaseFragment;

/**
 * Created by kevinhao on 2017/3/3.
 */

public abstract class TabFragment extends BaseFragment implements View.OnClickListener{

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        ViewGroup container = (ViewGroup) root;
        for (int i = 0; i < container.getChildCount(); i++) {
            final View view = container.getChildAt(i);
            if (view instanceof TextView) {
                view.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {
        widgetClick(view);
    }

    protected abstract void widgetClick(View view);
}
