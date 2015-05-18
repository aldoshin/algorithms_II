/*******************************************************************************
 * Move-to-front algorithm for compression preprocessing.
 * <p>
 * By preprocessing data to be compressed with the move-to-front algorithm, we
 * move commonly used characters close together, making Huffman coding more
 * effective.
 * <p>
 * Usage: <code>java MoveToFront { + | - } <em>binstdin</em> <em>binstdout
 * </em></code>
 * <p>
 * <code>-</code> encodes and <code>+</code> decodes.
 * 
 ******************************************************************************/
public class MoveToFront {
	private static final int R = 256;

	/**
	 * apply move-to-front encoding, reading from standard input and writing to
	 * standard output.
	 * 
	 * In this method we receive the ASCII char and then we need to move all
	 * element to the left until we find the desired char, starting from the
	 * beginning (position 0). When we found it we stop and write the ith
	 * position where the char was, and the move the char to the position 0.
	 * 
	 */
	public static void encode() {
		char[] radix = radixList();
		while (!BinaryStdIn.isEmpty()) {
			char c = BinaryStdIn.readChar();
			char tempout = radix[0], count = 0;
			while (radix[count] != c) {
				char tempin = radix[count];
				radix[count] = tempout;
				tempout = tempin;
				count++;
			}
			radix[count] = tempout;
			radix[0] = c;
			BinaryStdOut.write(count);
		}
		BinaryStdOut.close();
	}

	/**
	 * apply move-to-front decoding, reading from standard input and writing to
	 * standard output.
	 * 
	 * In this method we receive the ith position of the char in the 256 array.
	 * We do not need to look up for the char, then we just substract the char
	 * and move all the elements 1 position to the left, starting on the ith
	 * position, and set the position 0 with the char that was in the ith
	 * position received.
	 */
	public static void decode() {
		char[] radix = radixList();
		while (!BinaryStdIn.isEmpty()) {
			int count = BinaryStdIn.readChar();
			char ch = radix[count];
			BinaryStdOut.write(ch);
			while (count > 0)
				radix[count] = radix[--count];
			radix[0] = ch;
		}
		BinaryStdOut.close();
	}

	// Return an array list of elements of language with radix R in order.
	private static char[] radixList() {
		char[] rl = new char[R];
		for (char i = 0; i < R; rl[i] = i++)
			;
		return rl;
	}

	// if args[0] is '-', apply move-to-front encoding
	// if args[0] is '+', apply move-to-front decoding
	public static void main(String[] args) {
		if (args[0].equals("-")) {
			encode();
		} else {
			decode();
		}
	}
}