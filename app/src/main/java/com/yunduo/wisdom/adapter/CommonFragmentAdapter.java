package com.yunduo.wisdom.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by parade岁月 on 2019/4/11 22:07
 */
public class CommonFragmentAdapter extends FragmentPagerAdapter {
    private Fragment currentFragment;
    private List<Fragment> mFragments;
    public CommonFragmentAdapter(FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    public CommonFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    /**
     *记录当前的fragment
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        currentFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }

    /**
     * 获取保存的当前的fragment
     * @return
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}
