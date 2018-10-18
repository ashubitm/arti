package com.pru.mapping;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.couchbase.client.java.document.json.JsonObject;

import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

public class CreatePruJson {
	
/*
 * Load Input JSON Object and map them to Pru 2.0 	
 */
	
	public JsonObject loadTrasToPru(JSONObject inputJson,String file) {
		
		   try {
	       JSONParser parser = new JSONParser();

	          	System.out.println(file);
	            Object obj = parser.parse(new FileReader(file));
	            
	            //Call Json couchbase Pru2.0 creator 
	            JsonObject couchJsonObj = createJSONPru(inputJson);
	           
	          //  JSONObject outputJson =  (JSONObject) obj;
	     //       outputJson.put("Id", inputJson.get("EMPLOYEE_ID"));
	//            outputJson.put("name.firstName", inputJson.get("FIRST_NAME"));
	            
	          //  JSONObject param1  =(JSONObject)outputJson.getObject("name");
	            		
	            		
	     //       outputJson.put("name.surName", inputJson.get("LAST_NAME"));
	       //     outputJson.put("email", inputJson.get("EMAIL"));
		
		//Try to load Pru2.0 Sample 
		
		
//		Pru_2.0_API_Model_Customer_Sample_Data.json
		
/*		
		
	    // Create a JSON Document
	    JsonObject pruDataLake = JsonObject.create()
	            .put("customer", JsonArray.from("Id", "name"))
	            .put("email", "teep@goooogle.com")
	            .put("interests","abc" )
	            .put("age", 11);
//	    String id = "u:teep";
		*/
	      
	    
	    return  couchJsonObj;
	    
		   }catch(Exception e) {
	        	System.out.println("Exception"+e.toString());
	        }
		   
		   return null;
	   }
	
/*
 * Create Pru type JSON and return couchbase type Json Object	
 */
public JsonObject createJSONPru(JSONObject inputJson) {
	

	
	//Try to load Pru2.0 Sample 
	
	 List <Map> list = new ArrayList <Map>();
     Map<String, Object> m = new HashMap<String, Object>();
     m.put("firstName",inputJson.get("FIRST_NAME"));
     m.put("surName",inputJson.get("LAST_NAME"));
     m.put("middleName","");
     list.add(m);
     //Contact Details
     List <Map> contactList = new ArrayList <Map>();
     Map<String, Object> m1 = new HashMap<String, Object>();
     m1.put("email",inputJson.get("EMAIL"));
     m1.put("phone","");
     contactList.add(m1);
     
     //Address Details
     List <Map> addressList = new ArrayList <Map>();
     Map<String, Object> mAddress = new HashMap<String, Object>();
     mAddress.put("line1",inputJson.get("EMAIL"));
     mAddress.put("line2","");
     mAddress.put("city","");
     mAddress.put("zipcode","");
     mAddress.put("country","");
     mAddress.put("latitude","");
     mAddress.put("longitude","");
     
     addressList.add(mAddress);


 
     // Create a JSON Document
     JsonObject pruJson = JsonObject.create()
             .put("Id", inputJson.get("EMPLOYEE_ID"))
             .put("email", inputJson.get("EMAIL"))
             .put("name", JsonArray.from(list))
             .put("dob", 11)
     		.put("type", "")
     		.put("status", "")
     		.put("sex", "")
     		.put("maritalStatus", "")
     		.put("nationality", "")
     		.put("occupation", "")
     		.put("sex", "")
     		.put("contactDetails", JsonArray.from(contactList))
     		.put("address", JsonArray.from(addressList));
     //TODO add other Pru2.0		
     


	
    
    return pruJson;
    
}


////   JsonArray array = new JsonArray();
//   for (int i = 0; i < list.size(); i++) {
//           array.put(list.get(i));
//   }
//   JSONObject obj = new JSONObject();
//   try {
//       obj.put("result", array);
 //("":inputJson.get("firstName"), inputJson.get("surName")))
   
/*	            String s1 ="[{"+"firstName"+":"+"Petra"+","+"surName"+":"+ "Slater"+","+"middleName"+":"
   		+ "H"+
   		"}]";
*/    
//    String s ="[{"firstName":"Petra","surName":"Slater","middleName":"H"}]";
}
