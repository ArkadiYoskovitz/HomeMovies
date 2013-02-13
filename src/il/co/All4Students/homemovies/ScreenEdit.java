package il.co.All4Students.homemovies;

import static il.co.All4Students.homemovies.app.AppConstants.INTENT_TARGET;
import static il.co.All4Students.homemovies.app.AppConstants.Item_Take_Picture;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_SCREEN_EDIT;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_SCREEN_WEB;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_TextToSpeech;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_WEB_SITE;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_CANCEL;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_COMMIT;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_DELETE;
import il.co.All4Students.homemovies.app.ApplicationPreference;
import il.co.All4Students.homemovies.core.Item;
import il.co.All4Students.homemovies.util.db.ItemsHandler;
import il.co.All4Students.homemovies.util.dialog.RankDialog;
import il.co.All4Students.homemovies.util.dialog.ShareDialog;
import il.co.All4Students.homemovies.util.image.ExternalStorageLoader;
import il.co.All4Students.homemovies.util.json.JSONHandler;
import il.co.All4Students.homemovies.util.log.util.AppLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Edit screen
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
public class ScreenEdit extends Activity implements TextToSpeech.OnInitListener {

	// Attributes
	private Item mEditedItem;
	private DownloadImageTask mDownloadTask;
	private ApplicationPreference mSettings;
	private TextToSpeech mTextToSpeech;
	private boolean mIsLanguageSupported = true;

	// System Events
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_edit);
		mTextToSpeech = new TextToSpeech(this, this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			if (mEditedItem == null) {
				mEditedItem = getIntent().getExtras().getParcelable(
						INTENT_TARGET);
			}
			EditText text1 = (EditText) findViewById(R.id.ScreenEditEditText1);
			EditText text2 = (EditText) findViewById(R.id.ScreenEditEditText2);
			EditText text3 = (EditText) findViewById(R.id.ScreenEditEditText3);
			CheckBox checkBox = (CheckBox) findViewById(R.id.ScreenEditCheckBox);
			final ImageView screenEditImage = (ImageView) findViewById(R.id.ScreenEditImageView1);

			mSettings = new ApplicationPreference(ScreenEdit.this);

			if (mEditedItem.getSubject().length() == 0) {
				text1.setHint(mSettings.getSubject());
			} else {
				text1.setText(mEditedItem.getSubject());
			}

			if (mEditedItem.getUrlWeb().length() == 0) {
				text3.setText(mEditedItem.getUrlLocal());
			} else {
				text3.setText(mEditedItem.getUrlWeb());
			}

			text2.setText(mEditedItem.getBody());

			checkBox.setChecked(mEditedItem.getViewd());

			registerForContextMenu(screenEditImage);
		} catch (Exception e) {
			AppLog.log(ScreenEdit.this, LOG_TAG_SCREEN_EDIT, e.getMessage());
		}
	}

	@Override
	public void onBackPressed() {
		itemRefreshFromScreen();
		AppLog.log(ScreenEdit.this, LOG_TAG_SCREEN_EDIT,
				"Edit Screen - Cancel button was pressed");

		Intent returnIntent = new Intent();
		returnIntent.putExtra(INTENT_TARGET, mEditedItem);
		setResult(RESULT_CODE_CANCEL, returnIntent);
		finish();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		itemRefreshFromScreen();
		outState.putParcelable("mItem", mEditedItem);

		ImageView imageView = (ImageView) findViewById(R.id.ScreenEditImageView1);
		BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
		Bitmap bitmap = drawable.getBitmap();
		outState.putParcelable("mImage", bitmap);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mEditedItem = savedInstanceState.getParcelable("mItem");
		Bitmap bitmap = (Bitmap) savedInstanceState.getParcelable("mImage");

		ImageView editImage = (ImageView) findViewById(R.id.ScreenEditImageView1);
		if (bitmap != null) {
			editImage.setImageBitmap(bitmap);
		}
	}

	@Override
	protected void onDestroy() {
		if (mTextToSpeech != null) {
			mTextToSpeech.stop();
			mTextToSpeech.shutdown();
		}
		super.onDestroy();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Activity sending info Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Item_Take_Picture) {
			try {
				Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
				ImageView imgTakenPhoto = (ImageView) findViewById(R.id.ScreenEditImageView1);
				imgTakenPhoto.setImageBitmap(thumbnail);
			} catch (Exception e) {
				if (data == null) {
					AppLog.log(ScreenEdit.this, LOG_TAG_SCREEN_EDIT,
							"Failed to retrive image, intent is empty");
				} else {
					AppLog.log(ScreenEdit.this, LOG_TAG_SCREEN_EDIT,
							"Failed to retrive image");
				}
			}
		}
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
		getMenuInflater().inflate(R.menu.screen_edit_menu_option, menu);
		closeKeybord();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.screenEditOptionMenuShare:
			ShareDialog ShareDialog = new ShareDialog(mEditedItem,
					ScreenEdit.this);
			ShareDialog.showShareDialog();
			break;
		case R.id.screenEditOptionMenuSpeach:
			speakOut();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * onCreateContextMenu
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.screen_edit_menu_context, menu);
		closeKeybord();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.screenEditContextMenuSaveToCard:
			try {
				ImageView screenEditImage = (ImageView) findViewById(R.id.ScreenEditImageView1);
				BitmapDrawable drawable = (BitmapDrawable) screenEditImage
						.getDrawable();
				Bitmap bitmap = drawable.getBitmap();

				ExternalStorageLoader externalStogareLoader = new ExternalStorageLoader(
						ScreenEdit.this);

				mEditedItem.setUrlLocal(JSONHandler.insertURIintoJSON(
						mEditedItem.getUrlLocal(),
						externalStogareLoader.saveIamge(bitmap)));

				ItemsHandler itemsHandler = new ItemsHandler(ScreenEdit.this);
				itemsHandler.updateItem(mEditedItem);
				Toast.makeText(this, "Image saved to card", Toast.LENGTH_SHORT)
						.show();
			} catch (Exception e) {
				Toast.makeText(this, "Canot save to card at this time",
						Toast.LENGTH_SHORT).show();
				AppLog.log(ScreenEdit.this, "onContextItemSelected", e
						.getStackTrace().toString());
			}
			break;

		case R.id.screenEditContextMenuRank:
			RankDialog RankDialog = new RankDialog(mEditedItem, ScreenEdit.this);
			RankDialog.showRankDialog();
			break;
		case R.id.screenEditContextMenuViewed:
			CheckBox checkBox = (CheckBox) findViewById(R.id.ScreenEditCheckBox);
			checkBox.setChecked(!checkBox.isChecked());
			mEditedItem.setViewd(!mEditedItem.getViewd());
			break;
		case R.id.screenEditContextMenuShare:
			ShareDialog ShareDialog = new ShareDialog(mEditedItem,
					ScreenEdit.this);
			ShareDialog.showShareDialog();
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// OnClick Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void onClickEditCamera(View view) {
		closeKeybord();

		Intent returnIntent = new Intent();
		returnIntent.putExtra(INTENT_TARGET, mEditedItem);
		try {
			Intent cameraIntent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cameraIntent, Item_Take_Picture);

		} catch (ActivityNotFoundException NOFOUND) {
			Toast.makeText(this,
					"No Canera found, device doesn't support this feature",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void onClickEditGallery(View view) {
		closeKeybord();

		Intent intent = new Intent(ScreenEdit.this, ScreenGrid.class);
		intent.putExtra(INTENT_TARGET, mEditedItem);
		startActivity(intent);
	}

	public void onClickEditShow(View view) {
		closeKeybord();

		EditText txtSearch = (EditText) findViewById(R.id.ScreenEditEditText3);
		if (txtSearch.getText() != null) {
			try {
				if (mDownloadTask != null) {
					if (mDownloadTask.getStatus() != AsyncTask.Status.FINISHED) {
						AppLog.log(ScreenEdit.this, LOG_TAG_SCREEN_WEB,
								"onClickWebGo - no need to start a new task");

						return;
					}
				}
				String searchString = txtSearch.getText().toString();
				mDownloadTask = new DownloadImageTask(ScreenEdit.this);
				mDownloadTask.execute(searchString);
			} catch (Exception e) {
				AppLog.log(ScreenEdit.this, LOG_TAG_WEB_SITE, "Exception: "
						+ e.getMessage().toString());
			}
		}
	}

	public void onClickEditCancel(View view) {
		closeKeybord();
		itemRefreshFromScreen();
		AppLog.log(ScreenEdit.this, LOG_TAG_SCREEN_EDIT,
				"Edit Screen - Cancel button was pressed");

		Intent returnIntent = new Intent();
		returnIntent.putExtra(INTENT_TARGET, mEditedItem);
		setResult(RESULT_CODE_CANCEL, returnIntent);
		finish();
	}

	public void onClickEditDelete(View view) {
		closeKeybord();
		itemRefreshFromScreen();
		AppLog.log(ScreenEdit.this, LOG_TAG_SCREEN_EDIT,
				"Edit Screen - Delete button was pressed");

		Intent returnIntent = new Intent();
		returnIntent.putExtra(INTENT_TARGET, mEditedItem);
		setResult(RESULT_CODE_DELETE, returnIntent);
		finish();
	}

	public void onClickEditCommit(View view) {
		closeKeybord();
		EditText subjectText = (EditText) findViewById(R.id.ScreenEditEditText1);
		EditText bodyText = (EditText) findViewById(R.id.ScreenEditEditText2);
		EditText URLText = (EditText) findViewById(R.id.ScreenEditEditText3);
		CheckBox checkBox = (CheckBox) findViewById(R.id.ScreenEditCheckBox);

		if (subjectText.getText().toString().length() == 0) {
			mEditedItem.setSubject(subjectText.getHint().toString());
			mEditedItem.setBody(bodyText.getText().toString());
			mEditedItem.setUrlWeb(URLText.getText().toString());
			mEditedItem.setViewd(checkBox.isChecked());
		} else {
			itemRefreshFromScreen();
		}
		AppLog.log(ScreenEdit.this, LOG_TAG_SCREEN_EDIT,
				"Edit Screen - Commit button was pressed");

		Intent returnIntent = new Intent();
		returnIntent.putExtra(INTENT_TARGET, mEditedItem);
		setResult(RESULT_CODE_COMMIT, returnIntent);
		finish();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Additional Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void onInit(int status) {
		try {
			if (status == TextToSpeech.SUCCESS) {
				// int result = mTextToSpeech
				// .setLanguage(new Locale(getBaseContext().getResources()
				// .getConfiguration().locale.getLanguage()));

				int result = mTextToSpeech.setLanguage(Locale.US);

				if (result == TextToSpeech.LANG_MISSING_DATA
						|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
					Log.e(LOG_TAG_TextToSpeech, "Language is not supported");
					mIsLanguageSupported = false;
				}
			}
		} catch (Exception e) {
			AppLog.log(ScreenEdit.this, LOG_TAG_TextToSpeech,
					"Initilization Failed");
			AppLog.log(ScreenEdit.this, LOG_TAG_TextToSpeech, e.getMessage()
					.toString());
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Additional Methods
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void itemRefreshFromScreen() {
		EditText text1 = (EditText) findViewById(R.id.ScreenEditEditText1);
		EditText text2 = (EditText) findViewById(R.id.ScreenEditEditText2);
		EditText text3 = (EditText) findViewById(R.id.ScreenEditEditText3);
		CheckBox checkBox = (CheckBox) findViewById(R.id.ScreenEditCheckBox);

		mEditedItem.setSubject(text1.getText().toString());
		mEditedItem.setBody(text2.getText().toString());
		mEditedItem.setUrlWeb(text3.getText().toString());
		mEditedItem.setViewd(checkBox.isChecked());
	}

	private void speakOut() {
		if (mIsLanguageSupported) {
			String subject = mEditedItem.getSubject();
			String body = mEditedItem.getBody();
			mTextToSpeech.speak(subject + "\n" + body,
					TextToSpeech.QUEUE_FLUSH, null);
		} else {
			Toast.makeText(ScreenEdit.this,
					getResources().getString(R.string.ErrorMsgTextToSpeech),
					Toast.LENGTH_LONG).show();
		}
	}

	private void closeKeybord() {
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(
				getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Inner Classes
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * DownloadImageTask
	 * 
	 * @author Arkadi Yoskovitz
	 * @date 2013-02-08
	 */
	private class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {

		private Activity mActivity;
		private ProgressDialog mDialog;

		DownloadImageTask(Activity activity) {
			mActivity = activity;
			mDialog = new ProgressDialog(mActivity);
		}

		@Override
		protected Bitmap doInBackground(String... urls) {
			AppLog.log(ScreenEdit.this, "doInBackground",
					"starting download of image");
			Bitmap bitmap = null;

			if (isOnline()) {
				try {
					URL url = new URL(urls[0]);
					HttpURLConnection httpCon = (HttpURLConnection) url
							.openConnection();
					try {
						InputStream is = httpCon.getInputStream();
						int fileLength = httpCon.getContentLength();
						ByteArrayOutputStream buffer = new ByteArrayOutputStream();
						int nRead, total = 0;
						byte[] data = new byte[2048];
						// Read the image bytes in chunks of 2048 bytes
						while ((nRead = is.read(data, 0, data.length)) != -1) {
							buffer.write(data, 0, nRead);
							total += nRead;
							publishProgress(""
									+ (int) ((total * 100) / fileLength));
						}
						buffer.flush();
						byte[] imageArray = buffer.toByteArray();
						bitmap = BitmapFactory.decodeByteArray(imageArray, 0,
								imageArray.length);
						// save to sdcard
					} catch (IOException e) {
						AppLog.log(mActivity, LOG_TAG_SCREEN_EDIT, e
								.getStackTrace().toString());
					} finally {
						httpCon.disconnect();
					}
				} catch (Exception e) {
					AppLog.log(mActivity, LOG_TAG_SCREEN_EDIT, e
							.getStackTrace().toString());
				}
			} else {
				if (mEditedItem.getUrlLocal().length() != 0) {
					try {
						ArrayList<String> tmpArrayList = JSONHandler
								.getURIFromJSON(mEditedItem.getUrlLocal());

						bitmap = BitmapFactory.decodeFile(tmpArrayList.get(0));
					} catch (Exception e) {
						AppLog.log(ScreenEdit.this, LOG_TAG_SCREEN_EDIT, e
								.getStackTrace().toString());
					}
				}
			}
			return bitmap;
		}

		@Override
		protected void onPreExecute() {
			mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDialog.setCancelable(true);
			mDialog.setMessage("Loading...");
			mDialog.setMax(100);
			mDialog.setProgress(0);
			mDialog.show();
			TextView errorMsg = (TextView) mActivity
					.findViewById(R.id.ScreenEditTextViewErrorMsg);
			errorMsg.setVisibility(View.GONE);
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			mDialog.show();
			mDialog.setProgress(progress[0]);
		}

		private void publishProgress(String... progress) {
			mDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(Bitmap resultImage) {
			if (resultImage != null) {
				ImageView imageView = (ImageView) mActivity
						.findViewById(R.id.ScreenEditImageView1);
				imageView.setImageBitmap(resultImage);
			} else {
				TextView errorMsg = (TextView) mActivity
						.findViewById(R.id.ScreenEditTextViewErrorMsg);
				errorMsg.setVisibility(View.VISIBLE);
				errorMsg.setText("Problem downloading image. Try again later");
			}
			// Close the progress dialog
			mDialog.dismiss();
		}

		// Additional Methods
		public boolean isOnline() {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnectedOrConnecting()) {
				return true;
			}
			return false;
		}
	}

}
