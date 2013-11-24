package at.theengine.android.aquarinthhia;

import java.util.ArrayList;

import android.location.Location;
import android.os.Bundle;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import at.theengine.android.bestlocation.BestLocationListener;
import at.theengine.android.bestlocation.BestLocationProvider;
import at.theengine.android.bestlocation.BestLocationProvider.LocationType;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;    
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends FragmentActivity {

	private ProgressBar mPbLoading;
	private boolean mIsRainLoaded;
	private boolean mIsLakesLoaded;
	private boolean mIsRiversLoaded;
	
	private BestLocationListener mBestLocationListener;
	BestLocationProvider mBestLocationProvider;
	
	private DataLoader mDataLoader;
	
	private GoogleMap mMap;
	
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
	}
	
	private void moveToMe(){
		if(mBestLocationListener == null || mBestLocationProvider == null){
			mBestLocationListener = new BestLocationListener() {
	
				@Override
				public void onLocationUpdate(Location location, LocationType type,
						boolean isFresh) {
					
					mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
			        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
			        
			        if(isFresh && type == LocationType.GPS){
			        	mBestLocationProvider.stopLocationUpdates();
			        }
				}
	
				@Override
				public void onLocationUpdateTimeoutExceeded(LocationType type) {
					mBestLocationProvider.stopLocationUpdates();
				}
	
				@Override
				public void onStatusChanged(String provider, int status, Bundle extras) { }
	
				@Override
				public void onProviderEnabled(String provider) { }
	
				@Override
				public void onProviderDisabled(String provider) { }
				
			};
			
			mBestLocationProvider = new BestLocationProvider(this, true, true, 10000, 1000, 2, 0);
		}
		
		//start Location Updates
		mBestLocationProvider.startLocationUpdatesWithListener(mBestLocationListener);
	}
	
	@Override
	protected void onStop() {
		mBestLocationProvider.stopLocationUpdates();
		super.onStop();
	}
	
	@Override
	protected void onResume() {
		setupActionBar();
		
		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.setMyLocationEnabled(true);
		
		mContext = this;
		
		refreshData();
		
		moveToMe();
		super.onResume();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        getActionBar().setIcon(R.drawable.aquarinthia);
        
        getActionBar().setDisplayShowTitleEnabled(false);
        
        getActionBar().setCustomView(R.layout.ab_view_main);
        getActionBar().setDisplayShowCustomEnabled(true);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        mPbLoading = (ProgressBar) getActionBar().getCustomView().findViewById(R.id.pbLoading);
	}
	
	private void refreshData(){
		mMap.clear();
		
		mPbLoading.setVisibility(View.VISIBLE);
		mIsLakesLoaded = false;
		mIsRainLoaded = false;
		mIsRiversLoaded = false;
		
		if(mDataLoader == null){
			mDataLoader = new DataLoader(mContext, new DataLoaderCallback() {
				
				@Override
				public void onRainDataLoaded(ArrayList<RainData> items) {
					mIsRainLoaded = true;
					drawRainMarkers(items);
					hideLoading();
				}
				
				@Override
				public void onDataLoadingError(Exception ex) {
					Toast.makeText(mContext, ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
				}

				@Override
				public void onLakeDataLoaded(ArrayList<LakeData> items) {
					mIsLakesLoaded = true;
					drawLakeMarkers(items);
					hideLoading();
				}

				@Override
				public void onRiverDataLoaded(ArrayList<RiverData> items) {
					mIsRiversLoaded = true;
					drawRiverMarkers(items);
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

	private void drawRainMarkers(ArrayList<RainData> items){
		for(int i = 0; i < items.size(); i++){
			int icon = R.drawable.marker_rain_no;
			int rain = Integer.parseInt(items.get(i).getRain().replace("mm", ""));
			
			if(rain > 50){
				icon = R.drawable.marker_rain_heavy;
			} else if(rain > 10){
				icon = R.drawable.marker_rain_medium;
			} else if(rain > 0){
				icon = R.drawable.marker_rain_low;
			}
			
			mMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions()
	        .position(new LatLng(items.get(i).getLat(), items.get(i).getLng()))
	        .title(items.get(i).getLocationName())
	        .snippet("Niederschlag: " + items.get(i).getRain() + " - " + 
        			"Temperatur: " + items.get(i).getTemp())
	        .icon(BitmapDescriptorFactory
	            .fromResource(icon)));
		}
	}
	
	private void drawLakeMarkers(ArrayList<LakeData> items){
		for(int i = 0; i < items.size(); i++){
			int icon = R.drawable.height;
			
			mMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions()
	        .position(new LatLng(items.get(i).getLat(), items.get(i).getLng()))
	        .title(items.get(i).getLakeName())
	        .snippet("Pegel: " + items.get(i).getHeight() + " - " + 
	        			"Wassertemperatur: " + items.get(i).getTemp())
	        .icon(BitmapDescriptorFactory
	            .fromResource(icon)));
		}
	}
	
	private void drawRiverMarkers(ArrayList<RiverData> items){
		for(int i = 0; i < items.size(); i++){
			int icon = R.drawable.marker_river;
			
			mMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions()
	        .position(new LatLng(items.get(i).getLat(), items.get(i).getLng()))
	        .title(items.get(i).getRiverName())
	        .snippet("Pegel: " + items.get(i).getHeight() + " - " + 
	        			"Abfluss: " + items.get(i).getMass())
	        .icon(BitmapDescriptorFactory
	            .fromResource(icon)));
		}
	}
	
	@Override
 	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.action_refresh:
			refreshData();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

}
