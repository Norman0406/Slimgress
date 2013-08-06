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

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.norman0406.slimgress.API.Common.Location;
import com.norman0406.slimgress.API.Common.Team;

public class GameEntityPortal extends GameEntityBase
{
    public class LinkedEdge
    {
        public String edgeGuid;
        public String otherPortalGuid;
        public boolean isOrigin;
    }

    public class LinkedMod
    {
        // TODO: UNDONE: link with ItemMod?

        public String installingUser;
        public String displayName;
    }

    public class LinkedResonator
    {
        public int distanceToPortal;
        public int energyTotal;
        public int slot;
        public String id;
        public String ownerGuid;
        public int level;
    }

    private Location mPortalLocation;
    private Team mPortalTeam;
    private String mPortalTitle;
    private String mPortalAddress;
    private String mPortalAttribution;
    private String mPortalAttributionLink;
    private String mPortalImageUrl;
    private List<LinkedEdge> mPortalEdges;
    private List<LinkedMod> mPortalMods;
    private List<LinkedResonator> mPortalResonators;

    GameEntityPortal(JSONArray json) throws JSONException
    {
        super(GameEntityType.Portal, json);

        JSONObject item = json.getJSONObject(2);

        mPortalTeam = new Team(item.getJSONObject("controllingTeam"));
        mPortalLocation = new Location(item.getJSONObject("locationE6"));

        JSONObject imageUrl = item.optJSONObject("imageByUrl");
        if (imageUrl != null)
            mPortalImageUrl = imageUrl.getString("imageUrl");

        JSONObject portalV2 = item.getJSONObject("portalV2");

        // get edges
        mPortalEdges = new LinkedList<LinkedEdge>();
        JSONArray portalEdges = portalV2.getJSONArray("linkedEdges");
        for (int i = 0; i < portalEdges.length(); i++) {
            JSONObject edge = portalEdges.getJSONObject(i);

            LinkedEdge newEdge = new LinkedEdge();
            newEdge.edgeGuid = edge.getString("edgeGuid");
            newEdge.otherPortalGuid = edge.getString("otherPortalGuid");
            newEdge.isOrigin = edge.getBoolean("isOrigin");
            mPortalEdges.add(newEdge);
        }

        // get mods
        mPortalMods = new LinkedList<LinkedMod>();
        JSONArray portalMods = portalV2.getJSONArray("linkedModArray");
        for (int i = 0; i < portalMods.length(); i++) {
            JSONObject mod = portalMods.optJSONObject(i);

            if (mod != null) {
                LinkedMod newMod = new LinkedMod();
                newMod.installingUser = mod.getString("installingUser");
                newMod.displayName = mod.getString("displayName");

                // TODO: UNDONE

                mPortalMods.add(newMod);
            }
            else {
                // mod == null means the slot is unused
                mPortalMods.add(null);
            }
        }

        // get description
        JSONObject descriptiveText = portalV2.getJSONObject("descriptiveText");
        mPortalTitle = descriptiveText.getString("TITLE");
        mPortalAddress = descriptiveText.optString("ADDRESS");
        mPortalAttribution = descriptiveText.optString("ATTRIBUTION");
        mPortalAttributionLink = descriptiveText.optString("ATTRIBUTION_LINK");

        // get resonators
        mPortalResonators = new LinkedList<LinkedResonator>();
        JSONObject resonatorArray = item.getJSONObject("resonatorArray");
        JSONArray resonators = resonatorArray.getJSONArray("resonators");
        for (int i = 0; i < resonators.length(); i++) {
            JSONObject resonator = resonators.optJSONObject(i);

            if (resonator != null) {
                LinkedResonator newResonator = new LinkedResonator();
                newResonator.level = resonator.getInt("level");
                newResonator.distanceToPortal = resonator.getInt("distanceToPortal");
                newResonator.ownerGuid = resonator.getString("ownerGuid");
                newResonator.energyTotal = resonator.getInt("energyTotal");
                newResonator.slot = resonator.getInt("slot");
                newResonator.id = resonator.getString("id");

                mPortalResonators.add(newResonator);
            }
            else {
                // resonator == null means the slot is unused
                mPortalResonators.add(null);
            }
        }
    }

    public int getPortalEnergy()
    {
        // TODO: don't recalculate every time
        int energy = 0;
        for (LinkedResonator resonator : mPortalResonators) {
            if (resonator != null)
                energy += resonator.energyTotal;
        }
        return energy;
    }

    public int getPortalLevel()
    {
        // TODO: don't recalculate every time
        int level = 0;
        int resonatorCount = 0;
        for (LinkedResonator resonator : mPortalResonators) {
            if (resonator != null) {
                level += resonator.level;
                resonatorCount++;
            }
        }

        if (resonatorCount == 0)
            return 0;

        return level / resonatorCount;
    }

    public Location getPortalLocation()
    {
        return mPortalLocation;
    }

    public Team getPortalTeam()
    {
        return mPortalTeam;
    }

    public String getPortalTitle()
    {
        return mPortalTitle;
    }

    public String getPortalAddress()
    {
        return mPortalAddress;
    }

    public String getPortalAttribution()
    {
        return mPortalAttribution;
    }

    public String getPortalAttributionLink()
    {
        return mPortalAttributionLink;
    }

    public String getPortalImageUrl()
    {
        return mPortalImageUrl;
    }

    public List<LinkedEdge> getPortalEdges()
    {
        return mPortalEdges;
    }

    public List<LinkedMod> getPortalMods()
    {
        return mPortalMods;
    }

    public List<LinkedResonator> getPortalResonators()
    {
        return mPortalResonators;
    }
}
