package com.amazonaws.lambda.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class PokedexMgr {	
	
	private String basePokedexUrl = "https://pokeapi.co/api/v2/pokemon/?offset=0&limit=100";
	private String nextUrl= basePokedexUrl;	
	private ArrayList<String> results = new ArrayList<String>();
	private ArrayList<String> realOrder = new ArrayList<String>();	
	private ArrayList<String> pokedex = new ArrayList<String>();
	private HttpResponse<String> response;
	private JSONParser parser = new JSONParser();
	private int petitions = 0;

	public PokedexMgr( ArrayList<String> resultInput , ArrayList<String> previousPokedex, String previousNextUrl){
		results = resultInput;
		
		if (previousNextUrl!=null) {
			nextUrl = previousNextUrl;
		}		
		
		if (previousPokedex.size()>0) {
			for (String PreviousPokemon : previousPokedex) {
				pokedex.add(PreviousPokemon);				
			}
		}
	}
	
	public void FillPokedex() throws UnirestException, ParseException{		
		String Pokemon;
		String pokedexInfo = null;
		
		JSONObject responseBody;
		JSONObject responsePokemon;				
		JSONArray resultsArray;		
		
		
		// Se crea objecto para DynamoDB con la tabla pokedex
		DynamoDBHandler DynamoDB = new DynamoDBHandler("pokedex");
		
		do {
			
			// Se consultan datos para la primera petición
			pokedexInfo = DynamoDB.GetData("ID", nextUrl);
			
			if (pokedexInfo==null) {
				System.out.println("pokedexInfo empty");
				// Se obtienen el nombre y url de los 100 pokemon de acuerdo a la url
				response = Unirest.get(nextUrl).asString();
				pokedexInfo = response.getBody();
				System.out.println("new pokedexInfo to store as ID "+nextUrl);
				// Se almacena el resultado
				DynamoDB.SaveData("ID", nextUrl, "JSON", pokedexInfo);
				// Conteo de peticiones
				petitions++;
				
			} 	
			else {
				System.out.println("Loaded Pokedex info "+pokedexInfo);
				
			}	
			
			// Se parsea el resultado para obtener el siguiente llamado y el resultado de este llamado
			responseBody = (JSONObject)parser.parse(pokedexInfo);			
			nextUrl = (String) responseBody.get("next");
			resultsArray = (JSONArray) responseBody.get("results");
			System.out.println("nextUrl "+nextUrl);
			
			// Se recorren los pokemons para determinar si aplican para validar el resultado
			for (int i = 0; i < resultsArray.size(); i++) {
			    Pokemon = resultsArray.get(i).toString();
			    responsePokemon = (JSONObject) parser.parse(Pokemon);	
			    
			    // pokedex para "cache"
			    pokedex.add((String) responsePokemon.get("name"));			    
			   
			    /* 	Solo se agrega al ArrayList real order si está incluido en los resultados, ya que con este
			     	objeto se validan las peleas */
			    if (results.contains(responsePokemon.get("name"))) {
			    	realOrder.add((String) responsePokemon.get("name"));		    	
			    }
			}			
		}
		while ( nextUrl!=null && realOrder.size() != results.size() && petitions < 15 );			
	}			
	
	public ArrayList<String> getRealOrder(){
		return realOrder;
	}
	
	public ArrayList<String> getResults(){
		return results;
	}	
	
	public ArrayList<String> getPokedex(){
		return pokedex;
	}
	
	public int getPetitions() {
		return petitions;
	}
	
	public String getNextUrl() {
		return nextUrl;
	}	
	
}

