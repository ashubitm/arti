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
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
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
import com.pru.dedupe.CouchBaseJavaConnect;
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
		
		System.out.println("Params of Kafka :"+paramsKafka+"\n toString :"+paramsKafka.toString());
		
		
		
			
			DataStream<ObjectNode> messageStream = env.addSource(new FlinkKafkaConsumer011<>(paramsKafka.get("topic"), new JSONKeyValueDeserializationSchema(false), props));
		
		System.out.println("Print message Stream:"+messageStream);
		
		//TODO Apply Simple Transformation on Kafka Message 
		
		messageStream.map(new MapFunction<ObjectNode, Object>() {
			private static final long serialVersionUID = -6867736771747690202L;
			@Override
		    public Object map(ObjectNode value) throws Exception {
				
				value.getNodeType();
				
				value.get("value");
				
				System.out.println("Node Type : "+value.getNodeType()+"\n value of json:"+value.get("value").get(0));
				
				
			//	if(value.getNodeType()== "OBJECT") 
				
				//{
					
				JsonNode insideInputJSON= value.get("value");
				
				//System.out.println("Inside JSON elements :"+insideInputJSON.get("a"));
				
				JsonObject transPruJson =	transforminsideflink(insideInputJSON,paramsGlobalProp) ;
				
				if(transPruJson == null) {
					System.out.println("Error while transforming");
					return ("Failure Insertion into couchbase ..Please retry /n"+insideInputJSON+"/n");
				}else {
					
				
				writeToCouchbase(transPruJson, oper_mode, nodes, username, password, strBucket);
				
				}
				
			//}
		    	
		       // value.get("field").as(...)
		    	//TODO ... Call Transformation from here and save it to couchbase 
		    //	transforminsideflink
		    
			
		    	return ("Successful Insertion into couchbase : \n"+insideInputJSON+"\n");
			}
		}).print();
		
		//ObjectNode value1 = null;

		
		 env.execute();	
	//TODO convert Kafka JSON messages into JSON Object and use below code
		
	//Example of JSON coming in 
		
//		{"SALARY": "1071.00"},{"EMPLOYEE_ID": "1043"} {"COMMISSION_PCT": "0.00"}],
//		"bu_data":[{"salary": "1061.00"}, {"employee_id": "1043"},{"FIRST_NAME": "NULL}", 
//		{"LAST_NAME": "XXXXXX"}, {"EMAIL": "abc@xyz.com"} 

		
		//TODO Dedupe Message Stream
		

		
		//Read the file and transform to Pru2.0
		
		//Save it to Couchbase (Insert/Update)
			
			/*
			 * Couchbase via API
			 * 
			 */
			
			  
			  /*		
			//Call API to connect and write to couchbase
			ParameterTool paramsDb = ParameterTool.fromPropertiesFile(paramsGlobalProp.get("db.prop"));
			 
			//TODO env add source deduped message 
			
			 env =TwitterIntoCouchbase.callTwitterToCouchbase(env,paramsDb);
			 
			 env.execute();

		*/	
		
			
			

}

	
	

	
	public static JsonObject transforminsideflink(JsonNode insideInputJSON,ParameterTool paramsGlobalProp) {
		
		 // Kafka JSON read and pass meanwhile create an input JSON
		  try { 	 
			  
	//		  inputJson will be from Kafka 
		 
/*		   JSONParser parser = new JSONParser();
	          	
	            Object obj = parser.parse(new FileReader("C:\\Users\\SONY VAIO\\eclipse-workspace\\PrudentialDataLakeEngine\\src\\main\\resources\\InputFormat.json"));

	            JSONObject inputJSON = (JSONObject)obj;
	        */
		 
		 CreatePruJson jsonPru2 = new CreatePruJson();
		 
		 System.out.println("Start mapping to Pru2.0");
		 
//		JsonObject jsonObj = jsonPru2.createJSONPru(inputJSON);
		
		 JsonObject couchTypeJsonObj = jsonPru2.loadTrasToPru(insideInputJSON,paramsGlobalProp.get("CustomerPruFormat"));
		
		
		 return couchTypeJsonObj;
		 
		  } catch(Exception e) {
			  System.out.println(e.toString());
		  }
		  return null;
		  
		  

	}
	
	public static void   writeToCouchbase(JsonObject couchTypeJsonObj,String oper_mode,List<String> nodes,String username,String password,String strBucket) {
	
		try {
		 if(couchTypeJsonObj==null) {
			 System.out.println("Mapping was not sucessfull");
			 
			 //TODO Take action
		 }else {
	        String id = "cust::"+couchTypeJsonObj.get("Id");
	        
		 CouchBaseJavaConnect couchbaseDb = new CouchBaseJavaConnect();
		System.out.println("Strating writing to couchbase");
			
		couchbaseDb.callCouchbaseDb(oper_mode,couchTypeJsonObj,nodes,username,password,strBucket,id);
	
		System.out.println("Finished writing to couchbase");	
		 }
		
		  } catch(Exception e) {
			  System.out.println(e.toString());
		  }

	
		
	}
	
}
	

