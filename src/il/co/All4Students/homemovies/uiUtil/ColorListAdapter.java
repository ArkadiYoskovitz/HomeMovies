package il.co.All4Students.homemovies.uiUtil;

import il.co.All4Students.homemovies.R;
import il.co.All4Students.homemovies.core.Item;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ColorListAdapter extends BaseAdapter {

	private static LayoutInflater mInflater = null;
	private ArrayList<Item> mData = new ArrayList<Item>();
	private Context mContext;

	public ColorListAdapter(Context Context, ArrayList<Item> data) {
		this.mContext = Context;
		this.mData = data;
		mInflater = (LayoutInflater) mContext
				.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return mData.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (convertView == null) {
			rowView = new View(mContext);
			rowView = mInflater.inflate(R.layout.list_row, null);
		} else {
			rowView = convertView;
		}

		TextView textTitle = (TextView) rowView.findViewById(R.id.rowTitle);
		Item item = mData.get(position);
		textTitle.setText(item.toString());
		rowView.setBackgroundColor(item.getColor());
		return rowView;
	}
}