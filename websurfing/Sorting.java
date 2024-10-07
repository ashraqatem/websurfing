package websurfing;

import java.util.ArrayList;
import java.util.HashMap;

public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable.
	 * It returns an ArrayList containing all the keys from the map, ordered
	 * in descending order based on the values they mapped to.
	 *
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number
	 * of pairs in the map.
	 */
	public static <K, V extends Comparable<V>> ArrayList<K> slowSort (HashMap<K, V> results) {
		ArrayList<K> sortedUrls = new ArrayList<K>();
		sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

		int N = sortedUrls.size();
		for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) < 0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);
				}
			}
		}
		return sortedUrls;
	}


	/*
	 * This method takes as input an HashMap with values that are Comparable.
	 * It returns an ArrayList containing all the keys from the map, ordered
	 * in descending order based on the values they mapped to.
	 *
	 * The time complexity for this method is O(n*log(n)), where n is the number
	 * of pairs in the map.
	 */
	public static <K, V extends Comparable<V>> ArrayList<K> fastSort(HashMap<K, V> results) {
		// TODO: Add code here
		//quick sort runs in n * log n it sorts in place instead of in different arrays

		ArrayList<K> sortedUrls = new ArrayList<>(results.keySet());
//		quickSort(sortedUrls, results, 0, sortedUrls.size() - 1);
		return mergeSort(sortedUrls, results);
	}

	private static <K, V extends Comparable<V>> ArrayList<K> mergeSort(ArrayList<K> unsortedList, HashMap<K, V> results) {
		if (unsortedList.size() <= 1){
			return unsortedList;
		}

		int mid = unsortedList.size() / 2;
		ArrayList<K> leftList = new ArrayList<>(unsortedList.subList(0, mid));
		ArrayList<K> rightList = new ArrayList<>(unsortedList.subList(mid, unsortedList.size()));

		leftList = mergeSort(leftList, results);
		rightList = mergeSort(rightList, results);

		return merge(leftList, rightList, results);
	}

	private static <K, V extends Comparable<V>> ArrayList<K>  merge(ArrayList<K> leftList, ArrayList<K> rightList, HashMap<K, V> results ) {
		ArrayList<K> mergeList = new ArrayList<K>();
		int leftIndex = 0;
		int rightIndex = 0;

		while (leftIndex < leftList.size() && rightIndex < rightList.size()){
			K leftKey = leftList.get(leftIndex);
			K rightKey = rightList.get(rightIndex);

			if (results.get(leftKey).compareTo(results.get(rightKey)) >= 0) {
				mergeList.add(leftKey);
				leftIndex++;

			} else {
				mergeList.add(rightKey);
				rightIndex++;
			}
		}
		while (leftIndex < leftList.size()){
			mergeList.add(leftList.get(leftIndex));
			leftIndex++;
		}
		while (rightIndex < rightList.size()){
			mergeList.add(rightList.get(rightIndex));
			rightIndex++;
		}

		return mergeList;
	}






	//implemented as seen in class
//	private static <K, V extends Comparable<V>> void quickSort(ArrayList<K> keys, HashMap<K, V> results, int leftI, int rightI) {
//		if (leftI >= rightI) {
//			return;
//			//that part is sorted
//		} else{
//			int pivotI= placeAndDivide(keys, results, leftI, rightI); // get index of the pivot
//
//			//sort left side of the list and right side of the list recursively
//			quickSort(keys, results, leftI, pivotI - 1);
//			quickSort(keys, results, pivotI + 1, rightI);
//		}
//	}

//	//implemented as seen in class
//	private static <K, V extends Comparable<V>> int placeAndDivide(ArrayList<K> keys, HashMap<K, V> results, int leftI, int rightI) {
//		V pivot = results.get(keys.get(rightI)); // Choose the pivot value
//		int i = leftI - 1; // wall placed at the index
//
//		//compare all the elements to the pivot
//		for (int j = leftI; j < rightI; j++) {
//			if (results.get(keys.get(j)).compareTo(pivot) >= 0) {
//				i++;
//				// Swap keys[i] and keys[j]
//				K temp = keys.get(i);
//				keys.set(i, keys.get(j));
//				keys.set(j, temp);
//			}
//		}
//
//		// move the pivot to the wall)
//		K temp = keys.get(i + 1);
//		keys.set(i + 1, keys.get(rightI));
//		keys.set(rightI, temp);
//
//		return i + 1; // Return the new wall
//	}


}