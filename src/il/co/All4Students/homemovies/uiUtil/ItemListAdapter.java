package il.co.All4Students.homemovies.uiUtil;

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

import java.util.ArrayList;
import java.util.Collections;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class ItemListAdapter extends ArrayAdapter<Item> implements Filterable {
	// Attributes
	private static LayoutInflater mInflater = null;
	private ArrayList<Item> mItemList;
	private ArrayList<Item> mOriginalItemList;
	private ApplicationPreference mSettings;
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
		View rowView = convertView;

		if (convertView == null) {
			rowView = new View(mContext);
			rowView = mInflater.inflate(R.layout.list_row, null);
		} else {
			rowView = convertView;
		}

		TextView rowTitle = (TextView) rowView.findViewById(R.id.rowTitle);
		RatingBar rowRank = (RatingBar) rowView.findViewById(R.id.rowRating);
		CheckBox rowCheckBox = (CheckBox) rowView
				.findViewById(R.id.rowCheckBox);

		mItem = mItemList.get(position);

		rowTitle.setText(mItem.toString());
		rowRank.setRating(((float) mItem.getRank()) / 10);
		rowCheckBox.setChecked(mItem.getViewd());

		if (mContext instanceof ScreenWeb) {
			rowRank.setVisibility(View.GONE);
			rowCheckBox.setVisibility(View.GONE);
		} else {
			if (!mSettings.getEnablePreview()) {
				ImageView rowImage = (ImageView) rowView
						.findViewById(R.id.rowImage);
				rowImage.setVisibility(View.GONE);
			}
			if (mSettings.getEnableColor()) {
				rowView.setBackgroundColor(setColor());
			}
		}

		return rowView;
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

	// Inner Classes
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
			// Now we have to inform the adapter about the new list filtered
			if (results.count == 0)
				notifyDataSetInvalidated();
			else {
				mItemList = (ArrayList<Item>) results.values;
				notifyDataSetChanged();
			}
		}

	}

	// Additional Methods
	private void sortCompareable(ArrayList<Item> itemList) {
		int sortMethod = new ApplicationPreference(mContext).getSortMethod();
		if (itemList != null) {
			switch (sortMethod) {
			case SortByID:
				Collections.sort(itemList);
				break;

			case SortByRank:
				Collections.sort(itemList, new ItemCompareRank());
				break;

			case SortByRTID:
				Collections.sort(itemList, new ItemCompareRTID());
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
}