//package il.co.All4Students.homemovies.network;
//
//import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_WEB_DMovieInfo;
//import il.co.All4Students.homemovies.R;
//import il.co.All4Students.homemovies.core.Item;
//import il.co.All4Students.homemovies.jsonparsing.JSONParser;
//import il.co.All4Students.homemovies.uiUtil.ColorListAdapter;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.StatusLine;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.graphics.drawable.ColorDrawable;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.TextView;
//
//// Inner Classes
//public class DownloadMovieInfo extends AsyncTask<String, Integer, String> {
//
//	// Constants
//	private final String HOST = "http://api.rottentomatoes.com";
//	private final String MOVIE_SEARCH_ENDPOINT = "/api/public/v1.0/movies.json";
//	private final String QUERY_KEY_PARAM = "?q=";
//	private final String API_KEY_PARAM = "&apikey=".intern();
//	private final String DEFAULT_ENCODING = "utf-8";
//	private final String mApiKey = "e3hw7dwu5ud24e56873u99b2";
//
//	// JSON CONSTANTS
//	// JSON Node names
//	final String TAG_MOVIES = "movies";
//	final String TAG_ID = "id";
//	final String TAG_TITLE = "title";
//	final String TAG_YEAR = "year";
//	final String TAG_MPAA_RATING = "mpaa_rating";
//	final String TAG_RUNTIME = "runtime";
//	final String TAG_RELEASE_DATES = "release_dates";
//	final String TAG_RELEASE_DATES_THEATER = "theater";
//	final String TAG_RELEASE_DATES_DVD = "dvd";
//
//	final String TAG_RATINGS = "ratings";
//	final String TAG_RATINGS_CRITICS_SCORE = "critics_score";
//	final String TAG_RATINGS_AUDIENCE_RATING = "audience_rating";
//	final String TAG_RATINGS_AUDIENCE_SCORE = "audience_score";
//
//	final String TAG_SYNOPSIS = "synopsis";
//
//	final String TAG_POSTERS = "posters";
//	final String TAG_POSTERS_THUMBNAIL = "thumbnail";
//	final String TAG_POSTERS_PROFILE = "profile";
//	final String TAG_POSTERS_DETAILED = "detailed";
//	final String TAG_POSTERS_ORIGINAL = "original";
//
//	final String TAG_ALTERNATE_IDS = "alternate_ids";
//	final String TAG_ALTERNATE_IDS_IMDB = "imdb";
//
//	final String TAG_LINKS = "links";
//	final String TAG_LINKS_SELF = "self";
//	final String TAG_LINKS_ALTERNATE = "alternate";
//	final String TAG_LINKS_CAST = "cast";
//	final String TAG_LINKS_CLIPS = "clips";
//	final String TAG_LINKS_REVIEWS = "reviews";
//	final String TAG_LINKS_SIMILAR = "similar";
//	// ///////////////////////////////////////////////////////////////////////////////////
//
//	// Attributes
//	private Activity mActivity;
//	private ProgressDialog mDialog;
//	private ArrayList<Item> mItemList;
//	private ListView mListView;
//
//	// ///////////////////////////////////////////////////////////////////////////////////
//
//	// Constractors
//	public DownloadMovieInfo(Activity activity) {
//		mActivity = activity;
//		mDialog = new ProgressDialog(mActivity);
//	}
//
//	// ///////////////////////////////////////////////////////////////////////////////////
//
//	// Task Events
//	@Override
//	protected void onPreExecute() {
//		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//		mDialog.setCancelable(true);
//		mDialog.setMessage("Loading...");
//		mDialog.setProgress(0);
//
//		// Reset the progress bar
//		TextView errorMsg = (TextView) mActivity
//				.findViewById(R.id.ScreenWebTextView1);
//		errorMsg.setVisibility(View.INVISIBLE);
//	}
//
//	@Override
//	protected String doInBackground(String... params) {
//		Log.d(LOG_TAG_WEB_DMovieInfo,
//				"starting download of items found at the rotten tomatoes site");
//		HttpClient httpclient = new DefaultHttpClient();
//		HttpResponse response;
//		String responseString = null;
//		try {
//			// make a HTTP request
//			HttpGet hg = new HttpGet(params[0]);
//
//			response = httpclient.execute(hg);
//
//			StatusLine statusLine = response.getStatusLine();
//
//			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
//				// request successful - read the response and close the
//				// connection
//				ByteArrayOutputStream out = new ByteArrayOutputStream();
//				response.getEntity().writeTo(out);
//				out.close();
//				responseString = out.toString();
//			} else {
//				// request failed - close the connection
//				response.getEntity().getContent().close();
//				throw new IOException(statusLine.getReasonPhrase());
//			}
//		} catch (Exception e) {
//			Log.d("Test", "Couldn't make a successful request!");
//		}
//		return responseString;
//	}
//
//	@Override
//	protected void onProgressUpdate(Integer... progress) {
//		mDialog.show();
//		mDialog.setProgress(progress[0]);
//	}
//
//	@Override
//	protected void onPostExecute(String response) {
//		super.onPostExecute(response);
//
//		if (response != null) {
//
//			try {
//				// convert the String response to a JSON object,
//				// because JSON is the response format Rotten Tomatoes uses
//				JSONObject jsonResponse = new JSONObject(response);
//				// fetch the array of movies in the response
//				JSONArray foundItems = jsonResponse.getJSONArray(TAG_MOVIES);
//
//				for (int i = 0; i < foundItems.length(); i++) {
//					JSONObject c = foundItems.getJSONObject(i);
//
//					// Storing each json item in variable
//					String id = c.getString(TAG_ID);
//					String title = c.getString(TAG_TITLE);
//
//					String synopsis = c.getString(TAG_SYNOPSIS);
//
//					// Posters is agin JSON Object
//					JSONObject posters = c.getJSONObject(TAG_POSTERS);
//					String original = posters.getString(TAG_POSTERS_ORIGINAL);
//
//					Item item = new Item(title, synopsis, original,Integer.parseInt(id));
//					mItemList.add(item);
//				}
//			} catch (JSONException e) {
//				Log.d("Test", "Failed to parse the JSON response!");
//			}
//			// refresh mListView
//			mListView = (ListView) mActivity
//					.findViewById(R.id.ScreenWebListView);
//			ColorListAdapter adapter = new ColorListAdapter(mActivity,
//					mItemList);
//			mListView.setDivider(new ColorDrawable(mActivity.getResources()
//					.getColor(R.color.Crimson)));
//			mListView.setDividerHeight((int) mActivity.getResources()
//					.getDimension(R.dimen.Size2dp));
//			mListView.setAdapter(adapter);
//		} else {
//			TextView errorMsg = (TextView) mActivity
//					.findViewById(R.id.ScreenWebTextView1);
//			errorMsg.setVisibility(View.VISIBLE);
//			errorMsg.setText(mActivity.getResources().getString(
//					R.string.DownLoadWebErrorMsg));
//		}
//		mDialog.dismiss();
//
//	}
//
//	// ////////////////
////	private ArrayList<Item> downloadItemList(String searchString) {
////		// JSON Node names
////		// ////////////////////////////////////////////////////////////////////////////
////		try {
////			// Creating the query elemant from the searchstring
////			String query = new StringBuilder().append(HOST)
////					.append(MOVIE_SEARCH_ENDPOINT).append(QUERY_KEY_PARAM)
////					.append(URLEncoder.encode(searchString, DEFAULT_ENCODING))
////					.append(API_KEY_PARAM).append(mApiKey).toString();
////
////			// contacts JSONArray
////			JSONArray foundItems = null;
////			// Hashmap for ListView
////			ArrayList<HashMap<String, String>> itemList = new ArrayList<HashMap<String, String>>();
////			// Creating JSON Parser instance
////			JSONParser jParser = new JSONParser();
////			// getting JSON string from URL
////			JSONObject json = jParser.getJSONFromUrl(query);
////			try {
////				// Getting Array of Contacts
////
////				foundItems = json.getJSONArray(TAG_MOVIES);
////
////				// looping through All Contacts
////				for (int i = 0; i < foundItems.length(); i++) {
////					JSONObject c = foundItems.getJSONObject(i);
////
////					// Storing each json item in variable
////					String id = c.getString(TAG_ID);
////					String title = c.getString(TAG_TITLE);
////					String year = c.getString(TAG_YEAR);
////					String mpaa_rating = c.getString(TAG_MPAA_RATING);
////					String runtime = c.getString(TAG_RUNTIME);
////
////					// Release Dates is agin JSON Object
////					JSONObject releaseDates = c
////							.getJSONObject(TAG_RELEASE_DATES);
////					String theater = releaseDates
////							.getString(TAG_RELEASE_DATES_THEATER);
////					String dvd = releaseDates.getString(TAG_RELEASE_DATES_DVD);
////
////					// Ratings is agin JSON Object
////					JSONObject ratings = c.getJSONObject(TAG_RATINGS);
////					String critics_score = ratings
////							.getString(TAG_RATINGS_CRITICS_SCORE);
////					String audience_rating = ratings
////							.getString(TAG_RATINGS_AUDIENCE_RATING);
////					String audience_score = ratings
////							.getString(TAG_RATINGS_AUDIENCE_SCORE);
////
////					String synopsis = c.getString(TAG_SYNOPSIS);
////
////					// Posters is agin JSON Object
////					JSONObject posters = c.getJSONObject(TAG_POSTERS);
////					String thumbnail = posters.getString(TAG_POSTERS_THUMBNAIL);
////					String profile = posters.getString(TAG_POSTERS_PROFILE);
////					String detailed = posters.getString(TAG_POSTERS_DETAILED);
////					String original = posters.getString(TAG_POSTERS_ORIGINAL);
////
////					// Alternate IDs is agin JSON Object
////					JSONObject alternate_ids = c
////							.getJSONObject(TAG_ALTERNATE_IDS);
////					String imdb = alternate_ids
////							.getString(TAG_ALTERNATE_IDS_IMDB);
////
////					// Alternate IDs is agin JSON Object
////					JSONObject links = c.getJSONObject(TAG_LINKS);
////					String self = links.getString(TAG_LINKS_SELF);
////					String alternate = links.getString(TAG_LINKS_ALTERNATE);
////					String cast = links.getString(TAG_LINKS_CAST);
////					String clips = links.getString(TAG_LINKS_CLIPS);
////					String reviews = links.getString(TAG_LINKS_REVIEWS);
////					String similar = links.getString(TAG_LINKS_SIMILAR);
////
////					// creating new HashMap
////					HashMap<String, String> map = new HashMap<String, String>();
////
////					// adding each child node to HashMap key => value
////					map.put(TAG_ID, id);
////					map.put(TAG_TITLE, title);
////					map.put(TAG_YEAR, year);
////					map.put(TAG_MPAA_RATING, mpaa_rating);
////					map.put(TAG_RUNTIME, runtime);
////					map.put(TAG_RELEASE_DATES_THEATER, theater);
////					map.put(TAG_RELEASE_DATES_DVD, dvd);
////					map.put(TAG_RATINGS_CRITICS_SCORE, critics_score);
////					map.put(TAG_RATINGS_AUDIENCE_RATING, audience_rating);
////					map.put(TAG_RATINGS_AUDIENCE_SCORE, audience_score);
////					map.put(TAG_SYNOPSIS, synopsis);
////					map.put(TAG_POSTERS_THUMBNAIL, thumbnail);
////					map.put(TAG_POSTERS_PROFILE, profile);
////					map.put(TAG_POSTERS_DETAILED, detailed);
////					map.put(TAG_POSTERS_ORIGINAL, original);
////					map.put(TAG_ALTERNATE_IDS_IMDB, imdb);
////					map.put(TAG_LINKS_SELF, self);
////					map.put(TAG_LINKS_ALTERNATE, alternate);
////					map.put(TAG_LINKS_CAST, cast);
////					map.put(TAG_LINKS_CLIPS, clips);
////					map.put(TAG_LINKS_REVIEWS, reviews);
////					map.put(TAG_LINKS_SIMILAR, similar);
////
////					// adding HashList to ArrayList
////					itemList.add(map);
////				}
////			} catch (JSONException e) {
////				e.printStackTrace();
////			}
////		} catch (Exception e) {
////			e.printStackTrace();
////		} finally {
////
////		}
////
////		return null;
////	}
//
//}
