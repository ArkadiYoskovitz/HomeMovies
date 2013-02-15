package il.co.All4Students.homemovies.util.imageWeb;

import java.io.File;
import android.content.Context;

public class FileCache {
	// Attributes
	private File cacheDirectory;

	// Constractor
	public FileCache(Context context) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			cacheDirectory = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"Images_cache");
		} else {
			cacheDirectory = context.getCacheDir();
		}
		if (!cacheDirectory.exists()) {
			cacheDirectory.mkdirs();
		}
	}

	// Additional Methods
	public File getFile(String url) {
		String filename = String.valueOf(url.hashCode());
		File f = new File(cacheDirectory, filename);
		return f;

	}

	public void clear() {
		File[] files = cacheDirectory.listFiles();
		if (files == null) {
			return;
		}
		for (File f : files) {
			f.delete();
		}
	}

}