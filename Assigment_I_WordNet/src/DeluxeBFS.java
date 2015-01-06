import java.util.Iterator;

/**
 * Same implementation as <tt>BreadthFirstPaths</tt> class in algs4.jar the only
 * diferrence is that this keeps track of the ancestral path using the
 * markedVertex Queue
 * 
 * @author aldperez
 * 
 *         The <tt>BreadthFirstPaths</tt> class represents a data type for
 *         finding shortest paths (number of edges) from a source vertex
 *         <em>s</em> (or a set of source vertices) to every other vertex in an
 *         undirected graph.
 *         <p>
 *         This implementation uses breadth-first search. The constructor takes
 *         time proportional to <em>V</em> + <em>E</em>, where <em>V</em> is the
 *         number of vertices and <em>E</em> is the number of edges. It uses
 *         extra space (not including the graph) proportional to <em>V</em>.
 *         <p>
 *         For additional documentation, see <a href="/algs4/41graph">Section
 *         4.1</a> of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and
 *         Kevin Wayne.
 * 
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * 
 * 
 */
public class DeluxeBFS {
	private static final int INFINITY = Integer.MAX_VALUE;
	private boolean[] marked; // marked[v] = is there an s->v path?
	private int[] edgeTo; // edgeTo[v] = last edge on shortest s->v path
	private int[] distTo; // distTo[v] = length of shortest s->v path

	private Queue<Integer> markedVertex;

	/**
	 * Computes the shortest path between the source vertex <tt>s</tt> and every
	 * other vertex in the graph <tt>G</tt>.
	 * 
	 * @param G
	 *            the graph
	 * @param s
	 *            the source vertex
	 */
	public DeluxeBFS(Digraph G, int s) {
		initialize(G);
		bfs(G, s);
	}

	/**
	 * Computes the shortest path between any one of the source vertices in
	 * <tt>sources</tt> and every other vertex in graph <tt>G</tt>.
	 * 
	 * @param G
	 *            the graph
	 * @param sources
	 *            the source vertices
	 */
	public DeluxeBFS(Digraph G, Iterable<Integer> sources) {
		initialize(G);
		bfs(G, sources);
	}

	/**
	 * Initializes instance variables
	 * 
	 * @param G
	 */
	private void initialize(Digraph G) {
		marked = new boolean[G.V()];
		markedVertex = new Queue<>();
		distTo = new int[G.V()];
		edgeTo = new int[G.V()];
		for (int v = 0; v < G.V(); v++)
			distTo[v] = INFINITY;
	}

	// BFS from single source
	private void bfs(Digraph G, int s) {
		Queue<Integer> q = new Queue<Integer>();
		marked[s] = true;
		markedVertex.enqueue(s);
		distTo[s] = 0;
		q.enqueue(s);
		while (!q.isEmpty()) {
			int v = q.dequeue();
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					marked[w] = true;
					markedVertex.enqueue(w);
					q.enqueue(w);
				}
			}
		}
	}

	// BFS from multiple sources
	private void bfs(Digraph G, Iterable<Integer> sources) {
		Queue<Integer> q = new Queue<Integer>();
		for (int s : sources) {
			marked[s] = true;
			markedVertex.enqueue(s);
			distTo[s] = 0;
			q.enqueue(s);
		}
		while (!q.isEmpty()) {
			int v = q.dequeue();
			for (int w : G.adj(v)) {
				if (!marked[w]) {
					edgeTo[w] = v;
					distTo[w] = distTo[v] + 1;
					marked[w] = true;
					markedVertex.enqueue(w);
					q.enqueue(w);
				}
			}
		}
	}

	/**
	 * Returns the number of edges in a shortest path between the source vertex
	 * <tt>s</tt> (or sources) and vertex <tt>v</tt>?
	 * 
	 * @param v
	 *            the vertex
	 * @return the number of edges in a shortest path
	 */
	public int distTo(int v) {
		return distTo[v];
	}

	/**
	 * Is there a path between the source vertex <tt>s</tt> (or sources) and
	 * vertex <tt>v</tt>?
	 * 
	 * @param v
	 *            the vertex
	 * @return <tt>true</tt> if there is a path, and <tt>false</tt> otherwise
	 */
	public boolean hasPathTo(int v) {
		return marked[v];
	}

	/**
	 * Returns a shortest path between the source vertex <tt>s</tt> (or sources)
	 * and <tt>v</tt>, or <tt>null</tt> if no such path.
	 * 
	 * @param v
	 *            the vertex
	 * @return the sequence of vertices on a shortest path, as an Iterable
	 */
	public Iterable<Integer> pathTo(int v) {
		if (!hasPathTo(v))
			return null;
		Stack<Integer> path = new Stack<Integer>();
		int x;
		for (x = v; distTo[x] != 0; x = edgeTo[x])
			path.push(x);
		path.push(x);
		return path;
	}

	/**
	 * Gives the list of visited vertices in the form of a stack
	 */
	public Iterator<Integer> getMarked() {
		return markedVertex.iterator();
	}

	public static void main(String[] args) {
		In in = new In("wordnet/digraph-wordnet.txt");
		Digraph G = new Digraph(in);

		int s = 1;
		DeluxeBFS bfs = new DeluxeBFS(G, s);

		for (int v = 0; v < G.V(); v++) {
			if (bfs.hasPathTo(v)) {
				StdOut.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
				for (int x : bfs.pathTo(v)) {
					if (x == s)
						StdOut.print(x);
					else
						StdOut.print("->" + x);
				}
				StdOut.println();
			}
		}
	}
}