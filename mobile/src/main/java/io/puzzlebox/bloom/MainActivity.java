/**
 * Puzzlebox Bloom
 * Copyright 2015-2016 Puzzlebox Productions, LLC
 * License: GNU Affero General Public License Version 3
 */

package io.puzzlebox.bloom;

import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import io.puzzlebox.bloom.ui.MakerFragment;
import io.puzzlebox.bloom.ui.WelcomeFragment;
import io.puzzlebox.bloom.ui.BloomFragment;
import io.puzzlebox.jigsaw.ui.DrawerItem;
import io.puzzlebox.jigsaw.ui.EEGFragment;
import io.puzzlebox.jigsaw.ui.SessionFragment;
import io.puzzlebox.jigsaw.ui.SupportFragment;


public class MainActivity extends io.puzzlebox.jigsaw.ui.MainActivity implements
		  WelcomeFragment.OnFragmentInteractionListener,
		  SessionFragment.OnFragmentInteractionListener,
		  EEGFragment.OnFragmentInteractionListener,
		  BloomFragment.OnFragmentInteractionListener,
		  MakerFragment.OnFragmentInteractionListener,
		  SupportFragment.OnFragmentInteractionListener
{

	private final static String TAG = MainActivity.class.getSimpleName();

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mTitle;
	private CharSequence mDrawerTitle;
	List<DrawerItem> dataList;

	// ################################################################

	@Override
	protected void onCreateCustom() {

		// For use with custom applications

		dataList = getDrawerDataList();

		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(io.puzzlebox.jigsaw.R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(io.puzzlebox.jigsaw.R.id.navigation_drawer);


		Toolbar mToolbar = new Toolbar(this);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				  mToolbar, io.puzzlebox.jigsaw.R.string.drawer_open,
				  io.puzzlebox.jigsaw.R.string.drawer_close) {
			public void onDrawerClosed(View view) {
//                                getSupportActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

	}


	// ################################################################

	protected List<DrawerItem> getDrawerDataList() {
		List<DrawerItem> dataList = new ArrayList<>();

		dataList.add(new DrawerItem(getString(io.puzzlebox.jigsaw.R.string.title_fragment_welcome), io.puzzlebox.jigsaw.R.mipmap.ic_puzzlebox));
		dataList.add(new DrawerItem(getString(io.puzzlebox.jigsaw.R.string.title_fragment_session), io.puzzlebox.jigsaw.R.mipmap.ic_session_color));
		dataList.add(new DrawerItem(getString(io.puzzlebox.jigsaw.R.string.title_fragment_eeg), io.puzzlebox.jigsaw.R.mipmap.ic_eeg_color));
		dataList.add(new DrawerItem(getString(R.string.title_fragment_bloom), R.mipmap.ic_bloom));
		dataList.add(new DrawerItem(getString(R.string.title_fragment_maker), R.mipmap.ic_bloom));
		dataList.add(new DrawerItem(getString(io.puzzlebox.jigsaw.R.string.title_fragment_support), io.puzzlebox.jigsaw.R.mipmap.ic_support));

		return dataList;
	}


	// ################################################################

	@Override
	public void SelectItem(int position) {

		Fragment fragment = null;
		Bundle args = new Bundle();
		String backStackName = "";
		switch (position) {
			case 0:
				backStackName = "welcome";
				try{
					fragment = getSupportFragmentManager().findFragmentByTag(backStackName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (fragment == null)
					fragment = new WelcomeFragment();
				break;
			case 1:
				backStackName = "session";
				try{
					fragment = getSupportFragmentManager().findFragmentByTag(backStackName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (fragment == null)
					fragment = new SessionFragment();
				break;
			case 2:
				backStackName = "eeg";
				try{
					fragment = getSupportFragmentManager().findFragmentByTag(backStackName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (fragment == null)
					fragment = new EEGFragment();

				break;
			case 3:
				backStackName = "bloom";
				try{
					fragment = getSupportFragmentManager().findFragmentByTag(backStackName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (fragment == null)
					fragment = new BloomFragment();

				break;
			case 4:
				backStackName = "maker";
				try{
					fragment = getSupportFragmentManager().findFragmentByTag(backStackName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (fragment == null)
					fragment = new MakerFragment();

				break;
			case 5:
				backStackName = "support";
				try{
					fragment = getSupportFragmentManager().findFragmentByTag(backStackName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (fragment == null)
					fragment = new SupportFragment();

				break;
			default:
				break;
		}

		if (fragment != null)
			fragment.setArguments(args);

		FragmentManager frgManager = getSupportFragmentManager();
		frgManager.beginTransaction().replace(io.puzzlebox.jigsaw.R.id.container, fragment)
				  .addToBackStack(backStackName)
				  .commit();


		if (mDrawerList != null) {
			mDrawerList.setItemChecked(position, true);
			setTitle(dataList.get(position).getItemName());
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			Log.w(TAG, "mDrawerList == null");
		}

	}

}
