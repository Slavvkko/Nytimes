package com.hordiienko.nytimes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hordiienko.nytimes.ui.adapter.ArticleListFragmentPagerAdapter;
import com.hordiienko.nytimes.ui.favorite_list.FavoriteListActivity;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private Drawer drawer;
    private int favoriteId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolbar();
        initDrawer();
        initViewPager();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
    }

    private void initDrawer() {
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.drawer_header)
                .withOnDrawerItemClickListener(drawerItemClickListener)
                .build();

        drawer.addItem(new PrimaryDrawerItem()
                .withName(R.string.drawer_favorites)
                .withIdentifier(favoriteId)
                .withIcon(R.drawable.ic_star_border_white_24dp)
                .withIconTintingEnabled(true)
                .withIconColor(Color.BLACK)
                .withSelectable(false)
        );

    }

    private void initViewPager() {
        viewPager.setAdapter(new ArticleListFragmentPagerAdapter(getSupportFragmentManager(), this));
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void startFavoriteActivity() {
        startActivity(new Intent(this, FavoriteListActivity.class));
    }

    private Drawer.OnDrawerItemClickListener drawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            if (drawerItem.getIdentifier() == favoriteId) {
                startFavoriteActivity();
            }

            drawer.closeDrawer();

            return true;
        }
    };
}
