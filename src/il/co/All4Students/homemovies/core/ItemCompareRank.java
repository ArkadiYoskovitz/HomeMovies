package il.co.All4Students.homemovies.core;

import java.util.Comparator;

/**
 * 
 * @author Arkadi Yoskovitz
 */
public class ItemCompareRank implements Comparator<Item> {

	@Override
	public int compare(Item lhs, Item rhs) {
		return rhs.getRank() - lhs.getRank();
	}
}
