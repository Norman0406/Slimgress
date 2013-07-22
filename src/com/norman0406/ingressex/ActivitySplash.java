package com.norman0406.ingressex;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class ActivitySplash extends Activity
{
	private IngressApplication mApp = IngressApplication.getInstance();
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		// authenticate if necessary
		if (!mApp.isLoggedIn()) {
	        Intent myIntent = new Intent(getApplicationContext(), ActivityAuth.class);
	        startActivityForResult(myIntent, 0);
		}
		else {
	        // start main activity
	        finish();
	        startActivity(new Intent(getApplicationContext(), ActivityMain.class));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				mApp.setLoggedIn(true);
				
				// perform handshake
				mApp.getInterface().intHandshake(new Runnable() {
					@Override
					public void run() {				        
				        // start main activity
				        ActivitySplash.this.startActivity(new Intent(ActivitySplash.this, ActivityMain.class));
				        ActivitySplash.this.finish();
					}
				});
			}
			else if (resultCode == RESULT_FIRST_USER) {
				// user cancelled authentication
				finish();
			}
			else {
				// authentication failed
				mApp.setLoggedIn(false);
				
				// show an information dialog
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.splash_failure_title);
				builder.setMessage(R.string.splash_failure_msg);
				
				builder.setPositiveButton(R.string.ok,  new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();						
					}
				});
				
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		}
	}
}
