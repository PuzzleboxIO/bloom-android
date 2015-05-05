package io.puzzlebox.bloom;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.puzzlebox.jigsaw.data.SessionSingleton;
import io.puzzlebox.jigsaw.ui.BloomFragment;
import io.puzzlebox.jigsaw.ui.DrawerItem;
import io.puzzlebox.jigsaw.ui.EEGFragment;
import io.puzzlebox.jigsaw.ui.NavigationDrawerAdapter;
import io.puzzlebox.jigsaw.ui.SessionFragment;
import io.puzzlebox.jigsaw.ui.WelcomeFragment;


//public class MainActivity extends ActionBarActivity
//        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
public class MainActivity extends io.puzzlebox.jigsaw.ui.MainActivity implements
        WelcomeFragment.OnFragmentInteractionListener,
        SessionFragment.OnFragmentInteractionListener,
        EEGFragment.OnFragmentInteractionListener,
        BloomFragment.OnFragmentInteractionListener
{


//        private final static String TAG = MainActivity.class.getSimpleName();
//
//        /**
//         * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
//         */
//        private DrawerLayout mDrawerLayout;
//        private ListView mDrawerList;
//        private ActionBarDrawerToggle mDrawerToggle;
//
//        private CharSequence mTitle;
//        private CharSequence mDrawerTitle;
//        NavigationDrawerAdapter adapter;
//
//        List<DrawerItem> dataList;
//
//
//        // ################################################################
//
//        public void onFragmentInteraction(Uri uri) {
////		Log.d(TAG, "onFragmentInteraction()");
//        }
//
//
//        // ################################################################
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//                super.onCreate(savedInstanceState);
//                setContentView(io.puzzlebox.jigsaw.R.layout.activity_main);
//
//                dataList = new ArrayList<>();
//                mTitle = mDrawerTitle = getTitle();
//                mDrawerLayout = (DrawerLayout) findViewById(io.puzzlebox.jigsaw.R.id.drawer_layout);
//                mDrawerList = (ListView) findViewById(io.puzzlebox.jigsaw.R.id.navigation_drawer);
//
//                mDrawerLayout.setDrawerShadow(io.puzzlebox.jigsaw.R.drawable.drawer_shadow,
//                        GravityCompat.START);
//
//
//                // Add Drawer Item to dataList
//                dataList.add(new DrawerItem(getString(io.puzzlebox.jigsaw.R.string.title_fragment_welcome), io.puzzlebox.jigsaw.R.mipmap.ic_puzzlebox));
//                dataList.add(new DrawerItem(getString(io.puzzlebox.jigsaw.R.string.title_fragment_session), io.puzzlebox.jigsaw.R.mipmap.ic_session));
//                dataList.add(new DrawerItem(getString(io.puzzlebox.jigsaw.R.string.title_fragment_eeg), io.puzzlebox.jigsaw.R.mipmap.ic_eeg));
//                dataList.add(new DrawerItem(getString(io.puzzlebox.jigsaw.R.string.title_fragment_bloom), io.puzzlebox.jigsaw.R.mipmap.ic_bloom));
//
//                adapter = new NavigationDrawerAdapter(this, io.puzzlebox.jigsaw.R.layout.navigation_drawer_item,
//                        dataList);
//
//                mDrawerList.setAdapter(adapter);
//
//
//                mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
//
//
//                if (getSupportActionBar() != null) {
//                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                        getSupportActionBar().setHomeButtonEnabled(true);
//                        getSupportActionBar().setHomeAsUpIndicator(io.puzzlebox.jigsaw.R.drawable.ic_drawer);
//                }
//
//                Toolbar mToolbar = new Toolbar(this);
//
//                mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
//                        mToolbar, io.puzzlebox.jigsaw.R.string.drawer_open,
//                        io.puzzlebox.jigsaw.R.string.drawer_close) {
//                        public void onDrawerClosed(View view) {
//                                getSupportActionBar().setTitle(mTitle);
//                                invalidateOptionsMenu(); // creates call to
//                                // onPrepareOptionsMenu()
//                        }
//
//                        public void onDrawerOpened(View drawerView) {
//                                getSupportActionBar().setTitle(mDrawerTitle);
//                                invalidateOptionsMenu(); // creates call to
//                                // onPrepareOptionsMenu()
//                        }
//                };
//
//                mDrawerLayout.setDrawerListener(mDrawerToggle);
//
//                if (savedInstanceState == null) {
//                        SelectItem(0);
//                }
//
//                SessionSingleton.getInstance().resetSession();
//
//        }
//
//
//        // ################################################################
//
//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//
//                super.onOptionsItemSelected(item);
//
//                return mDrawerToggle.onOptionsItemSelected(item);
//
//        }
//
//
//        // ################################################################
//
//        public void SelectItem(int position) {
//
//                android.app.Fragment fragment = null;
//                Bundle args = new Bundle();
//                String backStackName = "";
//                switch (position) {
//                        case 0:
//                                backStackName = "welcome";
//                                try{
//                                        fragment = getFragmentManager().findFragmentByTag(backStackName);
//                                } catch (Exception e) {
//                                        e.printStackTrace();
//                                }
//                                if (fragment == null)
//                                        fragment = new WelcomeFragment();
//                                break;
//                        case 1:
//                                backStackName = "session";
//                                try{
//                                        fragment = getFragmentManager().findFragmentByTag(backStackName);
//                                } catch (Exception e) {
//                                        e.printStackTrace();
//                                }
//                                if (fragment == null)
//                                        fragment = new SessionFragment();
//                                break;
//                        case 2:
//                                backStackName = "eeg";
//                                try{
//                                        fragment = getFragmentManager().findFragmentByTag(backStackName);
//                                } catch (Exception e) {
//                                        e.printStackTrace();
//                                }
//                                if (fragment == null)
//                                        fragment = new EEGFragment();
//
//                                break;
//                        case 3:
//                                backStackName = "bloom";
//                                try{
//                                        fragment = getFragmentManager().findFragmentByTag(backStackName);
//                                } catch (Exception e) {
//                                        e.printStackTrace();
//                                }
//                                if (fragment == null)
//                                        fragment = new BloomFragment();
//
//                                break;
//                        default:
//                                break;
//                }
//
//                if (fragment != null)
//                        fragment.setArguments(args);
//                android.app.FragmentManager frgManager = getFragmentManager();
//                frgManager.beginTransaction().replace(io.puzzlebox.jigsaw.R.id.container, fragment)
//                        .addToBackStack(backStackName)
//                        .commit();
//
//                mDrawerList.setItemChecked(position, true);
//                setTitle(dataList.get(position).getItemName());
//                mDrawerLayout.closeDrawer(mDrawerList);
//
//        }
//
//
//        // ################################################################
//
//        @Override
//        public void setTitle(CharSequence title) {
//                mTitle = title;
//                if (getSupportActionBar() != null)
//                        getSupportActionBar().setTitle(mTitle);
//        }
//
//
//        // ################################################################
//
//        @Override
//        protected void onPostCreate(Bundle savedInstanceState) {
//                super.onPostCreate(savedInstanceState);
//                // Sync the toggle state after onRestoreInstanceState has occurred.
//                mDrawerToggle.syncState();
//
//        }
//
//
//        // ################################################################
//
//        @Override
//        public void onConfigurationChanged(Configuration newConfig) {
//                super.onConfigurationChanged(newConfig);
//                // Pass any configuration change to the drawer toggles
//                mDrawerToggle.onConfigurationChanged(newConfig);
//        }
//
//
//        // ################################################################
//
//        private class DrawerItemClickListener implements
//                ListView.OnItemClickListener {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position,
//                                        long id) {
//                        SelectItem(position);
//
//                }
//        }
//
//
//        // ################################################################
//
//        @Override
//        public void onPause() {
//
//                Log.v(TAG, "onPause()");
//
//                super.onPause();
//
//
//        } // onPause
//
//
//        // ################################################################
//
//        @Override
//        public void onDestroy() {
//
//                Log.v(TAG, "onDestroy()");
//
//                super.onDestroy();
//
//                SessionSingleton.getInstance().removeTemporarySessionFile();
//
//
//        } // onDestroy
//
//
//
//
////    /**
////     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
////     */
////    private NavigationDrawerFragment mNavigationDrawerFragment;
////
////    /**
////     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
////     */
////    private CharSequence mTitle;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
////
////        mNavigationDrawerFragment = (NavigationDrawerFragment)
////                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
////        mTitle = getTitle();
////
////        // Set up the drawer.
////        mNavigationDrawerFragment.setUp(
////                R.id.navigation_drawer,
////                (DrawerLayout) findViewById(R.id.drawer_layout));
////    }
////
////    @Override
////    public void onNavigationDrawerItemSelected(int position) {
////        // update the main content by replacing fragments
////        FragmentManager fragmentManager = getSupportFragmentManager();
////        fragmentManager.beginTransaction()
////                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
////                .commit();
////    }
////
////    public void onSectionAttached(int number) {
////        switch (number) {
////            case 1:
////                mTitle = getString(R.string.title_section1);
////                break;
////            case 2:
////                mTitle = getString(R.string.title_section2);
////                break;
////            case 3:
////                mTitle = getString(R.string.title_section3);
////                break;
////        }
////    }
////
////    public void restoreActionBar() {
////        ActionBar actionBar = getSupportActionBar();
////        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
////        actionBar.setDisplayShowTitleEnabled(true);
////        actionBar.setTitle(mTitle);
////    }
////
////
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        if (!mNavigationDrawerFragment.isDrawerOpen()) {
////            // Only show items in the action bar relevant to this screen
////            // if the drawer is not showing. Otherwise, let the drawer
////            // decide what to show in the action bar.
////            getMenuInflater().inflate(R.menu.main, menu);
////            restoreActionBar();
////            return true;
////        }
////        return super.onCreateOptionsMenu(menu);
////    }
////
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////        // Handle action bar item clicks here. The action bar will
////        // automatically handle clicks on the Home/Up button, so long
////        // as you specify a parent activity in AndroidManifest.xml.
////        int id = item.getItemId();
////
////        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////            return true;
////        }
////
////        return super.onOptionsItemSelected(item);
////    }
////
////    /**
////     * A placeholder fragment containing a simple view.
////     */
////    public static class PlaceholderFragment extends Fragment {
////        /**
////         * The fragment argument representing the section number for this
////         * fragment.
////         */
////        private static final String ARG_SECTION_NUMBER = "section_number";
////
////        /**
////         * Returns a new instance of this fragment for the given section
////         * number.
////         */
////        public static PlaceholderFragment newInstance(int sectionNumber) {
////            PlaceholderFragment fragment = new PlaceholderFragment();
////            Bundle args = new Bundle();
////            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
////            fragment.setArguments(args);
////            return fragment;
////        }
////
////        public PlaceholderFragment() {
////        }
////
////        @Override
////        public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                                 Bundle savedInstanceState) {
////            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
////            return rootView;
////        }
////
////        @Override
////        public void onAttach(Activity activity) {
////            super.onAttach(activity);
////            ((MainActivity) activity).onSectionAttached(
////                    getArguments().getInt(ARG_SECTION_NUMBER));
////        }
////    }

}
