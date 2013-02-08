package il.co.All4Students.homemovies.util.email;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * A genaric class, handels sending emails to recipeants
 * 
 * @author Arkadi Yoskovitz
 * 
 */
public class EmailUtil {
	/**
	 * The basic send email implementation, corently has a few constraints
	 * 
	 * @param context
	 *            - the context from which we called this method this is needed
	 *            inorder to integrate with the buildin sending via Intent
	 *            option of the OS
	 * @param emailTo
	 *            - our email resipeant, currantly a single resipeant
	 * @param emailCC
	 *            - our email CC resipeant, currantly a single resipeant
	 * @param emailSubject
	 *            - the mail Title
	 * @param emailText
	 *            - the mail content
	 * @param filePaths
	 *            - used inorder to send an attachment, can recive more the one,
	 */
	public static void sendEmail(Context context, String emailTo,
			String emailCC, String emailSubject, String emailText,
			List<String> filePaths) {
		final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		emailIntent.setType("text/plain");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { emailTo });
		emailIntent.putExtra(Intent.EXTRA_CC, new String[] { emailCC });
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
		emailIntent.putExtra(Intent.EXTRA_TEXT, emailText);

		if (filePaths != null) {
			// has to be an ArrayList
			ArrayList<Uri> uris = new ArrayList<Uri>();
			// convert from paths to Android friendly Parcelable Uri's
			for (String file : filePaths) {
				File fileIn = new File(file);
				Uri u = Uri.fromFile(fileIn);
				uris.add(u);
			}
			emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
		}
		context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	}
}
