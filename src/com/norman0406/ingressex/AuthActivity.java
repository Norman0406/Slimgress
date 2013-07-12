package com.norman0406.ingressex;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AuthActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);
		
		AccountManager accountMgr = AccountManager.get(getApplicationContext());
		Account[] accounts = accountMgr.getAccountsByType("com.google");
		
		final Activity activity = this;
		
		if (accounts.length > 0) {
			Account accToUse = accounts[0];
			
			// get account name (email)
			String name = accToUse.name;	// account e-mail
			((TextView)findViewById(R.id.username)).setText(name);
	        Intent myIntent = getIntent();
	        myIntent.putExtra("User", name);
			
	        // get authentication token from account manager and return it to the main activity	        
			accountMgr.getAuthToken(accToUse, "ah", null, activity, new AccountManagerCallback<Bundle>() {
			    public void run(AccountManagerFuture<Bundle> future) {
			    	try {
			    		String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
			    				    		
				        // switch to main activity
				        Intent myIntent = getIntent();
				        myIntent.putExtra("AuthToken", token);
				        setResult(RESULT_OK, myIntent);				        
				        finish();
				   	} catch (OperationCanceledException e) {
				   		setResult(RESULT_CANCELED);
				   		finish();
				   	} catch (Exception e) {
				   		setResult(RESULT_CANCELED);
				   		finish();
				   	}
			    }
			}, null);
		}
		else {
	   		setResult(RESULT_CANCELED);
	   		finish();
		}
	}

	@Override
	protected void onResume() 
	{
		// The activity must call the GL surface view's onResume() on activity onResume().
		super.onResume();
	}

	@Override
	protected void onPause() 
	{
		// The activity must call the GL surface view's onPause() on activity onPause().
		super.onPause();
	}	

}
