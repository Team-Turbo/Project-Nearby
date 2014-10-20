package com.team1011.project.nearbyapp;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    public static String KEY_STATE_TITLE = "state_title";

    // Current action bar title
    private static CharSequence mTitle;

    // Drawer stuff
    private static CharSequence mDrawerTitle;
    private static String[] mDrawerTitles;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    // Pager/tabs stuff
    private static String[] mTabsTitles;
    private static int[] mTabsIcons;

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    private static String userName;
    private static String displayName;
    private static String birthDay;
    private static String imageUrl;
    private static String aboutMe;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //>> Setup: variables
        final ActionBar mActionBar = getActionBar();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.categories_drawer);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);

        //>> Setup: titles, texts and other arrays of data
        if (savedInstanceState != null) {
            setTitle(savedInstanceState.getCharSequence(KEY_STATE_TITLE));
        } else {
            setTitle(getTitle());
        }

        mDrawerTitle = getResources().getString(R.string.title_drawer);
        mDrawerTitles = new String[] {
                 "Employment"
                ,"Dating"
                ,"Buy/Sell"
        };

        mTabsTitles = new String[] {
                 "[Receive]"
                ,"[Send]"
                ,"Queue"
        };
        mTabsIcons = new int[] {
                 R.drawable.ic_launcher
                ,R.drawable.ic_launcher
                ,R.drawable.ic_launcher
        };

        //>> Setup: drawer toggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };

        //>> Setup: drawer layout
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //>> Setup: drawer list
        // Set the adapter for the list view (Fills the mDrawerList with mDrawerTitles)
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.categories_list_item, mDrawerTitles));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        //>> Setup: view pager
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                mActionBar.setSelectedNavigationItem(position);
            }
        });

        //>> Setup: Add tabs
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); ++i) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            mActionBar.addTab(
                    mActionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setIcon(mSectionsPagerAdapter.getPageIcon(i))
                            .setTabListener(this));
        }

        //>> Setup: action bar
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mActionBar.setDisplayShowTitleEnabled(true);

        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

        //>> Bundle stuff

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        userName = bundle.getString("USER_NAME");
        displayName = bundle.getString("FIRST_NAME");
        birthDay = bundle.getString("BIRTH_DAY");
        aboutMe = bundle.getString("ABOUT_ME");
        imageUrl = bundle.getString("PROFILE_PIC");

        imageUrl = imageUrl.substring(0, imageUrl.length() - 2) + 400;

        //>> Other

        mActionBar.setSelectedNavigationItem(-1); // select the profile fragment
    }

    @Override
    public void onBackPressed() {
        SignInPage.getClient().disconnect();

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "SETTINGS", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the title for the next onCreate
        CharSequence title = mDrawerLayout.isDrawerOpen(mDrawerList) ? mDrawerTitle : mTitle;
        outState.putCharSequence(KEY_STATE_TITLE, title);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

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

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectDrawerItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectDrawerItem(int position) {
        /*
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new PlaceholderFragment(position))
                .commit();
        */

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case -1:
                    return new Profile(userName, displayName, birthDay, imageUrl, aboutMe);
                case 0:
                    return new PlaceholderFragment(i);
                default:
                    return new PlaceholderFragment(i);
            }
        }

        @Override
        public int getCount() {
            return 3;
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
