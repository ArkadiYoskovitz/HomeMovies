package il.co.All4Students.homemovies;

import static il.co.All4Students.homemovies.app.AppConstants.INTENT_TARGET;
import static il.co.All4Students.homemovies.app.AppConstants.INTENT_TARGET_URI;
import static il.co.All4Students.homemovies.app.AppConstants.Item_GridDetails;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_SCREEN_GRIDVIEW;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_CANCEL;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_COMMIT;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_DELETE;
import il.co.All4Students.homemovies.core.Item;
import il.co.All4Students.homemovies.util.adapter.ScreenGridAdapter;
import il.co.All4Students.homemovies.util.json.JSONHandler;
import il.co.All4Students.homemovies.util.log.util.AppLog;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * Grid screen
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
public class ScreenGrid extends Activity implements OnItemClickListener {
	// Attributes
	private ArrayList<String> mUriList;
	private ScreenGridAdapter mAdapter;
	private GridView mGridView;
	private String mFilePath;
	private Item mEditedItem;

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
			try {
				mFilePath = getIntent().getExtras()
						.getString(INTENT_TARGET_URI);
			} catch (Exception e) {
				AppLog.log(ScreenGrid.this, LOG_TAG_SCREEN_GRIDVIEW, e
						.getMessage().toString());
			}

			mUriList = JSONHandler.getURIFromJSON(mEditedItem.getUrlLocal());

			mGridView = (GridView) findViewById(R.id.gridview);
			mAdapter = new ScreenGridAdapter(ScreenGrid.this, mUriList);

			mGridView.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();

			mGridView.setOnItemClickListener(this);
		}

	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Activity sending info Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			mEditedItem = data.getExtras().getParcelable(INTENT_TARGET);
			mFilePath = data.getExtras().getString(INTENT_TARGET_URI);
			switch (requestCode) {
			case Item_GridDetails:
				switch (resultCode) {
				case RESULT_CODE_CANCEL:
					AppLog.log(ScreenGrid.this, LOG_TAG_SCREEN_GRIDVIEW,
							"onActivityResult - Item_GridDetails - CANCEL");
					break;
				case RESULT_CODE_DELETE:
					AppLog.log(ScreenGrid.this, LOG_TAG_SCREEN_GRIDVIEW,
							"onActivityResult - Item_GridDetails - Delete");
					mUriList.remove(mFilePath);
					mEditedItem.setUrlLocal(JSONHandler
							.putURIintoJSON(mUriList));

					mAdapter.notifyDataSetChanged();
					break;

				case RESULT_CODE_COMMIT:
					AppLog.log(ScreenGrid.this, LOG_TAG_SCREEN_GRIDVIEW,
							"onActivityResult - Item_GridDetails - COMMIT");
					break;

				default:
					AppLog.log(ScreenGrid.this, LOG_TAG_SCREEN_GRIDVIEW,
							"onActivityResult - Item_GridDetails - DEFAULT");
					break;
				}
				break;

			default:
				AppLog.log(ScreenGrid.this, LOG_TAG_SCREEN_GRIDVIEW,
						"onActivityResult - default - default");
				break;
			}
		} catch (Exception e) {
			AppLog.log(ScreenGrid.this, LOG_TAG_SCREEN_GRIDVIEW, e.getMessage()
					.toString());
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// OnClick Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Intent intentGridDetail = new Intent(ScreenGrid.this,
				ScreenGridDetail.class);
		intentGridDetail.putExtra(INTENT_TARGET, mEditedItem);
		intentGridDetail.putExtra(INTENT_TARGET_URI, mUriList.get(position));
		startActivityForResult(intentGridDetail, Item_GridDetails);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent returnIntent = new Intent();
		returnIntent.putExtra(INTENT_TARGET, mEditedItem);
		setResult(RESULT_CODE_CANCEL, returnIntent);
		finish();
	}

}