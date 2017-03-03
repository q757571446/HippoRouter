package com.hippo.router.sample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.hippo.router.sample.pager.base.BaseActivity;

/**
 * Created by kevinhao on 2017/3/3.
 */

public class LaunchActivity extends BaseActivity{
    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initData() {
        super.initData();

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_title);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.tab_content);

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
    }

    public class TabAdapter extends FragmentPagerAdapter {

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "activity";
                case 1:
                    return "fragment";
                case 2:
                    return "extend";
                default:
                    return "activity";
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new FragmentTab_1();
                case 1:
                    return new FragmentTab_2();
                case 2:
                    return new FragmentTab_3();
                default:
                    return new FragmentTab_1();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
