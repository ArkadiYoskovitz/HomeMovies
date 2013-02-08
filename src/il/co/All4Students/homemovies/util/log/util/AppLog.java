package il.co.All4Students.homemovies.util.log.util;

import il.co.All4Students.homemovies.util.log.db.LogHandler;
import android.content.Context;
/**
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
public class AppLog {
	private Context mContext;

	public AppLog(Context context) {
		mContext = context;
	}

	public void log(String tag, String msg) {
		LogHandler logHandler = new LogHandler(mContext);
		logHandler.addLogItem(tag + " ** " + msg);
	}

	public String getLog() {
		LogHandler logHandler = new LogHandler(mContext);
		return logHandler.getAllLogs();
	}

	public String getLogLocation() {
		LogHandler logHandler = new LogHandler(mContext);
		return logHandler.getAllLogs();
	}
}
