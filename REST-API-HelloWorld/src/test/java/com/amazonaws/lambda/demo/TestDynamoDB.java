package com.amazonaws.lambda.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentity.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.AmazonServiceException;

import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import java.util.HashMap;
import java.util.Map;

public class TestDynamoDB {
	
	 
	public TestDynamoDB() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		String pokedexInfo = null;
		
		if (pokedexInfo==null) {
			System.out.println("goddamn");
		}
		}
	  /*      String table_name = "pokedex";
	        String name = "ID";     		       		        
	        HashMap<String,AttributeValue> key_to_get = new HashMap<String,AttributeValue>();
	        key_to_get.put("ID", new AttributeValue("https://pokeapi.co/api/v2/pokemon/?offset=0&limit=100"));
	        GetItemRequest request = null;
	        request = new GetItemRequest().withKey(key_to_get).withTableName(table_name);		       
	        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();

	        try {
	            Map<String,AttributeValue> returned_item =ddb.getItem(request).getItem();         		            												           
	            
	            if (returned_item != null) {
	                Set<String> keys = returned_item.keySet();
	                	                
	                for (String key : keys) {
	                	if (key.equals("JSON")) {
	                		System.out.println(returned_item.get(key).getS());
	                	}            		                	
	                }
	            } else {
	                System.out.format("No item found with the key %s!\n", name);
	            }
	        } catch (AmazonServiceException e) {
	            System.err.println(e.getErrorMessage());
	            System.exit(1);
	        }*/
		// PUTITEM
		/*
		 final String USAGE = "\n" +
		            "Usage:\n" +
		            "    PutItem <table> <name> [field=value ...]\n\n" +
		            "Where:\n" +
		            "    table    - the table to put the item in.\n" +
		            "    name     - a name to add to the table. If the name already\n" +
		            "               exists, its entry will be updated.\n" +
		            "Additional fields can be added by appending them to the end of the\n" +
		            "input.\n\n" +
		            "Example:\n" +
		            "    PutItem Cellists Pau Language=ca Born=1876\n";

		        if (args.length < 2) {
		            System.out.println(USAGE);
		            System.exit(1);
		        }

		        String table_name = args[0];
		        String name = args[1];
		        ArrayList<String[]> extra_fields = new ArrayList<String[]>();

		        // any additional args (fields to add to database)?
		        for (int x = 2; x < args.length; x++) {
		            String[] fields = args[x].split("=", 2);
		            if (fields.length == 2) {
		                extra_fields.add(fields);
		            } else {
		                System.out.format("Invalid argument: %s\n", args[x]);
		                System.out.println(USAGE);
		                System.exit(1);
		            }
		        }

		        System.out.format("Adding \"%s\" to \"%s\"", name, table_name);
		        if (extra_fields.size() > 0) {
		            System.out.println("Additional fields:");
		            for (String[] field : extra_fields) {
		                System.out.format("  %s: %s\n", field[0], field[1]);
		            }
		        }

		        HashMap<String,AttributeValue> item_values =
		            new HashMap<String,AttributeValue>();

		        item_values.put("ID", new AttributeValue(name));

		        for (String[] field : extra_fields) {
		            item_values.put(field[0], new AttributeValue(field[1]));
		        }

		        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();

		        try {
		            ddb.putItem(table_name, item_values);
		        } catch (ResourceNotFoundException e) {
		            System.err.format("Error: The table \"%s\" can't be found.\n", table_name);
		            System.err.println("Be sure that it exists and that you've typed its name correctly!");
		            System.exit(1);
		        } catch (AmazonServiceException e) {
		            System.err.println(e.getMessage());
		            System.exit(1);
		        }
		        System.out.println("Done!");
		*/
		    }
	


