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
		JSONObject fullPetition = null;			
       
		String proxy = null;
		//String param1 = null;
		//String param2 = null;
		String bodyJson = null;			
		String responseCode = "200";
		String messageOutput = "Initial Message: ¡Shit is about to get real! ";
		
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
		JSONArray realOrderArray = new JSONArray();
		JSONArray resultsArray = new JSONArray();		
      
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
	       
		if (proxy.toLowerCase().equals("palletwar")) {								
			try {				
				if (results.size()>0) {				
					
					// Obtener el orden real de los pokemon utilizando los resultados recibidos
					PokedexMgr newPokedex = new PokedexMgr(results, pokedex, null);
					newPokedex.FillPokedex();		
					
					// Obtener los resultados, orden real, número de peticiones y todos los pokemones (pokedex)
					results = newPokedex.getResults();		
					realOrder = newPokedex.getRealOrder();	
					petitions = newPokedex.getPetitions();
					pokedex = newPokedex.getPokedex();					
					
					// Ejecución de algoritmo para determinar el número de peleas
					PalletWar newTournament = new PalletWar (results,realOrder);
					newTournament.CalculateFights();
										
					// Obtener la lista de las peleas, número de peleas y si hay inconsistencia de peleas
					fightList = newTournament.GetFightsList();								
					fightsNumber = newTournament.GetFightsNumber();					
					chaos = newTournament.isThereChaos();
					
					// Se arman los datos para generar la respuesta
					for ( Fight actualFight : fightList ) {
						jsonFight.put("winner", actualFight.getWinner());
						jsonFight.put("loser", actualFight.getLoser());		
						FightsArray.add(jsonFight);
						jsonFight = new JSONObject();
					}
					
					pokedex.forEach(index -> pokedexArray.add(index));
					realOrder.forEach(index -> realOrderArray.add(index));											
				}					
				
			} catch (UnirestException | ParseException e) {
				e.printStackTrace();
			}		
		} 
		else if (proxy.toLowerCase().equals("DummyPlug")) {
			messageOutput = "DummyPlug";
		}
		
		// Armado de respuesta
   
		 JSONObject responseBody = new JSONObject();
         responseBody.put("message", messageOutput);
         responseBody.put("chaos", chaos);
         responseBody.put("petitions", petitions);
         responseBody.put("fightsNumber", fightsNumber);         
         responseBody.put("FightsArray", FightsArray);    
         responseBody.put("pokedexArray", pokedexArray);
         responseBody.put("realOrder", realOrder);
         responseBody.put("resultsArray", resultsArray); 
         responseBody.put("Proxy",proxy.toLowerCase());
         
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