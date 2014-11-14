package com.team1011.project.nearbyapp;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Melvin on 10/22/2014.
 *
 * The shell fragment that houses category fragments.
 */
public class Category_Shell extends Fragment
{
    private static ActionBar mActionBar;

    // Pager/tabs stuff
    private static String[] mTabsTitles;
    private static int[] mTabsIcons;

    private static SectionsPagerAdapter mPagerAdapter;
    private static ViewPager mViewPager;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_shell, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //>> Setup: variables
        mActionBar = getActivity().getActionBar();

        mPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = (ViewPager) getActivity().findViewById(R.id.pager);

        //>> Setup: titles, texts and other arrays of data
        mTabsTitles = new String[] {
                  "Match Creation"
                , "Current Matches"
        };
        mTabsIcons = new int[] {
                  R.drawable.ic_launcher
                , R.drawable.ic_launcher
        };

        //>> Setup: view pager
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                mActionBar.setSelectedNavigationItem(position);
            }
        });

        //>> Setup: Tabs
        // Make a listener for the tabs
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // When the given tab is selected, switch to the corresponding page in the ViewPager.
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
        };

        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mActionBar.removeAllTabs();

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mPagerAdapter.getCount(); ++i) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            mActionBar.addTab(
                    mActionBar.newTab()
                            .setText(mPagerAdapter.getPageTitle(i))
                            //.setIcon(mPagerAdapter.getPageIcon(i))
                            .setTabListener(tabListener));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mActionBar.removeAllTabs();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return new CategoryFragment(i);
        }

        @Override
        public int getCount() {
            return mTabsTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabsTitles[position];
        }

        public int getPageIcon(int position) {
            return mTabsIcons[position];
        }
    }
}
