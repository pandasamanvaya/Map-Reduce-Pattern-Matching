# echo -e "Generating input file"
# python gen_input.py
$HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main MRNaiveSearch.java 
jar cf mrnaive.jar MRNaiveSearch*.class
rm -r output
time $HADOOP_HOME/bin/hadoop jar mrnaive.jar MRNaiveSearch sample_100000.txt output key_100000.txt
