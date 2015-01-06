import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Each vertex v is an integer that represents a synset, and each directed edge
 * v->w represents that w is a hypernym of v.
 * 
 * The wordnet digraph is a rooted DAG: it is acyclic and has one vertex—the
 * root—that is an ancestor of every other vertex. However, it is not
 * necessarily a tree because a synset can have more than one hypernym. A small
 * subgraph of the wordnet digraph is illustrated below.
 * 
 */
public class WordNet {

	/**
	 * The key is the noun and the value is the list of hypernyms
	 */
	private Map<String, List<Integer>> synsetsMap;
	/**
	 * The key is the vertex Number and the value is the synonym set
	 */
	private Map<Integer, String> keySynsetMap;
	/**
	 * DAG
	 */
	private Digraph dag;
	/**
	 * Shortest Ancestral Path
	 */
	private SAP sap;

	/**
	 * constructor takes the name of the two input files The constructor should
	 * throw a java.lang.IllegalArgumentException if the input does not
	 * correspond to a rooted DAG ( handled by readHypernyms method)
	 * 
	 * @param synsets
	 * @param hypernyms
	 */
	public WordNet(String synsets, String hypernyms) {
		readSynsets(synsets);
		readHypernyms(hypernyms);
		sap = new SAP(dag);
	}

	/**
	 * Read the hypernyms file which contains the hypernym relationships: The
	 * first field is a synset id; subsequent fields are the id numbers of the
	 * synset's hypernyms.
	 * 
	 * Determine if the DAG is rooted (has only one root) marking every vertex
	 * that is an hypernym of other node. Later iterates the list of hypernims
	 * and if the list of vertices adjacent from that vertex hypernym is empty
	 * (does not have hypernym) then it is a root.
	 * 
	 * Also validates if the DAG has a cycle using DirectedCycle.java from
	 * algs4.jar
	 * 
	 * Finds if the
	 * 
	 * @param hypernyms
	 */
	private void readHypernyms(String hypernyms) {
		In in = new In(hypernyms);
		boolean[] roots = new boolean[dag.V()];
		while (in.hasNextLine()) {
			String[] tokens = in.readLine().split(",");
			int synsetKey = Integer.parseInt(tokens[0]);
			for (int i = 1; i < tokens.length; i++) {
				int hipernymNode = Integer.parseInt(tokens[i]);
				dag.addEdge(synsetKey, hipernymNode);
				roots[hipernymNode] = true;
			}
		}
		DirectedCycle dc = new DirectedCycle(dag);
		if (dc.hasCycle()) {
			throw new IllegalArgumentException("DAG with cycle");
		}
		int rootsNo = 0;
		for (int j = 0; j < roots.length; j++) {
			boolean root = roots[j];
			if (root && !this.dag.adj(j).iterator().hasNext()) {
				rootsNo++;
				if (rootsNo > 1) {
					throw new IllegalArgumentException("No rooted DAG");
				}
			}
		}
	}

	/**
	 * The file synsets.txt lists all the (noun) synsets in WordNet. The first
	 * field is the synset id (an integer), the second field is the synonym set
	 * (or synset), and the third field is its dictionary definition (or gloss).
	 * 
	 * @param synsets
	 */
	private void readSynsets(String synsets) {
		synsetsMap = new HashMap<>();
		keySynsetMap = new HashMap<>();

		In in = new In(synsets);
		int v = 0;
		while (in.hasNextLine()) {
			String[] tokens = in.readLine().split(",");
			int synsetKey = Integer.parseInt(tokens[0]);
			String synset = tokens[1];
			keySynsetMap.put(synsetKey, synset);
			for (String noun : synset.split(" ")) {
				List<Integer> list = synsetsMap.get(noun);
				if (list == null) {
					list = new ArrayList<>();
				}
				list.add(synsetKey);
				synsetsMap.put(noun, list);
			}
			v++;
		}
		dag = new Digraph(v);
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return synsetsMap.keySet();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		return synsetsMap.containsKey(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		if (nounA == null || nounB == null) {
			throw new NullPointerException();
		}
		List<Integer> v = synsetsMap.get(nounA);
		List<Integer> w = synsetsMap.get(nounB);
		if (v == null || w == null) {
			throw new IllegalArgumentException();
		}
		return sap.length(v, w);
	}

	/**
	 * a synset (second field of synsets.txt) that is the common ancestor of
	 * nounA and nounB in a shortest ancestral path (defined below)
	 * 
	 * @param nounA
	 * @param nounB
	 * @return
	 */
	public String sap(String nounA, String nounB) {
		List<Integer> v = synsetsMap.get(nounA);
		List<Integer> w = synsetsMap.get(nounB);
		if (v == null || w == null) {
			throw new IllegalArgumentException();
		}
		return keySynsetMap.get(sap.ancestor(v, w));
	}

	// do unit testing of this class
	public static void main(String[] args) {
		WordNet wn = new WordNet("wordnet/synsets8.txt",
				"wordnet/hypernyms8WrongBFS.txt");
		System.out.println(wn.sap("a", "c"));
		System.out.println(wn.distance("a", "c"));
	}
}