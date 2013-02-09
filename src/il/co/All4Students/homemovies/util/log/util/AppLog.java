package il.co.All4Students.homemovies.util.log.util;

import il.co.All4Students.homemovies.app.ApplicationPreference;
import il.co.All4Students.homemovies.util.log.db.LogHandler;
import android.annotation.SuppressLint;
import android.content.Context;

/**
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
@SuppressLint("SimpleDateFormat")
public class AppLog {
	public static void log(Context context, String logTag, String logMsg) {
		ApplicationPreference mSetting = new ApplicationPreference(context);
		if (mSetting.getEnableLog()) {
			LogHandler logHandler = new LogHandler(context);
			logHandler.addLogItem(logTag, logMsg);
		}
	}

	public static String getAllLogs(Context context) {
		LogHandler logHandler = new LogHandler(context);
		return logHandler.getAllLogs();
	}

	public static String getLogLocation(Context context) {
		LogHandler logHandler = new LogHandler(context);
		return logHandler.getAllLogs();
	}
}
