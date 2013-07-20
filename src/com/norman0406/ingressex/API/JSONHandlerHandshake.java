package com.norman0406.ingressex.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONHandlerHandshake implements JSONHandler {
	
	enum VersionMatch {
		Current,
		OldIncompatible
	}
	
	private VersionMatch versionMatch;
	private String serverVersion;
	private String nickname;
	private boolean canPlay;	
	private String xsrfToken;
	private String syncTimestamp;
	private Agent agent = null;
	
	@Override
	public void handleJSON(JSONObject json) throws JSONException {
		JSONObject result = json.getJSONObject("result");
		
		String versionMatch = result.getString("versionMatch");
		if (versionMatch.equals("CURRENT"))
			this.versionMatch = VersionMatch.Current;
		else if (versionMatch.equals("OLD_INCOMPATIBLE"))
			this.versionMatch = VersionMatch.OldIncompatible;
		else
			throw new RuntimeException("unknown version match");
		
		serverVersion = result.getString("serverVersion");
		
		// get player entity
		nickname = result.getString("nickname");
		JSONArray playerEntity = result.optJSONArray("playerEntity");
		if (playerEntity != null)
			agent = new Agent(playerEntity, nickname);
		
		xsrfToken = result.optString("xsrfToken");
		canPlay = result.getBoolean("canPlay");
	}
	
	public boolean isValid() {
		return agent != null &&
				xsrfToken.length() > 0 &&
				versionMatch == VersionMatch.Current &&
				canPlay == true;
	}
	
	public VersionMatch getVersionMatch() {
		return versionMatch;
	}
	
	public String getServerVersion() {
		return serverVersion;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public boolean getCanPlay() {
		return canPlay;
	}
	
	public String getXSRFToken() {
		return xsrfToken;
	}
	
	public String getSyncTimestamp() {
		return syncTimestamp;
	}
	
	public Agent getAgent() {
		return agent;
	}
}