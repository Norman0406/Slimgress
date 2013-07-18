package com.norman0406.ingressex.API;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameEntityPortal extends GameEntity {
	
	private class LinkedEdge {
		private String edgeGuid;
		private String otherPortalGuid;
		private boolean isOrigin;
	}
	
	private class LinkedMod {
		// UNDONE: vielleicht mit ItemMod koppeln?
		
		private String installingUser;
		private String displayName;
	}
	
	private class LinkedResonator {
		private int distanceToPortal;
		private int energyTotal;
		private int slot;
		private String id;
		private String ownerGuid;
		private int level;
	}

	Utils.LocationE6 portalLocation;
	private String portalTitle;
	private String portalAttribution;
	private String portalAttributionLink;
	private String portalAddress;
	private List<LinkedEdge> portalEdges;
	private List<LinkedMod> portalMods;
	private List<LinkedResonator> portalResonators;
	private Utils.Team portalTeam;
	private String portalImageUrl;
		
	GameEntityPortal(JSONArray json) throws JSONException {
		super(json);
		
		JSONObject item = json.getJSONObject(2);
		
		portalTeam = Utils.getTeam(item.getJSONObject("controllingTeam"));
		portalLocation = new Utils.LocationE6(item.getJSONObject("location"));
		
		JSONObject portalV2 = item.getJSONObject("portalV2");
		JSONArray portalEdges = portalV2.getJSONArray("linkedEdges");
		JSONArray portalMods = portalV2.getJSONArray("linkedModArray");
		JSONObject descriptiveText = portalV2.getJSONObject("descriptiveText");
		
	}
}
