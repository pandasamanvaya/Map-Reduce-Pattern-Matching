# echo -e "Generating input file"
# python gen_input.py
$HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main MRQuickSearch.java 
jar cf mrquick.jar MRQuickSearch*.class
rm -r output
time $HADOOP_HOME/bin/hadoop jar mrquick.jar MRQuickSearch sample_100000.txt output key_100000.txt
