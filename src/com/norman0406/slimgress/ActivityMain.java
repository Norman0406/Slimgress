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

package com.norman0406.slimgress;

import android.support.v4.app.FragmentActivity;

import com.norman0406.slimgress.API.Common.Location;
import com.norman0406.slimgress.API.Common.Team;
import com.norman0406.slimgress.API.Game.GameState;
import com.norman0406.slimgress.API.Player.Agent;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityMain extends FragmentActivity
{
    private IngressApplication mApp = IngressApplication.getInstance();
    private GameState mGame = mApp.getGame();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // update agent data
        updateAgent();

        // create ops button callback
        final Button buttonOps = (Button)findViewById(R.id.buttonOps);
        buttonOps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent myIntent = new Intent(getApplicationContext(), ActivityOps.class);
                startActivity(myIntent);
            }
        });

        // create comm button callback
        final Button buttonComm = (Button)findViewById(R.id.buttonComm);
        buttonComm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showInfoBox("Info");
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    private void showInfoBox(String message)
    {
        DialogInfo newDialog = new DialogInfo(this);
        newDialog.setMessage(message);
        newDialog.show();
    }

    private void updateAgent()
    {
        // get agent data
        Agent agent = mGame.getAgent();

        if (agent != null) {
            int textColor = Color.BLUE;
            Team team = agent.getTeam();
            if (team.getTeamType() == Team.TeamType.Resistance)
                textColor = Color.BLUE;
            else
                textColor = Color.GREEN;

            ((TextView)findViewById(R.id.agentname)).setText(agent.getNickname());
            ((TextView)findViewById(R.id.agentname)).setTextColor(textColor);

            String agentlevel = "L" + Integer.toString(agent.getLevel());
            ((TextView)findViewById(R.id.agentlevel)).setText(agentlevel);
            ((TextView)findViewById(R.id.agentlevel)).setTextColor(textColor);

            ((ProgressBar)findViewById(R.id.agentxm)).setMax(agent.getEnergyMax());
            ((ProgressBar)findViewById(R.id.agentxm)).setProgress(agent.getEnergy());

            String agentinfo = "AP: " + agent.getAp() + " / XM: " + (agent.getEnergy() * 100 / agent.getEnergyMax()) + " %";
            ((TextView)findViewById(R.id.agentinfo)).setText(agentinfo);
            ((TextView)findViewById(R.id.agentinfo)).setTextColor(textColor);
        }

        // update current position
        mGame.updateLocation(new Location(50.345963, 7.588223));
    }
}
