/**
 * The goal of the Burrows-Wheeler transform is not to compress a message, but
 * rather to transform it into a form that is more amenable to compression. The
 * transform rearranges the characters in the input so that there are lots of
 * clusters with repeated characters, but in such a way that it is still
 * possible to recover the original input. It relies on the following intuition:
 * if you see the letters hen in English text, then most of the time the letter
 * preceding it is t or w. If you could somehow group all such preceding letters
 * together (mostly t's and some w's), then you would have an easy opportunity
 * for data compression.
 * 
 * @author aldperez
 * 
 */
public class BurrowsWheeler {

	// Radix of a byte.
	private static final int R = 256;

	/**
	 * apply Burrows-Wheeler encoding, reading from standard input and writing
	 * to standard output
	 * 
	 * Burrows-Wheeler encoding. The Burrows-Wheeler transform of a string s of
	 * length N is defined as follows: Consider the result of sorting the N
	 * circular suffixes of s. The Burrows-Wheeler transform is the last column
	 * in the sorted suffixes array t[], preceded by the row number first in
	 * which the original string ends up. Continuing with the "ABRACADABRA!"
	 * example above, we highlight the two components of the Burrows-Wheeler
	 * transform in the table below.
	 */
	public static void encode() {
		while (!BinaryStdIn.isEmpty()) {
			String s = BinaryStdIn.readString();
			int n = s.length();
			CircularSuffixArray csa = new CircularSuffixArray(s);
			int i = 0;
			while (csa.index(i) != 0 && i < n) {
				i++;
			}
			BinaryStdOut.write(i);
			for (int j = 0; j < n; j++) {
				BinaryStdOut.write(s.charAt((csa.index(j) + n - 1) % n));
			}
		}
		BinaryStdOut.close();
	}

	/**
	 * apply Burrows-Wheeler decoding, reading from standard input and writing
	 * to standard output
	 * 
	 * It uses Key-indexed counting methodology to find the next array values.
	 * on first iteration counts frequencies of each letter and store then in
	 * count array.
	 * 
	 * Second iteration compute frequency cumulates which specify destinations.
	 * 
	 * Third iteration access cumulates using key as index to find the original
	 * position of the next suffix and stores them in the next array
	 * 
	 * The last iteration writes the character in the ith position in String
	 * received. th ith position is gotten from the next array
	 * 
	 * If the jth original suffix (original string, shifted j characters to the
	 * left) is the ith row in the sorted order, we define next[i] to be the row
	 * in the sorted order where the (j + 1)st original suffix appears. For
	 * example, if first is the row in which the original input string appears,
	 * then next[first] is the row in the sorted order where the 1st original
	 * suffix (the original string left-shifted by 1) appears; next[next[first]]
	 * is the row in the sorted order where the 2nd original suffix appears;
	 * next[next[next[first]]] is the row where the 3rd original suffix appears;
	 * and so forth.
	 * 
	 */
	public static void decode() {
		while (!BinaryStdIn.isEmpty()) {
			int first = BinaryStdIn.readInt();
			String t = BinaryStdIn.readString();
			int N = t.length();
			int[] next = new int[N];

			int[] count = new int[R + 1];

			for (int i = 0; i < N; i++) {
				count[t.charAt(i) + 1]++;
			}
			for (int r = 0; r < R; r++) {
				count[r + 1] += count[r];
			}
			for (int i = 0; i < N; i++) {
				next[count[t.charAt(i)]++] = i;
			}
			for (int i = next[first], c = 0; c < N; i = next[i], c++)
				BinaryStdOut.write(t.charAt(i));
		}
		BinaryStdOut.close();
	}

	// if args[0] is '-', apply Burrows-Wheeler encoding
	// if args[0] is '+', apply Burrows-Wheeler decoding
	public static void main(String[] args) {
		if (args[0].equals("-")) {
			encode();
		} else {
			decode();
		}
	}
}