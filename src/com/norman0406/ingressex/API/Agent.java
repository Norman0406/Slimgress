package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;

public class Agent extends PlayerEntity
{
	private final String mNickname;
	
	private static int[] mLevels = {
			0,
			10000,
			30000,
			70000,
			150000,
			300000,
			600000,
			1200000
	};
	
	public Agent(JSONArray json, String nickname) throws JSONException
	{
		super(json);
		mNickname = nickname;
	}
	
	public String getNickname()
	{
		return mNickname;
	}
	
	public int getLevel()
	{
		// TODO: more efficient?
		
		for (int i = mLevels.length - 1; i >= 0; i--) {
			if (this.getAp() >= mLevels[i])
				return i + 1;
		}

		throw new IndexOutOfBoundsException("agent level could not be retrieved");
	}
	
	public int getEnergyMax()
	{
		return 2000 + (getLevel() * 1000);
	}
}
