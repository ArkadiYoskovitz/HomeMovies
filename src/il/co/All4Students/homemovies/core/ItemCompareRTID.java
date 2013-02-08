package il.co.All4Students.homemovies.core;

import java.util.Comparator;

/**
 * 
 * @author Arkadi Yoskovitz
 */
public class ItemCompareRTID implements Comparator<Item> {

	@Override
	public int compare(Item lhs, Item rhs) {
		return lhs.getRt_ID() - rhs.getRt_ID();
	}
}
