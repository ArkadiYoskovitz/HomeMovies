//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//import android.app.Activity;
//import android.widget.Toast;
//
//public class aaaaaa extends Activity {
//	public void name() {
//		File f = new File("/data/data/your.app.package/databases/your_db.db3");
//		FileInputStream fis = null;
//		FileOutputStream fos = null;
//
//		try {
//			fis = new FileInputStream(f);
//			fos = new FileOutputStream("/mnt/sdcard/db_dump.db");
//			while (true) {
//				int i = fis.read();
//				if (i != -1) {
//					fos.write(i);
//				} else {
//					break;
//				}
//			}
//			fos.flush();
//			Toast.makeText(this, "DB dump OK", Toast.LENGTH_LONG).show();
//		} catch (Exception e) {
//			e.printStackTrace();
//			Toast.makeText(this, "DB dump ERROR", Toast.LENGTH_LONG).show();
//		} finally {
//			try {
//				fos.close();
//				fis.close();
//			} catch (IOException ioe) {
//				ioe.getStackTrace();
//			}
//		}
//	}
//}
