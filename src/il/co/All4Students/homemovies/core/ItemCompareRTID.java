package il.co.All4Students.homemovies.core;

import java.util.Comparator;

/**
 * 
 * @author Arkadi Yoskovitz
 * @date 2013-02-08
 */
public class ItemCompareRTID implements Comparator<Item> {

	@Override
	public int compare(Item lhs, Item rhs) {
		return lhs.getRt_ID() - rhs.getRt_ID();
	}
}
