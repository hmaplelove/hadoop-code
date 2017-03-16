package com.casicloud.aop.hadoop.hdfs.utils;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
public class WordCount {
	 
	  public static class TokenizerMapper
	       extends Mapper<Object, Text, Text, IntWritable>{
	     
	    private final static IntWritable one = new IntWritable(1);
	    private Text word = new Text();
	       
	    public void map(Object key, Text value, Context context
	                    ) throws IOException, InterruptedException {
	      StringTokenizer itr = new StringTokenizer(value.toString());
	      while (itr.hasMoreTokens()) {
	        word.set(itr.nextToken());
	        context.write(word, one);      }
	    }
	  }
	   
	  public static class IntSumReducer extends Reducer<Text,IntWritable,Text,IntWritable> {
	    private IntWritable result = new IntWritable();
	 
	    public void reduce(Text key, Iterable<IntWritable> values, Context context ) throws IOException, InterruptedException {
	      int sum = 0;
	      for (IntWritable val : values) {
	        sum += val.get();
	      }
	      result.set(sum);
	      context.write(key, result);
	    }
	  }
	 
	  public static void main(String[] args) throws Exception {
		  
		System.setProperty("hadoop.home.dir", "E:\\hadoop\\hadoop-2.6.5");
	    Configuration conf = new Configuration();
	    //conf.addResource("core-site.xml");
	    //conf.addResource("hdfs-site.xml");
	    //wordcount(conf, ars);
	    //
	    
	    FileSystem fileSystem=FileSystem.get(new URI("hdfs://centos1:9000"), conf,"root");
	    //fileSystem.copyFromLocalFile(new Path("E:\\aop-uc-monitor.rar"),new Path("/tianzhi_logs/aop-uc-monitor.rar"));
	    fileSystem.deleteOnExit(new Path("/tianzhi_logs/aop-uc-monitor.rar"));
	    fileSystem.close();
	   
	  }

	private static void wordcount(Configuration conf)
			throws Exception{
		conf.set("mapred.job.tracker", "ambari.hadoop:9001");
		String[] ars=new String[]{"hdfs://ambari.hadoop:8020/test/","hdfs://ambari.hadoop:8020/output_logs"};
	    //conf.set("mapred.job.tracker", "centos1:9001");
	    //String[] ars=new String[]{"hdfs://centos1:9000/test/","hdfs://centos1:9000/output_logs1"};
		String[] otherArgs = new GenericOptionsParser(conf, ars).getRemainingArgs();
	    if (otherArgs.length != 2) {
	      System.err.println("Usage: wordcount  ");
	      System.exit(2);
	    }
	    Job job = new Job(conf, "word count");
	    job.setJarByClass(WordCount.class);
	    job.setMapperClass(TokenizerMapper.class);
	    job.setCombinerClass(IntSumReducer.class);
	    job.setReducerClass(IntSumReducer.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(IntWritable.class);
	    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
	    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
	}
