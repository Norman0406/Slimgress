package com.norman0406.ingressex.API;

import java.util.List;

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
		
	GameEntityPortal(String guid, String timestamp) {
		super(guid, timestamp);
	}

	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		super.initByJSON(json);
		
		portalLocation = new Utils.LocationE6(json.getJSONObject("location"));
		
		JSONObject portalV2 = json.getJSONObject("portalV2");
		
	}
}
