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
	

	
	/*
       

      
	 */
	
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

   //Bank Account Details
     List <Map> bankAccList = new ArrayList <Map>();
     Map<String, Object> bankAccount = new HashMap<String, Object>();
     bankAccount.put("bankName",inputJson.get("EMAIL"));
     bankAccount.put("bankCode","");
     bankAccount.put("branchCode","");
     bankAccount.put("accountNo","");
     bankAccount.put("accountName","");
    
     
     bankAccList.add(bankAccount);
     
     
     //Life Style Details
     
  //Alergies 
     
     List <Map> allergiesList = new ArrayList <Map>();
     Map<String, Object> allergies = new HashMap<String, Object>();
     allergies.put("Nuts",inputJson.get("EMAIL"));
     allergies.put("Wheat","");
     allergies.put("Smoke","");
     
     allergiesList.add(allergies); 
     
     List <Map> familyHistoryList = new ArrayList <Map>();
     Map<String, Object> familyHistory = new HashMap<String, Object>();
     familyHistory.put("BloodPressure",inputJson.get("EMAIL"));
     familyHistory.put("Obesity","");
//     familyHistory.put("Smoke","");
     
     familyHistoryList.add(familyHistory); 
     
     
     List <Map> lifestyleList = new ArrayList <Map>();
     Map<String, Object> lifestyle = new HashMap<String, Object>();
     lifestyle.put("ageNextBday",inputJson.get("EMAIL"));
     lifestyle.put("isSmoker","");
     lifestyle.put("height","");
     lifestyle.put("heightUnit","");
     lifestyle.put("weight","");
     lifestyle.put("weightUnit","");
     lifestyle.put("allergies",JsonArray.from(allergiesList));
     lifestyle.put("familyHistory",JsonArray.from(familyHistoryList));
     
     
     List <Map> spouseList = new ArrayList <Map>();
     Map<String, Object> spouse = new HashMap<String, Object>();
     spouse.put("id","");
     spouse.put("name","");
     spouse.put("contactDetails",JsonArray.from(contactList));
    
     List <Map> childrenList = new ArrayList <Map>();
     Map<String, Object> children = new HashMap<String, Object>();
     children.put("id",90031001);
     children.put("name","");
     
    //TODO Check this  
    children.put("chidren",JsonArray.from(childrenList));
    
    List <Map> financialStatusList = new ArrayList <Map>();
    Map<String, Object> financialStatus = new HashMap<String, Object>();
    
    List <Map> incomeList = new ArrayList <Map>();
    Map<String, Object> income = new HashMap<String, Object>();
    income.put("source","Interest");
    income.put("frequency","Monthly");
    income.put("amount","");
    income.put("currency","IDR");
    incomeList.add(income);
    
    List <Map> assetsList = new ArrayList <Map>();
    Map<String, Object> assets = new HashMap<String, Object>();
    assets.put("type","CAR");
    assets.put("valuation","");
    assets.put("currency","");
    assetsList.add(assets);

    financialStatus.put("income",JsonArray.from(incomeList));
    financialStatus.put("assets",JsonArray.from(assetsList));
    
    
    List <Map> documentsList = new ArrayList <Map>();
    Map<String, Object> documents = new HashMap<String, Object>();
    documents.put("id",90031001);
    documents.put("type","");
    documents.put("name","");
    documents.put("format","");
    documents.put("filename","");
    documents.put("extension","");
    documentsList.add(documents);
   

    
    List <Map> policyList = new ArrayList <Map>();
    Map<String, Object> policy = new HashMap<String, Object>();
    policy.put("Id","");
    policy.put("policyNo","");
    policy.put("product","");
    policy.put("productOptions","");
    policy.put("totalPremium","");
    policy.put("term","");
    policy.put("sumAssured","");
    policy.put("contractDate","");
    policy.put("inceptionDate","");
    policy.put("endDate","");
    policy.put("status","");
    
    policyList.add(policy);


 
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
     		.put("address", JsonArray.from(addressList))
     		.put("bankAccount", JsonArray.from(bankAccList))
     		.put("lifestyle", JsonArray.from(lifestyleList))
     		.put("spouse", JsonArray.from(spouseList))
     		.put("children", JsonArray.from(childrenList))
     		.put("financialStatus", JsonArray.from(financialStatusList))
     		.put("documents", JsonArray.from(documentsList))
     		.put("documents", JsonArray.from(policyList));
     
    //PRU 2.0 Cutomer Json complete (Test req)	
     
     
     


	
    
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
