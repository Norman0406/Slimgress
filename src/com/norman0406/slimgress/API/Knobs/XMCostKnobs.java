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

package com.norman0406.slimgress.API.Knobs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class XMCostKnobs extends Knobs
{
    private List<Integer> mHeatsinkDeployCostByLevel;
    private List<Integer> mFlipCardCostByLevel;
    private List<Integer> mTurretDeployCostByLevel;
    private List<Integer> mPortalHackNeutralCostByLevel;
    private List<Integer> mShieldDeployCostByLevel;
    private List<Integer> mXmpFiringCostByLevel;
    private List<Integer> mResonatorUpgradeCostByLevel;
    private List<Integer> mPortalHackFriendlyCostByLevel;
    private List<Integer> mMultihackDeployCostByLevel;
    private List<Integer> mPortalHackEnemyCostByLevel;
    private List<Integer> mResonatorDeployCostByLevel;
    private List<Integer> mForceAmplifierDeployCostByLevel;
    private List<Integer> mLinkAmplifierDeployCostByLevel;
    private Map<String, List<Integer>> mPortalModByLevel;

    public XMCostKnobs(JSONObject json) throws JSONException
    {
        super(json);

        mHeatsinkDeployCostByLevel = getIntArray(json, "heatsinkDeployCostByLevel");
        mFlipCardCostByLevel = getIntArray(json, "flipCardCostByLevel");
        mTurretDeployCostByLevel = getIntArray(json, "turretDeployCostByLevel");
        mPortalHackNeutralCostByLevel = getIntArray(json, "portalHackNeutralCostByLevel");
        mShieldDeployCostByLevel = getIntArray(json, "shieldDeployCostByLevel");
        mXmpFiringCostByLevel = getIntArray(json, "xmpFiringCostByLevel");
        mResonatorUpgradeCostByLevel = getIntArray(json, "resonatorUpgradeCostByLevel");
        mPortalHackFriendlyCostByLevel = getIntArray(json, "portalHackFriendlyCostByLevel");
        mMultihackDeployCostByLevel = getIntArray(json, "multihackDeployCostByLevel");
        mPortalHackEnemyCostByLevel = getIntArray(json, "portalHackEnemyCostByLevel");
        mResonatorDeployCostByLevel = getIntArray(json, "resonatorDeployCostByLevel");
        mForceAmplifierDeployCostByLevel = getIntArray(json, "forceAmplifierDeployCostByLevel");
        mLinkAmplifierDeployCostByLevel = getIntArray(json, "linkAmplifierDeployCostByLevel");

        mPortalModByLevel = new HashMap<String, List<Integer>>();
        JSONObject portalModByLevel = json.getJSONObject("portalModByLevel");
        Iterator<?> it = portalModByLevel.keys();
        while (it.hasNext()) {
            String key = (String)it.next();
            mPortalModByLevel.put(key, getIntArray(portalModByLevel, key));
        }
    }

    public List<Integer> getHeatsinkDeployCostByLevel()
    {
        return mHeatsinkDeployCostByLevel;
    }

    public List<Integer> getFlipCardCostByLevel()
    {
        return mFlipCardCostByLevel;
    }

    public List<Integer> getTurretDeployCostByLevel()
    {
        return mTurretDeployCostByLevel;
    }

    public List<Integer> getPortalHackNeutralCostByLevel()
    {
        return mPortalHackNeutralCostByLevel;
    }

    public List<Integer> getShieldDeployCostByLevel()
    {
        return mShieldDeployCostByLevel;
    }

    public List<Integer> getXmpFiringCostByLevel()
    {
        return mXmpFiringCostByLevel;
    }

    public List<Integer> getResonatorUpgradeCostByLevel()
    {
        return mResonatorUpgradeCostByLevel;
    }

    public List<Integer> getPortalHackFriendlyCostByLevel()
    {
        return mPortalHackFriendlyCostByLevel;
    }

    public List<Integer> getMultihackDeployCostByLevel()
    {
        return mMultihackDeployCostByLevel;
    }

    public List<Integer> getPortalHackEnemyCostByLevel()
    {
        return mPortalHackEnemyCostByLevel;
    }

    public List<Integer> getResonatorDeployCostByLevel()
    {
        return mResonatorDeployCostByLevel;
    }

    public List<Integer> getForceAmplifierDeployCostByLevel()
    {
        return mForceAmplifierDeployCostByLevel;
    }

    public List<Integer> getLinkAmplifierDeployCostByLevel()
    {
        return mLinkAmplifierDeployCostByLevel;
    }

    public List<Integer> getPortalModByLevel(String key)
    {
        if (!mPortalModByLevel.containsKey(key))
            Log.e("XMCostKnobs", "key not found in hash map: " + key);

        return mPortalModByLevel.get(key);
    }
}
