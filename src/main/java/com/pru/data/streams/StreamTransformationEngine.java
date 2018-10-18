package com.pru.data.streams;

import java.io.FileReader;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
//import org.apache.flink.streaming.api.scala.function.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.twitter.TwitterSource;
import org.apache.flink.streaming.connectors.wikiedits.WikipediaEditEvent;
import org.apache.flink.streaming.connectors.wikiedits.WikipediaEditsSource;
import org.apache.flink.streaming.util.serialization.JSONDeserializationSchema;
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;
//import org.apache.flink.formats.json.JsonNodeDeserializationSchema;
import org.apache.flink.util.Collector;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.couchbase.client.java.document.json.JsonObject;
//import com.couchbase.client.java.document.json.JsonArray;
//import com.couchbase.client.java.document.json.JsonObject;
import com.manolo.flink.CouchbaseSink;
import com.manolo.flink.TwitterIntoCouchbase;
import com.pru.dedupe.CouchBaseJavaConnect;
import com.pru.dedupe.DukeDedupeNew;
import com.pru.mapping.CreatePruJson;

//import com.manolo.flink.TwitterIntoCouchbase.TweetToJsonDocument;

import no.priv.garshol.duke.Record;


public class StreamTransformationEngine {

	public static void main(String[] args) throws Exception {
		
		 final  long DEDUPE_CACHE_EXPIRATION_TIME_MS = 1_000;
		// TODO Auto-generated method stub
			/*Kafka working set-up needs to be moved to properties file */
			// configure Kafka consumer
			// Reading sucessful
			
			//pass global properties file name as String args
			
			//ParameterTool parameterTool = ParameterTool.fromArgs(args);
			ParameterTool paramsGlobalProp = ParameterTool.fromPropertiesFile("global.properties");
			

//			ParameterTool paramsGlobalProp = ParameterTool.fromPropertiesFile("C:\\Users\\SONY VAIO\\eclipse-workspace\\arti\\src\\main\\resources\\global.properties");


			ParameterTool paramsKafka = ParameterTool.fromPropertiesFile(paramsGlobalProp.get("kafka.proploc"));
			
			ParameterTool paramsDb = ParameterTool.fromPropertiesFile(paramsGlobalProp.get("db.prop"));
			
			
			StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
			
			// configure event-time characteristics
	//		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
			// generate a Watermark every second
	//		env.getConfig().setAutoWatermarkInterval(1000);
			
			//Kafka configuration settings 
			Properties props = new Properties();
			props.setProperty("zookeeper.connect", paramsKafka.get("zookeeper.connect")); // Zookeeper default host:port
			props.setProperty("bootstrap.servers", paramsKafka.get("bootstrap.servers")); // Broker default host:port
			props.setProperty("group.id", paramsKafka.get("group.id"));                 // Consumer group ID
			props.setProperty("auto.offset.reset", paramsKafka.get("auto.offset.reset"));  // Always read topic from start
			
			// Couchbase configuration Settings 

	        // To be parameterized
	        List<String> nodes = Arrays.asList(paramsDb.get("clusterUri"), paramsDb.get("clusterUri"));
	        String username = paramsDb.get("user");
	        String password = paramsDb.get("password");
	        String strBucket = paramsDb.get("bucketName");
		 
//Call couchbase Database		- already tested 
	//	String oper_mode ="CREATE"; // Insert Tested 
		String oper_mode ="UPDATE";


	
	//	DataStream<WikipediaEditEvent> edits = env.addSource(new WikipediaEditsSource());
	

/*
 * Read from Kafka code 
 */
 
 
 //parse user parameters
	//	ParameterTool parameterTool = ParameterTool.fromArgs(args);
		//--topic test --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181 --group.id myGroup

//		DataStream<String> messageStream = env.addSource(new FlinkKafkaConsumer011<>(parameterTool.getRequired("topic"), new SimpleStringSchema(), parameterTool.getProperties()));
		
		
 //Kafka code commented to test couchbase and transformation
			
			DataStream<ObjectNode> messageStream = env.addSource(new FlinkKafkaConsumer011<>(paramsKafka.get("topic"), new JSONKeyValueDeserializationSchema(false), props));
		
		System.out.println("Print message Stream:"+messageStream);
		
		//TODO Apply Simple Transformation on Kafka Message 
		
		messageStream.map(new MapFunction<ObjectNode, Object>() {
			private static final long serialVersionUID = -6867736771747690202L;
			@Override
		    public Object map(ObjectNode value) throws Exception {
		    	return "Kafka and Flink says: " + value.get(0);
		       // value.get("field").as(...)
		    	//TODO ... Call Transformation from here and save it to couchbase 
		    	
		    }
		}).print();
		
		
	//TODO convert Kafka JSON messages into JSON Object and use below code
		
	//Example of JSON coming in 
		
//		{"SALARY": "1071.00"},{"EMPLOYEE_ID": "1043"} {"COMMISSION_PCT": "0.00"}],
//		"bu_data":[{"salary": "1061.00"}, {"employee_id": "1043"},{"FIRST_NAME": "NULL}", 
//		{"LAST_NAME": "XXXXXX"}, {"EMAIL": "abc@xyz.com"} 

		// print() will write the contents of the stream to the TaskManager's standard out stream
		// the rebelance call is causing a repartitioning of the data so that all machines
		// see the messages (for example in cases when "num kafka partitions" < "num flink operators"
/*		messageStream.rebalance().map(new MapFunction<String, String>() {
			private static final long serialVersionUID = -6867736771747690202L;

			public String map(String value) throws Exception {
				return "Kafka and Flink says: " + value;
			}
		}).print();*/
		
		//TODO Dedupe Message Stream
		

		
		//Read the file and transform to Pru2.0
		
		//Save it to Couchbase (Insert/Update)
			
			/*
			 * Couchbase via API
			 * 
			 */
			
	
			 // Kafka JSON read and pass meanwhile create an input JSON
			  try { 	 
				  
		//		  inputJson will be from Kafka 
			 
			   JSONParser parser = new JSONParser();
		          	
		            Object obj = parser.parse(new FileReader("C:\\Users\\SONY VAIO\\eclipse-workspace\\PrudentialDataLakeEngine\\src\\main\\resources\\InputFormat.json"));

		            JSONObject inputJSON = (JSONObject) obj;
		        
			 
			 CreatePruJson jsonPru2 = new CreatePruJson();
			 
			 System.out.println("Start mapping to Pru2.0");
			 
//			JsonObject jsonObj = jsonPru2.createJSONPru(inputJSON);
			
			 JsonObject couchTypeJsonObj = jsonPru2.loadTrasToPru(inputJSON,paramsGlobalProp.get("CustomerPruFormat"));
			
			 if(couchTypeJsonObj==null) {
				 System.out.println("Mapping was not sucessfull");
				 //TODO Take action
			 }
		        String id = "cust::"+couchTypeJsonObj.get("Id");
		        
			CouchBaseJavaConnect couchbaseDb = new CouchBaseJavaConnect();
			System.out.println("Strating writing to couchbase");
			
			couchbaseDb.callCouchbaseDb(oper_mode,couchTypeJsonObj,nodes,username,password,strBucket,id);
		
			System.out.println("Finished writing to couchbase");	
			
			
			  } catch(Exception e) {
				  System.out.println(e.toString());
			  }
	
			  
			  
			  
			  
			  /*		
			//Call API to connect and write to couchbase
			ParameterTool paramsDb = ParameterTool.fromPropertiesFile(paramsGlobalProp.get("db.prop"));
			 
			//TODO env add source deduped message 
			
			 env =TwitterIntoCouchbase.callTwitterToCouchbase(env,paramsDb);
			 
			 env.execute();

		*/	
		
			
			
			
		
		// Count changes in 2 seconds and print Date,how many events,Byte changed
/*		
		edits.timeWindowAll(Time.seconds(2))
		.apply(new AllWindowFunction<WikipediaEditEvent, Tuple3<Date, Long, Long>, TimeWindow>() {
	        public void apply(TimeWindow timeWindow, Iterable<WikipediaEditEvent> iterable, Collector<Tuple3<Date, Long, Long>> collector) throws Exception {
	    	        long count = 0;
	            long bytesChanged = 0;
	            // Count number of edits
	            for (WikipediaEditEvent event : iterable) {
	                count++;
	                bytesChanged += event.getByteDiff();
	            }
	            // Output a number of edits and window's end time
	            collector.collect(new Tuple3<>(new Date(timeWindow.getEnd()), count, bytesChanged));
	        }
	    })
	    .print();
	*/
		
		// Call Dedupe in streaming mode custom funcition
/*		
 	//Dedupe inside flink 
		edits.timeWindowAll(Time.seconds(3))
		.apply(new AllWindowFunction<WikipediaEditEvent, Tuple3<Date, Long, Long>, TimeWindow>() {
	        @Override
			public void apply(TimeWindow timeWindow, Iterable<WikipediaEditEvent> iterable, Collector<Tuple3<Date, Long, Long>> collector) throws Exception {
	    	        long count = 0;
	            long bytesChanged = 0;
	            System.out.println("Start Deduping by calling apply method inside a stream");
	            
	        	DukeDedupeNew.performDedupe(paramsGlobalProp.get("dedupe.configdb"), "dedupe");
	    	//	System.out.println("Deduping complete totalMatches : "+totalMatches);
	    		
	    		
	            // Count number of edits
	            for (WikipediaEditEvent event : iterable) {
	                count++;
	                bytesChanged += event.getByteDiff();
	            }
	            // Output a number of edits and window's end time
	            collector.collect(new Tuple3<>(new Date(timeWindow.getEnd()), count, bytesChanged));
	        }
	    })
	    .print();
		
*/		
	
//		 env.execute();
	/* Working code already 	
		// Find the user who edited it and then print
		
		edits
	    .keyBy((KeySelector<WikipediaEditEvent, String>) WikipediaEditEvent::getUser)
	    .timeWindow(Time.minutes(1))
	 	   .apply(new WindowFunction<WikipediaEditEvent, Tuple2<String, Long>, String, TimeWindow>() {
	        @Override
	        public void apply(String userName, TimeWindow timeWindow, Iterable<WikipediaEditEvent> iterable, Collector<Tuple2<String, Long>> collector) throws Exception {
	            long changesCount = 0;
	            // Count number of changes
	            for (WikipediaEditEvent ignored : iterable) {
	                changesCount++;
	            }
	            // Output user name and number of changes
	            collector.collect(new Tuple2<>(userName, changesCount));
	        }
	    })
	    .print();
	*/	
	
		/*
	
//	// stream in time window 
		edits.filter((FilterFunction<WikipediaEditEvent>) edit -> {
		    return !edit.isBotEdit() && edit.getByteDiff() > 1000;
		})
		.print();
	
	*/
		
	/*KeyedStream<WikipediaEditEvent, String> keyedEdits = edits
		    .keyBy(new KeySelector<WikipediaEditEvent, String>() {
		        @Override
		        public String getKey(WikipediaEditEvent event) {
		            return event.getUser();
		        }
		    });
	
	System.out.println("###########"+keyedEdits);
	DataStream<Tuple2<String, Long>> result = keyedEdits
		    .timeWindow(Time.seconds(1))
		    .fold(new Tuple2<>("", 0L), new FoldFunction<WikipediaEditEvent, Tuple2<String, Long>>() {
		        @Override
		        public Tuple2<String, Long> fold(Tuple2<String, Long> acc, WikipediaEditEvent event) {
		            acc.f0 = event.getUser();
		            acc.f1 += event.getByteDiff();
		            return acc;
		        }
		    });
		    
		   
		
		
	System.out.println("###########"+result);
	result.print();
	
	 */
		/*Run deduping engine */
		
		
	//	System.out.println("Now run the deduping engine");
		
		//App.dukeDedupe();
/*	int totalMatches = DukeDedupeNew.performDedupe(paramsGlobalProp.get("dedupe.configdb"), "link");
		System.out.println("Deduping complete totalMatches : "+totalMatches);
		
		*/
		
	/*try {
		env.execute();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	*/
		
		
		
		/*
		 * Commented code for existing flow which is read from kafka - apply deduping -write to couchbase
		 *  Write to Kafka again 
		*/
		
/*		
	//	String topicName ="test"; // working producer code 
		String topicName ="test1";
		
		
		// Write to Kafka
	
	messageStream.addSink(new FlinkKafkaProducer011 <String>
	("localhost:9092", topicName, new SimpleStringSchema()))
	;
	
	
	*/
	
/*		// create a Kafka consumer
 		// Kafka Direct sink test 
				FlinkKafkaConsumer011 consumer = new FlinkKafkaConsumer011 (
						"test",
						new SimpleStringSchema(),
						props);
	
		String topicName ="";
			// Write to Kafka
		edits.addSink(new FlinkKafkaProducer010<>(topicName,
                                new SerializationSchema<LinkedHashMap<String, Integer>>() {
                                        @Override
                                        public byte[] serialize(LinkedHashMap<String, Integer> element) {
                                                return element.toString().getBytes();
                                        }
                                },
                                params.getProperties())
			).name("Kafka Sink");

		*/


}

	
	/**
	 * Assigns timestamps to TaxiRide records.
	 * Watermarks are a fixed time interval behind the max timestamp and are periodically emitted.
	 *//*
	public static class TaxiRideTSExtractor extends BoundedOutOfOrdernessTimestampExtractor<TaxiRide> {

		public TaxiRideTSExtractor() {
			super(Time.seconds(MAX_EVENT_DELAY));
		}

		@Override
		public long extractTimestamp(TaxiRide ride) {
			if (ride.isStart) {
				return ride.startTime.getMillis();
			}
			else {
				return ride.endTime.getMillis();
			}
		}
	}*/
}
