import java.io.*;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MRQuickSearch {

  private static String query = "";
  private static HashMap<Character, Integer> table = new HashMap<Character, Integer>();

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, IntWritable>{

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public static List<String> splitString(String s, int l){
      List<String> chunks = new ArrayList<String>();
      for(int start = 0; start < s.length(); start += l){
        chunks.add(s.substring(start, Math.min(s.length(), start+l+query.length()-1)));
      }
      return chunks;
    }
    

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      
      List<String> stream = splitString(value.toString(), 1000);
      for(String cur : stream){
        int i=0;
        int s = query.length(), t = cur.length();
        while(i < t-s+1){
          Character last = cur.charAt(i+s-1);
          boolean equal = true;
          for(int j=i+s-1; j >= i; j--){
            if(cur.charAt(j) != query.charAt(j-i)){
              equal = false;
              if(table.containsKey(last) == false)
                i += s;
              else
                i += table.get(last);
              break;
            }

          }
          if(equal){
            word.set(query);
            context.write(word, one);
            i++;
          }
        }
      }
    }
  }

  public static class IntSumReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }
  }

  public static String ReadFile(String fileName){
    String data = "";
    
    try{
      File file = new File(fileName);
      Scanner fileReader = new Scanner(file);

      while(fileReader.hasNextLine()){
        data += fileReader.nextLine();
      }
      fileReader.close();
    }catch(FileNotFoundException e){
      System.out.println("File not found : "+fileName);
    }
    return data;
  }

  private static HashMap<Character, Integer> preProcess(String key){
    HashMap<Character, Integer> table = new HashMap<Character, Integer>();

    for(int i=0; i < key.length()-1; i++){
      table.put(key.charAt(i), key.length()-i-1);
    }
    if(table.containsKey(key.charAt(key.length()-1)) == false)
      table.put(key.charAt(key.length()-1), key.length());

    return table;
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "quick search");
    job.setJarByClass(MRQuickSearch.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    query = ReadFile(args[2]);
    table = preProcess(query);

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}