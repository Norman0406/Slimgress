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

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.norman0406.slimgress.API.Common.EntityBase;

public class PlextBase extends EntityBase
{
    public enum PlextType
    {
        PlayerGenerated,
        SystemBroadcast
    }

    private PlextType mPlextType;
    private String mText;
    private List<Markup> mMarkups;

    public static PlextBase createByJSON(JSONArray json) throws JSONException
    {
        if (json.length() != 3) {
            Log.e("PlextBase", "invalid array size");
            return null;
        }

        JSONObject item = json.getJSONObject(2);
        JSONObject plext = item.getJSONObject("plext");

        PlextBase newPlext = null;

        String plextType = plext.getString("plextType");
        if (plextType.equals("PLAYER_GENERATED"))
            newPlext = new PlextPlayer(json);
        else if (plextType.equals("SYSTEM_BROADCAST"))
            newPlext = new PlextSystem(json);
        else
            Log.w("PlextBase", "unknown plext type: " + plextType);

        return newPlext;
    }

    protected PlextBase(PlextType type, JSONArray json) throws JSONException
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

            Markup newMarkup = Markup.createByJSON(markupItem);
            if (newMarkup != null)
                mMarkups.add(newMarkup);
        }
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
