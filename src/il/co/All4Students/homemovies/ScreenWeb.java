package il.co.All4Students.homemovies;

import static il.co.All4Students.homemovies.app.AppConstants.INTENT_TARGET;
import static il.co.All4Students.homemovies.app.AppConstants.Item_Edit;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_MAIN;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_WEB;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_WEB_SITE;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_CANCEL;
import il.co.All4Students.homemovies.core.Item;
import il.co.All4Students.homemovies.network.DownloadMovieInfo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;

public class ScreenWeb extends Activity implements OnItemClickListener {
	// Attributes
	private ArrayList<Item> mItemList;
	private EditText txtSearch;
	private DownloadMovieInfo downloadTask;

	// System Events
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_web);
		Log.d(LOG_TAG_MAIN, "Screen Web Layout was Created and loaded");
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Activity sending info Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Menu Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// OnClick Events
	public void onClickWebGo(View view) {
		txtSearch = (EditText) findViewById(R.id.ScreenWebEditText1);
		if (txtSearch.getText() != null) {
			try {
				if (downloadTask != null) {
					if (downloadTask.getStatus() != AsyncTask.Status.FINISHED) {
						Log.d(LOG_TAG_WEB,
								"onClickWebGo - no need to start a new task");
						return;
					}
				}
				String searchString = txtSearch.getText().toString();
				DownloadMovieInfo downloadTask = new DownloadMovieInfo(
						ScreenWeb.this);
				downloadTask.execute(searchString);

			} catch (Exception e) {
				Log.e(LOG_TAG_WEB_SITE, "Exception: " + e.getMessage());
			}
		}

	}

	public void onClickWebCancel(View view) {
		Log.d(LOG_TAG_WEB, "Edit Screen - Cancel button was pressed");
		Intent returnIntent = new Intent();
		setResult(RESULT_CODE_CANCEL, returnIntent);
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.d(LOG_TAG_MAIN, "ScreenWeb - An Item was clicked from the list");
		Intent intent = new Intent(ScreenWeb.this, ScreenEdit.class);
		intent.putExtra(INTENT_TARGET, mItemList.get(position));
		startActivityForResult(intent, Item_Edit);

	}
}
