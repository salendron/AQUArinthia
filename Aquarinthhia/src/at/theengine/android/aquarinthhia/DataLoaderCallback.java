package at.theengine.android.aquarinthhia;

import java.util.ArrayList;

public abstract class DataLoaderCallback {

	public abstract void onRainDataLoaded( ArrayList<RainData> items);
	
	public abstract void onLakeDataLoaded( ArrayList<LakeData> items);
	
	public abstract void onDataLoadingError(Exception ex);
	
}
