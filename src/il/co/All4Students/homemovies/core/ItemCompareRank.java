package il.co.All4Students.homemovies.core;

import java.util.Comparator;

public class ItemCompareRank implements Comparator<Item> {

	@Override
	public int compare(Item lhs, Item rhs) {
		return lhs.getRank() - rhs.getRank();
	}
}
