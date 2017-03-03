package com.hippo.router.sample.pager.base;

import android.os.Bundle;
import android.widget.TextView;

import com.hippo.router.sample.R;

import java.util.Set;

/**
 * Created by kevinhao on 2017/3/2.
 */

public class ExtrasActivity extends BaseActivity{
    @Override
    protected int getLayoutId() {
        return R.layout.activity_extras;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        TextView textView = (TextView) findViewById(R.id.txt_extras);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Set<String> keys = extras.keySet();

            int padding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
            textView.setPadding(padding, padding, padding, padding);
            textView.setText(getClass().getSimpleName());
            textView.append("\n\n");

            for (String key : keys) {
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
