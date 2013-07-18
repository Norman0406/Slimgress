package com.norman0406.ingressex.API;

import org.json.JSONArray;
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
	
	GameEntityControlField(JSONArray json) throws JSONException {
		super(json);
		
		JSONObject item = json.getJSONObject(2);
		
		JSONObject capturedRedgion = item.getJSONObject("capturedRegion");
		
		fieldVertexA = new Vertex(capturedRedgion.getJSONObject("vertexA"));
		fieldVertexB = new Vertex(capturedRedgion.getJSONObject("vertexB"));
		fieldVertexC = new Vertex(capturedRedgion.getJSONObject("vertexC"));
		fieldScore = Integer.parseInt(item.getJSONObject("entityScore").getString("entityScore"));
		fieldControllingTeam = Utils.getTeam(item.getJSONObject("controllingTeam"));
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
