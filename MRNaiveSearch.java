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

public class MRNaiveSearch {

  private static String query = "";

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
        // System.out.println("Current String : "+cur);
        for(int i = 0; i < cur.length()-query.length()+1; i++){

          if(cur.substring(i, i+query.length()).equals(query)){
            // System.out.println(i);
            word.set(query);
            context.write(word, one);
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

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "navie search");
    job.setJarByClass(MRNaiveSearch.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    query = ReadFile(args[2]);
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}