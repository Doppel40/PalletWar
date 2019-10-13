package com.amazonaws.lambda.demo;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.io.BufferedReader;
import java.util.ArrayList;

import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LambdaFunctionHandler implements RequestStreamHandler
{
	
	private DynamoDB dynamoDb;
	private String DYNAMODB_TABLE_NAME = "Person";
	private Regions REGION = Regions.US_EAST_1;
	JSONParser parser = new JSONParser();

	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
		LambdaLogger logger = context.getLogger();
		logger.log("Loading Java Lambda handler of ProxyWithStream");
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		HttpResponse<String> response = null;
		JSONObject responseJson = new JSONObject();		
		JSONObject event = null;			
       
		String proxy = null;
		String param1 = null;
		String param2 = null;
		String bodyJson = null;
		String responseText = null;		
		String responseCode = "200";
		String messageOutput = "no proxy... you kidding or something? ¡Shit is about to get real! ";
		
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<String> pokedex = new ArrayList<String>();		
		ArrayList<String> realOrder = new ArrayList<String>();
		
		int fightsNumber =0;
		boolean chaos = false;
		int petitions = 0;
		
		ArrayList<Fight> fightList = new JSONArray();
		JSONObject jsonFight = new JSONObject();		
		JSONArray pokedexArray = new JSONArray();
		JSONArray FightsArray = new JSONArray();		
      
		// Recepción de entrada
		try {
			event = (JSONObject)parser.parse(reader);
			
			// Obtener el "Proxy" que es el indicador de lo que voy a ejecutar
			if (event.get("pathParameters") != null) {
				JSONObject pps = (JSONObject)event.get("pathParameters");
				if ( pps.get("proxy") != null) {
					proxy = (String)pps.get("proxy");
				}
			}
			
			// Si necesito obtener parámetros de la URL
		/*	if (event.get("queryStringParameters") != null)
			{
				JSONObject qps = (JSONObject)event.get("queryStringParameters");
				if ( qps.get("param1") != null)
				{
					param1 = (String)qps.get("param1");
				}
			}			
			if (event.get("queryStringParameters") != null)
			{
				JSONObject qps = (JSONObject)event.get("queryStringParameters");
				if ( qps.get("param2") != null)
				{
					param2 = (String)qps.get("param2");
				}
			}
		*/
			
			// Si necesito obtener el body de la petición
			if (event.get("body") != null){
				bodyJson = (String) event.get("body");		
				JSONArray resultsArray = (JSONArray) parser.parse(bodyJson);								
				for (int i = 0; i < resultsArray.size(); i++) {
				    String Pokemon = resultsArray.get(i).toString();				    			   
				    results.add(Pokemon.toLowerCase());			    			    
				}				
			}			
		}
		catch(Exception pex) {
			responseJson.put("statusCode", "400");
			responseJson.put("exception", pex);
		}		
		
		for (String result : results ) {
			responseText = responseText + " position (" +results.indexOf(result)+") = "+ result;
		}
	
		
		  // Implement your logic here
		int output = 0;
		
	       
		if (proxy.equals("palletWar")) {					
			
			// Getting the pokedex content
			try {				
				if (results.size()>0) {				
					PokedexMgr newPokedex = new PokedexMgr(results, pokedex, null);
					newPokedex.FillPokedex();		
					results = newPokedex.getResults();		
					realOrder = newPokedex.getRealOrder();		
					
					PalletWar newTournament = new PalletWar (results,realOrder);
					newTournament.CalculateFights();
					
					fightList = newTournament.GetFightsList();
					for ( Fight actualFight : fightList ) {
						jsonFight.put("winner", actualFight.getWinner());
						jsonFight.put("loser", actualFight.getLoser());		
						FightsArray.add(jsonFight);
						jsonFight = new JSONObject();
					}	
					
					fightsNumber = newTournament.GetFightsNumber();
					pokedex = newPokedex.getPokedex();						
					pokedex.forEach(pokemon -> pokedexArray.add(pokemon));										
					chaos = newTournament.isThereChaos();	
				}					
				
			} catch (UnirestException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		} 
		else if (proxy.equals("DummyPlug")) {
			messageOutput = "DummyPlug";
		}
		
		// Armado de respuesta
   
		 JSONObject responseBody = new JSONObject();
         responseBody.put("message", messageOutput);
         responseBody.put("chaos", chaos);
         responseBody.put("fightsNumber", fightsNumber);         
         responseBody.put("FightsArray", FightsArray);    
         responseBody.put("pokedexArray", pokedexArray);         

         JSONObject headerJson = new JSONObject();
         headerJson.put("x-custom-header", "Dont know what should i put here");
         headerJson.put("Access-Control-Allow-Origin", "*");

         responseJson.put("isBase64Encoded", false);
         responseJson.put("statusCode", responseCode);
         responseJson.put("headers", headerJson);
         responseJson.put("body", responseBody.toString());  

         OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
         writer.write(responseJson.toJSONString());  
         writer.close();
	}	
}