package com.example.hipporouter.pager.activity;

import android.content.Intent;
import android.widget.TextView;

import com.example.hipporouter.R;
import com.example.hipporouter.pager.base.BaseActivity;
import com.example.library.annotation.RouterMap;

/**
 * Created by Kevin on 2016/11/26.
 */
@RouterMap("activity://content/annotated/:s{username}/:i{password}")
public class AnnotatedActivity extends BaseActivity{

    @Override
    protected int getLayoutId() {
        return R.layout.activity_annotation;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        String username = getIntent().getStringExtra("username");
        int password = getIntent().getIntExtra("password", -1);
        String extra_id = getIntent().getStringExtra("extra_id");

        TextView txtUsername = (TextView) findViewById(R.id.txt_username);
        TextView txtPassword = (TextView) findViewById(R.id.txt_password);
        TextView txtExtra = (TextView) findViewById(R.id.txt_extra_id);

        txtUsername.setText(username);
        txtPassword.setText(String.valueOf(password));
        txtExtra.setText(extra_id);

    }
}
