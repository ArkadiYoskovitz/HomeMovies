package il.co.All4Students.homemovies;

import static il.co.All4Students.homemovies.app.AppConstants.INTENT_TARGET;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_EDIT;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_WEB;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_WEB_SITE;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_CANCEL;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_COMMIT;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_DELETE;
import il.co.All4Students.homemovies.app.ApplicationPreference;
import il.co.All4Students.homemovies.core.Item;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Edit screen
 * 
 * @author Arkadi Yoskovitz
 * 
 */
public class ScreenEdit extends Activity {

	// Attributes
	private Item mEditedItem;
	private DownloadImageTask downloadTask;
	private ApplicationPreference mSettings;

	// System Events
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_edit);
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

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

	// OnClick Events
	public void onClickEditSelectColor(View view) {
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
				// String query = new StringBuilder()
				// .append(HOST)
				// .append(MOVIE_SEARCH_ENDPOINT)
				// .append(QUERY_KEY_PARAM)
				// .append(URLEncoder.encode(searchString,
				// DEFAULT_ENCODING)).append(API_KEY_PARAM)
				// .append(APP_API_KEY).toString();
				//
				// downloadTask.execute(query);
			} catch (Exception e) {
				Log.e(LOG_TAG_WEB_SITE, "Exception: " + e.getMessage());
			}
		}
	}

	public void onClickEditEmail(View view) {
		itemRefreshFromScreen();
		Log.d(LOG_TAG_EDIT, "Edit Screen - Email button was pressed");

		EditText subjectText = (EditText) findViewById(R.id.ScreenEditEditText1);

		if (subjectText.getText().toString().length() == 0) {
			mEditedItem.setSubject(subjectText.getHint().toString());
		} else {
			itemRefreshFromScreen();
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

	// Additional Methods

	private void itemRefreshFromScreen() {
		EditText text1 = (EditText) findViewById(R.id.ScreenEditEditText1);
		EditText text2 = (EditText) findViewById(R.id.ScreenEditEditText2);
		EditText text3 = (EditText) findViewById(R.id.ScreenEditEditText3);
		mEditedItem.setSubject(text1.getText().toString());
		mEditedItem.setBody(text2.getText().toString());
		mEditedItem.setUrlWeb(text3.getText().toString());
	}

	// Inner Classes
	private class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {

		private Activity mActivity;
		private ProgressDialog mDialog;

		DownloadImageTask(Activity activity) {
			mActivity = activity;
			mDialog = new ProgressDialog(mActivity);
		}

		protected Bitmap doInBackground(String... urls) {
			Log.d("doInBackground", "starting download of image");
			Bitmap image = downloadImage(urls[0]);
			return image;
		}

		protected void onPreExecute() {
			ImageView imageView = (ImageView) mActivity
					.findViewById(R.id.ScreenEditImageView1);
			imageView.setImageBitmap(null);
			// Reset the progress bar
			mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDialog.setCancelable(true);
			mDialog.setMessage("Loading...");
			mDialog.setProgress(0);
			mDialog.show();
			TextView errorMsg = (TextView) mActivity
					.findViewById(R.id.ScreenEditTextViewErrorMsg);
			errorMsg.setVisibility(View.INVISIBLE);
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

		private Bitmap downloadImage(String urlString) {
			URL url;
			try {
				url = new URL(urlString);
				HttpURLConnection httpCon = (HttpURLConnection) url
						.openConnection();

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
				byte[] image = buffer.toByteArray();
				Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,
						image.length);
				return bitmap;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}
}
