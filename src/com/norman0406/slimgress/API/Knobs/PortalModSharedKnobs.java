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

public class PortalModSharedKnobs extends Knobs
{
    private List<Integer> mMultiTurretFreqDiminishingValues;
    private List<Integer> mMultiForceAmpDiminishingValues;
    private List<Integer> mMultiLinkAmpDiminishingValues;
    private Map<String, List<Integer>> mDiminishingValues;

    public PortalModSharedKnobs(JSONObject json) throws JSONException
    {
        super(json);

        mMultiTurretFreqDiminishingValues = getIntArray(json, "multiTurretFreqDiminishingValues");
        mMultiForceAmpDiminishingValues = getIntArray(json, "multiForceAmpDiminishingValues");
        mMultiLinkAmpDiminishingValues = getIntArray(json, "multiLinkAmpDiminishingValues");

        mDiminishingValues = new HashMap<String, List<Integer>>();
        JSONObject diminishingValues = json.getJSONObject("diminishingValues");
        Iterator<?> it = diminishingValues.keys();
        while (it.hasNext()) {
            String key = (String)it.next();
            mDiminishingValues.put(key, getIntArray(diminishingValues, key));
        }
    }

    public List<Integer> getMultiTurretFreqDiminishingValues()
    {
        return mMultiTurretFreqDiminishingValues;
    }

    public List<Integer> getMultiForceAmpDiminishingValues()
    {
        return mMultiForceAmpDiminishingValues;
    }

    public List<Integer> getMultiLinkAmpDiminishingValues()
    {
        return mMultiLinkAmpDiminishingValues;
    }

    public List<Integer> getDiminishingValues(String key)
    {
        if (!mDiminishingValues.containsKey(key))
            Log.e("PortalModSharedKnobs", "key not found in hash map: " + key);

        return mDiminishingValues.get(key);
    }
}
