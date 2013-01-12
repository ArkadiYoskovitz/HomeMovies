package il.co.All4Students.homemovies;

import static il.co.All4Students.homemovies.app.AppConstants.INTENT_TARGET;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_EDIT;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_MAIN;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_TextToSpeech;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_WEB;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_WEB_SITE;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_CANCEL;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_COMMIT;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_DELETE;
import il.co.All4Students.homemovies.app.ApplicationPreference;
import il.co.All4Students.homemovies.core.Item;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Edit screen
 * 
 * @author Arkadi Yoskovitz
 * 
 */
public class ScreenEdit extends Activity implements TextToSpeech.OnInitListener {

	// Attributes
	private Item mEditedItem;
	private DownloadImageTask downloadTask;
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
			mEditedItem = getIntent().getExtras().getParcelable(INTENT_TARGET);
			EditText text1 = (EditText) findViewById(R.id.ScreenEditEditText1);
			EditText text2 = (EditText) findViewById(R.id.ScreenEditEditText2);
			EditText text3 = (EditText) findViewById(R.id.ScreenEditEditText3);
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
			final ImageView screenEditImage = (ImageView) findViewById(R.id.ScreenEditImageView1);
			registerForContextMenu(screenEditImage);
		} catch (Exception e) {
			Log.d(LOG_TAG_EDIT, "Unable to load empty Item object");
		}
	}

	@Override
	public void onBackPressed() {
		itemRefreshFromScreen();

		Log.d(LOG_TAG_EDIT, "Edit Screen - Cancel button was pressed");

		Intent returnIntent = new Intent();
		returnIntent.putExtra(INTENT_TARGET, mEditedItem);
		setResult(RESULT_CODE_CANCEL, returnIntent);
		finish();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		EditText text1 = (EditText) findViewById(R.id.ScreenEditEditText1);
		EditText text2 = (EditText) findViewById(R.id.ScreenEditEditText2);
		EditText text3 = (EditText) findViewById(R.id.ScreenEditEditText3);

		outState.putString("subject", text1.getText().toString());
		outState.putString("body", text2.getText().toString());
		outState.putString("url", text3.getText().toString());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		EditText text1 = (EditText) findViewById(R.id.ScreenEditEditText1);
		EditText text2 = (EditText) findViewById(R.id.ScreenEditEditText2);
		EditText text3 = (EditText) findViewById(R.id.ScreenEditEditText3);

		String subject = savedInstanceState.getString("subject");
		String body = savedInstanceState.getString("body");
		String url = savedInstanceState.getString("url");

		text1.setText(subject);
		text2.setText(body);
		text3.setText(url);
	}

	@Override
	protected void onDestroy() {
		// Don't forget to shutdown!
		if (mTextToSpeech != null) {
			mTextToSpeech.stop();
			mTextToSpeech.shutdown();
		}
		super.onDestroy();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Menu Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * onCreateOptionsMenu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.screen_edit_menu_option, menu);
		Log.d(LOG_TAG_MAIN,
				"Activity Edit Option Menu Layout was Created and loaded");
		return true;
	}

	/**
	 * Handling the different available operation
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.screenEditOptionMenuShare:
			ShareDialog ShareDialog = new ShareDialog();
			ShareDialog.showAlertDialog();
			break;

		case R.id.screenEditOptionMenuSpeach:
			speakOut();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.screen_edit_menu_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.screenEditContextMenuSaveToCard:
			// do something
			break;
		case R.id.screenEditContextMenuRank:
			// do something
			break;
		// ////////////////////
		case R.id.screenEditContextMenuShare:
			ShareDialog ShareDialog = new ShareDialog();
			ShareDialog.showAlertDialog();
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// OnClick Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void onClickEditSelectColor(View view) {
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(
				getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

		final Dialog SCDialog = new Dialog(ScreenEdit.this);
		SCDialog.setContentView(R.layout.custom_dialog_colors);
		SCDialog.setTitle(R.string.SCDialogTitle);
		SCDialog.setCancelable(false);
		SCDialog.show();

		Button btnSCDialogColorRed = (Button) SCDialog
				.findViewById(R.id.CustomDialogColorsButton1);
		btnSCDialogColorRed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEditedItem.setColor(getResources().getColor(R.color.Red));
				SCDialog.dismiss();
			}
		});

		Button btnSCDialogColorGreen = (Button) SCDialog
				.findViewById(R.id.CustomDialogColorsButton2);
		btnSCDialogColorGreen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEditedItem.setColor(getResources().getColor(R.color.Green));
				SCDialog.dismiss();
			}
		});

		Button btnSCDialogColorYellow = (Button) SCDialog
				.findViewById(R.id.CustomDialogColorsButton3);
		btnSCDialogColorYellow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEditedItem.setColor(getResources().getColor(R.color.Yellow));
				SCDialog.dismiss();
			}
		});

		Button btnSCDialogColorBlue = (Button) SCDialog
				.findViewById(R.id.CustomDialogColorsButton4);
		btnSCDialogColorBlue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEditedItem.setColor(getResources().getColor(R.color.Blue));
				SCDialog.dismiss();
			}
		});

		Button btnSCDialogColorDefault = (Button) SCDialog
				.findViewById(R.id.CustomDialogColorsButton5);
		btnSCDialogColorDefault.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEditedItem.setColor(ApplicationPreference.getDefaultColor());
				SCDialog.dismiss();
			}
		});
	}

	public void onClickEditShow(View view) {
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(
				getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

		EditText txtSearch = (EditText) findViewById(R.id.ScreenEditEditText3);
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
				DownloadImageTask downloadTask = new DownloadImageTask(
						ScreenEdit.this);
				downloadTask.execute(searchString);
			} catch (Exception e) {
				Log.e(LOG_TAG_WEB_SITE, "Exception: " + e.getMessage());
			}
		}
	}

	public void onClickEditCancel(View view) {
		itemRefreshFromScreen();

		Log.d(LOG_TAG_EDIT, "Edit Screen - Cancel button was pressed");

		Intent returnIntent = new Intent();
		returnIntent.putExtra(INTENT_TARGET, mEditedItem);
		setResult(RESULT_CODE_CANCEL, returnIntent);
		finish();
	}

	public void onClickEditDelete(View view) {
		itemRefreshFromScreen();

		Log.d(LOG_TAG_EDIT, "Edit Screen - Delete button was pressed");

		Intent returnIntent = new Intent();
		returnIntent.putExtra(INTENT_TARGET, mEditedItem);
		setResult(RESULT_CODE_DELETE, returnIntent);
		finish();
	}

	public void onClickEditCommit(View view) {
		EditText subjectText = (EditText) findViewById(R.id.ScreenEditEditText1);
		EditText bodyText = (EditText) findViewById(R.id.ScreenEditEditText2);
		EditText URLText = (EditText) findViewById(R.id.ScreenEditEditText3);

		if (subjectText.getText().toString().length() == 0) {
			mEditedItem.setSubject(subjectText.getHint().toString());
			mEditedItem.setBody(bodyText.getText().toString());
			mEditedItem.setUrlWeb(URLText.getText().toString());
		} else {
			itemRefreshFromScreen();
		}
		Log.d(LOG_TAG_EDIT, "Edit Screen - Commit button was pressed");

		Intent returnIntent = new Intent();
		returnIntent.putExtra(INTENT_TARGET, mEditedItem);
		setResult(RESULT_CODE_COMMIT, returnIntent);
		finish();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Additional Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			int result = mTextToSpeech.setLanguage(Locale.US);
			// mTextToSpeech.setPitch(5); // set pitch level
			// mTextToSpeech.setSpeechRate(2); // set speech speed rate
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e(LOG_TAG_TextToSpeech, "Language is not supported");
				mIsLanguageSupported = false;
			}
		} else {
			Log.e(LOG_TAG_TextToSpeech, "Initilization Failed");
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Additional Methods
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void itemRefreshFromScreen() {
		EditText text1 = (EditText) findViewById(R.id.ScreenEditEditText1);
		EditText text2 = (EditText) findViewById(R.id.ScreenEditEditText2);
		EditText text3 = (EditText) findViewById(R.id.ScreenEditEditText3);
		mEditedItem.setSubject(text1.getText().toString());
		mEditedItem.setBody(text2.getText().toString());
		mEditedItem.setUrlWeb(text3.getText().toString());
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

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Inner Classes
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {

		private Activity mActivity;
		private ProgressDialog mDialog;

		DownloadImageTask(Activity activity) {
			mActivity = activity;
			mDialog = new ProgressDialog(mActivity);
		}

		protected Bitmap doInBackground(String... urls) {
			Log.d("doInBackground", "starting download of image");
			Bitmap bitmap = null;
			try {
				URL url = new URL(urls[0]);
				HttpURLConnection httpCon = (HttpURLConnection) url
						.openConnection();
				try {
					InputStream is = httpCon.getInputStream();
					int fileLength = httpCon.getContentLength();
					ByteArrayOutputStream buffer = new ByteArrayOutputStream();
					int nRead, totalBytesRead = 0;
					byte[] data = new byte[2048];
					mDialog.setMax(fileLength);
					// Read the image bytes in chunks of 2048 bytes
					while ((nRead = is.read(data, 0, data.length)) != -1) {
						buffer.write(data, 0, nRead);
						totalBytesRead += nRead;
						publishProgress(totalBytesRead);
					}
					buffer.flush();
					byte[] imageArray = buffer.toByteArray();
					bitmap = BitmapFactory.decodeByteArray(imageArray, 0,
							imageArray.length);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					httpCon.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		protected void onPreExecute() {
			mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDialog.setCancelable(true);
			mDialog.setMessage("Loading...");
			mDialog.setMax(9999999);
			mDialog.setProgress(0);
			mDialog.show();
			TextView errorMsg = (TextView) mActivity
					.findViewById(R.id.ScreenEditTextViewErrorMsg);
			errorMsg.setVisibility(View.GONE);
		}

		protected void onProgressUpdate(Integer... progress) {
			mDialog.show();
			mDialog.setProgress(progress[0]);
		}

		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				ImageView imageView = (ImageView) mActivity
						.findViewById(R.id.ScreenEditImageView1);
				imageView.setImageBitmap(result);
			} else {
				TextView errorMsg = (TextView) mActivity
						.findViewById(R.id.ScreenEditTextViewErrorMsg);
				errorMsg.setVisibility(View.VISIBLE);
				errorMsg.setText("Problem downloading image. Try again later");
			}
			// Close the progress dialog
			mDialog.dismiss();
		}
	}

	/**
	 * ShareDialog The class handles the alert dialog foe the diffrent Share
	 * options that the App implements
	 * 
	 * Curentlly: - Email
	 * 
	 * in Prograsse: - FaceBook - Tweeter
	 * 
	 * @author Arkadi Yoskovitz
	 */
	private class ShareDialog {
		public void showAlertDialog() {
			LayoutInflater li = LayoutInflater.from(ScreenEdit.this);

			View ShareDialogView = li.inflate(R.layout.custom_dialog_share,
					null);

			AlertDialog.Builder shareDialog = new AlertDialog.Builder(
					ScreenEdit.this);
			shareDialog.setView(ShareDialogView);
			shareDialog.setTitle(ScreenEdit.this.getResources().getString(
					R.string.ShareDialogTitle));
			shareDialog.setIcon(ScreenEdit.this.getResources().getDrawable(
					R.drawable.ic_dialog_share));
			shareDialog.create();
			// Showing Alert Message
			final AlertDialog SDialog = shareDialog.show();

			View btnEmail = ShareDialogView
					.findViewById(R.id.customDialogShareButtonAirMail);
			btnEmail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ScreenEdit.this.itemRefreshFromScreen();
					EditText subjectText = (EditText) findViewById(R.id.ScreenEditEditText1);

					if (subjectText.getText().toString().length() == 0) {
						mEditedItem
								.setSubject(subjectText.getHint().toString());
					} else {
						ScreenEdit.this.itemRefreshFromScreen();
					}
					mSettings = new ApplicationPreference(ScreenEdit.this);

					String emailAddress = mSettings.getEmail().toString();

					String uriText = "mailto:" + emailAddress + "?subject="
							+ Uri.encode(mEditedItem.getSubject()) + "&body="
							+ Uri.encode(mEditedItem.getBody()) + "\n\n\n"
							+ Uri.encode(mEditedItem.getUrlWeb());

					Uri uri = Uri.parse(uriText);

					Intent intent = new Intent(Intent.ACTION_SENDTO);
					intent.setData(uri);
					startActivity(Intent.createChooser(intent, "Send email"));
					SDialog.dismiss();
				}
			});

			View btnFaceBook = ShareDialogView
					.findViewById(R.id.customDialogShareButtonFaceBook);
			btnFaceBook.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(ScreenEdit.this,
							"FaceBook is unavlible at this time",
							Toast.LENGTH_SHORT).show();
					SDialog.dismiss();
				}
			});

			View btnTweeter = ShareDialogView
					.findViewById(R.id.customDialogShareButtonTweeter);
			btnTweeter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(ScreenEdit.this,
							"Tweeter is unavlible at this time",
							Toast.LENGTH_SHORT).show();
					SDialog.dismiss();
				}
			});

		}
	}
}
