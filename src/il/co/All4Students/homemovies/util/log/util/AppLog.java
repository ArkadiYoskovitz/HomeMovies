package il.co.All4Students.homemovies.util.log.util;

import static il.co.All4Students.homemovies.app.AppConstants.LOG_TAG_AppLog;
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

	public static void clearAllLogs(Context context) {
		try {
			LogHandler logHandler = new LogHandler(context);
			logHandler.deleteAllLogs();
		} catch (Exception e) {
			AppLog.log(context, LOG_TAG_AppLog, e.getMessage());
		}
	}

	public static String getLogLocation(Context context) {
		String s = "/data/data/il.co.All4Students.homemovies/databases/log_database.db";
		return s;
	}
}
