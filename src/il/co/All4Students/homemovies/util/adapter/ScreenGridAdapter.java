package il.co.All4Students.homemovies.util.adapter;

import il.co.All4Students.homemovies.R;
import il.co.All4Students.homemovies.app.ApplicationPreference;
import il.co.All4Students.homemovies.core.Item;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ScreenGridAdapter extends ArrayAdapter<String> {
	// Attributes
	private static LayoutInflater mInflater = null;
	private ApplicationPreference mSettings;
	private ArrayList<String> mURIList;
	private Context mContext;
	private Item mItem;

	// Constractors
	/**
	 * ItemListAdapter - Constructor
	 * 
	 * @param itemList
	 *            - The objects to represent in the ListView
	 * @param context
	 *            - The current context
	 */

	public ScreenGridAdapter(Context context, ArrayList<String> uriList) {
		super(context, R.layout.list_row, uriList);
		this.mContext = context;
		this.mURIList = uriList;
		this.mSettings = new ApplicationPreference(mContext);
		mInflater = (LayoutInflater) mContext
				.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
	}

	// Adapter Methods
	/**
	 * How many items are in the data set represented by this Adapter.
	 * 
	 * @return Count of items.
	 */
	@Override
	public int getCount() {
		return mURIList.size();
	}

	/**
	 * Get the data item associated with the specified position in the data set.
	 * 
	 * @param position
	 *            - Position of the item whose data we want within the adapter's
	 *            data set.
	 * @return The data at the specified position.
	 */
	@Override
	public String getItem(int position) {
		return mURIList.get(position);
	}

	/**
	 * Get the row id associated with the specified position in the list.
	 * 
	 * @param position
	 *            - The position of the item within the adapter's data set whose
	 *            row id we want.
	 * @return The id of the item at the specified position.
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Get a View that displays the data at the specified position in the data
	 * set. You can either create a View manually or inflate it from an XML
	 * layout file. When the View is inflated, the parent View (GridView,
	 * ListView...) will apply default layout parameters unless you use
	 * inflate(int, android.view.ViewGroup, boolean) to specify a root view and
	 * to prevent attachment to the root.
	 * 
	 * @param position
	 *            - The position of the item within the adapter's data set of
	 *            the item whose view we want.
	 * @param convertView
	 *            - The old view to reuse, if possible. Note: You should check
	 *            that this view is non-null and of an appropriate type before
	 *            using. If it is not possible to convert this view to display
	 *            the correct data, this method can create a new view.
	 *            Heterogeneous lists can specify their number of view types, so
	 *            that this View is always of the right type (see
	 *            getViewTypeCount() and getItemViewType(int)).
	 * @param parent
	 *            - The parent that this view will eventually be attached to
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GridViewHolder viewHolder;
// implement a new inflater element, temporary using list_row
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_row, parent, false);
			viewHolder = new GridViewHolder();
			viewHolder.gridImage = (ImageView) convertView.findViewById(R.id.rowImage);
			viewHolder.gridImage.setImageURI(Uri.fromFile(new File(mURIList.get(position))));
			viewHolder.gridImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
			viewHolder.gridImage.setLayoutParams(new GridView.LayoutParams(70, 70));
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (GridViewHolder) convertView.getTag();
		}
		return convertView;
	}

	// Inner Classes
	/**
	 * RowViewHolder
	 * 
	 * @author Arkadi Yoskovitz
	 * @date 2013-02-08
	 */
	static class GridViewHolder {
		ImageView gridImage;
	}
}
