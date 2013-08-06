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

package com.norman0406.slimgress.API.GameEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.norman0406.slimgress.API.Common.EntityBase;

public abstract class GameEntityBase extends EntityBase
{
    public enum GameEntityType
    {
        ControlField,
        Link,
        Portal,
        Item
    }

    private enum OwnerType
    {
        Creator,
        Conqueror
    }

    GameEntityType mGameEntityType;
    OwnerType mOwnerType;	// created or captured
    String mOwnerGuid;
    String mOwnerTimestamp;

    public static GameEntityBase createByJSON(JSONArray json) throws JSONException
    {
        if (json.length() != 3) {
            Log.e("GameEntityBase", "invalid array size");
            return null;
        }

        JSONObject item = json.getJSONObject(2);

        // create entity
        GameEntityBase newEntity = null;
        if (item.has("edge"))
            newEntity = new GameEntityLink(json);
        else if (item.has("capturedRegion"))
            newEntity = new GameEntityControlField(json);
        else if (item.has("portalV2"))
            newEntity = new GameEntityPortal(json);
        else if (item.has("resource") || item.has("resourceWithLevels") || item.has("modResource"))
            newEntity = new GameEntityItem(json);
        else {
            // unknown game entity
            Log.w("GameEntityBase", "unknown game entity");
        }

        return newEntity;
    }

    GameEntityBase(GameEntityType type, JSONArray json) throws JSONException
    {
        super(json);
        mGameEntityType = type;

        JSONObject item = json.getJSONObject(2);

        if (item.has("creator")) {
            JSONObject creator = item.getJSONObject("creator");
            mOwnerGuid = creator.getString("creatorGuid");
            mOwnerTimestamp = creator.getString("creationTime");
            mOwnerType = OwnerType.Creator;
        }
        else if (item.has("captured")) {
            JSONObject creator = item.getJSONObject("captured");
            mOwnerGuid = creator.getString("capturingPlayerId");
            mOwnerTimestamp = creator.getString("capturedTime");
            mOwnerType = OwnerType.Conqueror;
        }
        else {
            // UNDONE: GameEntityItem does not have owner information
            //throw new RuntimeException("no owner information available");
        }
    }

    public GameEntityType getGameEntityType()
    {
        return mGameEntityType;
    }

    public OwnerType getOwnerType()
    {
        return mOwnerType;
    }

    public String getOwnerGuid()
    {
        return mOwnerGuid;
    }

    public String getOwnerTimestamp()
    {
        return mOwnerTimestamp;
    }
}
