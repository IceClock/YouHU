package com.example.abood.youhu;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class HomeAdmin extends  AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.play,
            R.drawable.book,
            R.drawable.news,
            R.drawable.upload,
            R.drawable.settings};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar2);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lectures");




        viewPager = (ViewPager) findViewById(R.id.viewpager2);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tablayout2);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);




//changing the toolbar name
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            String title[] = {"Lectures","Pictures","News","Upload","Settings"};
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                toolbar.setTitle(title[position]);

                // customize your toolbar
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }




    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Lectures(),"");
        adapter.addFragment(new Pictures(), "");
        adapter.addFragment(new News(), "");
        adapter.addFragment(new UploadActivity(), "");
        adapter.addFragment(new Settings(), "");
        viewPager.setAdapter(adapter);
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }}
