package com.amazonaws.lambda.demo;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.mashape.unirest.http.exceptions.UnirestException;

public class TestPalletWar {

	public static void main(String[] args) throws UnirestException, ParseException {
		// TODO Auto-generated method stub		
				
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<String> pokedex = new ArrayList<String>();		
		JSONArray resultsArray = new JSONArray();				
		
		// resultado 1 Pelea CH>BU
	/*	results.add("charmander");		
		results.add("bulbasaur");
		results.add("squirtle");		
		results.add("caterpie");
		results.add("pidgey");	*/			
		
		// resultado 4 Pelea BU<Everyone -- ["Charmander", "Squirtle", "Caterpie", "Pidgey", "Bulbasaur"]		
		/*results.add("charmander");		
		results.add("squirtle");
		results.add("caterpie");
		results.add("pidgey");		
		results.add("bulbasaur");	*/
		
		// resultado 7 Pelea -- ["Squirtle", "Pidgey", "Charmander", "Caterpie", "Bulbasaur"]		
		results.add("squirtle");				
		results.add("charmander");
		results.add("pidgey");		
		results.add("caterpie");		
		results.add("bulbasaur");	
				
		results.forEach(pokemon -> resultsArray.add(pokemon));				
		JSONObject responseBody = new JSONObject();		
		PokemonLeagueLauncher newLeague = new PokemonLeagueLauncher(results, pokedex, null);
		responseBody = newLeague.startPalletWar();
			
        System.out.println("------------------------------------------------");
        System.out.println(responseBody.toString());
         
		System.out.println("------------------------------------------------");
		//System.out.println("Número de peticiones: "+newPokedex.getPetitions());
		System.out.println("------------------------------------------------");
	//	System.out.println("Number of fights: "+newTournament.GetFightsNumber());
		//System.out.println(NewTournament.GetFights());
		
		System.out.println("------------------------------------------------");
	//	Fights = newTournament.GetFightsList();		
	/*	Fights.forEach(Fight -> {
			System.out.println("Winner "+ Fight.getWinner()+" VS  Loser "+Fight.getLoser());
			
		});
		*/
	}

}
