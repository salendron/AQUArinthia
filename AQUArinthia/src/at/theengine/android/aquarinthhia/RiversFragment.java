package at.theengine.android.aquarinthhia;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class RiversFragment extends Fragment {

	private Context mContext;
	private ListView mListView; 
	private MainActivity mActvitiy;
	
	public RiversFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_rivers, container, false);
		
		mContext = getActivity();
		mListView = (ListView) rootView;
		mActvitiy = (MainActivity) getActivity();
		
		loadData();
		
		return rootView;
	}
	
	public void loadData(){
		if(mActvitiy == null) { mActvitiy = (MainActivity) getActivity(); }
		DataLoader loader = mActvitiy.getDataLoader();
		
		if(loader != null){
			ArrayList<RiverData> riverItems = loader.getRiverItems();
			if(riverItems != null){
				mListView.setAdapter(new RiverListAdapter(mContext, R.layout.listitem_rivers, riverItems));
			}
		}
	}
}
