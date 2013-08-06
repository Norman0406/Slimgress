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

package com.norman0406.slimgress.API.Plext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public abstract class Markup
{
    public enum MarkupType
    {
        Secure,
        Sender,
        Player,
        ATPlayer,
        Portal,
        Text
    }

    private MarkupType mType;
    private String mPlain;

    public static Markup createByJSON(JSONArray json) throws JSONException
    {
        if (json.length() != 2) {
            Log.e("Markup", "invalid array size");
            return null;
        }

        JSONObject markupObj = json.getJSONObject(1);

        Markup newMarkup = null;

        String markupString = json.getString(0);
        if (markupString.equals("SECURE"))
            newMarkup = new MarkupSecure(markupObj);
        else if (markupString.equals("SENDER"))
            newMarkup = new MarkupSender(markupObj);
        else if (markupString.equals("PLAYER"))
            newMarkup = new MarkupPlayer(markupObj);
        else if (markupString.equals("AT_PLAYER"))
            newMarkup = new MarkupATPlayer(markupObj);
        else if (markupString.equals("PORTAL"))
            newMarkup = new MarkupPortal(markupObj);
        else if (markupString.equals("TEXT"))
            newMarkup = new MarkupText(markupObj);
        else
            Log.w("Markup", "unknown markup type: " + markupString);

        return newMarkup;
    }

    public Markup(MarkupType type, JSONObject json) throws JSONException
    {
        mType = type;
        mPlain = json.getString("plain");
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
