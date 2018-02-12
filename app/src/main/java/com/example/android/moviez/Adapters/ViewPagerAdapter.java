package com.example.android.moviez.Adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by pc on 1/9/2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();


    public void addFragment(Fragment fragmentArrayList1, String title){
        fragmentArrayList.add(fragmentArrayList1);
        titles.add(title);
    }


    public void setFragmentArrayList (ArrayList<Fragment> fragmentArrayList_){
        fragmentArrayList = fragmentArrayList_;
    }


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public  Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

}