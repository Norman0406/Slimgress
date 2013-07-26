package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;

public class PlextSystem extends Plext
{
	public PlextSystem(JSONArray json) throws JSONException
	{
		super(PlextType.SystemBroadcast, json);
	}
}
