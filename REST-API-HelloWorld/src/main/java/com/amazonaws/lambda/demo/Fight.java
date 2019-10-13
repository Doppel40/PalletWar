package com.amazonaws.lambda.demo;

public class Fight {
	
	private String winner = null;
	private String loser = null;	

	public Fight(String winnerName, String loserName) {
		winner = winnerName;
		loser = loserName;
	}
	
	public String getWinner() {
		return winner;
	}
	public String getLoser() {
		return loser;
	}	
	
}
