package il.co.All4Students.homemovies.core;

import java.util.Comparator;

/**
 * 
 * @author Arkadi Yoskovitz
 */
public class ItemCompareSubject implements Comparator<Item> {

	@Override
	public int compare(Item lhs, Item rhs) {
		return lhs.getSubject().compareTo(rhs.getSubject());
	}
}
