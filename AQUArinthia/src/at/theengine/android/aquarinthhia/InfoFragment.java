package at.theengine.android.aquarinthhia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoFragment extends Fragment {
	
	public InfoFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_info, container, false);
		
		TextView tvAuthorLinks = (TextView) rootView.findViewById(R.id.tvAuthorLinks);
		TextView tvOGDLinks = (TextView) rootView.findViewById(R.id.tvOGDLinks);
		
		Linkify.addLinks(tvAuthorLinks, Linkify.ALL);
		Linkify.addLinks(tvOGDLinks, Linkify.ALL);
		
		return rootView;
	}
}
