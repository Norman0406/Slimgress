package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// TODO: let Agent deserialize it's values automatically from handshake and gameBasket data

public class Agent extends Entity {
		
	// can never change during the game
	private final String nickname;
	private Utils.Team team;
	
	// dynamic information
	private int ap;
	private int energy;
	
	// other information
	private boolean allowNicknameEdit;
	private boolean allowFactionChoice;
	
	public Agent(JSONArray json, String nickname) throws JSONException {
		super(json);
		this.nickname = nickname;
		
		JSONObject player = json.getJSONObject(2);
		this.team = Utils.getTeam(player.getJSONObject("controllingTeam"));
		
		JSONObject playerPersonal = player.optJSONObject("playerPersonal");
		if (playerPersonal != null) {
			ap = Integer.parseInt(playerPersonal.getString("ap"));
			energy = playerPersonal.getInt("energy");
			allowNicknameEdit = playerPersonal.getBoolean("playerNicknameEdit");
			allowFactionChoice = playerPersonal.getBoolean("playerFactionChoice");
		}
	}
	
	public void update(JSONObject json) throws JSONException {
		// UNDONE: update ap, energy and energyState
	}

	public int getAp() {
		return ap;
	}

	public int getEnergy() {
		return energy;
	}

	public boolean isAllowNicknameEdit() {
		return allowNicknameEdit;
	}

	public boolean isAllowFactionChoice() {
		return allowFactionChoice;
	}
	
	public String getNickname() {
		return nickname;
	}

	public Utils.Team getTeam() {
		return team;
	}
}
