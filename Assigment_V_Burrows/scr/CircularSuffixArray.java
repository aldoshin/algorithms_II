public class CircularSuffixArray {
	private static final int CUTOFF = 15;
	private int[] order;
	private int n;

	// circular suffix array of s
	/**
	 * Order will contain the original index of the suffix but sorted based on
	 * circular suffix value
	 * 
	 * @param s
	 */
	public CircularSuffixArray(String s) {
		n = s.length();
		order = new int[n];
		for (int i = 0; i < n; i++) {
			order[i] = i;
		}
		sort(s, 0, n - 1, 0);
	}

	/**
	 * Return the <code>dth</code> character of the suffix beginning in the
	 * string <code>s</code> at <code>suffixPosition</code>.
	 * 
	 * Because we don't build an array of string containing all suffix, then we
	 * rely on the suffixPosition parameter to know for which suffix we are
	 * calculating the char at <b>offset</b> position
	 * 
	 * Using module operation we ensure that if the suffix + d is greater than
	 * s.lenght we restart the counting from the beginning.
	 * 
	 * 
	 * @param s
	 * @param suffixPosition
	 * @param offset
	 * @return
	 */
	private int charAt(String s, int suffixPosition, int d) {
		return s.charAt((suffixPosition + d) % n);
	}

	/**
	 * 3-way String Quicksort circular suffixes of string s from lo to hi
	 * starting at index offset. Code adapted from.
	 * http://algs4.cs.princeton.edu/51radix/Quick3string.java.html
	 * 
	 * Will store the sorted array inside the <code>order</code> instance
	 * variable, <code>order</code> only contains the indexes of the subarrays
	 * but it is sorted based on the subarrays string values.
	 * 
	 * <code>charAt</code>
	 * 
	 * @param s
	 * @param lo
	 * @param hi
	 * @param d
	 */
	private void sort(String s, int lo, int hi, int d) {

		// cutoff to insertion sort for small subarrays
		if (hi <= lo + CUTOFF) {
			insertion(s, lo, hi, d);
			return;
		}

		int lt = lo, gt = hi;
		int v = charAt(s, order[lo], d);
		int i = lo + 1;
		while (i <= gt) {
			int t = charAt(s, order[i], d);
			if (t < v)
				exch(lt++, i++);
			else if (t > v)
				exch(i, gt--);
			else
				i++;
		}

		// a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
		sort(s, lo, lt - 1, d);
		if (v >= 0)
			sort(s, lt, gt, d + 1);
		sort(s, gt + 1, hi, d);

	}

	// sort from a[lo] to a[hi], starting at the dth character
	private void insertion(String a, int lo, int hi, int d) {
		for (int i = lo; i <= hi; i++)
			for (int j = i; j > lo && less(a, j, j - 1, d); j--)
				exch(j, j - 1);
	}

	// is v less than w, starting at character d
	private boolean less(String s, int v, int w, int d) {
		// for (; offset < n; offset++) {
		// int ival = charAt(s, order[i], offset), jval = charAt(s, order[j],
		// offset);
		for (int i = d; i < n; i++) {
			int iChar = charAt(s, order[v], i);
			int jChar = charAt(s, order[w], i);
			if (iChar < jChar)
				return true;
			if (iChar > jChar)
				return false;
		}
		return false;
	}

	/**
	 * exchange order[i] and order[j] meaning that the suffix in position i is
	 * exchanged with the suffix in position j. Again <code>order</code>
	 * instance variable only store the original index of the suffix (before
	 * sort operation).
	 * 
	 * @param i
	 * @param j
	 */
	private void exch(int i, int j) {
		int temp = order[i];
		order[i] = order[j];
		order[j] = temp;
	}

	// length of s
	public int length() {
		return n;
	}

	// returns index of ith sorted suffix
	public int index(int i) {
		if (i < 0 || i >= n) {
			throw new IndexOutOfBoundsException();
		}
		return order[i];
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
		int SCREEN_WIDTH = 80;
		String s = StdIn.readString();
		int n = s.length();
		int digits = (int) Math.log10(n) + 1;
		String fmt = "%" + (digits == 0 ? 1 : digits) + "d ";
		StdOut.printf("String length: %d\n", n);
		CircularSuffixArray csa = new CircularSuffixArray(s);
		for (int i = 0; i < n; i++) {
			StdOut.printf(fmt, i);
			for (int j = 0; j < (SCREEN_WIDTH - digits - 1) && j < n; j++) {
				char c = s.charAt((j + csa.index(i)) % n);
				if (c == '\n')
					c = ' ';
				StdOut.print(c);
			}
			StdOut.println();
		}
	}
}