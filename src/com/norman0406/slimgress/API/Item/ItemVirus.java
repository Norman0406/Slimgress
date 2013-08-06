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

public class ItemVirus extends ItemBase
{
    public enum VirusType
    {
        Jarvis,
        Ada
    }

    private VirusType mVirusType;

    public ItemVirus(JSONArray json) throws JSONException
    {
        super(ItemType.Virus, json);

        JSONObject item = json.getJSONObject(2);
        JSONObject flipCard = item.getJSONObject("flipCard");

        if (flipCard.getString("flipCardType").equals("JARVIS"))
            mVirusType = VirusType.Jarvis;
        else if (flipCard.getString("flipCardType").equals("ADA"))
            mVirusType = VirusType.Ada;
        else
            System.out.println("unknown virus type");
    }

    public static String getNameStatic()
    {
        return "FLIP_CARD";
    }

    public String getName()
    {
        return getNameStatic();
    }

    public VirusType getVirusType()
    {
        return mVirusType;
    }
}
