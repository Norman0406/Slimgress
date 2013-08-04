package com.norman0406.ingressex;

import com.norman0406.ingressex.API.Game;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ActivitySplash extends Activity
{
	private IngressApplication mApp = IngressApplication.getInstance();
	private Game mGame = mApp.getGame();
	
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
	    final Context context = this;
	    
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
                mApp.setLoggedIn(true);
                
				// perform handshake
				mGame.intHandshake(new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg)
                    {                        
                        Bundle data = msg.getData();
                        
                        if (data.getBoolean("Successful") == true) {
                            // start main activity
                            ActivitySplash.this.finish();
                            ActivitySplash.this.startActivity(new Intent(ActivitySplash.this, ActivityMain.class));
                        }
                        else {            
                            mApp.setLoggedIn(false);
                            
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Handshake error");
                            builder.setMessage(data.getString("Error"));
                            builder.setNegativeButton("OK", new Dialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            Dialog dialog = builder.create();
                            dialog.show();
                        }
                        
                        return true;
                    }
                }));
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
