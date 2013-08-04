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

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Plext extends Entity
{
	public enum PlextType
	{
		PlayerGenerated,
		SystemBroadcast
	}
	
	public enum MarkupType {
		Secure,
		Sender,
		Player,
		ATPlayer,
		Text
	}
	
	public abstract class Markup
	{
		private MarkupType mType;
		private String mPlain;
		
		public Markup(MarkupType type, String plain)
		{
			mType = type;
			mPlain = plain;
		}
		
		public String getPlain()
		{
			return mPlain;
		}
		
		public MarkupType getType()
		{
			return mType;
		}
	}
	
	public class MarkupSecure extends Markup
	{
		public MarkupSecure(String plain)
		{
			super(MarkupType.Secure, plain);
		}
	}
	
	public class MarkupSender extends Markup
	{
		private String mGUID;
		private Utils.Team mTeam;
		
		public MarkupSender(String plain, String guid, Utils.Team team)
		{
			super(MarkupType.Sender, plain);
			mGUID = guid;
			mTeam = team;
		}
		
		public String getGUID()
		{
			return mGUID;
		}
		
		public Utils.Team getTeam()
		{
			return mTeam;
		}
	}

	public class MarkupPlayer extends Markup
	{
		private String mGUID;
		private Utils.Team mTeam;
		
		public MarkupPlayer(String plain, String guid, Utils.Team team)
		{
			super(MarkupType.Player, plain);
			mGUID = guid;
			mTeam = team;
		}
		
		public String getGUID()
		{
			return mGUID;
		}
		
		public Utils.Team getTeam()
		{
			return mTeam;
		}
	}

	public class MarkupATPlayer extends Markup
	{
		private String mGUID;
		private Utils.Team mTeam;
		
		public MarkupATPlayer(String plain, String guid, Utils.Team team)
		{
			super(MarkupType.ATPlayer, plain);
			mGUID = guid;
			mTeam = team;
		}
		
		public String getGUID()
		{
			return mGUID;
		}
		
		public Utils.Team getTeam()
		{
			return mTeam;
		}
	}

	public class MarkupPortal extends Markup
	{
		private String mGUID;
		private Utils.Team mTeam;
		private Utils.LocationE6 mLocation;
		private String mAddress;
		private String mName;
		
		public MarkupPortal(String plain, String guid, Utils.Team team, Utils.LocationE6 location,
				String address, String name)
		{
			super(MarkupType.Player, plain);
			mGUID = guid;
			mTeam = team;
			mLocation = location;
			mAddress = address;
			mName = name;
		}
		
		public String getGUID()
		{
			return mGUID;
		}
		
		public Utils.Team getTeam()
		{
			return mTeam;
		}
		
		public Utils.LocationE6 getLocation()
		{
			return mLocation;
		}
		
		public String getAddress()
		{
			return mAddress;
		}
		
		public String getName()
		{
			return mName;
		}
	}
	
	private class MarkupText extends Markup
	{
		public MarkupText(String plain)
		{
			super(MarkupType.Text, plain);
		}
	}
	
	private PlextType mPlextType;
	private String mText;
	private List<Markup> mMarkups;
	
	protected Plext(PlextType type, JSONArray json) throws JSONException
	{
		super(json);
		mPlextType = type;
		
		JSONObject item = json.getJSONObject(2);
		
		JSONObject plext = item.getJSONObject("plext");
		JSONArray markup = plext.getJSONArray("markup");

		mText = plext.getString("text");		
		
		mMarkups = new LinkedList<Markup>();
		for (int i = 0; i < markup.length(); i++) {
			JSONArray markupItem = markup.getJSONArray(i);
			
			String markupString = markupItem.getString(0);
			JSONObject markupObj = markupItem.getJSONObject(1);
			
			if (markupString.equals("SECURE")) {
				mMarkups.add(new MarkupSecure(markupObj.getString("plain")));
			}
			else if (markupString.equals("SENDER")) {
				mMarkups.add(new MarkupSender(markupObj.getString("plain"),
						markupObj.getString("guid"),
						Utils.getTeam(markupObj.getString("team"))));
			}
			else if (markupString.equals("PLAYER")) {
				mMarkups.add(new MarkupPlayer(markupObj.getString("plain"),
						markupObj.getString("guid"),
						Utils.getTeam(markupObj.getString("team"))));
			}
			else if (markupString.equals("AT_PLAYER")) {
				mMarkups.add(new MarkupATPlayer(markupObj.getString("plain"),
						markupObj.getString("guid"),
						Utils.getTeam(markupObj.getString("team"))));
			}
			else if (markupString.equals("PORTAL")) {
				mMarkups.add(new MarkupPortal(markupObj.getString("plain"),
						markupObj.getString("guid"),
						Utils.getTeam(markupObj.getString("team")),
						new Utils.LocationE6(markupObj.getInt("latE6"), markupObj.getInt("lngE6")),
						markupObj.getString("address"),
						markupObj.getString("name")));
			}
			else if (markupString.equals("TEXT")) {
				mMarkups.add(new MarkupText(markupObj.getString("plain")));
			}
			else {
				System.out.println("unknown markup type: " + markupString);
			}
		}			
	}
	
	public static Plext createPlext(JSONArray json) throws JSONException
	{
		if (json.length() != 3)
			throw new JSONException("invalid array size");
		
		JSONObject item = json.getJSONObject(2);

		JSONObject plext = item.getJSONObject("plext");
		
		Plext newPlext = null;
		String plextType = plext.getString("plextType");
		if (plextType.equals("PLAYER_GENERATED"))
			newPlext = new PlextPlayer(json);
		else if (plextType.equals("SYSTEM_BROADCAST"))
			newPlext = new PlextSystem(json);
		else
			throw new RuntimeException("unknown plext type: " + plextType);
		
		return newPlext;
	}

	public PlextType getPlextType()
	{
		return mPlextType;
	}

	public String getText()
	{
		return mText;
	}
	
	public List<Markup> getMarkups()
	{
		return mMarkups;
	}	
}
