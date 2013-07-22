package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayerEntity extends Entity
{
	public enum EnergyState
	{
		OK,
		Depleted
	}
	
	private Utils.Team mTeam;
	private int mAP;
	private int mEnergy;
	private EnergyState mEnergyState;
	private int mClientLevel;
	private boolean mAllowNicknameEdit;
	private boolean mAllowFactionChoice;	
	
	public PlayerEntity(JSONArray json) throws JSONException
	{
		super(json);

		JSONObject playerEntity = json.getJSONObject(2);
		mTeam = Utils.getTeam(playerEntity.getJSONObject("controllingTeam"));
		
		JSONObject playerPersonal = playerEntity.optJSONObject("playerPersonal");
		if (playerPersonal != null) {
			mAP = Integer.parseInt(playerPersonal.getString("ap"));
			mEnergy = playerPersonal.getInt("energy");
			
			String energyStateString = playerPersonal.getString("energyState");
			if (energyStateString.equals("XM_OK"))
				mEnergyState = EnergyState.OK;
			else if (energyStateString.equals("XM_DEPLETED"))
				mEnergyState = EnergyState.Depleted;
			else
				throw new RuntimeException("unknown energy state");
			
			mClientLevel = playerPersonal.getInt("clientLevel");
			mAllowNicknameEdit = playerPersonal.getBoolean("allowNicknameEdit");
			mAllowFactionChoice = playerPersonal.getBoolean("allowFactionChoice");
		}
	}
	
	public void update(PlayerEntity entity)
	{
		mTeam = entity.mTeam;
		mAP = entity.mAP;
		mEnergy = entity.mEnergy;
		mEnergyState = entity.mEnergyState;
		mClientLevel = entity.mClientLevel;
		mAllowNicknameEdit = entity.mAllowNicknameEdit;
		mAllowFactionChoice = entity.mAllowFactionChoice;
	}

	public Utils.Team getTeam()
	{
		return mTeam;
	}

	public int getAp()
	{
		return mAP;
	}

	public int getEnergy()
	{
		return mEnergy;
	}
	
	public EnergyState getEnergyState()
	{
		return mEnergyState;
	}
	
	public int getClientLevel()
	{
		return mClientLevel;
	}

	public boolean isAllowNicknameEdit()
	{
		return mAllowNicknameEdit;
	}

	public boolean isAllowFactionChoice()
	{
		return mAllowFactionChoice;
	}
}
