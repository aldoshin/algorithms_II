public class TrieBoggle<Value> {
	private static int R = 26; // Radix, English alphabet
	private static final int SHIFT = 'A'; // Shift all alphabets by the Decimal
											// value for 'A'
	private Node root;

	private static class Node {
		private Object val;
		private Node[] next = new Node[R];
	}

	/**
	 * Returns the value associated with the given key.
	 * 
	 * @param key
	 *            the key
	 * @return the value associated with the given key if the key is in the
	 *         symbol table and <tt>null</tt> if the key is not in the symbol
	 *         table
	 * @throws NullPointerException
	 *             if <tt>key</tt> is <tt>null</tt>
	 */
	@SuppressWarnings("unchecked")
	public Value get(String key) {
		Node x = get(root, key, 0);
		if (x == null)
			return null;
		return (Value) x.val;
	}

	/**
	 * Does this symbol table contain the given key?
	 * 
	 * @param key
	 *            the key
	 * @return <tt>true</tt> if this symbol table contains <tt>key</tt> and
	 *         <tt>false</tt> otherwise
	 * @throws NullPointerException
	 *             if <tt>key</tt> is <tt>null</tt>
	 */
	public boolean contains(String key) {
		return get(key) != null;
	}
	
	// Return value associated with key in the subtrie rooted at x.
	private Node get(Node x, String key, int d) {
		if (x == null)
			return null;
		if (d == key.length())
			return x;
		// Use dth key char to identify subtrie.
		char c = key.charAt(d);
		return get(x.next[c - SHIFT], key, d + 1);
	}

	/**
	 * Inserts the key-value pair into the symbol table, overwriting the old
	 * value with the new value if the key is already in the symbol table. If
	 * the value is <tt>null</tt>, this effectively deletes the key from the
	 * symbol table.
	 * 
	 * @param key
	 *            the key
	 * @param val
	 *            the value
	 * @throws NullPointerException
	 *             if <tt>key</tt> is <tt>null</tt>
	 */
	public void put(String key, Value val) {
		root = put(root, key, val, 0);
	}

	// Change value associated with key if in subtrie rooted at x.
	private Node put(Node x, String key, Value val, int d) {
		if (x == null)
			x = new Node();
		if (d == key.length()) {
			x.val = val;
			return x;
		}
		// Use dth key char to identify subtrie.
		char c = key.charAt(d);
		x.next[c - SHIFT] = put(x.next[c - SHIFT], key, val, d + 1);
		return x;
	}

	/**
	 * Returns true if keys in the set start with <tt>prefix</tt>, false
	 * otherwise.
	 * 
	 * @param prefix
	 *            the prefix
	 * @return true if keys in the set start with <tt>prefix</tt>.
	 */
	public boolean keysWithPrefix(String pre) {
		return get(root, pre, 0) != null;
	}

	/**
	 * Returns all of the keys in the symbol table that match <tt>pattern</tt>.
	 * 
	 * @param pattern
	 *            the pattern
	 * @return all of the keys in the symbol table that match <tt>pattern</tt>,
	 *         as an iterable.
	 */
	public Iterable<String> keysThatMatch(String pattern) {
		Queue<String> results = new Queue<String>();
		collect(root, new StringBuilder(), pattern, results);
		return results;
	}

	private void collect(Node x, StringBuilder prefix, String pattern,
			Queue<String> results) {
		if (x == null)
			return;
		int d = prefix.length();
		// For boggle only matching words with length >= 3 are added
		if (d == pattern.length() && x.val != null && d >= 3)
			results.enqueue(prefix.toString());
		if (d == pattern.length())
			return;
		char c = pattern.charAt(d);
		prefix.append(c);
		collect(x.next[c - SHIFT], prefix, pattern, results);
		prefix.deleteCharAt(prefix.length() - 1);
	}
}