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

public class ItemModTurret extends ItemMod
{
    private int mAttackFrequency;
    private int mHitBonus;

    public ItemModTurret(JSONArray json) throws JSONException
    {
        super(ItemBase.ItemType.ModTurret, json);

        JSONObject item = json.getJSONObject(2);
        JSONObject modResource = item.getJSONObject("modResource");
        JSONObject stats = modResource.getJSONObject("stats");
        mAttackFrequency = Integer.parseInt(stats.getString("ATTACK_FREQUENCY"));
        mHitBonus = Integer.parseInt(stats.getString("HIT_BONUS"));
    }

    public static String getNameStatic()
    {
        return "TURRET";
    }

    public String getName()
    {
        return getNameStatic();
    }

    public int getAttackFrequency()
    {
        return mAttackFrequency;
    }

    public int getHitBonus()
    {
        return mHitBonus;
    }
}
