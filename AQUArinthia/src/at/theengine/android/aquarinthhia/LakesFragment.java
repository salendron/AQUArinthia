package at.theengine.android.aquarinthhia;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class LakesFragment extends Fragment {

	private Context mContext;
	private ListView mListView; 
	private MainActivity mActvitiy;
	
	public LakesFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_lakes, container, false);
		
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
			ArrayList<LakeData> lakeItems = loader.getLakeItems();
			if(lakeItems != null){
				mListView.setAdapter(new LakeListAdapter(mContext, R.layout.listitem_lakes, lakeItems));
			}
		}
	}
}
