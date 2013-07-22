package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;

public abstract class Entity
{
	private String mEntityGuid;
	private String mEntityTimestamp;
	
	public Entity(JSONArray json) throws JSONException
	{
		if (json.length() != 3)
			throw new JSONException("invalid array size");
		
		mEntityGuid = json.getString(0);
		mEntityTimestamp = json.getString(1);
	}

	public String getEntityGuid()
	{
		return mEntityGuid;
	}

	public String getEntityTimestamp()
	{
		return mEntityTimestamp;
	}
}
