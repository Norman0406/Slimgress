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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class PortalKnobs extends Knobs
{
    public class Band
    {
        List<Integer> applicableLevels;
        int remaining;
    }

    private List<Band> mBands;
    private boolean mCanPlayerRemoveMod;
    private int mMaxModsPerPlayer;

    public PortalKnobs(JSONObject json) throws JSONException
    {
        super(json);

        JSONObject resonatorLimits = json.getJSONObject("resonatorLimits");
        JSONArray bands = resonatorLimits.getJSONArray("bands");
        mBands = new ArrayList<Band>();
        for (int i = 0; i < bands.length(); i++) {
            JSONObject band = bands.getJSONObject(i);
            Band newBand = new Band();
            newBand.applicableLevels = getIntArray(band, "applicableLevels");
            newBand.remaining = band.getInt("remaining");
            mBands.add(newBand);
        }

        mCanPlayerRemoveMod = json.getBoolean("canPlayerRemoveMod");
        mMaxModsPerPlayer = json.getInt("maxModsPerPlayer");
    }

    public Band getBandForLevel(int level)
    {
        for (Band band : mBands) {
            if (band.applicableLevels.contains((Integer)level))
                return band;
        }

        Log.w("PortalKnobs", "band not found for level: " + level);
        return null;
    }

    public List<Band> getBands()
    {
        return mBands;
    }

    public boolean getCanPlayerRemoveMod()
    {
        return mCanPlayerRemoveMod;
    }

    public int getMaxModsPerPlayer()
    {
        return mMaxModsPerPlayer;
    }
}
