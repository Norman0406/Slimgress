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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.norman0406.slimgress.API.Knobs.KnobsBundle;
import com.norman0406.slimgress.API.Player.Agent;

public class Handshake
{
    public interface Callback
    {
        public void handle(Handshake handshake);
    }

    public enum PregameStatus
    {
        ClientMustUpgrade,
        NoActionsRequired
    }

    private PregameStatus mPregameStatus;
    private String mServerVersion;
    private String mNickname;
    private String mXSRFToken;
    private Agent mAgent = null;
    private KnobsBundle mKnobs = null;

    public Handshake(JSONObject json) throws JSONException
    {
        JSONObject result = json.getJSONObject("result");

        String pregameStatus = result.getJSONObject("pregameStatus").getString("action");
        if (pregameStatus.equals("CLIENT_MUST_UPGRADE"))
                mPregameStatus = PregameStatus.ClientMustUpgrade;
        else if (pregameStatus.equals("NO_ACTIONS_REQUIRED"))
            mPregameStatus = PregameStatus.NoActionsRequired;
        else
            throw new RuntimeException("unknown pregame status " + pregameStatus);

        mServerVersion = result.getString("serverVersion");

        // get player entity
        mNickname = result.optString("nickname");
        JSONArray playerEntity = result.optJSONArray("playerEntity");
        if (playerEntity != null)
            mAgent = new Agent(playerEntity, mNickname);

        mXSRFToken = result.optString("xsrfToken");

        // get knobs
        JSONObject knobs = result.optJSONObject("initialKnobs");
        if (knobs != null)
            mKnobs = new KnobsBundle(knobs);
    }

    public boolean isValid()
    {
        return mAgent != null &&
                mXSRFToken.length() > 0 &&
                mPregameStatus == PregameStatus.NoActionsRequired;
    }

    public PregameStatus getPregameStatus()
    {
        return mPregameStatus;
    }

    public String getServerVersion()
    {
        return mServerVersion;
    }

    public String getNickname()
    {
        return mNickname;
    }

    public String getXSRFToken()
    {
        return mXSRFToken;
    }

    public Agent getAgent()
    {
        return mAgent;
    }

    public KnobsBundle getKnobs()
    {
        return mKnobs;
    }
}
