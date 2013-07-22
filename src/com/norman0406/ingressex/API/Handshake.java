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
	
	enum VersionMatch {
		Current,
		OldIncompatible
	}
	
	private VersionMatch mVersionMatch;
	private String mServerVersion;
	private String mNickname;
	private boolean mCanPlay;	
	private String mXSRFToken;
	private String mSyncTimestamp;
	private Agent mAgent = null;
	
	public Handshake(JSONObject json) throws JSONException
	{
		JSONObject result = json.getJSONObject("result");
		
		String versionMatch = result.getString("versionMatch");
		if (versionMatch.equals("CURRENT"))
			mVersionMatch = VersionMatch.Current;
		else if (versionMatch.equals("OLD_INCOMPATIBLE"))
			mVersionMatch = VersionMatch.OldIncompatible;
		else
			throw new RuntimeException("unknown version match");
		
		mServerVersion = result.getString("serverVersion");
		
		// get player entity
		mNickname = result.getString("nickname");
		JSONArray playerEntity = result.optJSONArray("playerEntity");
		if (playerEntity != null)
			mAgent = new Agent(playerEntity, mNickname);
		
		mXSRFToken = result.optString("xsrfToken");
		mCanPlay = result.getBoolean("canPlay");
	}

	public boolean isValid()
	{
		return mAgent != null &&
				mXSRFToken.length() > 0 &&
				mVersionMatch == VersionMatch.Current &&
				mCanPlay == true;
	}
	
	public VersionMatch getVersionMatch()
	{
		return mVersionMatch;
	}
	
	public String getServerVersion()
	{
		return mServerVersion;
	}
	
	public String getNickname()
	{
		return mNickname;
	}
	
	public boolean getCanPlay()
	{
		return mCanPlay;
	}
	
	public String getXSRFToken()
	{
		return mXSRFToken;
	}
	
	public String getSyncTimestamp()
	{
		return mSyncTimestamp;
	}
	
	public Agent getAgent()
	{
		return mAgent;
	}
}
