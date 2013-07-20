package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;

public class Agent extends PlayerEntity {
		
	private final String nickname;
	
	private static int[] levels = {
			0,
			10000,
			30000,
			70000,
			150000,
			300000,
			600000,
			1200000
	};
	
	public Agent(JSONArray json, String nickname) throws JSONException {
		super(json);
		this.nickname = nickname;
	}
		
	public String getNickname() {
		return nickname;
	}
	
	public int getLevel() {
		// TODO: more efficient?
		
		for (int i = 0; i < levels.length; i++) {
			if (this.getAp() >= levels[i])
				return i + 1;
		}

		throw new IndexOutOfBoundsException("agent level could not be retrieved");
	}
	
	public int getEnergyMax() {
		return 2000 + (getLevel() * 1000);
	}
}
