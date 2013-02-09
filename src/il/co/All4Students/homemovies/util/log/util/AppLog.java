package il.co.All4Students.homemovies.util.log.util;

import il.co.All4Students.homemovies.app.ApplicationPreference;
import il.co.All4Students.homemovies.util.log.db.LogHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
@SuppressLint("SimpleDateFormat")
public class AppLog {
	public static void log(Context context, String tag, String msg) {
		ApplicationPreference mSetting = new ApplicationPreference(context);
		if (mSetting.getEnableLog()) {
			LogHandler logHandler = new LogHandler(context);
			logHandler.addLogItem(new StringBuilder()
					.append(tag)
					.append(" ** ")
					.append(new SimpleDateFormat("yyyy-MM-dd E HH:mm:ss.SSSZ")
							.format(new Date()).toString()).append(" ** ")
					.append(msg).toString());
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
