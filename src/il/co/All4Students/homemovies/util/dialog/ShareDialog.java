package il.co.All4Students.homemovies.util.dialog;

import il.co.All4Students.homemovies.R;
import il.co.All4Students.homemovies.app.ApplicationPreference;
import il.co.All4Students.homemovies.core.Item;
import il.co.All4Students.homemovies.util.email.EmailUtil;
import il.co.All4Students.homemovies.util.json.JSONHandler;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * ShareDialog The class handles the alert dialog foe the diffrent Share options
 * that the App implements
 * 
 * Curentlly: - Email
 * 
 * in Prograsse: - FaceBook - Tweeter
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
public class ShareDialog {
	// Attributes
	private ApplicationPreference mSettings;
	private Context mContext;
	private Item mItem;

	// Constractors
	public ShareDialog(Item item, Context context) {
		super();
		this.mItem = item;
		this.mContext = context;
	}

	// Additional Methods
	public void showAlertDialog() {
		LayoutInflater li = LayoutInflater.from(mContext);

		View ShareDialogView = li.inflate(R.layout.custom_dialog_share, null);

		AlertDialog.Builder shareDialog = new AlertDialog.Builder(mContext);
		shareDialog.setView(ShareDialogView);
		shareDialog.setTitle(mContext.getResources().getString(
				R.string.ShareDialogTitle));
		shareDialog.setIcon(mContext.getResources().getDrawable(
				R.drawable.ic_dialog_share));
		shareDialog.create();
		// Showing Alert Message
		final AlertDialog SDialog = shareDialog.show();

		View btnEmail = ShareDialogView
				.findViewById(R.id.customDialogShareButtonAirMail);
		btnEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSettings = new ApplicationPreference(mContext);

				String emailToAddress = "";
				String emailCCAddress = mSettings.getEmail().toString();
				String emailSubject = mItem.getSubject();
				String emailText = mItem.getBody() + "\n\n\n"
						+ mItem.getUrlWeb();
				ArrayList<String> emailFilePaths = JSONHandler
						.getURIFromJSON(mItem.getUrlLocal());

				EmailUtil.sendEmail(mContext, emailToAddress, emailCCAddress,
						emailSubject, emailText, emailFilePaths);
				SDialog.dismiss();
			}
		});

		View btnFaceBook = ShareDialogView
				.findViewById(R.id.customDialogShareButtonFaceBook);
		btnFaceBook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "FaceBook is unavlible at this time",
						Toast.LENGTH_SHORT).show();
				SDialog.dismiss();
			}
		});

		View btnTweeter = ShareDialogView
				.findViewById(R.id.customDialogShareButtonTweeter);
		btnTweeter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "Tweeter is unavlible at this time",
						Toast.LENGTH_SHORT).show();
				SDialog.dismiss();
			}
		});

	}
}
