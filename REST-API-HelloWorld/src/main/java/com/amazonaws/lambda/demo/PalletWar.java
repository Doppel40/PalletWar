package com.amazonaws.lambda.demo;

import java.util.ArrayList;

public class PalletWar {
	
	private ArrayList<String> results = new ArrayList<String>(); 
	private ArrayList<String> realOrder = new ArrayList<String>(); 
	private ArrayList<String> midFight = new ArrayList<String>(); 	
	private ArrayList<String> Fights = new ArrayList<String>();
	private ArrayList<Fight> FightsClass = new ArrayList<Fight>();	
	private boolean isThereChaos;
	
	public PalletWar ( ArrayList<String> resultInput, ArrayList<String> realOrderInput){
		results = resultInput;
		realOrder = realOrderInput;
	}
	
	public void CalculateFights() {		
		int	resultPosition=0;
		int	OriginalPosition=0;
		int	fightsWon = 0;
		int winnerIndex=0;
		int loserIndex=0;
		//String pokemonToEvaluate = null;
		String loser="";
		String winner="";
		String sbFight="";		
		midFight = realOrder;
		
		for (String result : results)
	    {
			resultPosition = results.indexOf(result);
			OriginalPosition = midFight.indexOf(result);
			
			// Evalua el nombre en caso de que el Api lo devuelva con mayusculas
			/*if (OriginalPosition<0) {
				pokemonToEvaluate = pokemonToEvaluate.substring(0, 1).toUpperCase() + pokemonToEvaluate.substring(1);
				OriginalPosition = midFight.indexOf(pokemonToEvaluate);
				if (OriginalPosition<0) {
					OriginalPosition = midFight.indexOf(pokemonToEvaluate.toUpperCase());
				}
			}*/
			
			fightsWon = OriginalPosition - resultPosition;						
			
			if (fightsWon > 2) {
				isThereChaos = true;
				break;
			} else if (fightsWon > 0) {
				for (int i = 1; i <= fightsWon; i++) {
					loserIndex = OriginalPosition - i ;
					winnerIndex = OriginalPosition - i + 1;
					sbFight = "Winner "+midFight.get(winnerIndex)+" VS Loser "+midFight.get(loserIndex);					
					Fights.add(sbFight);			
					FightsClass.add(new Fight(midFight.get(winnerIndex),midFight.get(loserIndex)));
					loser = midFight.get(loserIndex);
					winner = midFight.get(winnerIndex);
										
					midFight.set(loserIndex, winner);
					midFight.set(winnerIndex, loser);
				}				
			}
	    }				
	}
	
	public int GetFightsNumber()
	{
		return Fights.size();
	}
	
	public boolean isThereChaos()
	{
		return isThereChaos;		
	}
	
	public String GetFights() {
		String output="";
				
		for (String fight : Fights) {
			output = output + fight + " \n";
		}
		
		if (output.isEmpty()) {			
			output = "there were no fights";			
		}
		
		return output;		
	}	
	
	public ArrayList<Fight> GetFightsList() {
		return FightsClass;
	}
	
	public ArrayList<String> getMidFight() {
		return midFight;
	}
	
	
	
}


