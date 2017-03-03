package com.hippo.router.sample.pager.activity;

import android.graphics.Bitmap;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hippo.router.compile.Route;
import com.hippo.router.sample.R;
import com.hippo.router.sample.pager.base.ExtrasActivity;

/**
 * Created by kevinhao on 2017/3/3.
 */
@Route("activity://five/:s{username}/:i{password}")
public class FiveActivity extends ExtrasActivity{

    @Override
    protected void initWidget() {
        super.initWidget();


        Bitmap bitmap = getIntent().getParcelableExtra("bitmap");
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        ((ViewGroup)getRootView()).addView(imageView);
    }
}
