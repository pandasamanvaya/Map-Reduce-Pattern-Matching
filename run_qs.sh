# echo -e "Generating input file"
# python gen_input.py
$HADOOP_HOME/bin/hadoop com.sun.tools.javac.Main MRQuickSearch.java 
jar cf mrquick.jar MRQuickSearch*.class
if [ -d "./output" ]
then
	rm -r output
fi
time $HADOOP_HOME/bin/hadoop jar mrquick.jar MRQuickSearch sample_temp.txt output key_temp.txt