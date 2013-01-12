package il.co.All4Students.homemovies.core;

import java.util.Comparator;

public class ItemCompareSubject implements Comparator<Item> {

	@Override
	public int compare(Item lhs, Item rhs) {
		return lhs.getSubject().compareTo(rhs.getSubject());
	}
}
