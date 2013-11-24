package at.theengine.android.aquarinthhia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private ArrayList<RiverData> mRiverData;
	
	private Context mContext;
	private DataLoaderCallback mCallback;
	
	public DataLoader(Context context, DataLoaderCallback callback){
		this.mCallback = callback;
		this.mContext = context;
	}
	
	public void clearCache(){
		mRainItems = null;
		mLakeData = null;
		mRiverData = null;
	}
			
	public ArrayList<RainData> getRainItems(){
		return mRainItems;
	}
	
	public ArrayList<LakeData> getLakeItems(){
		return mLakeData;
	}
	
	public ArrayList<RiverData> getRiverItems(){
		return mRiverData;
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
		
		if(mRiverData == null) {
			SimpleRss2Parser parser = new SimpleRss2Parser("https://info.ktn.gv.at/asp/hydro/hydro_stationen_abfluss_rss_mit_Werte.asp", 
			    new SimpleRss2ParserCallback() {
					
					@Override
					public void onFeedParsed(List<RSSItem> items) {
						mRiverData = parseRiverData(items);
						mCallback.onRiverDataLoaded(mRiverData);
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
		        
		        //get image
		        String p = "src=\"[a-z A-Z / : \\. \\- \\_ 0-9]*[jpg png gif]\"";
				Pattern pattern = Pattern.compile(p);
				
				String data = items.get(i).getDescription();
				Matcher matcher = pattern.matcher(data);
				
				String path = null;
				if( matcher.find()) {
					path = matcher.group();
					path = path.replace("\"", "").replace("src=", "");
				}
				
				rainData.setImage(path);
		        
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
		        
		        //get image
		        String p = "src=\"[a-z A-Z / : \\. \\- \\_ 0-9]*[jpg png gif]\"";
				Pattern pattern = Pattern.compile(p);
				
				String data = items.get(i).getDescription();
				Matcher matcher = pattern.matcher(data);
				
				String path = null;
				if( matcher.find()) {
					path = matcher.group();
					path = path.replace("\"", "").replace("src=", "");
				}
				
				lakeData.setImage(path);
		        
		        lakeDataSet.add(lakeData);
			} catch(Exception ex){
				Log.e(TAG, "Lakedata could not be parsed: " + ex.getMessage());
			}
		}
		
		//sort by Location Name
        Collections.sort(lakeDataSet, new LakeDataComperator());
        
		return lakeDataSet;
	}
	
	private ArrayList<RiverData> parseRiverData(List<RSSItem> items){
		ArrayList<RiverData> riverDataSet = new ArrayList<RiverData>();
		
		for(int i = 0; i < items.size(); i++){
			try {
				RiverData riverData = new RiverData();
				riverData.setRiverName(items.get(i).getTitle().replace("ZAMG", "").trim());
				riverData.setLat(items.get(i).getLat());
				riverData.setLng(items.get(i).getLng());
				
				String height = "";
		        String mass = "";
		        String time = "";
		        
		        String[] desc = items.get(i).getDescription().replace("<p>", "").replace("\n", "").split("<br>");
		        
		        for(int j = 0; j < desc.length; j++){	        	
		        	if(desc[j].trim().startsWith("Datum")){
		        		time = desc[j].split(":")[1].trim();
		        		
		        		//concate rest of date
		        		for(int k = 2; k < desc[j].split(":").length - 1; k++){
		        			time += ":" + desc[j].split(":")[k];
		        		}
		        		time = time.split(" ")[1] + " Uhr";
		        		
		        	} else if(desc[j].startsWith("Wasserstand")){
		        		height = desc[j].split(":")[1].trim() + "cm";
		        	} else if(desc[j].startsWith("Abfluss")){
		        		mass = desc[j].split(":")[1].trim() + "m³";
		        	}
		        }
				
		        riverData.setHeight(height);
		        riverData.setMass(mass);
		        riverData.setTime(time);
		        
		        //get image
		        String p = "src=\"[a-z A-Z / : \\. \\- \\_ 0-9]*[jpg png gif]\"";
				Pattern pattern = Pattern.compile(p);
				
				String data = items.get(i).getDescription();
				Matcher matcher = pattern.matcher(data);
				
				String path = null;
				if( matcher.find()) {
					path = matcher.group();
					path = path.replace("\"", "").replace("src=", "");
				}
				
				riverData.setImage(path);
		        
		        riverDataSet.add(riverData);
			} catch(Exception ex){
				Log.e(TAG, "Lakedata could not be parsed: " + ex.getMessage());
			}
		}
		
		//sort by Location Name
        Collections.sort(riverDataSet, new RiverDataComperator());
        
		return riverDataSet;
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
	
	public class RiverDataComperator implements Comparator<RiverData> {
	    @Override
	    public int compare(RiverData o1, RiverData o2) {
	        return o1.getRiverName().compareTo(o2.getRiverName());
	    }
	}

}
