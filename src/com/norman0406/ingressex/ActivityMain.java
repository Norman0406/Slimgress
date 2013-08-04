package com.norman0406.ingressex;

import android.support.v4.app.FragmentActivity;

import com.norman0406.ingressex.API.Agent;
import com.norman0406.ingressex.API.Game;
import com.norman0406.ingressex.API.Utils;

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
	private Game mGame = mApp.getGame();
    
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
			Utils.Team team = agent.getTeam();
			if (team == Utils.Team.Resistance)
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
        mGame.updateLocation(new Utils.LocationE6(50.345963, 7.588223));
		
		mGame.intGetInventory(new Handler(new Handler.Callback() {
			@Override
	        public boolean handleMessage(Message msg) {
				// inventory is loaded
				System.out.println("inventory loaded");
				return true;
			}
		}));
	}
}
