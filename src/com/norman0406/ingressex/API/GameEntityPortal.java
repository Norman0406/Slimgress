package com.norman0406.ingressex.API;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameEntityPortal extends GameEntity
{
	public class LinkedEdge
	{
		public String edgeGuid;
		public String otherPortalGuid;
		public boolean isOrigin;
	}
	
	public class LinkedMod
	{
		// UNDONE: vielleicht mit ItemMod koppeln?
		
		public String installingUser;
		public String displayName;
	}
	
	public class LinkedResonator
	{
		public int distanceToPortal;
		public int energyTotal;
		public int slot;
		public String id;
		public String ownerGuid;
		public int level;
	}

	Utils.LocationE6 portalLocation;
	private Utils.Team portalTeam;
	private String portalTitle;
	private String portalAddress;
	private String portalAttribution;
	private String portalAttributionLink;
	private String portalImageUrl;
	private List<LinkedEdge> portalEdges;
	private List<LinkedMod> portalMods;
	private List<LinkedResonator> portalResonators;
	
	GameEntityPortal(JSONArray json) throws JSONException
	{
		super(json);
		
		JSONObject item = json.getJSONObject(2);
		
		portalTeam = Utils.getTeam(item.getJSONObject("controllingTeam"));
		portalLocation = new Utils.LocationE6(item.getJSONObject("location"));
		
		JSONObject portalV2 = item.getJSONObject("portalV2");
		
		// get edges
		JSONArray portalEdges = portalV2.getJSONArray("linkedEdges");
		for (int i = 0; i < portalEdges.length(); i++) {
			JSONObject edge = portalEdges.getJSONObject(i);
			
			LinkedEdge newEdge = new LinkedEdge();
			newEdge.edgeGuid = edge.getString("edgeGuid");
			newEdge.otherPortalGuid = edge.getString("otherPortalGuid");
			newEdge.isOrigin = edge.getBoolean("isOrigin");
			this.portalEdges.add(newEdge);
		}
		
		// get mods
		JSONArray portalMods = portalV2.getJSONArray("linkedModArray");
		for (int i = 0; i < portalMods.length(); i++) {
			JSONObject mod = portalMods.getJSONObject(i);
			
			LinkedMod newMod = new LinkedMod();
			newMod.installingUser = mod.getString("installingUser");
			newMod.displayName = mod.getString("displayName");
			
			// UNDONE
			
			this.portalMods.add(newMod);
		}
		
		// get description
		JSONObject descriptiveText = portalV2.getJSONObject("descriptiveText");
		portalTitle = descriptiveText.getString("TITLE");
		portalAddress = descriptiveText.getString("ADDRESS");
		portalAttribution = descriptiveText.optString("ATTRIBUTION");
		portalAttributionLink = descriptiveText.optString("ATTRIBUTION_LINK");
		
		// get resonators
		JSONObject resonatorArray = item.getJSONObject("resonatorArray");
		JSONArray resonators = resonatorArray.getJSONArray("resonators");
		for (int i = 0; i < resonators.length(); i++) {
			JSONObject resonator = resonators.getJSONObject(i);
			
			LinkedResonator newResonator = new LinkedResonator();
			newResonator.level = resonator.getInt("level");
			newResonator.distanceToPortal = resonator.getInt("distanceToPortal");
			newResonator.ownerGuid = resonator.getString("ownerGuid");
			newResonator.energyTotal = resonator.getInt("energyTotal");
			newResonator.slot = resonator.getInt("slot");
			newResonator.id = resonator.getString("id");
			
			this.portalResonators.add(newResonator);			
		}
	}

	Utils.LocationE6 getPortalLocation()
	{
		return portalLocation;
	}
	
	public Utils.Team getPortalTeam()
	{
		return portalTeam;
	}
	
	public String getPortalTitle()
	{
		return portalTitle;
	}
	
	public String getPortalAddress()
	{
		return portalAddress;
	}
	
	public String getPortalAttribution()
	{
		return portalAttribution;
	}
	
	public String getPortalAttributionLink()
	{
		return portalAttributionLink;
	}
	
	public String getPortalImageUrl()
	{
		return portalImageUrl;
	}
	
	public List<LinkedEdge> getPortalEdges()
	{
		return portalEdges;
	}
	
	public List<LinkedMod> getPortalMods()
	{
		return portalMods;
	}
	
	public List<LinkedResonator> getPortalResonators()
	{
		return portalResonators;
	}
}
