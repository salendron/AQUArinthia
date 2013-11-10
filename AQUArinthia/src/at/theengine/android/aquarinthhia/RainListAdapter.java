package at.theengine.android.aquarinthhia;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.theengine.android.simple_rss2_android.RSSItem;

public class RainListAdapter extends ArrayAdapter<RainData> {

    private ArrayList<RainData> items;
    private Context ctx;
    private int layout;

    public RainListAdapter(Context context, int layout, List<RainData> items) {
        super(context, layout, items);
        this.items = (ArrayList<RainData>) items;
        this.ctx = context;
        this.layout = layout;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v;

        if (convertView == null) {  // if it's not recycled, initialize some attributes
            LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(layout, null);
        } else {
            v = convertView;
        }
        
        TextView tvLocation = (TextView) v.findViewById(
                R.id.tvLocation
        );
        tvLocation.setText(items.get(position).getLocationName());
        
        TextView tvTemp = (TextView) v.findViewById(
                R.id.tvTemp
        );
        tvTemp.setText(items.get(position).getTemp());
        
        TextView tvRain = (TextView) v.findViewById(
                R.id.tvRain
        );
        tvRain.setText(items.get(position).getRain());
        
        TextView tvTime = (TextView) v.findViewById(
                R.id.tvTime
        );
        tvTime.setText(items.get(position).getTime());
        
        //set diagram
        final WebView webView = (WebView) v.findViewById(R.id.wvDiagramm);
        String content = "<html><head><title>news</title></head><body><img src='" + items.get(position).getImage() + "' width='100%'></body></html>";
		webView.loadDataWithBaseURL(null,content,"text/html","UTF-8",null);
		
		final Button btnCloseDetail = (Button) v.findViewById(R.id.btnCloseDetail);
		btnCloseDetail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnCloseDetail.setVisibility(View.GONE);
				webView.setVisibility(View.GONE);
			}
		});
		
		LinearLayout llItem = (LinearLayout)  v.findViewById(R.id.llItem);
		llItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnCloseDetail.setVisibility(View.VISIBLE);
				webView.setVisibility(View.VISIBLE);
			}
		});
		
		webView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webView.getUrl()));
				ctx.startActivity(browserIntent);
			}
		});
		
		btnCloseDetail.setVisibility(View.GONE);
		webView.setVisibility(View.GONE);

        return v;
    }
}
