package il.co.All4Students.homemovies.util.ui;

import static il.co.All4Students.homemovies.app.AppConstants.SortByID;
import static il.co.All4Students.homemovies.app.AppConstants.SortByRTID;
import static il.co.All4Students.homemovies.app.AppConstants.SortByRank;
import static il.co.All4Students.homemovies.app.AppConstants.SortBySubject;
import il.co.All4Students.homemovies.R;
import il.co.All4Students.homemovies.ScreenWeb;
import il.co.All4Students.homemovies.app.ApplicationPreference;
import il.co.All4Students.homemovies.core.Item;
import il.co.All4Students.homemovies.core.ItemCompareRTID;
import il.co.All4Students.homemovies.core.ItemCompareRank;
import il.co.All4Students.homemovies.core.ItemCompareSubject;
import il.co.All4Students.homemovies.util.db.ItemsHandler;
import il.co.All4Students.homemovies.util.imageutils.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;

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
	public ItemListAdapter(ArrayList<Item> itemList, Context context) {
		super(context, R.layout.list_row, itemList);
		this.mContext = context;
		sortCompareable(itemList);
		this.mItemList = itemList;
		this.mOriginalItemList = itemList;
		this.mSettings = new ApplicationPreference(mContext);
		mInflater = (LayoutInflater) mContext
				.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);

		imageLoader = new ImageLoader(mContext.getApplicationContext());
	}

	// Adapter Methods
	public int getCount() {
		return mItemList.size();
	}

	public Item getItem(int position) {
		return mItemList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

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
		} else {
			if (isOnline()) {
				imageLoader
						.DisplayImage(mItem.getUrlWeb(), viewHolder.rowImage);
			} else {
				if (mItem.getUrlLocal().length() != 0)
					imageLoader.DisplayImage(mItem.getUrlLocal(),
							viewHolder.rowImage);
			}
			if (mSettings.getEnableColor()) {
				convertView.setBackgroundColor(setColor());
			}

		}

		return convertView;
	}

	// Additional Methods
	private void sortCompareable(ArrayList<Item> itemList) {
		int sortMethod = new ApplicationPreference(mContext).getSortMethod();
		if (itemList != null) {
			switch (sortMethod) {
			case SortByID:
				Collections.sort(itemList);
				break;

			case SortByRTID:
				Collections.sort(itemList, new ItemCompareRTID());
				break;

			case SortByRank:
				Collections.sort(itemList, new ItemCompareRank());
				break;

			case SortBySubject:
				Collections.sort(itemList, new ItemCompareSubject());
				break;

			default:
				Collections.sort(itemList);
				break;
			}
		}
	}

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
	static class RowViewHolder {
		LinearLayout rowThumbnail;
		ImageView rowImage;
		TextView rowTitle;
		RatingBar rowRank;
		CheckBox rowCheckBox;
	}

	@SuppressLint("DefaultLocale")
	private class ItemFilter extends Filter {

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

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			mItemList = (ArrayList<Item>) results.values;
			notifyDataSetChanged();
		}

	}
}