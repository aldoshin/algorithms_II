algorithms_II
=============

Repository for Coursera's Algorithms, Part II from Princeton (http://algs4.cs.princeton.edu/home/)

Librarys
========

Libraries used by all classes can be dowloaded from [Book's site](http://algs4.cs.princeton.edu/code/), [algs4.jar](http://algs4.cs.princeton.edu/code/algs4.jar)

Wordnet
=======================
WordNet is a semantic lexicon for the English language that is used extensively by computational linguists and cognitive scientists; for example, it was a key component in IBM's Watson. WordNet groups words into sets of synonyms called synsets and describes semantic relationships between them. One such relationship is the is-a relationship, which connects a hyponym (more specific synset) to a hypernym (more general synset). For example, a plant organ is a hypernym of carrot and plant organ is a hypernym of plant root.

The WordNet digraph. Your first task is to build the wordnet digraph: each vertex v is an integer that represents a synset, and each directed edge v→w represents that w is a hypernym of v. The wordnet digraph is a rooted DAG: it is acyclic and has one vertex—the root—that is an ancestor of every other vertex. However, it is not necessarily a tree because a synset can have more than one hypernym. A small subgraph of the wordnet digraph is illustrated below.

Seam Carving
=====================================
Seam-carving is a content-aware image resizing technique where the image is reduced in size by one pixel of height (or width) at a time. A vertical seam in an image is a path of pixels connected from the top to the bottom with one pixel in each row. (A horizontal seam is a path of pixels connected from the left to the right with one pixel in each column.) Below left is the original 505-by-287 pixel image; below right is the result after removing 150 vertical seams, resulting in a 30% narrower image. Unlike standard content-agnostic resizing techniques (e.g. cropping and scaling), the most interesting features (aspect ratio, set of objects present, etc.) of the image are preserved.

Finding and removing a seam involves three parts and a tiny bit of notation:

Notation. In image processing, pixel (x, y) refers to the pixel in column x and row y, with pixel (0, 0) at the upper left corner and pixel (W − 1, H − 1) at the bottom right corner. This is consistent with the Picture data type in stdlib.jar. Warning: this is the opposite of the standard mathematical notation used in linear algebra where (i, j) refers to row i and column j and with Cartesian coordinates where (0, 0) is at the lower left corner.

**Energy calculation**. The first step is to calculate the energy of each pixel, which is a measure of the importance of each pixel—the higher the energy, the less likely that the pixel will be included as part of a seam (as we'll see in the next step). In this assignment, you will implement the dual gradient energy function, which is described below. Here is the dual gradient of the surfing image above:
Dr. Hug as energy
The energy is high (white) for pixels in the image where there is a rapid color gradient (such as the boundary between the sea and sky and the boundary between the surfing Josh Hug on the left and the ocean behind him). The seam-carving technique avoids removing such high-energy pixels.

**Seam identification**. The next step is to find a vertical seam of minimum total energy. This is similar to the classic shortest path problem in an edge-weighted digraph except for the following:
The weights are on the vertices instead of the edges.
We want to find the shortest path from any of the W pixels in the top row to any of the W pixels in the bottom row.
The digraph is acyclic, where there is a downward edge from pixel (x, y) to pixels (x − 1, y + 1), (x, y + 1), and (x + 1, y + 1), assuming that the coordinates are in the prescribed range.
Vertical Seam
Seam removal. The final step is to remove from the image all of the pixels along the seam.


Baseball Elimination
==========================================

Given the standings in a sports division at some point during the season, determine which teams have been mathematically eliminated from winning their division.

The baseball elimination problem.   In the baseball elimination problem, there is a division consisting of N teams. At some point during the season, team i has w[i] wins, l[i] losses, r[i] remaining games, and g[i][j] games left to play against team j. A team is mathematically eliminated if it cannot possibly finish the season in (or tied for) first place. The goal is to determine exactly which teams are mathematically eliminated. For simplicity, we assume that no games end in a tie (as is the case in Major League Baseball) and that there are no rainouts (i.e., every scheduled game is played).

The problem is not as easy as many sports writers would have you believe, in part because the answer depends not only on the number of games won and left to play, but also on the schedule of remaining games. To see the complication, consider the following scenario:

```
                w[i] l[i] r[i]        g[i][j]
i  team         wins loss left   Atl Phi NY  Mon
------------------------------------------------
0  Atlanta       83   71    8     -   1   6   1
1  Philadelphia  80   79    3     1   -   0   2
2  New York      78   78    6     6   0   -   0
3  Montreal      77   82    3     1   2   0   -
```

Montreal is mathematically eliminated since it can finish with at most 80 wins and Atlanta already has 83 wins. This is the simplest reason for elimination. However, there can be more complicated reasons. For example, Philadelphia is also mathematically eliminated. It can finish the season with as many as 83 wins, which appears to be enough to tie Atlanta. But this would require Atlanta to lose all of its remaining games, including the 6 against New York, in which case New York would finish with 84 wins. We note that New York is not yet mathematically eliminated despite the fact that it has fewer wins than Philadelphia.

It is sometimes not so easy for a sports writer to explain why a particular team is mathematically eliminated. Consider the following scenario from the American League East on August 30, 1996:

```
                w[i] l[i] r[i]          g[i][j]
i  team         wins loss left   NY Bal Bos Tor Det
---------------------------------------------------
0  New York      75   59   28     -   3   8   7   3
1  Baltimore     71   63   28     3   -   2   7   7
2  Boston        69   66   27     8   2   -   0   3
3  Toronto       63   72   27     7   7   0   -   3
4  Detroit       49   86   27     3   7   3   3   -
```

It might appear that Detroit has a remote chance of catching New York and winning the division because Detroit can finish with as many as 76 wins if they go on a 27-game winning steak, which is one more than New York would have if they go on a 28-game losing streak. Try to convince yourself that Detroit is already mathematically eliminated. Here's one ad hoc explanation; we will present a simpler explanation below.

**A maxflow formulation.** We now solve the baseball elimination problem by reducing it to the maxflow problem. To check whether team x is eliminated, we consider two cases.

+ _Trivial elimination_. If the maximum number of games team x can win is less than the number of wins of some other team i, then team x is trivially eliminated (as is Montreal in the example above). That is, if w[x] + r[x] < w[i], then team x is mathematically eliminated.
+ _Nontrivial elimination_. Otherwise, we create a flow network and solve a maxflow problem in it. In the network, feasible integral flows correspond to outcomes of the remaining schedule. There are vertices corresponding to teams (other than team x) and to remaining divisional games (not involving team x). Intuitively, each unit of flow in the network corresponds to a remaining game. As it flows through the network from s to t, it passes from a game vertex, say between teams i and j, then through one of the team vertices i or j, classifying this game as being won by that team.

  More precisely, the flow network includes the following edges and capacities.

  * We connect an artificial source vertex s to each game vertex i-j and set its capacity to g[i][j]. If a flow uses all g[i][j] units of capacity on this edge, then we interpret this as playing all of these games, with the wins distributed between the team vertices i and j.

  + We connect each game vertex i-j with the two opposing team vertices to ensure that one of the two teams earns a win. We do not need to restrict the amount of flow on such edges.
Finally, we connect each team vertex to an artificial sink vertex t. We want to know if there is some way of completing all the games so that team x ends up winning at least as many games as team i. Since team x can win as many as w[x] + r[x] games, we prevent team i from winning more than that many games in total, by including an edge from team vertex i to the sink vertex with capacity w[x] + r[x] - w[i].

  If all edges in the maxflow that are pointing from s are full, then this corresponds to assigning winners to all of the remaining games in such a way that no team wins more games than x. If some edges pointing from s are not full, then there is no scenario in which team x can win the division. In the flow network below Detroit is team x = 4.


  **What the min cut tells us**.   By solving a maxflow problem, we can determine whether a given team is mathematically eliminated. We would also like to explain the reason for the team's elimination to a friend in nontechnical terms (using only grade-school arithmetic). Here's such an explanation for Detroit's elimination in the American League East example above. With the best possible luck, Detroit finishes the season with 49 + 27 = 76 wins. Consider the subset of teams R = { New York, Baltimore, Boston, Toronto }. Collectively, they already have 75 + 71 + 69 + 63 = 278 wins; there are also 3 + 8 + 7 + 2 + 7 = 27 remaining games among them, so these four teams must win at least an additional 27 games. Thus, on average, the teams in R win at least 305 / 4 = 76.25 games. Regardless of the outcome, one team in R will win at least 77 games, thereby eliminating Detroit.

In fact, when a team is mathematically eliminated there always exists such a convincing certificate of elimination, where R is some subset of the other teams in the division. Moreover, you can always find such a subset R by choosing the team vertices on the source side of a min s-t cut in the baseball elimination network. Note that although we solved a maxflow/mincut problem to find the subset R, once we have it, the argument for a team's elimination involves only grade-school algebra.


Programming Assignment 4: Boggle
===================================================
Possible timing improvements implementing dfs with stack instead of recursive method

The Boggle game. Boggle is a word game designed by Allan Turoff and distributed by Hasbro. It involves a board made up of 16 cubic dice, where each die has a letter printed on each of its sides. At the beginning of the game, the 16 dice are shaken and randomly distributed into a 4-by-4 tray, with only the top sides of the dice visible. The players compete to accumulate points by building valid words out of the dice according to the following rules:

+ A valid word must be composed by following a sequence of adjacent dice—two dice are adjacent if they are horizontal, vertical, or diagonal neighbors.
+ A valid word can use each die at most once.
+ A valid word must contain at least 3 letters.
+ A valid word must be in the dictionary (which typically does not contain proper nouns).

**Scoring.** Words are scored according to their length, using this table:

```
word length   points  
  0–2           0
  3–4           1
  5             2
  6             3
  7             5
  8+           11
```
**The Qu special case.** In the English language, the letter Q is almost always followed by the letter U. Consequently, the side of one die is printed with the two-letter sequence Qu instead of Q (and this two-letter sequence must be used together when forming words). When scoring, Qu counts as two letters; for example, the word QuEUE scores as a 5-letter word even though it is formed by following a sequence of 4 dice.

**Testing**. We provide a number of dictionary and board files for testing.

+ _Dictionaries._ A dictionary consists of a sequence of words, separated by whitespace, in alphabetical order. You can assume that each word contains only the uppercase letters A through Z. For example, here are the two files dictionary-algs4.txt and dictionary-yawl.txt:

The former is a list of 6,013 words that appear in Algorithms 4/e; the latter is a comprehensive list of 264,061 English words (known as Yet Another Word List) that is widely used in word-game competitions.
+ _Boggle boards._ A boggle board consists of two integers M and N, followed by the M × N characters in the board, with the integers and characters separated by whitespace. You can assume the integers are nonnegative and that the characters are uppercase letters A through Z (with the two-letter sequence Qu represented as either Q or Qu). For example, here are the files board4x4.txt and board-q.txt:

```
% more board4x4.txt        % more board-q.txt
4 4                        4 4
A  T  E  E                 S  N  R  T
A  P  Y  O                 O  I  E  L
T  I  N  U                 E  Qu T  T
E  D  S  E                 R  S  A  T
```

The following test client takes the filename of a dictionary and the filename of a Boggle board as command-line arguments and prints out all valid words for the given board using the given dictionary.

Here are two sample executions:
```
% java BoggleSolver dictionary-algs4.txt board4x4.txt     % java BoggleSolver dictionary-algs4.txt board-q.txt
AID                                                       EQUATION
DIE                                                       EQUATIONS
END                                                       ...
ENDS                                                      QUERIES
...                                                       QUESTION
YOU                                                       QUESTIONS
Score = 33                                                ...
                                                          TRIES
                                                          Score = 84
```
