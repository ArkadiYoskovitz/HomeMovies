package il.co.All4Students.homemovies;

import static il.co.All4Students.homemovies.app.AppConstants.INTENT_TARGET;
import static il.co.All4Students.homemovies.app.AppConstants.INTENT_TARGET_URI;
import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_SCREEN_GRIDDETAILS;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_CANCEL;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_COMMIT;
import static il.co.All4Students.homemovies.app.AppConstants.RESULT_CODE_DELETE;
import il.co.All4Students.homemovies.core.Item;
import il.co.All4Students.homemovies.util.log.util.AppLog;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ScreenGridDetail extends Activity {

	// Attributes
	private Item mEditedItem;
	private String filePath;

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// System Events
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_detail);
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			if (mEditedItem == null) {
				mEditedItem = getIntent().getExtras().getParcelable(
						INTENT_TARGET);
			}
			filePath = getIntent().getExtras().getString(INTENT_TARGET_URI);

			ImageView imageView = (ImageView) findViewById(R.id.gridDetailImageView1);
			imageView.setImageURI(Uri.fromFile(new File(filePath)));

			OkCancelBar okCancelBar = (OkCancelBar) findViewById(R.layout.okcancelbar);

			Button btnSave = (Button) okCancelBar
					.findViewById(R.id.okcancelbar_ok);
			btnSave.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent returnIntent = new Intent();
					returnIntent.putExtra(INTENT_TARGET, mEditedItem);
					setResult(RESULT_CODE_COMMIT, returnIntent);
					finish();
				}
			});

			Button btnDelete = (Button) okCancelBar
					.findViewById(R.id.okcancelbar_cancel);
			btnDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent returnIntent = new Intent();
					returnIntent.putExtra(INTENT_TARGET, mEditedItem);
					returnIntent.putExtra(INTENT_TARGET_URI, filePath);
					setResult(RESULT_CODE_DELETE, returnIntent);
					finish();
				}
			});
		} catch (Exception e) {
			AppLog.log(ScreenGridDetail.this, LOG_TAG_SCREEN_GRIDDETAILS,
					e.getMessage());
		}
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
