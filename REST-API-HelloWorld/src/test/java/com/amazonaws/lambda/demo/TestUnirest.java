package com.amazonaws.lambda.demo;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.cognitoidentity.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class TestUnirest {
	static String basePokedexUrl = "https://pokeapi.co/api/v2/pokemon/?offset=0&limit=100";
	
	static ArrayList<String> results = new ArrayList<String>();
	static ArrayList<String> realOrder = new ArrayList<String>();	
	static ArrayList<String> pokedex = new ArrayList<String>();
	static  HttpResponse<String> response;
	static JSONParser parser = new JSONParser();
	
	public TestUnirest() {
		// TODO Auto-generated constructor stub
		System.out.println("Shit is about to get real ");
	}

	public static void main(String[] args) throws UnirestException, ParseException {
		// TODO Auto-generated method stub
		String nextUrl= basePokedexUrl;	
		JSONObject responseBody;
		JSONObject responsePokemon;				
		JSONArray resultsArray;	
		String Pokemon;
		String savedUrl;
		String pokedexInfo = null;
		
		String table_name = "pokedex";
		
		DynamoDBHandler DynamoDB = new DynamoDBHandler("pokedex");
		
		do {
			
		pokedexInfo = DynamoDB.GetData("ID", nextUrl);
		
		if (pokedexInfo==null) {
			System.out.println("pokedexInfo empty");
			response = Unirest.get(nextUrl).asString();
			pokedexInfo = response.getBody();
			System.out.println("new pokedexInfo to store as ID "+nextUrl);
			DynamoDB.SaveData("ID", nextUrl, "JSON", pokedexInfo);			
			
		} 	
		else {
			System.out.println("Loaded Pokedex info "+pokedexInfo);
			
		}
		responseBody = (JSONObject)parser.parse(pokedexInfo);        
							
		nextUrl = (String) responseBody.get("next");
		resultsArray = (JSONArray) responseBody.get("results");		
		
		// Se recorren los pokemons para determinar si aplican para validar el resultado
		for (int i = 0; i < resultsArray.size(); i++) {
		    Pokemon = resultsArray.get(i).toString();
		    responsePokemon = (JSONObject) parser.parse(Pokemon);	
		    
		    // pokedex para "cache"
		    pokedex.add((String) responsePokemon.get("name"));			    
		}		
		
		}while ( nextUrl != null );
		
		
		System.out.println("[");
		for (String pokemon : pokedex)		{
			System.out.println("\""+pokemon+"\",");
		}
		System.out.println("]");
	}
}
	

