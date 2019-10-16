package com.amazonaws.lambda.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.cognitoidentity.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;

public class DynamoDBHandler {
	private final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
	private String tableName = null;
    private String field = null;     

	public DynamoDBHandler( String inputTable) {
		// TODO Auto-generated constructor stub		
		tableName = inputTable;		
	}
	
	public String GetData(String field, String url) {
		String responseJson = null;
		HashMap<String,AttributeValue> key_to_get = new HashMap<String,AttributeValue>();
        key_to_get.put(field, new AttributeValue(url));
        GetItemRequest request = null;
        request = new GetItemRequest().withKey(key_to_get).withTableName(tableName);        
        try {
            Map<String,AttributeValue> returned_item =ddb.getItem(request).getItem();         		            												           
            
            if (returned_item != null) {
                Set<String> keys = returned_item.keySet();
                for (String key : keys) {
                	if (key.equals("JSON")) {
                		responseJson = returned_item.get(key).getS();
                	}            		                	
                }
            } 
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
		
		return responseJson;
	}
	
	public void SaveData(String urlField, String urlValue, String jsonField, String jsonValue) {
		
		HashMap<String,AttributeValue> item_values = new HashMap<String,AttributeValue>();

	    item_values.put(urlField, new AttributeValue(urlValue));	       
	    item_values.put(jsonField, new AttributeValue(jsonValue));	        	    

	        try {
	            ddb.putItem(tableName, item_values);
	        } catch (ResourceNotFoundException e) {
	        	System.err.println(e.getMessage());
	        	System.exit(1);	        	
	        } catch (AmazonServiceException e) {
	            System.err.println(e.getMessage());
	            System.exit(1);
	        }
	}
}
