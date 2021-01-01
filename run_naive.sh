# echo -e "Generating input file"
# python gen_input.py
$HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main MRNaiveSearch.java 
jar cf mrnaive.jar MRNaiveSearch*.class
if [ -d "./output" ]
then
	rm -r output
fi
time $HADOOP_HOME/bin/hadoop jar mrnaive.jar MRNaiveSearch sample_temp.txt output key_temp.txt