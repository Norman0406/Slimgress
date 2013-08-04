/***********************************************************************
*
* Slimgress: Ingress API for Android
* Copyright (C) 2013 Norman Link <norman.link@gmx.net>
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
***********************************************************************/

package com.norman0406.slimgress.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameEntityControlField extends GameEntity
{
	public class Vertex
	{
		private String mPortalGuid;
		private Utils.LocationE6 mPortalLocation;
		
		public Vertex(JSONObject json) throws JSONException
		{
			mPortalGuid = json.getString("guid");
			mPortalLocation = new Utils.LocationE6(json.getJSONObject("location"));
		}

		public String getPortalGuid()
		{
			return mPortalGuid;
		}

		public Utils.LocationE6 getPortalLocation()
		{
			return mPortalLocation;
		}
	}
	
	private Vertex mFieldVertexA;
	private Vertex mFieldVertexB;
	private Vertex mFieldVertexC;
	private int mFieldScore;
	private Utils.Team mFieldControllingTeam;
	
	GameEntityControlField(JSONArray json) throws JSONException
	{
		super(GameEntityType.ControlField, json);
		
		JSONObject item = json.getJSONObject(2);
		
		JSONObject capturedRedgion = item.getJSONObject("capturedRegion");
		
		mFieldVertexA = new Vertex(capturedRedgion.getJSONObject("vertexA"));
		mFieldVertexB = new Vertex(capturedRedgion.getJSONObject("vertexB"));
		mFieldVertexC = new Vertex(capturedRedgion.getJSONObject("vertexC"));
		mFieldScore = Integer.parseInt(item.getJSONObject("entityScore").getString("entityScore"));
		mFieldControllingTeam = Utils.getTeam(item.getJSONObject("controllingTeam"));
	}
	
	public Vertex getFieldVertexA()
	{
		return mFieldVertexA;
	}
	
	public Vertex getFieldVertexB()
	{
		return mFieldVertexB;
	}
	
	public Vertex getFieldVertexC()
	{
		return mFieldVertexC;
	}
	
	public int getFieldScore()
	{
		return mFieldScore;
	}
	
	public Utils.Team getFieldControllingTeam()
	{
		return mFieldControllingTeam;
	}
}
