package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Handshake
{
	public interface Callback
	{
		public void handle(Handshake handshake);
	}
    
    public enum PregameStatus
    {
        ClientMustUpgrade,
        NoActionsRequired
    }
	
	private PregameStatus mPregameStatus;
	private String mServerVersion;
	private String mNickname;
	private String mXSRFToken;
	private Agent mAgent = null;
	
	public Handshake(JSONObject json) throws JSONException
	{
		JSONObject result = json.getJSONObject("result");
		
		String pregameStatus = result.getJSONObject("pregameStatus").getString("action");
		if (pregameStatus.equals("CLIENT_MUST_UPGRADE"))
		        mPregameStatus = PregameStatus.ClientMustUpgrade;
		else if (pregameStatus.equals("NO_ACTIONS_REQUIRED"))
            mPregameStatus = PregameStatus.NoActionsRequired;
		else
		    throw new RuntimeException("unknown pregame status " + pregameStatus);
				
		mServerVersion = result.getString("serverVersion");
		
		// get player entity
		mNickname = result.optString("nickname");
		JSONArray playerEntity = result.optJSONArray("playerEntity");
		if (playerEntity != null)
			mAgent = new Agent(playerEntity, mNickname);
		
		mXSRFToken = result.optString("xsrfToken");
	}

	public boolean isValid()
	{
		return mAgent != null &&
		        mXSRFToken.length() > 0 &&
				mPregameStatus == PregameStatus.NoActionsRequired;
	}
	
	public PregameStatus getPregameStatus()
	{
	    return mPregameStatus;
	}
		
	public String getServerVersion()
	{
		return mServerVersion;
	}
	
	public String getNickname()
	{
		return mNickname;
	}
	
	public String getXSRFToken()
	{
		return mXSRFToken;
	}
	
	public Agent getAgent()
	{
		return mAgent;
	}
}
