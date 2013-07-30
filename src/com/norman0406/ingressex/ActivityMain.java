package com.norman0406.ingressex;

import android.support.v4.app.FragmentActivity;

import com.norman0406.ingressex.API.Agent;
import com.norman0406.ingressex.API.Game;
import com.norman0406.ingressex.API.Utils;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
		
		/*ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.MyDialog); 
		//ContextThemeWrapper ctw = new ContextThemeWrapper(this, android.R.style.Theme_Translucent);
		//AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialog);
		AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
		
		builder.setTitle("Hacking");
		builder.setMessage("Hallo");
		
		Dialog dialog = builder.create();
		//dialog.setContentView(R.style.MyDialog);
		//dialog.getWindow().setContentView(R.style.MyDialog);
		dialog.show();
		TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
		messageText.setGravity(Gravity.CENTER);
		
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.dimAmount = 0.0f;
		lp.gravity = Gravity.BOTTOM;
		//lp.alpha = 0.3f;
		//lp.format = PixelFormat.TRANSLUCENT;
		dialog.getWindow().setAttributes(lp);
		dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
		dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/
		
		/*AlphaAnimation anim = new AlphaAnimation(0, 1);
		anim.setStartTime(0);
		anim.setDuration(2000);
		anim.setFillAfter(true);		
		View view = dialog.getWindow().getDecorView();
		view.setAnimation(anim);
		view.postInvalidate();*/
		
		/*Drawable background = new ColorDrawable(android.graphics.Color.BLUE);
		background.setAlpha(100);
		dialog.getWindow().setBackgroundDrawable(background);*/
		
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
		
		// get inventory
		//Utils.LocationE6 playerLocation = new Utils.LocationE6(50.345963, 7.588223);
		/*mGame.intGetInventory(playerLocation, new Handler.Callback() {
			@Override
	        public boolean handleMessage(Message msg) {
				// inventory is loaded
				System.out.println("inventory loaded");
				return true;
			}
		});*/
		
		// 2f116dad563a45f4bbb547873cd7a010.5
		/*mGame.intRecycleItem(mGame.getInventory().getItems().get(0), playerLocation, new Handler.Callback() {
			@Override
	        public boolean handleMessage(Message msg) {
				return true;
			}
		});*/
		
		/*mApp.getInterface().intGetGameScore(new Handler.Callback() {
	        public boolean handleMessage(Message msg) {
				int resistanceScore = msg.getData().getInt("ResistanceScore");
				int enlightenedScore = msg.getData().getInt("EnlightenedScore");
				System.out.println("resistance score loaded");
				return true;
	        }
		});*/
		
		/*mGame.intRedeemReward("5vzd5augustar276u", new Handler.Callback() {
	        public boolean handleMessage(Message msg) {
				System.out.println("resistance score loaded");
				return true;
	        }
		});*/
	}
}
