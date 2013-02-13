package il.co.All4Students.homemovies.util.app;

import static il.co.All4Students.homemovies.app.AppConstants.SortByID;
import static il.co.All4Students.homemovies.app.AppConstants.SortByRTID;
import static il.co.All4Students.homemovies.app.AppConstants.SortByRank;
import static il.co.All4Students.homemovies.app.AppConstants.SortBySubject;
import il.co.All4Students.homemovies.app.ApplicationPreference;
import il.co.All4Students.homemovies.core.Item;
import il.co.All4Students.homemovies.core.ItemCompareRTID;
import il.co.All4Students.homemovies.core.ItemCompareRank;
import il.co.All4Students.homemovies.core.ItemCompareSubject;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;

public class AppUtil {
	public static ArrayList<Item> sortCompareable(Context context,
			ArrayList<Item> itemList) {
		int sortMethod = new ApplicationPreference(context).getSortMethod();
		if (itemList != null) {
			switch (sortMethod) {
			case SortByID:
				Collections.sort(itemList);
				break;

			case SortByRTID:
				Collections.sort(itemList, new ItemCompareRTID());
				break;

			case SortByRank:
				Collections.sort(itemList, new ItemCompareRank());
				break;

			case SortBySubject:
				Collections.sort(itemList, new ItemCompareSubject());
				break;

			default:
				Collections.sort(itemList);
				break;
			}
		}
		return itemList;
	}
}
