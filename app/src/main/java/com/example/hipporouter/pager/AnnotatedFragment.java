package com.example.hipporouter.pager;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.example.hipporouter.R;
import com.example.hipporouter.pager.base.BaseFragment;
import com.example.module.annotation.RouterMap;

/**
 * Created by Kevin on 2016/11/26.
 */
@RouterMap("fragment://annotated/:s{username}/:i{password}#wrap")
public class AnnotatedFragment extends BaseFragment{
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_annotated;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        String username = getArguments().getString("username");
        int password = getArguments().getInt("password", -1);
        String extra_id = getArguments().getString("extra_id");

        TextView txtUsername = (TextView) root.findViewById(R.id.txt_username);
        TextView txtPassword = (TextView) root.findViewById(R.id.txt_password);
        TextView txtExtra = (TextView) root.findViewById(R.id.txt_extra_id);

        txtUsername.setText(username);
        txtPassword.setText(String.valueOf(password));
        txtExtra.setText(extra_id);
    }
}
