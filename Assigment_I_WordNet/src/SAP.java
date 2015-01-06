import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Shortest ancestral path. An ancestral path between two vertices v and w in a
 * digraph is a directed path from v to a common ancestor x
 * 
 * Corner cases. All methods should throw a java.lang.NullPointerException if
 * any argument is null. All methods should throw a
 * java.lang.IndexOutOfBoundsException if any argument vertex is invalid—not
 * between 0 and G.V() - 1.
 * 
 * Performance requirements. All methods (and the constructor) should take time
 * at most proportional to E + V in the worst case, where E and V are the number
 * of edges and vertices in the digraph, respectively. Your data type should use
 * space proportional to E + V.
 * 
 * @author aldperez
 * 
 */
public class SAP {

	private Digraph dag;
	private int sap;
	private int minLength;

	/**
	 * constructor takes a digraph (not necessarily a DAG)
	 * 
	 * @param G
	 */
	public SAP(Digraph G) {
		this.dag = new Digraph(G);
	}

	/**
	 * length of shortest ancestral path between v and w; -1 if no such path
	 * 
	 * @param v
	 * @param w
	 * @return
	 */
	public int length(int v, int w) {
		return length(asList(v), asList(w));
	}

	/**
	 * An ancestral path between two vertices v and w in a digraph is a directed
	 * path from v to a common ancestor x, together with a directed path from w
	 * to the same ancestor x. A shortest ancestral path is an ancestral path of
	 * minimum total length.
	 */
	public int ancestor(int v, int w) {
		return ancestor(asList(v), asList(w));
	}

	/**
	 * length of shortest ancestral path between any vertex in v and any vertex
	 * in w; -1 if no such path
	 */
	public int length(Iterable<Integer> v, Iterable<Integer> w) {

		this.sap = -1;
		this.minLength = -1;

		DeluxeBFS bfsv = new DeluxeBFS(this.dag, v);
		DeluxeBFS bfsw = new DeluxeBFS(this.dag, w);

		minLengthOnPath(bfsv, bfsw);
		minLengthOnPath(bfsw, bfsv);

		return minLength;
	}

	/**
	 * a common ancestor that participates in shortest ancestral path; -1 if no
	 * such path.
	 */
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		this.sap = -1;
		this.minLength = -1;

		DeluxeBFS bfsv = new DeluxeBFS(this.dag, v);
		DeluxeBFS bfsw = new DeluxeBFS(this.dag, w);

		minLengthOnPath(bfsv, bfsw);
		minLengthOnPath(bfsw, bfsv);

		return sap;
	}

	/**
	 * Main method that iterates over the ancestral path (marked nodes) of both
	 * vertex; if both BFS have path to the node then keeps the shortest
	 * ancestral path. The Deluxe BFS contains the list of markedNodes
	 * (ancestral path)
	 * 
	 * @param bfsv
	 * @param bfsw
	 */
	private void minLengthOnPath(DeluxeBFS bfsv, DeluxeBFS bfsw) {
		Iterator<Integer> vPath = bfsv.getMarked();
		while (vPath.hasNext()) {
			Integer node = vPath.next();
			if (bfsv.hasPathTo(node) && bfsw.hasPathTo(node)) {
				int dist = bfsv.distTo(node) + bfsw.distTo(node);
				if (minLength < 0 || dist < minLength) {
					minLength = dist;
					sap = node;
				}
			}
		}
	}

	private List<Integer> asList(int v) {
		List<Integer> vList = new ArrayList<>();
		vList.add(v);
		return vList;
	}

	public static void main(String[] args) {
		In in = new In("wordnet/digraph1.txt");
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);
		int v = 3;
		int w = 11;
		int length = sap.length(v, w);
		int ancestor = sap.ancestor(v, w);
		StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		v = 9;
		w = 12;
		length = sap.length(v, w);
		ancestor = sap.ancestor(v, w);
		StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		v = 7;
		w = 2;
		length = sap.length(v, w);
		ancestor = sap.ancestor(v, w);
		StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		v = 1;
		w = 6;
		length = sap.length(v, w);
		ancestor = sap.ancestor(v, w);
		StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	}
}