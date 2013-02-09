package il.co.All4Students.homemovies.util.image;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;

/**
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
public class ExternalStorageLoader {
	// Attributes
	private Context mContext;
	private File mFile;

	public ExternalStorageLoader(Context context) {
		super();
		this.mContext = context;

		// check if external storage is available and not read only
		if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
			Toast.makeText(mContext,
					"No External storage available at this time",
					Toast.LENGTH_LONG).show();
		}
	}

	public String SaveIamge(Bitmap finalBitmap) {
		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/HomeMovieStorage/saved_images");
		myDir.mkdirs();
		Random generator = new Random();
		int n = 10000;
		n = generator.nextInt(n);
		String fname = "Image-" + n + ".png";
		File mFile = new File(myDir, fname);
		if (mFile.exists()) {
			mFile.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(mFile);
			finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mFile.toString();
	}

	public String getFileURI() {
		return mFile.toString();
	}

	private static boolean isExternalStorageReadOnly() {
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
			return true;
		}
		return false;
	}

	private static boolean isExternalStorageAvailable() {
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
			return true;
		}
		return false;
	}

}
