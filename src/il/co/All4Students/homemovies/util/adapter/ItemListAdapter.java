package il.co.All4Students.homemovies.util.adapter;

import il.co.All4Students.homemovies.R;
import il.co.All4Students.homemovies.ScreenWeb;
import il.co.All4Students.homemovies.app.ApplicationPreference;
import il.co.All4Students.homemovies.core.Item;
import il.co.All4Students.homemovies.util.app.AppUtil;
import il.co.All4Students.homemovies.util.db.ItemsHandler;
import il.co.All4Students.homemovies.util.imageWeb.ImageLoader;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * A generic class, handel's the different view list adapter
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
@SuppressLint("DefaultLocale")
public class ItemListAdapter extends ArrayAdapter<Item> implements Filterable {
	// Attributes
	private static LayoutInflater mInflater = null;
	private ArrayList<Item> mItemList;
	private ArrayList<Item> mOriginalItemList;
	private ApplicationPreference mSettings;
	private ImageLoader imageLoader;
	private Context mContext;
	private Filter itemFilter;
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
	public ItemListAdapter(ArrayList<Item> itemList, Context context) {
		super(context, R.layout.list_row, itemList);
		this.mContext = context;
		AppUtil.sortCompareable(mContext, itemList);
		this.mItemList = itemList;
		this.mOriginalItemList = itemList;
		this.mSettings = new ApplicationPreference(mContext);
		mInflater = (LayoutInflater) mContext
				.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(mContext.getApplicationContext());
	}

	// Adapter Methods
	/**
	 * How many items are in the data set represented by this Adapter.
	 * 
	 * @return Count of items.
	 */
	public int getCount() {
		return mItemList.size();
	}

	/**
	 * Get the data item associated with the specified position in the data set.
	 * 
	 * @param position
	 *            - Position of the item whose data we want within the adapter's
	 *            data set.
	 * @return The data at the specified position.
	 */
	public Item getItem(int position) {
		return mItemList.get(position);
	}

	/**
	 * Get the row id associated with the specified position in the list.
	 * 
	 * @param position
	 *            - The position of the item within the adapter's data set whose
	 *            row id we want.
	 * @return The id of the item at the specified position.
	 */
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
	public View getView(int position, View convertView, ViewGroup parent) {
		RowViewHolder viewHolder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_row, parent, false);
			viewHolder = new RowViewHolder();
			viewHolder.rowTitle = (TextView) convertView
					.findViewById(R.id.rowTitle);
			viewHolder.rowRank = (RatingBar) convertView
					.findViewById(R.id.rowRating);
			viewHolder.rowCheckBox = (CheckBox) convertView
					.findViewById(R.id.rowCheckBox);
			viewHolder.rowImage = (ImageView) convertView
					.findViewById(R.id.rowImage);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (RowViewHolder) convertView.getTag();
		}

		mItem = mItemList.get(position);

		viewHolder.rowTitle.setText(mItem.toString());
		viewHolder.rowRank.setRating(((float) mItem.getRank()) / 10);
		viewHolder.rowCheckBox.setChecked(mItem.getViewd());

		viewHolder.rowCheckBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						mItem.setViewd(isChecked);
						ItemsHandler itemHandler = new ItemsHandler(mContext);
						itemHandler.updateItem(mItem);
						notifyDataSetChanged();
					}
				});

		if (!mSettings.getEnablePreview()) {
			viewHolder.rowImage.setVisibility(View.GONE);
		}

		if (mContext instanceof ScreenWeb) {
			viewHolder.rowImage.setVisibility(View.GONE);
			viewHolder.rowRank.setVisibility(View.GONE);
			viewHolder.rowCheckBox.setVisibility(View.GONE);
			viewHolder.rowTitle.setHeight((int) getContext().getResources()
					.getDimension(R.dimen.Size30dp));
		} else {
			if (mSettings.getEnablePreview()) {
				imageLoader
						.DisplayImage(mItem.getUrlWeb(), viewHolder.rowImage);
			}
			if (mSettings.getEnableColor()) {
				convertView.setBackgroundColor(setColor());
			}

		}
		return convertView;
	}

	// Additional Methods
	public void resetData() {
		mItemList = mOriginalItemList;
	}

	private int setColor() {
		int tmpRed, tmpGreen, tmpBlue;
		if (mItem.getRank() <= 50) {
			tmpRed = 255;
			tmpGreen = 255 * (mItem.getRank() * 2) / 100;
			tmpBlue = 0;
		} else {
			tmpRed = 255 * (100 - (mItem.getRank() * 2)) / 100;
			tmpGreen = 255;
			tmpBlue = 0;
		}
		return Color.rgb(tmpRed, tmpGreen, tmpBlue);
	}

	@Override
	public Filter getFilter() {
		if (itemFilter == null)
			itemFilter = new ItemFilter();
		return itemFilter;
	}

	// Additional Methods
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	// Inner Classes
	/**
	 * RowViewHolder
	 * 
	 * @author Arkadi Yoskovitz
	 * @date 2013-02-08
	 */
	static class RowViewHolder {
		LinearLayout rowThumbnail;
		ImageView rowImage;
		TextView rowTitle;
		RatingBar rowRank;
		CheckBox rowCheckBox;
	}

	/**
	 * A filter constrains data with a filtering pattern.
	 * 
	 * @author Arkadi Yoskovitz
	 * @date 2013-02-08
	 */
	@SuppressLint("DefaultLocale")
	private class ItemFilter extends Filter {

		/**
		 * Invoked in a worker thread to filter the data according to the
		 * constraint.
		 * 
		 * Subclasses must implement this method to perform the filtering
		 * operation.
		 * 
		 * Results computed by the filtering operation must be returned as a
		 * Filter.FilterResults that will then be published in the UI thread
		 * through publishResults(CharSequence,
		 * android.widget.Filter.FilterResults).
		 * 
		 * @param constraint
		 *            - the constraint used to filter the data
		 * @return the results of the filtering operation
		 */
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			// We implement here the filter logic
			if (constraint == null || constraint.length() == 0) {
				// No filter implemented we return all the list
				results.values = mOriginalItemList;
				results.count = mOriginalItemList.size();
			} else {
				// We perform filtering operation
				ArrayList<Item> nItemList = new ArrayList<Item>();

				for (Item item : mItemList) {
					if (item.getSubject().toUpperCase()
							.startsWith(constraint.toString().toUpperCase()))
						nItemList.add(item);
				}

				results.values = nItemList;
				results.count = nItemList.size();
			}
			return results;
		}

		/**
		 * Invoked in the UI thread to publish the filtering results in the user
		 * interface. Subclasses must implement this method to display the
		 * results computed in performFiltering(CharSequence).
		 * 
		 * @param constraint
		 *            - the constraint used to filter the data
		 * @param results
		 *            - the results of the filtering operation
		 */
		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			mItemList = (ArrayList<Item>) results.values;
			notifyDataSetChanged();
		}
	}
}