package com.amazonaws.lambda.demo;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.mashape.unirest.http.exceptions.UnirestException;

public class PokemonLeagueLauncher {
	
	private ArrayList<String> results = new ArrayList<String>();		
	private ArrayList<String> initialpokedex = new ArrayList<String>();			
	private String initialUrl  = null;
	
	public PokemonLeagueLauncher(ArrayList<String> resultsInput, ArrayList<String> pokedexInput, String InitialUrlInput) {
		// TODO Auto-generated constructor stub
		System.out.println("Pallet War is starting!");
		resultsInput.forEach(index -> results.add(index));
		pokedexInput.forEach(index -> initialpokedex.add(index));		
		initialUrl = InitialUrlInput;
	}
	
	public JSONObject startPalletWar () {
		ArrayList<String> pokedex = new ArrayList<String>();		
		ArrayList<String> realOrder = new ArrayList<String>();
		ArrayList<Fight> fightList = new JSONArray();				
		
		JSONObject jsonFight = new JSONObject();		
		JSONArray pokedexArray = new JSONArray();
		JSONArray FightsArray = new JSONArray();
		JSONArray realOrderArray = new JSONArray();
		
		
		int fightsNumber =0;		
		int petitions = 0;		
		String chaos = "N";		
		
		try {				
			if (results.size()>0) {				
				
				// Obtener el orden real de los pokemon utilizando los resultados recibidos
				PokedexMgr newPokedex = new PokedexMgr(results, initialpokedex, initialUrl);
				newPokedex.FillPokedex();		
				
				// Obtener los resultados, orden real, número de peticiones y todos los pokemones (pokedex)				
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
				
				if (chaos==null) {
					chaos = "N";
				}
				
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
		
		JSONObject responseBody = new JSONObject();			
        responseBody.put("chaos", chaos);
        responseBody.put("petitions", petitions);
        responseBody.put("fightsNumber", fightsNumber);         
        responseBody.put("FightsArray", FightsArray);    
        responseBody.put("pokedexArray", pokedexArray);
        responseBody.put("realOrder", realOrder);
		
		return responseBody;
	}

}
