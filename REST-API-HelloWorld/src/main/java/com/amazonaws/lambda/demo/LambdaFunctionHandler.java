package com.amazonaws.lambda.demo;

import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.util.ArrayList;

import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LambdaFunctionHandler implements RequestStreamHandler
{	
	JSONParser parser = new JSONParser();

	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
		LambdaLogger logger = context.getLogger();
		logger.log("Loading Java Lambda handler of ProxyWithStream");
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));		
		JSONObject responseJson = new JSONObject();					
		JSONObject responseBody = new JSONObject();		
		JSONObject fullPetition = new JSONObject();						
		JSONArray resultsArray = new JSONArray();
				
		//String param1 = null;
		//String param2 = null;
		String proxy = null;		
		String bodyJson = null;			
		String responseCode = "200";
		String messageOutput = "Initial Message: ¡Starting the process! ";
		
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<String> initialpokedex = new ArrayList<String>();		
      
		// Recepción de entrada
		try {
			fullPetition = (JSONObject)parser.parse(reader);
			
			// Obtener el "Proxy"
			if (fullPetition.get("pathParameters") != null) {
				JSONObject pathParameters = (JSONObject)fullPetition.get("pathParameters");
				if ( pathParameters.get("proxy") != null) {
					proxy = (String)pathParameters.get("proxy");
				}
			}
			
			// Obtener parámetros de la URL
		/*	if (event.get("queryStringParameters") != null)	{
				JSONObject qps = (JSONObject)event.get("queryStringParameters");
				if ( qps.get("param1") != null) { param1 = (String)qps.get("param1"); }
			}			
			if (event.get("queryStringParameters") != null)	{
				JSONObject qps = (JSONObject)event.get("queryStringParameters");
				if ( qps.get("param2") != null) { param2 = (String)qps.get("param2"); }
			} 
		*/
			
			// Obtener el body de la petición
			if (fullPetition.get("body") != null) {
				bodyJson = (String) fullPetition.get("body");		
				resultsArray = (JSONArray) parser.parse(bodyJson);				
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
	    		
		// Ejecución principal
		if (proxy.toLowerCase().equals("palletwar")) {								
			PokemonLeagueLauncher newLeague = new PokemonLeagueLauncher(results, initialpokedex, null);
			responseBody = newLeague.startPalletWar();
		} 
		else if (proxy.toLowerCase().equals("DummyPlug")) {
			messageOutput = "DummyPlug";
		}
		
		// Completando respuesta		 
         responseBody.put("message", messageOutput);
         responseBody.put("resultsArray", resultsArray); 
         responseBody.put("Proxy",proxy.toLowerCase());

         JSONObject headerJson = new JSONObject();
         headerJson.put("x-custom-header", "Custom_header");
         headerJson.put("Content-Type", "application/json");
         headerJson.put("Access-Control-Allow-Origin", "*");
        /* headerJson.put("Access-Control-Max-Age", "3600");         
         headerJson.put("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
         headerJson.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
         headerJson.put("Access-Control-Allow-Credentials", "true");
         
         headerJson.put("Vary", "Origin"); */        

         responseJson.put("isBase64Encoded", false);
         responseJson.put("statusCode", responseCode);
         responseJson.put("headers", headerJson);
         responseJson.put("body", responseBody.toString());  

         OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
         writer.write(responseJson.toJSONString());  
         writer.close();
	}	
}