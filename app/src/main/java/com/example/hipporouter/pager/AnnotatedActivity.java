package com.example.hipporouter.pager;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hipporouter.R;
import com.example.hipporouter.pager.base.BaseActivity;
import com.example.module.annotation.RouterMap;

/**
 * Created by Kevin on 2016/11/26.
 */
@RouterMap("activity://annotated/:s{username}/:i{password}")
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
