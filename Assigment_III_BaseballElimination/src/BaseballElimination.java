import java.util.HashMap;
import java.util.Map;

public class BaseballElimination {

	private final Map<String, Integer> teamsMap;
	private final Map<String, Iterable<String>> results;

	private final int[] wins;
	private final int[] losses;
	private final int[] remaining;
	private final int[][] remainingAgainst;

	private int maxAlreadyWon;
	private int remainingOthers;
	private final int numberTeams;

	/**
	 * Create a baseball division from given filename in format specified below.
	 * 
	 * Each line contains the team name (with no internal whitespace
	 * characters), the number of wins, the number of losses, the number of
	 * remaining games, and the number of remaining games against each team in
	 * the division
	 * 
	 * @param filename
	 */
	public BaseballElimination(String filename) {

		this.maxAlreadyWon = 0;
		this.results = new HashMap<>();
		this.teamsMap = new HashMap<>();

		In in = new In(filename);

		this.numberTeams = in.readInt();
		this.wins = new int[this.numberTeams];
		this.losses = new int[this.numberTeams];
		this.remaining = new int[this.numberTeams];
		this.remainingAgainst = new int[this.numberTeams][this.numberTeams];

		for (int i = 0; i < this.numberTeams; i++) {
			String teamName = in.readString();

			int teamWins = in.readInt();
			this.maxAlreadyWon = this.maxAlreadyWon < teamWins ? teamWins
					: this.maxAlreadyWon;

			this.teamsMap.put(teamName, i);
			this.wins[i] = teamWins;
			this.losses[i] = in.readInt();
			this.remaining[i] = in.readInt();
			for (int j = 0; j < this.numberTeams; j++) {
				this.remainingAgainst[i][j] = in.readInt();
			}
		}
	}

	// number of teams
	public int numberOfTeams() {
		return this.numberTeams;
	}

	// all teams
	public Iterable<String> teams() {
		return teamsMap.keySet();
	}

	// number of wins for given team
	public int wins(String team) {
		isTeam(team);
		return wins[teamsMap.get(team)];
	}

	// number of losses for given team
	public int losses(String team) {
		isTeam(team);
		return losses[teamsMap.get(team)];

	}

	// number of remaining games for given team
	public int remaining(String team) {
		isTeam(team);
		return remaining[teamsMap.get(team)];
	}

	// number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		isTeam(team1);
		isTeam(team2);
		return remainingAgainst[teamsMap.get(team1)][teamsMap.get(team2)];
	}

	// is given team eliminated?
	public boolean isEliminated(String team) {
		Iterable<String> certificateOfElimination = certificateOfElimination(team);
		return certificateOfElimination != null
				&& certificateOfElimination.iterator().hasNext();
	}

	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		isTeam(team);
		if (!results.containsKey(team)) {
			results.put(team, solveForTeam(team));
		}
		return results.get(team);
	}

	private Iterable<String> solveForTeam(String team) {
		Bag<String> teams = new Bag<>();
		Integer teamId = this.teamsMap.get(team);
		int maxWins = wins[teamId] + remaining[teamId];

		// trivial Eliminated
		for (String otherTeam : this.teamsMap.keySet()) {
			int otherTeamwins = wins[this.teamsMap.get(otherTeam)];
			if (!otherTeam.equals(team) && otherTeamwins > maxWins) {
				teams.add(otherTeam);
				return teams;
			}
		}

		FordFulkerson f = this.buildGraphForTeam(teamId);
		if (this.remainingOthers > f.value()) {
			for (String keyTeam : this.teamsMap.keySet()) {
				if (f.inCut(this.teamsMap.get(keyTeam))) {
					teams.add(keyTeam);
				}
			}
			return teams;
		}
		return null;
	}

	// Throw IllegalArgumentException if team name not recognized.
	private void isTeam(String team) {
		if (!teamsMap.containsKey(team))
			throw new IllegalArgumentException("Unrecognized team: " + team);
	}

	/**
	 * In order to identify the created nodes numerated as follow:
	 * 
	 * - from 0 to numberTeams are the teams nodes (excluding the team whichs
	 * the graph is build for)
	 * 
	 * - source node (s) is equal to the value of numberTeams
	 * 
	 * - sink node (t) is equal to the value of (numberTeams + 1)
	 * 
	 * - the nodes that represents a game between two teams are representing
	 * from the value of (numberTeam + 2) u to the value of numberTeam!
	 * (factorial)
	 * 
	 * @param teamId
	 * @return
	 */
	private FordFulkerson buildGraphForTeam(int teamId) {
		Bag<FlowEdge> bagEdges = new Bag<>();
		this.remainingOthers = 0;

		int sNodeId = this.numberTeams;
		int tNodeId = this.numberTeams + 1;
		int gameNodeId = this.numberTeams + 2;

		for (int i = 0; i < this.numberTeams; i++) {
			if (i != teamId && (wins[i] + remaining[i] >= maxAlreadyWon)) {
				int capacityTeamToSink = wins[teamId] + remaining[teamId]
						- wins[i];
				FlowEdge edgeTeamToT = new FlowEdge(i, tNodeId,
						capacityTeamToSink < 0 ? 0 : capacityTeamToSink);
				bagEdges.add(edgeTeamToT);
				for (int j = 0; j < i; j++) {
					if (j != teamId
							&& (wins[j] + remaining[j] >= maxAlreadyWon)) {
						int remainingIvsJ = remainingAgainst[i][j];
						FlowEdge sourceToGameIvsJ = new FlowEdge(sNodeId,
								gameNodeId, remainingIvsJ);
						FlowEdge edgeGameToI = new FlowEdge(gameNodeId, i,
								Double.POSITIVE_INFINITY);
						FlowEdge edgeGameToJ = new FlowEdge(gameNodeId, j,
								Double.POSITIVE_INFINITY);
						bagEdges.add(edgeGameToI);
						bagEdges.add(edgeGameToJ);
						bagEdges.add(sourceToGameIvsJ);
						this.remainingOthers += remainingIvsJ;
						// New game node was created increment node id
						gameNodeId++;
					}
				}
			}
		}
		FlowNetwork flowNtwrk = new FlowNetwork(gameNodeId);
		for (FlowEdge edge : bagEdges) {
			flowNtwrk.addEdge(edge);
		}
		return new FordFulkerson(flowNtwrk, sNodeId, tNodeId);
	}

	public static void main(String[] args) {
		BaseballElimination division = new BaseballElimination(
				"baseball/teams42.txt");

		for (String team : division.teams()) {
			System.out.print(team + " " + division.wins(team) + " "
					+ division.losses(team) + " " + division.remaining(team));
			for (String teamVs : division.teams()) {
				System.out.print(" " + division.against(team, teamVs));
			}
			System.out.println();
		}

		System.out.println();

		for (String team : division.teams()) {
			// String team = "Montreal";
			if (division.isEliminated(team)) {
				StdOut.print(team + " is eliminated by the subset R = { ");
				for (String t : division.certificateOfElimination(team)) {
					StdOut.print(t + " ");
				}
				StdOut.println("}");
			} else {
				StdOut.println(team + " is not eliminated");
			}
		}
	}

}
