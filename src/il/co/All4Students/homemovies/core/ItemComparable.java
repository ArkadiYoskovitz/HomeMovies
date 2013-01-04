package il.co.All4Students.homemovies.core;

import java.util.Comparator;

public class ItemComparable implements Comparator<Item> {

	@Override
	public int compare(Item lhs, Item rhs) {
		// assuming the parameters are an objects of type Person
				return lhs.getSubject().compareTo(rhs.getSubject());
	}
	
}
