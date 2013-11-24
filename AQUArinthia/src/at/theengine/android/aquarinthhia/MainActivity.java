package at.theengine.android.aquarinthhia;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import at.theengine.android.simple_rss2_android.RSSItem;
import at.theengine.android.simple_rss2_android.SimpleRss2Parser;
import at.theengine.android.simple_rss2_android.SimpleRss2ParserCallback;

public class MainActivity extends FragmentActivity {

	private Context mContext;
	
	private DataLoader mDataLoader;
	
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	
	private ProgressBar mPbLoading;
	private boolean mIsRainLoaded;
	private boolean mIsLakesLoaded;
	private boolean mIsRiversLoaded;
	
	private RainfallFragment rainFallFragment;
	private LakesFragment lakesFragment;
	private RiversFragment riversFragment;
	private InfoFragment infoFragment;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		mContext = this;
		setUpActionBar();
		refreshData();
	}
	
	@Override
	protected void onResume() {
		refreshData();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
			case R.id.action_refresh:
				refreshData();
				break;
			case R.id.action_karte:
				Intent intent = new Intent(this, MapActivity.class);
				startActivity(intent);
				break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	private void setUpActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        getActionBar().setIcon(R.drawable.aquarinthia);
        
        getActionBar().setDisplayShowTitleEnabled(false);
        
        getActionBar().setCustomView(R.layout.ab_view_main);
        getActionBar().setDisplayShowCustomEnabled(true);

        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(false);
        
        mPbLoading = (ProgressBar) getActionBar().getCustomView().findViewById(R.id.pbLoading);
	}
	
	private void refreshData(){
		mPbLoading.setVisibility(View.VISIBLE);
		mIsLakesLoaded = false;
		mIsRainLoaded = false;
		mIsRiversLoaded = false;
		
		if(mDataLoader == null){
			mDataLoader = new DataLoader(mContext, new DataLoaderCallback() {
				
				@Override
				public void onRainDataLoaded(ArrayList<RainData> items) {
					try { rainFallFragment.loadData(); } catch(Exception ex) {}
					mIsRainLoaded = true;
					hideLoading();
				}
				
				@Override
				public void onDataLoadingError(Exception ex) {
					Toast.makeText(mContext, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
				}

				@Override
				public void onLakeDataLoaded(ArrayList<LakeData> items) {
					try { lakesFragment.loadData(); } catch(Exception ex) {}
					mIsLakesLoaded = true;
					hideLoading();
				}

				@Override
				public void onRiverDataLoaded(ArrayList<RiverData> items) {
					try { riversFragment.loadData(); } catch(Exception ex) {}
					mIsRiversLoaded = true;
					hideLoading();
				}
			});
		}
		
		mDataLoader.clearCache();
		mDataLoader.loadData();
	}
	
	private void hideLoading(){
		if(mIsLakesLoaded && mIsRainLoaded && mIsRiversLoaded){
			mPbLoading.setVisibility(View.GONE);
		}
	}
	
	public DataLoader getDataLoader(){
		return mDataLoader;
	}
	
	@Override
	public void onBackPressed()
	{
		this.finish();
	}
	
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if(position == 0){
				if(rainFallFragment == null){ rainFallFragment = new RainfallFragment(); }
				return rainFallFragment;
			} else if(position == 1){
				if(lakesFragment == null){ lakesFragment = new LakesFragment(); }
				return lakesFragment;
			} else if(position == 2){
				if(riversFragment == null){ riversFragment = new RiversFragment(); }
				return riversFragment;
			} else {
				if(infoFragment == null){ infoFragment = new InfoFragment(); }
				return infoFragment;
			}
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			}
			return null;
		}
	}

}
