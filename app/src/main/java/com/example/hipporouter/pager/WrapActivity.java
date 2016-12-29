package com.example.hipporouter.pager;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.example.hipporouter.R;
import com.example.hipporouter.pager.base.BaseActivity;
import com.example.library.annotation.RouterMap;

/**
 * Created by Kevin on 2016/11/26.
 */

@RouterMap("activity://wrap/:s{fragment}")
public class WrapActivity extends BaseActivity{
    private static final String TAG = "WrapActivity";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wrap;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initFragmentFromIntent();
    }

    private void initFragmentFromIntent() {
//        String clazzPath = getIntent().getStringExtra(FRAGMENT_KEY_URL);
//        if (!TextUtils.isEmpty(clazzPath)) {
//            Fragment instantiate = getFragmentFromName(clazzPath);
//            replaceContainer(instantiate);
//        } else {
//            Log.e(TAG, "NO FRAGMENT ATTACH!");
//        }
    }

    private void replaceContainer(Fragment instantiate) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,instantiate)
                .commit();
    }

    protected Fragment getFragmentFromName(String clazzPath){
        return Fragment.instantiate(this, clazzPath,getIntent().getExtras());
    }
}
