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

package com.norman0406.slimgress.API.Interface;

import org.json.JSONException;
import org.json.JSONObject;

public class PlayerDamage
{
    private int mAmount;
    private String mAttackerGuid;
    private String mWeaponSerializationTag;

    public PlayerDamage(JSONObject json) throws NumberFormatException, JSONException
    {
        mAmount = Integer.parseInt(json.getString("damageAmount"));
        mAttackerGuid = json.getString("attackerGuid");
        mWeaponSerializationTag = json.getString("weaponSerializationTag");
    }

    public int getAmount()
    {
        return mAmount;
    }

    public String getAttackerGuid()
    {
        return mAttackerGuid;
    }

    public String getWeaponSerializationTag()
    {
        return mWeaponSerializationTag;
    }
}
