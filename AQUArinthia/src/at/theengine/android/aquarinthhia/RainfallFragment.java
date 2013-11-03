package at.theengine.android.aquarinthhia;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import at.theengine.android.simple_rss2_android.RSSItem;
import at.theengine.android.simple_rss2_android.SimpleRss2Parser;
import at.theengine.android.simple_rss2_android.SimpleRss2ParserCallback;

public class RainfallFragment extends Fragment {

	private Context mContext;
	private ListView mListView; 
	private MainActivity mActvitiy;
	
	public RainfallFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_rainfall, container, false);
		
		mContext = getActivity();
		mListView = (ListView) rootView;
		mActvitiy = (MainActivity) getActivity();
		
		loadData();
		
		return rootView;
	}
	
	public void loadData(){
		DataLoader loader = mActvitiy.getDataLoader();
		
		if(loader != null){
			ArrayList<RainData> rainItems = loader.getRainItems();
			if(rainItems != null){
				mListView.setAdapter(new RainListAdapter(mContext, R.layout.listitem_rainfall, rainItems));
			}
		}
	}
}
