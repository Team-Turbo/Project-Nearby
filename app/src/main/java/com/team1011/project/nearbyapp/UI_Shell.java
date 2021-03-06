package com.team1011.project.nearbyapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Melvin on 10/22/2014.
 *
 * The app's UI shell. It's the activity that the fragments will live in.
 */
public class UI_Shell extends FragmentActivity
{
    public static final String KEY_STATE_TITLE = "state_title";

    public static ChatFragment activeChatFrag;

    public static boolean runnnnnnnin = false;

    // Current action bar title
    private static CharSequence mTitle;

    // Drawer stuff
    private static CharSequence mDrawerTitle;
    private static String[] mDrawerTitles;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private DrawerItemClickListener mDrawerItemClickListener;
    private ListView mDrawerList;

    public static String userName;
    private static String displayName;
    private static String birthDay;
    private static String imageUrl;
    private static String aboutMe;

    protected static String myRegID;

    // Menu stuff
    private static Menu menu;

    // For category pages
    private static int currCategory = -1;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static int getCurrentCategoryInt() {
        return currCategory;
    }

    public static String getCurrentCategory() {
        return mDrawerTitles[currCategory];
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_shell);

        runnnnnnnin = true;

        //>> Setup: variables
        final ActionBar mActionBar = getActionBar();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.categories_drawer);

        GCMstatic.gcm.registerInBackground(getApplicationContext());

        activeChatFrag = new ChatFragment();

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

        mDrawerItemClickListener = new DrawerItemClickListener();
        mDrawerList.setOnItemClickListener(mDrawerItemClickListener);

        //>> Setup: action bar
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

        //>> Set content
        Profile profile = new Profile();
        profile.setArgs(userName, displayName, birthDay, imageUrl, aboutMe);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame,profile)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getActionBar().getTitle().equals(getResources().getString(R.string.title_activity_profile))) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        } else {
            Profile profile = new Profile();
            profile.setArgs(userName, displayName, birthDay, imageUrl, aboutMe);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame,profile)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        UI_Shell.menu = menu;

        menu.clear();
        getMenuInflater().inflate(R.menu.menu_main, menu);

        updateBroadcastMenuItem(BroadcastService.isRunning);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onResume() {
        super.onResume();
        runnnnnnnin = true;
    }

    @Override
    public void onPause() {
        runnnnnnnin = false;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        runnnnnnnin = false;
        super.onDestroy();
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
        switch (item.getItemId())
        {
            case R.id.action_profile:
                mDrawerItemClickListener.clean();
                Profile profile = new Profile();
                profile.setArgs(userName, displayName, birthDay, imageUrl, aboutMe);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, profile)
                        .commit();
                return true;

            case R.id.action_notification:
                mDrawerItemClickListener.clean();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new NotificationFragment())
                        .commit();
                return true;

            case R.id.broadcast:
                Intent intent = new Intent(this, BroadcastService.class);
                Bundle bundle = new Bundle();

                bundle.putString("REG_ID", myRegID);

                intent.putExtras(bundle);

                updateBroadcastMenuItem(!BroadcastService.isRunning);

                if (BroadcastService.isRunning) {
                    stopService(intent);
                } else {
                    startService(intent);
                }
                return true;

            case R.id.restartWifi:
                WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(false);
                wifiManager.setWifiEnabled(true);
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

    private void updateBroadcastMenuItem(boolean broadcastEnabled) {
        MenuItem broadcastMenuItem = menu.findItem(R.id.broadcast);

        if (broadcastEnabled) {
            broadcastMenuItem.setTitle("Disable Broadcast");
        } else {
            broadcastMenuItem.setTitle("Enable Broadcast");
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        public int prevPos = -1;

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            currCategory = position;

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new Category_Shell())
                    .commit();

            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(mDrawerTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

            prevPos = position;
        }

        public void clean() {
            mDrawerList.setItemChecked(prevPos, false);
        }
    }
}
