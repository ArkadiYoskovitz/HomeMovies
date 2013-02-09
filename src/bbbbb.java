//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.nio.channels.FileChannel;
//
//import android.app.Activity;
//import android.os.Environment;
//import android.widget.Toast;
//
//public class ddddd extends Activity {
//	public void name() {
//		try {
//			File sd = Environment.getExternalStorageDirectory();
//			File data = Environment.getDataDirectory();
//
//			if (sd.canWrite()) {
//				String currentDBPath = "//data//" + getPackageName()
//						+ "//databases//" + dbList[0];
//				String backupDBPath = dbList[0];
//				File currentDB = new File(data, currentDBPath);
//				File backupDB = new File(sd, backupDBPath);
//
//				FileChannel src = new FileInputStream(currentDB).getChannel();
//				FileChannel dst = new FileOutputStream(backupDB).getChannel();
//				dst.transferFrom(src, 0, src.size());
//				src.close();
//				dst.close();
//				Toast.makeText(getBaseContext(), backupDB.toString(),
//						Toast.LENGTH_LONG).show();
//
//			}
//		} catch (Exception e) {
//
//			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
//					.show();
//
//		}
//	}
//}
