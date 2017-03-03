package com.hippo.router.sample.pager.base;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hippo.router.router.impl.activity.ActivityRouter;
import com.hippo.router.router.impl.fragment.FragmentRouter;
import com.hippo.router.sample.R;

import java.util.Set;

/**
 * Created by kevinhao on 2017/3/3.
 */

public class ExtrasFragment extends BaseFragment{
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_extras;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        TextView textView = (TextView) view.findViewById(R.id.txt_extras);
        Bundle extras = getArguments();
        if (extras != null) {
            Set<String> keys = extras.keySet();

            int padding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
            textView.setPadding(padding, padding, padding, padding);
            textView.setText(extras.getString(FragmentRouter.FRAGMENT_KEY_URL)+"=>"+getClass().getSimpleName());
            textView.append("\n\n");

            for (String key : keys) {
                if(FragmentRouter.FRAGMENT_KEY_URL.endsWith(key))
                    continue;
                textView.append(key + "=>");
                Object v = extras.get(key);
                if (v != null) {
                    textView.append(v + "=>" + v.getClass().getSimpleName());
                } else {
                    textView.append("null");
                }
                textView.append("\n\n");
            }
        }
    }
}
