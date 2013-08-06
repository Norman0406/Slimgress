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

public class ItemPortalKey extends ItemBase
{
    private String mPortalGuid;
    private String mPortalLocation;
    private String mPortalImageUrl;
    private String mPortalTitle;
    private String mPortalAddress;

    public ItemPortalKey(JSONArray json) throws JSONException
    {
        super(ItemType.PortalKey, json);

        JSONObject item = json.getJSONObject(2);
        JSONObject portalCoupler = item.getJSONObject("portalCoupler");

        mPortalGuid = portalCoupler.getString("portalGuid");
        mPortalLocation = portalCoupler.getString("portalLocation");
        mPortalImageUrl = portalCoupler.getString("portalImageUrl");
        mPortalTitle = portalCoupler.getString("portalTitle");
        mPortalAddress = portalCoupler.getString("portalAddress");
    }

    public static String getNameStatic()
    {
        return "PORTAL_LINK_KEY";
    }

    public String getName()
    {
        return getNameStatic();
    }

    public String getPortalGuid()
    {
        return mPortalGuid;
    }

    public String getPortalLocation()
    {
        return mPortalLocation;
    }

    public String getPortalImageUrl()
    {
        return mPortalImageUrl;
    }

    public String getPortalTitle()
    {
        return mPortalTitle;
    }

    public String getPortalAddress()
    {
        return mPortalAddress;
    }
}
