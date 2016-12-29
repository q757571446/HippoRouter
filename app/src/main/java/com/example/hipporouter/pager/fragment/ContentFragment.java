package com.example.hipporouter.pager.fragment;

import android.view.View;

import com.example.hipporouter.R;
import com.example.hipporouter.pager.base.BaseFragment;
import com.example.library.router.router.impl.activity.ActivityRequest;

import java.util.UUID;

/**
 * Created by Kevin on 2016/11/26.
 */

public class ContentFragment extends BaseFragment implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_content;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        root.findViewById(R.id.btn_animation_open).setOnClickListener(this);
        root.findViewById(R.id.btn_static_params_open).setOnClickListener(this);
        root.findViewById(R.id.btn_dynamic_params_open).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_static_params_open:
                ActivityRequest.from(getActivity(),"fragment://annotated/kevin/123456")
                        .open();
                break;
            case R.id.btn_dynamic_params_open:
                ActivityRequest.from(getActivity(),"fragment://annotated/kevin/123456")
                        .withParams("extra_id", UUID.randomUUID().toString())
                        .open();
                break;
            case R.id.btn_animation_open:
                ActivityRequest.from(getActivity(),"fragment://annotated/kevin/123456")
                        .withParams("extra_id", UUID.randomUUID().toString())
                        .withAnimation(R.anim.slide_in_bottom,0)
                        .open();
                break;
        }
    }
}
