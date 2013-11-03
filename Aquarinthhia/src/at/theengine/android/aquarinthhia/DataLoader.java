package at.theengine.android.aquarinthhia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import at.theengine.android.aquarinthhia.R.layout;
import at.theengine.android.simple_rss2_android.RSSItem;
import at.theengine.android.simple_rss2_android.SimpleRss2Parser;
import at.theengine.android.simple_rss2_android.SimpleRss2ParserCallback;

public class DataLoader {
			
	private static final String TAG = "at.theengine.android.aquarinthhia.DataLoader";
	
	private ArrayList<RainData> mRainItems;
	private ArrayList<LakeData> mLakeData;
	
	private Context mContext;
	private DataLoaderCallback mCallback;
	
	public DataLoader(Context context, DataLoaderCallback callback){
		this.mCallback = callback;
		this.mContext = context;
	}
	
	public void clearCache(){
		mRainItems = null;
	}
			
	public ArrayList<RainData> getRainItems(){
		return mRainItems;
	}
	
	public ArrayList<LakeData> getLakeItems(){
		return mLakeData;
	}
	
	public void loadData(){
		if(mRainItems == null) {
			SimpleRss2Parser parser = new SimpleRss2Parser("https://info.ktn.gv.at/asp/hydro/hydro_stationen_niederschlag_rss_mit_Werte.asp", 
			    new SimpleRss2ParserCallback() {
					
					@Override
					public void onFeedParsed(List<RSSItem> items) {
						mRainItems = parseRainData(items);
						mCallback.onRainDataLoaded(mRainItems);
					}
					
					@Override
					public void onError(Exception ex) {
						mCallback.onDataLoadingError(ex);
						Log.e(TAG,ex.getMessage());
					}
				}
			);
			parser.parseAsync();
		} else {
			mCallback.onRainDataLoaded(mRainItems);
		}
		
		if(mLakeData == null) {
			SimpleRss2Parser parser = new SimpleRss2Parser("https://info.ktn.gv.at/asp/hydro/hydro_stationen_see_rss_mit_Werte.asp", 
			    new SimpleRss2ParserCallback() {
					
					@Override
					public void onFeedParsed(List<RSSItem> items) {
						mLakeData = parseLakeData(items);
						mCallback.onLakeDataLoaded(mLakeData);
					}
					
					@Override
					public void onError(Exception ex) {
						mCallback.onDataLoadingError(ex);
						Log.e(TAG,ex.getMessage());
					}
				}
			);
			parser.parseAsync();
		} else {
			mCallback.onLakeDataLoaded(mLakeData);
		}
	}
	
	private ArrayList<RainData> parseRainData(List<RSSItem> items){
		ArrayList<RainData> rainDataSet = new ArrayList<RainData>();
		
		for(int i = 0; i < items.size(); i++){
			try {
				RainData rainData = new RainData();
				rainData.setLocationName(items.get(i).getTitle().replace("ZAMG", "").split("-")[0].trim());
				rainData.setLat(items.get(i).getLat());
				rainData.setLng(items.get(i).getLng());
				
				String rain = "";
		        String temp = "";
		        String time = "";
		        
		        String[] desc = items.get(i).getDescription().replace("<p>", "").replace("\n", "").split("<br>");
		        
		        for(int j = 0; j < desc.length; j++){	        	
		        	if(desc[j].startsWith("Datum")){
		        		time = desc[j].split(":")[1].trim();
		        		
		        		//concate rest of date
		        		for(int k = 2; k < desc[j].split(":").length - 1; k++){
		        			time += ":" + desc[j].split(":")[k];
		        		}
		        		time = time.split(" ")[1] + " Uhr";
		        		
		        	} else if(desc[j].startsWith("Niederschlag")){
		        		rain = desc[j].split(":")[1].trim() + "mm";
		        	} else if(desc[j].startsWith("Temperatur")){
		        		temp = desc[j].split(":")[1].trim() + "°C";
		        	}
		        }
				
		        rainData.setRain(rain);
		        rainData.setTemp(temp);
		        rainData.setTime(time);
		        
		        rainDataSet.add(rainData);
			} catch(Exception ex){
				Log.e(TAG, "Raindata could not be parsed: " + ex.getMessage());
			}
		}
		
		//sort by Location Name
        Collections.sort(rainDataSet, new RainDataComperator());
        
		return rainDataSet;
	}
	
	private ArrayList<LakeData> parseLakeData(List<RSSItem> items){
		ArrayList<LakeData> lakeDataSet = new ArrayList<LakeData>();
		
		for(int i = 0; i < items.size(); i++){
			try {
				LakeData lakeData = new LakeData();
				lakeData.setLakeName(items.get(i).getTitle().replace("ZAMG", "").split("-")[0].trim());
				lakeData.setLat(items.get(i).getLat());
				lakeData.setLng(items.get(i).getLng());
				
				String height = "";
		        String temp = "";
		        String time = "";
		        
		        String[] desc = items.get(i).getDescription().replace("<p>", "").replace("\n", "").split("<br>");
		        
		        for(int j = 0; j < desc.length; j++){	        	
		        	if(desc[j].startsWith("Datum")){
		        		time = desc[j].split(":")[1].trim();
		        		
		        		//concate rest of date
		        		for(int k = 2; k < desc[j].split(":").length - 1; k++){
		        			time += ":" + desc[j].split(":")[k];
		        		}
		        		time = time.split(" ")[1] + " Uhr";
		        		
		        	} else if(desc[j].startsWith("Wasserstand")){
		        		height = desc[j].split(":")[1].trim() + "cm";
		        	} else if(desc[j].startsWith("Wassertemperatur")){
		        		temp = desc[j].split(":")[1].trim() + "°C";
		        	}
		        }
				
		        lakeData.setHeight(height);
		        lakeData.setTemp(temp);
		        lakeData.setTime(time);
		        
		        lakeDataSet.add(lakeData);
			} catch(Exception ex){
				Log.e(TAG, "Lakedata could not be parsed: " + ex.getMessage());
			}
		}
		
		//sort by Location Name
        Collections.sort(lakeDataSet, new LakeDataComperator());
        
		return lakeDataSet;
	}
	
	public class RainDataComperator implements Comparator<RainData> {
	    @Override
	    public int compare(RainData o1, RainData o2) {
	        return o1.getLocationName().compareTo(o2.getLocationName());
	    }
	}
	
	public class LakeDataComperator implements Comparator<LakeData> {
	    @Override
	    public int compare(LakeData o1, LakeData o2) {
	        return o1.getLakeName().compareTo(o2.getLakeName());
	    }
	}

}
