package il.co.All4Students.homemovies.util.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;

/**
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
@SuppressLint("DefaultLocale")
public class JSONHandler {
	// JSON Node names
	private static final String TAG_LOCAL_URLS = "localURLs";
	private static final String TAG_URI = "localURI";

	public static String putURIintoJSON(ArrayList<String> objectsToEncode) {
		String encodedString = new String();
		try {
			JSONArray encodeItems = new JSONArray(JSONUtilWithTags.list2json(
					objectsToEncode, TAG_URI));

			JSONObject jsonToEncode = new JSONObject();
			jsonToEncode.put(TAG_LOCAL_URLS, encodeItems);

			encodedString = jsonToEncode.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encodedString;
	}

	public static ArrayList<String> getURIFromJSON(String stringToParce) {
		ArrayList<String> localURI = new ArrayList<String>();
		try {
			JSONObject jsonToParce = new JSONObject(stringToParce);

			JSONArray foundItems = jsonToParce.getJSONArray(TAG_LOCAL_URLS);

			for (int i = 0; i < foundItems.length(); i++) {
				JSONObject l = foundItems.getJSONObject(i);

				String foundLocalUri = l.getString(TAG_URI);
				localURI.add(foundLocalUri);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return localURI;
	}

	public static String insertURIintoJSON(String jsonString,
			String stringInfoToInsert) {

		ArrayList<String> tmpArrayList = JSONHandler.getURIFromJSON(jsonString);
		tmpArrayList.add(stringInfoToInsert);
		jsonString = JSONHandler.putURIintoJSON(tmpArrayList);

		return jsonString;
	}
	
	public static String removeURIfromJSON(String jsonString,
			String stringInfoToRemove) {

		ArrayList<String> tmpArrayList = JSONHandler.getURIFromJSON(jsonString);
		tmpArrayList.remove(stringInfoToRemove);
		jsonString = JSONHandler.putURIintoJSON(tmpArrayList);

		return jsonString;
	}
}
