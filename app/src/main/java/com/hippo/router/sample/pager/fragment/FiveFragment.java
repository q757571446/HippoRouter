package com.hippo.router.sample.pager.fragment;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hippo.router.compile.Route;
import com.hippo.router.sample.pager.base.ExtrasActivity;
import com.hippo.router.sample.pager.base.ExtrasFragment;

/**
 * Created by kevinhao on 2017/3/3.
 */
@Route("fragment://five/:s{username}/:i{password}")
public class FiveFragment extends ExtrasFragment {

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);

        Bitmap bitmap = getArguments().getParcelable("bitmap");
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageBitmap(bitmap);
        ((ViewGroup)view).addView(imageView);
    }
}
