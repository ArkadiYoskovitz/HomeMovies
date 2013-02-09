package il.co.All4Students.homemovies;

import static il.co.All4Students.homemovies.app.AppConstants.INTENT_TARGET;
import il.co.All4Students.homemovies.app.ApplicationPreference;
import il.co.All4Students.homemovies.core.Item;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Grid screen
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
public class ScreenGrid extends Activity implements OnItemClickListener {
	// Attributes
	private Item mEditedItem;
	private ApplicationPreference mSettings;
	private GridView mGridView;
	private ImageAdapter mAdapter;

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// System Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_grid);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mEditedItem == null) {
			mEditedItem = getIntent().getExtras().getParcelable(INTENT_TARGET);
		}
		mGridView = (GridView) findViewById(R.id.gridview);
		mAdapter = new ImageAdapter(this);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Activity sending info Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Menu Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// OnClick Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(ScreenGrid.this, Integer.valueOf(position).toString(),
				Toast.LENGTH_SHORT).show();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Additional Methods
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Inner Classes
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * @author Arkadi Yoskovitz
	 * @date 2013-02-08
	 */
	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context c) {
			mContext = c;
		}

		public int getCount() {
			return mThumbIds.length;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(8, 8, 8, 8);
			} else {
				imageView = (ImageView) convertView;
			}

			imageView.setImageResource(mThumbIds[position]);
			return imageView;
		}

		// references to our images
		private Integer[] mThumbIds = { R.drawable.sample_0,
				R.drawable.sample_1, R.drawable.sample_2, R.drawable.sample_3,
				R.drawable.sample_4, R.drawable.sample_5, R.drawable.sample_6,
				R.drawable.sample_7, R.drawable.sample_0, R.drawable.sample_1,
				R.drawable.sample_2, R.drawable.sample_3, R.drawable.sample_4,
				R.drawable.sample_5, R.drawable.sample_6, R.drawable.sample_7,
				R.drawable.sample_0, R.drawable.sample_1, R.drawable.sample_2,
				R.drawable.sample_3, R.drawable.sample_4, R.drawable.sample_5,
				R.drawable.sample_6, R.drawable.sample_7 };
	}

}