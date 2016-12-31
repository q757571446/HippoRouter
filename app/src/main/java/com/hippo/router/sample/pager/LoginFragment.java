package com.hippo.router.sample.pager;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hippo.router.annotation.RouterMap;
import com.hippo.router.sample.R;
import com.hippo.router.sample.pager.base.BaseFragment;

import static com.hippo.router.router.impl.fragment.FragmentRouter.FRAGMENT_KEY_URL;

/**
 * Created by Kevin on 2016/12/31.
 */
@RouterMap("fragment://login")
public class LoginFragment extends BaseFragment{

    private String username;
    private String password;
    private String router;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initData() {
        super.initData();

        router = getArguments().getString(FRAGMENT_KEY_URL);
        username = getArguments().getString("username");
        password = getArguments().getString("password");
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        EditText edtRouter = (EditText) root.findViewById(R.id.edt_router);
        EditText edtUsername = (EditText) root.findViewById(R.id.edt_username);
        EditText edtPassword = (EditText) root.findViewById(R.id.edt_password);

        edtRouter.setText(router);
        edtUsername.setText(username);
        edtPassword.setText(password);
    }
}
