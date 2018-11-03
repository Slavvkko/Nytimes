package com.hordiienko.nytimes.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hordiienko.nytimes.Constants;
import com.hordiienko.nytimes.article_list.ArticleListFragment;

public class ArticleListFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context context;

    public ArticleListFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        return ArticleListFragment.newInstance(Constants.ApiType.values()[i].getName());
    }

    @Override
    public int getCount() {
        return Constants.ApiType.values().length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(Constants.ApiType.values()[position].getTitle());
    }
}
