package com.amazonaws.lambda.demo;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.mashape.unirest.http.exceptions.UnirestException;

public class TestPalletWar {

	public static void main(String[] args) throws UnirestException, ParseException {
		// TODO Auto-generated method stub		
		
		/*
		
		for (Pokemon poke : realOrder){
			System.out.println(realOrder.indexOf(poke));
			System.out.println(poke.getName());
			System.out.println(poke.getUrl());			
		}
		System.out.println("------------------------");
		for (String result : results) {
			System.out.println(result);
		}
		System.out.println("------------------------");
		for (String dex : pokedex) {
			System.out.println(dex);
		}		
		*/		
		
		int fightsNumber =0;
		boolean chaos = false;
		int petitions = 0;
		
		ArrayList<String> results = new ArrayList<String>();		
		ArrayList<Fight> Fights = new ArrayList<Fight>();
		ArrayList<String> realOrder = new ArrayList<String>();
		ArrayList<String> pokedex = new ArrayList<String>();
		
		JSONArray pokedexArray = new JSONArray();
		JSONArray FightsArray = new JSONArray();
		JSONArray resultsArray = new JSONArray();		
		JSONObject jsonFight = new JSONObject();
		results.add("charmander");		
		results.add("squirtle");
		results.add("caterpie");
		results.add("pidgey");		
		results.add("bulbasaur");			
		
		results.forEach(pokemon -> resultsArray.add(pokemon));
		/*				
		PokedexMgr newPokedex = new PokedexMgr(results, pokedex, null);
		newPokedex.FillPokedex();		
		results = newPokedex.getResults();		
		realOrder = newPokedex.getRealOrder();		
		
		PalletWar newTournament = new PalletWar (results,realOrder);
		newTournament.CalculateFights();
		*/
		//Fights = newTournament.GetFightsList();
		/*	
		for ( Fight actualFight : Fights ) {
			jsonFight.put("winner", actualFight.getWinner());
			jsonFight.put("loser", actualFight.getLoser());		
			FightsArray.add(jsonFight);
			jsonFight = new JSONObject();
		}		*/
		
		/*	fightsNumber = newTournament.GetFightsNumber();
		
		pokedex = newPokedex.getPokedex();
		
		pokedex.forEach(pokemon -> pokedexArray.add(pokemon));
		
		chaos = newTournament.isThereChaos();	
		*/
		JSONObject responseBody = new JSONObject();
		
		responseBody.put("resultsArray", resultsArray);   
        //responseBody.put("message", "testing");
        //responseBody.put("chaos", chaos);
        //responseBody.put("fightsNumber", fightsNumber);
        //responseBody.put("pokedexArray", pokedexArray);*/
        //responseBody.put("FightsArray", FightsArray);        
		
        System.out.println("------------------------------------------------");
        System.out.println(responseBody.toString());
         
		System.out.println("------------------------------------------------");
		//System.out.println("Número de peticiones: "+newPokedex.getPetitions());
		System.out.println("------------------------------------------------");
	//	System.out.println("Number of fights: "+newTournament.GetFightsNumber());
		//System.out.println(NewTournament.GetFights());
		
		System.out.println("------------------------------------------------");
	//	Fights = newTournament.GetFightsList();		
		Fights.forEach(Fight -> {
			System.out.println("Winner "+ Fight.getWinner()+" VS  Loser "+Fight.getLoser());
			
		});
	}

}
