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

Programming Assignment 5: Burrows-Wheeler Data Compression [http://coursera.cs.princeton.edu/algs4/assignments/burrows.html]
===================================================
Implement the Burrows-Wheeler data compression algorithm. This revolutionary algorithm outcompresses gzip and PKZIP, is relatively easy to implement, and is not protected by any patents. It forms the basis of the Unix compression utililty bzip2.

The Burrows-Wheeler compression algorithm consists of three algorithmic components, which are applied in succession:

1. _Burrows-Wheeler transform._ Given a typical English text file, transform it into a text file in which sequences of the same character occur near each other many times.
2. _Move-to-front encoding._ Given a text file in which sequences of the same character occur near each other many times, convert it into a text file in which certain characters appear more frequently than others.
3. _Huffman compression._ Given a text file in which certain characters appear more frequently than others, compress it by encoding freqently occuring characters with short codewords and rare ones with long codewords.

Step 3 is the one that compresses the message: it is particularly effective because Steps 1 and 2 steps result in a text file in which certain characters appear much more frequently than others. To expand a message, apply the inverse operations in reverse order: first apply the Huffman expansion, then the move-to-front decoding, and finally the inverse Burrows-Wheeler transform. Your task is to implement Burrows-Wheeler and move-to-front components efficiently.

**Binary input and binary output.** To enable your programs to work with binary data, you will use the libraries BinaryStdIn.java and BinaryStdOut.java described in Algorithms, 4th edition. To display the binary output when debugging, you can use HexDump.java, which takes a command-line argument N, reads bytes from standard input and writes them to standard output in hexadecimal, N per line.
```
% more abra.txt
ABRACADABRA!

% java HexDump 16 < abra.txt
41 42 52 41 43 41 44 41 42 52 41 21
96 bits
```
Note that in ASCII, 'A' is 41 (hex) and '!' is 21 (hex).
**Huffman encoding and decoding.** _Huffman.java (Program 5.10 in Algorithms, 4th edition)_ implements the classic Huffman compression and expansion algorithms.
```sh
% java Huffman - < abra.txt | java HexDump 16
50 4a 22 43 43 54 a8 40 00 00 01 8f 96 8f 94
120 bits
% java Huffman - < abra.txt | java Huffman +
ABRACADABRA!
```
You will not write any code for this step.
Move-to-front encoding and decoding. The main idea of move-to-front encoding is to maintain an ordered sequence of the characters in the alphabet, and repeatedly read in a character from the input message, print out the position in which that character appears, and move that character to the front of the sequence. As a simple example, if the initial ordering over a 6-character alphabet is A B C D E F, and we want to encode the input CAAABCCCACCF, then we would update the move-to-front sequences as follows:

```
move-to-front    in   out
-------------    ---  ---
 A B C D E F      C    2 
 C A B D E F      A    1
 A C B D E F      A    0
 A C B D E F      A    0
 A C B D E F      B    2
 B A C D E F      C    2
 C B A D E F      C    0
 C B A D E F      C    0
 C B A D E F      A    2
 A C B D E F      C    1
 C A B D E F      C    0
 C A B D E F      F    5
 F C A B D E  
```
If the same character occurs next to each other many times in the input, then many of the output values will be small integers, such as 0, 1, and 2. The extremely high frequency of certain characters makes an ideal scenario for Huffman coding.
+ _Move-to-front encoding._ Your task is to maintain an ordered sequence of the 256 extended ASCII characters. Initialize the sequence by making the ith character in the sequence equal to the ith extended ASCII character. Now, read in each 8-bit character c from standard input one at a time, output the 8-bit index in the sequence where c appears, and move c to the front.
```
% java MoveToFront - < abra.txt | java HexDump 16
41 42 52 02 44 01 45 01 04 04 02 26
96 bits
```
+ _Move-to-front decoding._ Initialize an ordered sequence of 256 characters, where extended ASCII character i appears ith in the sequence. Now, read in each 8-bit character i (but treat it as an integer between 0 and 255) from standard input one at a time, write the ith character in the sequence, and move that character to the front. Check that the decoder recovers any encoded message.
```
% java MoveToFront - < abra.txt | java MoveToFront +
ABRACADABRA!
```

**Circular suffix array.** To efficiently implement the key component in the Burrows-Wheeler transform, you will use a fundamental data structure known as the circular suffix array, which describes the abstraction of a sorted array of the N circular suffixes of a string of length N. As an example, consider the string "ABRACADABRA!" of length 12. The table below shows its 12 circular suffixes and the result of sorting them.
```
 i       Original Suffixes           Sorted Suffixes         index[i]
--    -----------------------     -----------------------    --------
 0    A B R A C A D A B R A !     ! A B R A C A D A B R A    11
 1    B R A C A D A B R A ! A     A ! A B R A C A D A B R    10
 2    R A C A D A B R A ! A B     A B R A ! A B R A C A D    7
 3    A C A D A B R A ! A B R     A B R A C A D A B R A !    0
 4    C A D A B R A ! A B R A     A C A D A B R A ! A B R    3
 5    A D A B R A ! A B R A C     A D A B R A ! A B R A C    5
 6    D A B R A ! A B R A C A     B R A ! A B R A C A D A    8
 7    A B R A ! A B R A C A D     B R A C A D A B R A ! A    1
 8    B R A ! A B R A C A D A     C A D A B R A ! A B R A    4
 9    R A ! A B R A C A D A B     D A B R A ! A B R A C A    6
10    A ! A B R A C A D A B R     R A ! A B R A C A D A B    9
11    ! A B R A C A D A B R A     R A C A D A B R A ! A B    2
```
We define index[i] to be the index of the original suffix that appears ith in the sorted array. For example, index[11] = 2 means that the 2nd original suffix appears 11th in the sorted order (i.e., last alphabetically).

**Burrows-Wheeler transform.** The goal of the Burrows-Wheeler transform is not to compress a message, but rather to transform it into a form that is more amenable to compression. The transform rearranges the characters in the input so that there are lots of clusters with repeated characters, but in such a way that it is still possible to recover the original input. It relies on the following intuition: if you see the letters hen in English text, then most of the time the letter preceding it is t or w. If you could somehow group all such preceding letters together (mostly t's and some w's), then you would have an easy opportunity for data compression.

+ _Burrows-Wheeler encoding._ The Burrows-Wheeler transform of a string s of length N is defined as follows: Consider the result of sorting the N circular suffixes of s. The Burrows-Wheeler transform is the last column in the sorted suffixes array t[], preceded by the row number first in which the original string ends up. Continuing with the "ABRACADABRA!" example above, we highlight the two components of the Burrows-Wheeler transform in the table below.
```
 i     Original Suffixes          Sorted Suffixes       t    index[i]
--    -----------------------     -----------------------    --------
 0    A B R A C A D A B R A !     ! A B R A C A D A B R A    11
 1    B R A C A D A B R A ! A     A ! A B R A C A D A B R    10
 2    R A C A D A B R A ! A B     A B R A ! A B R A C A D    7
*3    A C A D A B R A ! A B R     A B R A C A D A B R A !   *0
 4    C A D A B R A ! A B R A     A C A D A B R A ! A B R    3
 5    A D A B R A ! A B R A C     A D A B R A ! A B R A C    5
 6    D A B R A ! A B R A C A     B R A ! A B R A C A D A    8
 7    A B R A ! A B R A C A D     B R A C A D A B R A ! A    1
 8    B R A ! A B R A C A D A     C A D A B R A ! A B R A    4
 9    R A ! A B R A C A D A B     D A B R A ! A B R A C A    6
10    A ! A B R A C A D A B R     R A ! A B R A C A D A B    9
11    ! A B R A C A D A B R A     R A C A D A B R A ! A B    2
```
Since the original string ABRACADABRA! ends up in row 3, we have first = 3. Thus, the Burrows-Wheeler transform is
``` sh
3
ARD!RCAAAABB
```
Notice how there are 4 consecutive As and 2 consecutive Bs—these clusters make the message easier to compress.
```sh
% java BurrowsWheeler - < abra.txt | java HexDump 16
00 00 00 03 41 52 44 21 52 43 41 41 41 41 42 42
128 bits
```
Also, note that the integer 3 is represented using 4 bytes (00 00 00 03). The character 'A' is represented by hex 41, the character 'R' by 52, and so forth.

_Burrows-Wheeler decoder._ Now, we describe how to invert the Burrows-Wheeler transform and recover the original input string. If the jth original suffix (original string, shifted j characters to the left) is the ith row in the sorted order, we define next[i] to be the row in the sorted order where the (j + 1)st original suffix appears. For example, if first is the row in which the original input string appears, then next[first] is the row in the sorted order where the 1st original suffix (the original string left-shifted by 1) appears; next[next[first]] is the row in the sorted order where the 2nd original suffix appears; next[next[next[first]]] is the row where the 3rd original suffix appears; and so forth.

* _Decoding the message given t[], first, and the next[] array._ The input to the Burrows-Wheeler decoder is the last column t[] of the sorted suffixes along with first. From t[], we can deduce the first column of the sorted suffixes because it consists of precisely the same characters, but in sorted order.
```
 i      Sorted Suffixes     t      next
--    -----------------------      ----
 0    ! ? ? ? ? ? ? ? ? ? ? A        3
 1    A ? ? ? ? ? ? ? ? ? ? R        0
 2    A ? ? ? ? ? ? ? ? ? ? D        6
*3    A ? ? ? ? ? ? ? ? ? ? !        7
 4    A ? ? ? ? ? ? ? ? ? ? R        8
 5    A ? ? ? ? ? ? ? ? ? ? C        9
 6    B ? ? ? ? ? ? ? ? ? ? A       10
 7    B ? ? ? ? ? ? ? ? ? ? A       11
 8    C ? ? ? ? ? ? ? ? ? ? A        5
 9    D ? ? ? ? ? ? ? ? ? ? A        2
10    R ? ? ? ? ? ? ? ? ? ? B        1
11    R ? ? ? ? ? ? ? ? ? ? B        4
```
Now, given the next[] array and first, we can reconstruct the original input string because the first character of the ith original suffix is the ith character in the input string. In the example above, since first = 3, we know that the original input string appears in row 3; thus, the original input string starts with 'A' (and ends with '!'). Since next[first] = 7, the next original suffix appears in row 7; thus, the next character in the original input string is 'B'. Since next[next[first]] = 11, the next original suffix appears in row 11; thus, the next character in the original input string is 'R'.
* _Constructing the next[] array from t[] and first._ Amazingly, the information contained in the Burrows-Wheeler transform suffices to reconstruct the next[] array, and, hence, the original message! Here's how. It is easy to deduce a next[] value for a character that appears exactly once in the input string. For example, consider the suffix that starts with 'C'. By inspecting the first column, it appears 8th in the sorted order. The next original suffix after this one will have the character 'C' as its last character. By inspecting the last column, the next original appears 5th in the sorted order. Thus, next[8] = 5. Similarly, 'D' and '!' each occur only once, so we can deduce that next[9] = 2 and next[0] = 3.
```
 i      Sorted Suffixes     t      next
--    -----------------------      ----
 0    ! ? ? ? ? ? ? ? ? ? ? A        3
 1    A ? ? ? ? ? ? ? ? ? ? R
 2    A ? ? ? ? ? ? ? ? ? ? D
*3    A ? ? ? ? ? ? ? ? ? ? !
 4    A ? ? ? ? ? ? ? ? ? ? R 
 5    A ? ? ? ? ? ? ? ? ? ? C
 6    B ? ? ? ? ? ? ? ? ? ? A
 7    B ? ? ? ? ? ? ? ? ? ? A
 8    C ? ? ? ? ? ? ? ? ? ? A        5
 9    D ? ? ? ? ? ? ? ? ? ? A        2
10    R ? ? ? ? ? ? ? ? ? ? B
11    R ? ? ? ? ? ? ? ? ? ? B
```
However, since 'R' appears twice, it may seem ambiguous whether next[10] = 1 and next[11] = 4, or whether next[10] = 4 and next[11] = 1. Here's the key rule that resolves the apparent ambiguity:
        _If sorted row i and j both start with the same character and i < j, then next[i] < next[j]._
This rule implies next[10] = 1 and next[11] = 4. Why is this rule valid? The rows are sorted so row 10 is lexicographically less than row 11. Thus the ten unknown characters in row 10 must be less than the ten unknown characters in row 11 (since both start with 'R'). We also know that between the two rows that end with 'R', row 1 is less than row 4. But, the ten unknown characters in row 10 and 11 are precisely the first ten characters in rows 1 and 4. Thus, next[10] = 1 and next[11] = 4 or this would contradict the fact that the suffixes are sorted.

Check that the decoder recovers any encoded message.
```
% java BurrowsWheeler - < abra.txt | java BurrowsWheeler +
ABRACADABRA!
```
