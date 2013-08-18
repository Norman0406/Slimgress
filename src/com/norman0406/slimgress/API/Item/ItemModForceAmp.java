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

package com.norman0406.slimgress.API.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemModForceAmp extends ItemMod
{
    private int mForceAmplifier;

    public ItemModForceAmp(JSONArray json) throws JSONException
    {
        super(ItemBase.ItemType.ModForceAmp, json);

        JSONObject item = json.getJSONObject(2);
        JSONObject modResource = item.getJSONObject("modResource");
        JSONObject stats = modResource.getJSONObject("stats");
        mForceAmplifier = Integer.parseInt(stats.getString("FORCE_AMPLIFIER"));
    }

    public static String getNameStatic()
    {
        return "FORCE_AMP";
    }

    public String getName()
    {
        return getNameStatic();
    }

    public int getForceAmplifier()
    {
        return mForceAmplifier;
    }
}
