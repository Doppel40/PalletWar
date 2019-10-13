package com.amazonaws.lambda.demo;

public class Pokemon {
	private String name;
	private String url;

	public Pokemon( String pokemon, String pokemonUrl) {
		// TODO Auto-generated constructor stub
		name = pokemon;
		url = pokemonUrl;		
	}
	
	public String getName() {
		return name;
	}
	
	public String getUrl() {
		return url;
	}
}
