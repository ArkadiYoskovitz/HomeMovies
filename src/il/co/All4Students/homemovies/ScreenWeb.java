package il.co.All4Students.homemovies;

import static il.co.All4Students.homemovies.app.AppConstants.APP_API_KEY;
import static il.co.All4Students.homemovies.app.AppConstants.INTENT_TARGET;
import static il.co.All4Students.homemovies.app.AppConstants.Item_Add_Web;
import static il.co.All4Students.homemovies.app.AppConstants.Item_App_Settings;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_SCREEN_WEB;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_WEB_DMovieInfo;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_WEB_SITE;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_CANCEL;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_COMMIT;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_DELETE;
import il.co.All4Students.homemovies.core.Item;
import il.co.All4Students.homemovies.util.adapter.ItemListAdapter;
import il.co.All4Students.homemovies.util.app.AppUtil;
import il.co.All4Students.homemovies.util.db.ItemsHandler;
import il.co.All4Students.homemovies.util.log.util.AppLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Web Screen
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
public class ScreenWeb extends Activity implements OnItemClickListener {
	// Constants
	private final String HOST = "http://api.rottentomatoes.com";
	private final String MOVIE_SEARCH_ENDPOINT = "/api/public/v1.0/movies.json";
	private final String QUERY_KEY_PARAM = "?q=";
	private final String PAGE_LIMIT_PARAM = "&page_limit=50&page=1";
	private final String API_KEY_PARAM = "&apikey=".intern();
	private final String DEFAULT_ENCODING = "utf-8";

	// Attributes
	private ArrayList<Item> mItemList;
	private Item mReturnedItem;
	private EditText txtSearch;
	private DownloadMovieInfo downloadTask;
	private ItemListAdapter mAdapter;
	private ListView mListView;

	// System Events
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_web);
		AppLog.log(ScreenWeb.this, LOG_TAG_SCREEN_WEB,
				"Screen Web Layout was Created and loaded");
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		EditText searchText = (EditText) findViewById(R.id.ScreenWebEditText1);
		outState.putString("searchText", searchText.getText().toString());
		outState.putParcelableArrayList("mItemList", mItemList);
		AppLog.log(ScreenWeb.this, LOG_TAG_SCREEN_WEB, "onSaveInstanceState");
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		EditText searchText = (EditText) findViewById(R.id.ScreenWebEditText1);
		searchText.setText(savedInstanceState.getString("searchText"));
		mItemList = savedInstanceState.getParcelableArrayList("mItemList");
		RefreshScreen();
		AppLog.log(ScreenWeb.this, LOG_TAG_SCREEN_WEB, "onRestoreInstanceState");
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Activity sending info Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		ItemsHandler itemHandler = new ItemsHandler(this);
		int lastItem = 0;
		switch (requestCode) {
		// Handaling the return of the Add button
		case Item_Add_Web:
			switch (resultCode) {

			case RESULT_CODE_CANCEL:
				AppLog.log(ScreenWeb.this, LOG_TAG_SCREEN_WEB,
						"onActivityResult - Item_Add_Local - CANCEL");
				break;

			case RESULT_CODE_DELETE:
				AppLog.log(ScreenWeb.this, LOG_TAG_SCREEN_WEB,
						"onActivityResult - Item_Add_Local - Delete");
				AppLog.log(ScreenWeb.this, LOG_TAG_SCREEN_WEB,
						"No item was added, just log for now");
				break;

			case RESULT_CODE_COMMIT:
				AppLog.log(ScreenWeb.this, LOG_TAG_SCREEN_WEB,
						"onActivityResult - Item_Add_Local - COMMIT");
				mReturnedItem = data.getExtras().getParcelable(INTENT_TARGET);
				// mReturnedItem = fixItemNumber(mReturnedItem);
				itemHandler.addItem(mReturnedItem);
				lastItem = itemHandler.getLastItemId();
				mReturnedItem = itemHandler.getItem(lastItem);
				// mItemList.add(mReturnedItem);
				mListView = (ListView) findViewById(R.id.ScreenWebListView);
				AppUtil.sortCompareable(ScreenWeb.this, mItemList);
				mAdapter = new ItemListAdapter(mItemList, ScreenWeb.this);
				mListView.setAdapter(mAdapter);
				break;
			case Item_App_Settings:
				switch (resultCode) {

				case RESULT_CODE_CANCEL:
					RefreshScreen();
					break;

				default:
					break;
				}
			default:
				AppLog.log(ScreenWeb.this, LOG_TAG_SCREEN_WEB,
						"onActivityResult - Item_Add_Local - default");
				break;
			}
			break;
		// ////////////////////////////////////////////////////////////////////////////////

		default:
			AppLog.log(ScreenWeb.this, LOG_TAG_SCREEN_WEB,
					"onActivityResult - default - default");
			break;
		}

		// Handaling the screen refresh
		AppUtil.sortCompareable(ScreenWeb.this, mItemList);
		mAdapter = new ItemListAdapter(mItemList, this);
		mAdapter.notifyDataSetChanged();
		AppLog.log(ScreenWeb.this, LOG_TAG_SCREEN_WEB, "View re-loaded");

		super.onActivityResult(requestCode, resultCode, data);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Menu Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * onCreateOptionsMenu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.screen_web_menu_option, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mListView = (ListView) findViewById(R.id.ScreenWebListView);

		switch (item.getItemId()) {
		case R.id.screenWebOptionMenuExitSettings:
			AppLog.log(ScreenWeb.this, LOG_TAG_SCREEN_WEB,
					"optionMenuExitSettings was pressed");
			System.exit(0);
			break;

		case R.id.screenWebOptionMenuSettings:
			Intent intent = new Intent(ScreenWeb.this, ScreenPreferences.class);
			startActivityForResult(intent, Item_App_Settings);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// OnClick Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void onClickWebGo(View view) {
		closeKeybord();
		txtSearch = (EditText) findViewById(R.id.ScreenWebEditText1);
		if (txtSearch.getText() != null) {
			try {
				if (downloadTask != null) {
					if (downloadTask.getStatus() != AsyncTask.Status.FINISHED) {
						AppLog.log(ScreenWeb.this, LOG_TAG_SCREEN_WEB,
								"onClickWebGo - no need to start a new task");
						return;
					}
				}
				String searchString = txtSearch.getText().toString();
				DownloadMovieInfo downloadTask = new DownloadMovieInfo(
						ScreenWeb.this);
				String query = new StringBuilder()
						.append(HOST)
						.append(MOVIE_SEARCH_ENDPOINT)
						.append(QUERY_KEY_PARAM)
						.append(URLEncoder.encode(searchString,
								DEFAULT_ENCODING)).append(PAGE_LIMIT_PARAM)
						.append(API_KEY_PARAM).append(APP_API_KEY).toString();
				downloadTask.execute(query);
			} catch (Exception e) {
				Log.e(LOG_TAG_WEB_SITE, "Exception: " + e.getMessage());
			}
		}

	}

	public void onClickWebCancel(View view) {
		closeKeybord();
		AppLog.log(ScreenWeb.this, LOG_TAG_SCREEN_WEB,
				"Edit Screen - Cancel button was pressed");
		Intent returnIntent = new Intent();
		setResult(RESULT_CODE_CANCEL, returnIntent);
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		closeKeybord();
		AppLog.log(ScreenWeb.this, LOG_TAG_SCREEN_WEB,
				"ScreenWeb - An Item was clicked from the list");
		Intent intent = new Intent(ScreenWeb.this, ScreenEdit.class);
		intent.putExtra(INTENT_TARGET, mItemList.get(position));
		startActivityForResult(intent, Item_Add_Web);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Additional Methods
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void RefreshScreen() {
		mListView = (ListView) findViewById(R.id.ScreenWebListView);
		AppUtil.sortCompareable(ScreenWeb.this, mItemList);
		mAdapter = new ItemListAdapter(mItemList, ScreenWeb.this);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		mListView.setOnItemClickListener(this);

	}

	private void closeKeybord() {
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(
				getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Inner Classes
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * @author Arkadi Yoskovitz
	 * @date 2013-02-08
	 */
	private class DownloadMovieInfo extends AsyncTask<String, Integer, String> {
		// ///////////////////////////////////////////////////////////////////////////////////
		// Attributes
		// ///////////////////////////////////////////////////////////////////////////////////
		private ArrayList<Item> mItemList;
		private ProgressDialog mDialog;
		private Activity mActivity;

		// ///////////////////////////////////////////////////////////////////////////////////
		// Constants - JSON Node names
		// ///////////////////////////////////////////////////////////////////////////////////
		final String TAG_MOVIES = "movies";
		final String TAG_ID = "id";
		final String TAG_TITLE = "title";
		final String TAG_SYNOPSIS = "synopsis";
		final String TAG_POSTERS = "posters";
		final String TAG_POSTERS_ORIGINAL = "original";

		// ///////////////////////////////////////////////////////////////////////////////////
		// Constractors
		// ///////////////////////////////////////////////////////////////////////////////////
		public DownloadMovieInfo(Activity activity) {
			mActivity = activity;
			mDialog = new ProgressDialog(mActivity);
		}

		// ///////////////////////////////////////////////////////////////////////////////////
		// Task Events
		// ///////////////////////////////////////////////////////////////////////////////////
		@Override
		protected void onPreExecute() {
			mItemList = new ArrayList<Item>();
			mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDialog.setCancelable(true);
			mDialog.setMessage("Loading...");
			mDialog.setProgress(0);
			mDialog.show();

			// Reset the progress bar
			TextView errorMsg = (TextView) mActivity
					.findViewById(R.id.ScreenWebTextView1);
			errorMsg.setVisibility(View.INVISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			AppLog.log(ScreenWeb.this, LOG_TAG_WEB_DMovieInfo,
					"starting download of items found at the rotten tomatoes site");

			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				// make a HTTP request
				response = httpclient.execute(new HttpGet(params[0]));

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					// request successful - read the response and close the
					// connection
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					// request failed - close the connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {
				AppLog.log(ScreenWeb.this, LOG_TAG_WEB_DMovieInfo,
						"Couldn't make a successful request!");
			}
			return responseString;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			mDialog.show();
			mDialog.setProgress(progress[0]);
		}

		@Override
		protected void onPostExecute(String response) {
			super.onPostExecute(response);

			if (response != null) {

				try {
					// convert the String response to a JSON object,
					JSONObject jsonResponse = new JSONObject(response);

					// fetch the array of movies in the response
					JSONArray foundItems = jsonResponse
							.getJSONArray(TAG_MOVIES);

					for (int i = 0; i < foundItems.length(); i++) {
						JSONObject c = foundItems.getJSONObject(i);

						// Storing each JSON item in variable
						String id = c.getString(TAG_ID);
						String title = c.getString(TAG_TITLE);

						String synopsis = c.getString(TAG_SYNOPSIS);

						// Posters is a JSON Object
						JSONObject posters = c.getJSONObject(TAG_POSTERS);
						String original = posters
								.getString(TAG_POSTERS_ORIGINAL);

						Item item = new Item(title, synopsis, original,
								Integer.parseInt(id));
						mItemList.add(item);
					}
				} catch (JSONException e) {
					AppLog.log(ScreenWeb.this, LOG_TAG_WEB_DMovieInfo,
							"Failed to parse the JSON response!");
				}
				// refresh mListView
				mListView = (ListView) mActivity
						.findViewById(R.id.ScreenWebListView);
				AppUtil.sortCompareable(mActivity, mItemList);
				mAdapter = new ItemListAdapter(mItemList, mActivity);
				mListView.setAdapter(mAdapter);
				mListView.setOnItemClickListener(ScreenWeb.this);
			} else {
				TextView errorMsg = (TextView) mActivity
						.findViewById(R.id.ScreenWebTextView1);
				errorMsg.setVisibility(View.VISIBLE);
				errorMsg.setText(mActivity.getResources().getString(
						R.string.ErrorMsgDownLoadWeb));
			}
			mDialog.dismiss();
			ScreenWeb.this.mItemList = mItemList;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}
}
