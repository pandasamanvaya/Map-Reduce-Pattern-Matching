# Map-Reduce-Pattern-Matching
This project implements the Quick-Search algorithm for pattern matching on large document. Quick-Search algorithm is a variant of Boyer-Moore algorithm that uses only the bad character match rule. Distributed version of the algorithm is implemented using Hadoop Map-Reduce framework. Navie and Quick-Search pattern matching algorithms are compared along with their distributed versions for execution time and chunk-size.

## Running the program
* Generate the input file and corresponding pattern to be matched(This may take few seconds)
`python3 gen_input.py`

* For distributed setting:-
-- Run `./run_navie.sh` for Navie search.
-- Run `./run_qs.sh` for Quick-Search

* For normal setting:-
-- Run `javac NavieSearch.java` and `java NavieSearch sample_temp.txt key_temp.txt` for Navie search
-- Run `javac QuickSearch.java` and `java QuickSearch sample_temp.txt key_temp.txt` for Quick-Search