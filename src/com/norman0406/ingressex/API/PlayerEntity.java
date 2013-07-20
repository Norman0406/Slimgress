package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayerEntity extends Entity {

	public enum EnergyState {
		OK,
		Depleted
	}
	
	private Utils.Team team;
	private int ap;
	private int energy;
	private EnergyState energyState;
	private int clientLevel;
	private boolean allowNicknameEdit;
	private boolean allowFactionChoice;	
	
	public PlayerEntity(JSONArray json) throws JSONException {
		super(json);

		JSONObject playerEntity = json.getJSONObject(2);
		this.team = Utils.getTeam(playerEntity.getJSONObject("controllingTeam"));
		
		JSONObject playerPersonal = playerEntity.optJSONObject("playerPersonal");
		if (playerPersonal != null) {
			ap = Integer.parseInt(playerPersonal.getString("ap"));
			energy = playerPersonal.getInt("energy");
			
			String energyStateString = playerPersonal.getString("energyState");
			if (energyStateString.equals("XM_OK"))
				energyState = EnergyState.OK;
			else if (energyStateString.equals("XM_DEPLETED"))
				energyState = EnergyState.Depleted;
			else
				throw new RuntimeException("unknown energy state");
			
			clientLevel = playerPersonal.getInt("clientLevel");
			allowNicknameEdit = playerPersonal.getBoolean("allowNicknameEdit");
			allowFactionChoice = playerPersonal.getBoolean("allowFactionChoice");
		}
	}
	
	public void update(PlayerEntity entity) {
		team = entity.team;
		ap = entity.ap;
		energy = entity.energy;
		energyState = entity.energyState;
		clientLevel = entity.clientLevel;
		allowNicknameEdit = entity.allowNicknameEdit;
		allowFactionChoice = entity.allowFactionChoice;
	}

	public Utils.Team getTeam() {
		return team;
	}

	public int getAp() {
		return ap;
	}

	public int getEnergy() {
		return energy;
	}
	
	public EnergyState getEnergyState() {
		return energyState;
	}
	
	public int getClientLevel() {
		return clientLevel;
	}

	public boolean isAllowNicknameEdit() {
		return allowNicknameEdit;
	}

	public boolean isAllowFactionChoice() {
		return allowFactionChoice;
	}
}
