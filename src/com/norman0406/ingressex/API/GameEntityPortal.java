package com.norman0406.ingressex.API;

import java.util.LinkedList;
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

	private Utils.LocationE6 mPortalLocation;
	private Utils.Team mPortalTeam;
	private String mPortalTitle;
	private String mPortalAddress;
	private String mPortalAttribution;
	private String mPortalAttributionLink;
	private String mPortalImageUrl;
	private List<LinkedEdge> mPortalEdges;
	private List<LinkedMod> mPortalMods;
	private List<LinkedResonator> mPortalResonators;
	
	GameEntityPortal(JSONArray json) throws JSONException
	{
		super(GameEntityType.Portal, json);
		
		JSONObject item = json.getJSONObject(2);
		
		mPortalTeam = Utils.getTeam(item.getJSONObject("controllingTeam"));
		mPortalLocation = new Utils.LocationE6(item.getJSONObject("locationE6"));
		
		JSONObject portalV2 = item.getJSONObject("portalV2");
		
		// get edges
		mPortalEdges = new LinkedList<LinkedEdge>();
		JSONArray portalEdges = portalV2.getJSONArray("linkedEdges");
		for (int i = 0; i < portalEdges.length(); i++) {
			JSONObject edge = portalEdges.getJSONObject(i);
			
			LinkedEdge newEdge = new LinkedEdge();
			newEdge.edgeGuid = edge.getString("edgeGuid");
			newEdge.otherPortalGuid = edge.getString("otherPortalGuid");
			newEdge.isOrigin = edge.getBoolean("isOrigin");
			mPortalEdges.add(newEdge);
		}
		
		// get mods
		mPortalMods = new LinkedList<LinkedMod>();
		JSONArray portalMods = portalV2.getJSONArray("linkedModArray");
		for (int i = 0; i < portalMods.length(); i++) {
			JSONObject mod = portalMods.optJSONObject(i);
			
			if (mod != null) {
				LinkedMod newMod = new LinkedMod();
				newMod.installingUser = mod.getString("installingUser");
				newMod.displayName = mod.getString("displayName");
				
				// UNDONE
				
				mPortalMods.add(newMod);
			}
			else {
				// mod == null means the slot is unused
				mPortalMods.add(null);
			}
		}
		
		// get description
		JSONObject descriptiveText = portalV2.getJSONObject("descriptiveText");
		mPortalTitle = descriptiveText.getString("TITLE");
		mPortalAddress = descriptiveText.optString("ADDRESS");
		mPortalAttribution = descriptiveText.optString("ATTRIBUTION");
		mPortalAttributionLink = descriptiveText.optString("ATTRIBUTION_LINK");
		
		// get resonators
		mPortalResonators = new LinkedList<LinkedResonator>();
		JSONObject resonatorArray = item.getJSONObject("resonatorArray");
		JSONArray resonators = resonatorArray.getJSONArray("resonators");
		for (int i = 0; i < resonators.length(); i++) {
			JSONObject resonator = resonators.optJSONObject(i);
			
			if (resonator != null) {
				LinkedResonator newResonator = new LinkedResonator();
				newResonator.level = resonator.getInt("level");
				newResonator.distanceToPortal = resonator.getInt("distanceToPortal");
				newResonator.ownerGuid = resonator.getString("ownerGuid");
				newResonator.energyTotal = resonator.getInt("energyTotal");
				newResonator.slot = resonator.getInt("slot");
				newResonator.id = resonator.getString("id");
				
				mPortalResonators.add(newResonator);
			}
			else {
				// resonator == null means the slot is unused
				mPortalResonators.add(null);
			}
		}
	}
	
	public int getPortalEnergy()
	{
		// TODO: don't recalculate every time
		int energy = 0;
		for (LinkedResonator resonator : mPortalResonators) {
			if (resonator != null)
				energy += resonator.energyTotal;
		}		
		return energy;
	}
	
	public int getPortalLevel()
	{
		// TODO: don't recalculate every time
		int level = 0;
		int resonatorCount = 0;
		for (LinkedResonator resonator : mPortalResonators) {
			if (resonator != null) {
				level += resonator.level;
				resonatorCount++;
			}
		}
		
		if (resonatorCount == 0)
			return 0;
		
		return level / resonatorCount;
	}

	public Utils.LocationE6 getPortalLocation()
	{
		return mPortalLocation;
	}
	
	public Utils.Team getPortalTeam()
	{
		return mPortalTeam;
	}
	
	public String getPortalTitle()
	{
		return mPortalTitle;
	}
	
	public String getPortalAddress()
	{
		return mPortalAddress;
	}
	
	public String getPortalAttribution()
	{
		return mPortalAttribution;
	}
	
	public String getPortalAttributionLink()
	{
		return mPortalAttributionLink;
	}
	
	public String getPortalImageUrl()
	{
		return mPortalImageUrl;
	}
	
	public List<LinkedEdge> getPortalEdges()
	{
		return mPortalEdges;
	}
	
	public List<LinkedMod> getPortalMods()
	{
		return mPortalMods;
	}
	
	public List<LinkedResonator> getPortalResonators()
	{
		return mPortalResonators;
	}
}
