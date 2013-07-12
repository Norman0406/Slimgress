package com.norman0406.ingressex.API;

import org.json.JSONException;
import org.json.JSONObject;

public class GameEntityControlField extends GameEntity {
	
	public class Vertex {
		private String portalGuid;
		private Utils.LocationE6 portalLocation;
		
		public Vertex(JSONObject json) throws JSONException {
			portalGuid = json.getString("guid");
			portalLocation = new Utils.LocationE6(json.getJSONObject("location"));
		}

		public String getPortalGuid() {
			return portalGuid;
		}

		public Utils.LocationE6 getPortalLocation() {
			return portalLocation;
		}
	}
	
	private Vertex fieldVertexA;
	private Vertex fieldVertexB;
	private Vertex fieldVertexC;
	private int fieldScore;
	private Utils.Team fieldControllingTeam;
	
	GameEntityControlField(String guid, String timestamp) {
		super(guid, timestamp);
	}

	@Override
	protected void initByJSON(JSONObject json) throws JSONException {
		super.initByJSON(json);
		
		JSONObject capturedRedgion = json.getJSONObject("capturedRegion");
		String team = json.getJSONObject("controllingTeam").getString("team");
		
		fieldVertexA = new Vertex(capturedRedgion.getJSONObject("vertexA"));
		fieldVertexB = new Vertex(capturedRedgion.getJSONObject("vertexB"));
		fieldVertexC = new Vertex(capturedRedgion.getJSONObject("vertexC"));
		fieldScore = Integer.parseInt(json.getJSONObject("entityScore").getString("entityScore"));
		
		if (team.equals("RESISTANCE"))
			fieldControllingTeam = Utils.Team.Resistance;
		else if (team.equals("ALIENS"))
			fieldControllingTeam = Utils.Team.Enlightened;
		else
			System.out.println("unknown team string");	
	}
	
	public Vertex getFieldVertexA() {
		return fieldVertexA;
	}
	
	public Vertex getFieldVertexB() {
		return fieldVertexB;
	}
	
	public Vertex getFieldVertexC() {
		return fieldVertexC;
	}
	
	public int getFieldScore() {
		return fieldScore;
	}
	
	public Utils.Team getFieldControllingTeam() {
		return fieldControllingTeam;
	}
}
