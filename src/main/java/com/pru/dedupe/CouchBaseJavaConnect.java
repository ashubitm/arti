package com.pru.dedupe;
//import com.couchbase.client.CouchbaseClient;
import java.io.IOException;
import com.couchbase.client.java.document.*;
import com.couchbase.client.java.document.json.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;

import java.util.Arrays;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.AsyncBucket;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.view.DefaultView;
import com.couchbase.client.java.view.DesignDocument;
import com.couchbase.client.java.view.View;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewResult;
import com.couchbase.client.java.view.ViewRow;

public class CouchBaseJavaConnect {

    private static Bucket bucket;
    private static JsonDocument document;

    public void callCouchbaseDb(String operation,JsonObject teep,List<String> nodes,String username,
    		String password,String strBucket,String id) {

        // Set up Environment
        System.setProperty("com.couchbase.kvEndpoints", "2");
        System.setProperty("com.couchbase.viewServiceEndpoints", "1");

        CouchbaseEnvironment environment = DefaultCouchbaseEnvironment
                .builder()
                .connectTimeout(TimeUnit.SECONDS.toMillis(10))
                .requestBufferSize(1024)
                .build();
        // Cluster object
        CouchbaseCluster cluster;

/*        // To be parameterized
        List<String> nodes = Arrays.asList("127.0.0.1", "127.0.0.1");
        String username = "Administrator";
        String password = "Qwerty@123";
        String strBucket = "Pru";*/

        // Connect to Couchbase cluster
        cluster = CouchbaseCluster.create(environment, nodes);
        cluster.authenticate(username, password);

        // Open bucket - sync & async
        bucket = cluster.openBucket(strBucket);
        AsyncBucket asyncBucket = bucket.async();

      /*  // Create a JSON Document
        JsonObject couchJsonObj = JsonObject.create()
                .put("name", "Tee P")
                .put("email", "teep@goooogle.com")
                .put("interests", JsonArray.from("Badminton", "Poetry"))
                .put("age", 11);*/
                
   //     String id = "u:"+teep;

        // Add document to Couchbase bucket
        
        if(operation.equalsIgnoreCase("CREATE")) {
        	
        	getDocument(id);
        	if(document==null)
         createDocument(teep, id);
        	else {
        		 updateDocument(teep, id);
        	}
        }
        else if(operation.equalsIgnoreCase("UPDATE")) {
         updateDocument(teep, id);
        } else if(operation.equalsIgnoreCase("REMOVE")) {
        	removeDocument(id);
        }
      //  getDocument(id);
         
      //  queryView();

        // Release the resources by closing all
        // Required if env is provided in the cluster connection
        bucket.close();
        asyncBucket.close();
        cluster.disconnect();
        environment.shutdown();

    }
    public static void createDocument(JsonObject jsonObj, String id) {

        // Store the Document
            bucket.insert(JsonDocument.create(id, jsonObj));
    }
    
    public static void getDocument(String id){
        document = bucket.get(id);

        if (document == null) {
            System.err.println("Document not found!");
        }
        else {
            System.out.println("Cas Value: " + document.cas());
            System.out.println("Person age is : " + document.content().getInt("age"));
            System.out.println("Person's name is : " + document.content().getString("name"));
        }
    }
    public static void updateDocument(JsonObject jsonObj, String id) {
    	document = JsonDocument.create(id, jsonObj);
    	document = bucket.upsert(document);
    }
    public static void removeDocument(String id) {
        document = bucket.remove(id);
        System.out.println("Cas Value: " + document.cas());
        System.out.println("Catalog: " + document.content());
    }
    public static void queryView() {
        ViewResult result = bucket.query(ViewQuery.from("dev_activities_view","activities_view"));
        for (ViewRow row : result) {
            System.out.println(row);
        }
    }
}