import java.util.Arrays;

/*************************************************************************
 * 
 * 
 * Implementation steps
 * 
 * 
 * - Use a standard set data type to represent the dictionary, e.g., a
 * SET<String>, a TreeSet<String>, or a HashSet<String>.
 * 
 * - Create the data type BoggleSolver. Write a method based on depth-first
 * search to enumerate all strings that can be composed by following sequences
 * of adjacent dice. That is, enumerate all simple paths in the Boggle graph
 * (but there is no need to explicitly form the graph). For now, ignore the
 * special two-letter sequence Qu.
 * 
 * - Now, implement the following critical backtracking optimization: when the
 * current path corresponds to a string that is not a prefix of any word in the
 * dictionary, there is no need to expand the path further. To do this, you will
 * need to create a data structure for the dictionary that supports the prefix
 * query operation: given a prefix, is there any word in the dictionary that
 * starts with that prefix?
 * 
 * - Deal with the special two-letter sequence Qu.
 * 
 * 
 * Boards. We provide a number of boards for testing. The boards named
 * boards-points[xxxx].txt are Boggle board that results in a maximum score of
 * xxxx points using the dictionary dictionary-yawl.txt. The other boards are
 * designed to test various corner cases, including dealing with the two-letter
 * sequence Qu and boards of dimensions other than 4-by-
 * 
 * @author aldperez
 * 
 *************************************************************************/
public class BoggleSolver {

	private TST<String> tst;
	private BoggleBoard boggleBoard;
	private SET<String> validWords;
	private Object[] neightbors;

	// Initializes the data structure using the given array of strings as the
	// dictionary.
	// (You can assume each word in the dictionary contains only the uppercase
	// letters A through Z.)
	public BoggleSolver(String[] dictionary) {
		tst = new TST<>();
		for (String word : dictionary) {
			tst.put(word, word);
		}
	}

	// Returns the set of all valid words in the given Boggle as an
	// Iterable.
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		validWords = new SET<>();
		this.boggleBoard = board;
		dfs();
		return validWords;
	}

	// Returns the score of the given word if it is in the dictionary, zero
	// otherwise.
	// (You can assume the word contains only the uppercase letters A through
	// Z.)
	public int scoreOf(String word) {

		int length = word.length();
		if (tst.contains(word)) {
			if (length < 3) {
				return 0;
			} else if (length < 5) {
				return 1;
			} else if (length == 5) {
				return 2;
			} else if (length == 6) {
				return 3;
			} else if (length == 7) {
				return 5;
			} else {
				return 11;
			}
		}
		return 0;
	}

	private void dfs() {
		int N = this.boggleBoard.cols() * this.boggleBoard.rows();
		neightbors = new Object[N];
		for (int node = 0; node < N; node++) {
			boolean[] marked = new boolean[N];
			StringBuilder sb = new StringBuilder();
			buildChar(node, marked, sb, 0);
		}

	}

	@SuppressWarnings("unchecked")
	private void buildChar(int node, boolean[] marked, StringBuilder sb,
			int nodeP) {
		int columns = this.boggleBoard.cols();
		int rows = this.boggleBoard.rows();
		int N = columns * rows;
		if (node >= 0 && node < N && !marked[node]) {
			marked[node] = true;
			int i = node / columns;
			int j = node % columns;
			char boardLetter = this.boggleBoard.getLetter(i, j);
			sb.append(boardLetter);
			if (boardLetter == 'Q') {
				sb.append("U");
			}

			if (tst.keysWithPrefix(sb.toString()).iterator().hasNext()) {
				if (sb.length() > 2) {
					String word = tst.get(sb.toString());
					if (word != null) {
						validWords.add(word);
					}
				}
				if (neightbors[node] == null) {
					neightbors[node] = buildNeighbours(i, j, rows, columns);
				}
				for (Integer neightbor : ((Iterable<Integer>) neightbors[node])) {
					buildChar(neightbor, Arrays.copyOf(marked, marked.length),
							new StringBuilder(sb), node);
				}
			}
		}
	}

	/**
	 * Building the neighbours
	 * 
	 * You can try this. First decide the size of the grid Lets say its 8 X 8 &
	 * assign MIN_X = 0, MIN_Y = 0, MAX_X =7, MAX_Y =7
	 * 
	 * Your curren position is represented by thisPosX , thisPosY, then try
	 * this:
	 * 
	 * The basic principle is not to access things that are out of bounds -- so
	 * either protect the bounds or don't go out of bounds in the first place.
	 * That is, start at a place where you won't immediately go out of bounds
	 * and stop before you get out of bounds.
	 * 
	 * for the matrix 4 x 4
	 * 
	 * 0  1  2  3
	 * 4  5  6  7
	 * 8  9  10 11
	 * 12 13 14 15
	 * 
	 * element 0 is in position i=0 and j=0
	 * element 3 is i=0, j=3
	 * element 12 is i=3, j=0
	 * 
	 * @param i
	 * @param j
	 * @param rows
	 * @param columns
	 * @return
	 */
	private Queue<Integer> buildNeighbours(int i, int j, int rows, int columns) {

		Queue<Integer> neighbours = new Queue<>();
		int startPosX = (i - 1 < 0) ? i : i - 1;
		int startPosY = (j - 1 < 0) ? j : j - 1;
		int endPosX = (i + 1 >= rows) ? i : i + 1;
		int endPosY = (j + 1 >= columns) ? j : j + 1;
		for (int rowNum = startPosX; rowNum <= endPosX; rowNum++) {
			for (int colNum = startPosY; colNum <= endPosY; colNum++) {
				if (rowNum != i || colNum != j) {
					// System.out.printf("rowNum %d, colNum %d, node %d \n",
					// rowNum, colNum, xyTo1D(rowNum, colNum, columns));
					neighbours.enqueue(xyTo1D(rowNum, colNum, columns));
				}
			}
		}
		return neighbours;
	}

	private int xyTo1D(int i, int j, int N) {
		return j + (N * i);
	}

	public static void main(String[] args) {
		// String[] args1 = { "boggle/dictionary-yawl.txt",
		// "boggle/board-antidisestablishmentarianisms.txt" };// 40
		// String[] args1 = { "boggle/dictionary-yawl.txt",
		// "boggle/board-dichlorodiphenyltrichloroethanes.txt" };
		// String[] args1 = { "boggle/dictionary-algs4.txt",
		// "boggle/board4x4.txt" };// 33
		String[] args1 = { "boggle/dictionary-algs4.txt", "boggle/board-q.txt" };// 84
		// String[] args1 = { "boggle/dictionary-common.txt" };
		//
		// char[][] a = {
		// { 'B', 'D', 'H', 'N', 'I' },
		// { 'E', 'E', 'I', 'O', 'S' },
		// { 'O', 'E', 'L', 'I', 'E' },
		// { 'A', 'H', 'H', 'E', 'W' },
		// { 'T', 'T', 'F', 'O', 'V' },
		// { 'N', 'F', 'D', 'H', 'E' },
		// { 'H', 'W', 'E', 'E', 'U' },
		// { 'I', 'D', 'E', 'E', 'A' },
		// { 'O', 'C', 'U', 'N', 'O' },
		// { 'H', 'E', 'N', 'S', 'O' } };

		In in = new In(args1[0]);
		String[] dictionary = in.readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard(args1[1]);
		// BoggleBoard board = new BoggleBoard(a);
		System.out.println(board);

		int score = 0;
		int entries = 0;
		Stopwatch sw = new Stopwatch();
		for (String word : solver.getAllValidWords(board)) {
			StdOut.println(word);
			entries++;
			score += solver.scoreOf(word);
		}
		System.out
				.println("Searching  time: " + sw.elapsedTime() + " seconds.");
		StdOut.println(entries);
		StdOut.println("Score = " + score);
	}
}
