package com.norman0406.ingressex;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityAuth extends Activity {
	
	private AccountManager accountMgr;
		
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);
		
		/* Workflow: This activity is created first without any extra data. The authentication token
		 * is returned as extra "AuthToken" to the calling activity if everything was okay. Elsewise,
		 * RESULT_CANCELLED is returned, which means that the user could not be authenticated.
		 * In case the network interface returns HTTP 401 on login, the token has probably expired and
		 * needs to be refreshed. Then, the activity is called again with extra "RefreshToken" and the
		 * expired token, which will then return a new authentication token to the calling activity.
		 */
		
		/* NOTE: the above could possibly all be done in the same thread from within the AuthActivity
		 * to prevent the activity popping up multiple times, in case the token expired and needs to
		 * be refreshed.
		 */
		
		accountMgr = AccountManager.get(getApplicationContext());

        Intent myIntent = getIntent();    	
    	if (myIntent.hasExtra("RefreshToken")) {
    		String tokenToRefresh = myIntent.getStringExtra("RefreshToken");
    		refreshToken(tokenToRefresh);
    	}
    	else
    		authorize();
	}

	@Override
	protected void onResume() 
	{
		// The activity must call the GL surface view's onResume() on activity onResume().
		super.onResume();
	}
	
	private void authorize()
	{
		// authorize user
		new Thread(new Runnable() {
			public void run() {
				Account[] accounts = accountMgr.getAccountsByType("com.google");
							
				// UNDONE: let user choose account
							
				if (accounts.length > 1) {
					Account accToUse = accounts[1];
					
					// get account name (email)
					String name = accToUse.name;	// account e-mail
					((TextView)findViewById(R.id.username)).setText(name);
			        Intent myIntent = getIntent();
			        myIntent.putExtra("User", name);
			        
			        // get authentication token from account manager and return it to the main activity	        
			        accountMgr.getAuthToken(accToUse, "ah", null, getActivity(), new AccountManagerCallback<Bundle>() {
						public void run(AccountManagerFuture<Bundle> future) {
					        try {
								if (future.getResult().containsKey(AccountManager.KEY_AUTHTOKEN))
									// everything is ok, token obtained
									authFinished(future.getResult().getString(AccountManager.KEY_AUTHTOKEN));
								else if (future.getResult().containsKey(AccountManager.KEY_INTENT)) {
									// the system need further user input, handle in onActivityResult
									Intent launch = (Intent)future.getResult().get(AccountManager.KEY_INTENT);
							        if (launch != null) {
							            startActivityForResult(launch, 0);
							            return;
							        }
								}
								else
									authFailed();
							} catch (OperationCanceledException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (AuthenticatorException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
			        }, null);
			         
				}
				else
					authFailed();
			}
		}).start();
	}
	
	private void refreshToken(String token) {
		accountMgr.invalidateAuthToken("com.google", token);
		authorize();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				// authorize again to obtain the code
				authorize();
			}
			else {
				// something went wrong
				authFailed();
			}
		}
	}
	
	public Activity getActivity() {
		return this;
	}
	
	public void authFinished(String token) {
        // switch to main activity and set token result
        Intent myIntent = getIntent();
        myIntent.putExtra("AuthToken", token);
        setResult(RESULT_OK, myIntent);				        
        finish();
	}
	
	public void authFailed() {
        // switch to main activity
        Intent myIntent = getIntent();
        setResult(RESULT_CANCELED, myIntent);				        
        finish();
	}

	@Override
	protected void onPause() 
	{
		// The activity must call the GL surface view's onPause() on activity onPause().
		super.onPause();
	}
}
