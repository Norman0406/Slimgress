package com.norman0406.ingressex;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.norman0406.ingressex.API.Interface;
import com.norman0406.ingressex.API.Interface.AuthSuccess;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityAuth extends Activity
{
	private AccountManager mAccountMgr;
	private IngressApplication mApp = IngressApplication.getInstance();
	private int mNumAttempts = 0;
	private static final int mMaxNumAttempts = 2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);
		
		mAccountMgr = AccountManager.get(getApplicationContext());
		((TextView)findViewById(R.id.login)).setText(getString(R.string.auth_login));
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
		final Account[] accounts = mAccountMgr.getAccountsByType("com.google");
		
		if (accounts.length > 1) {	// let user choose account
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(R.string.auth_identity);
			
			String[] ids = new String[accounts.length];
			for (int i = 0; i < accounts.length; i++)
				ids[i] = accounts[i].name;
			
			builder.setItems(ids, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					authenticateUser(accounts[which]);
					dialog.dismiss();
				}
			});
			
			builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					authCancelled();
					dialog.dismiss();
				}
			});
			
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		else if (accounts.length == 1)	// choose the one and only account
			authenticateUser(accounts[0]);
		else	// no account available
			authFailed();
	}
	
	private void authenticateUser(final Account accToUse)
	{
		// authorize user
		new Thread(new Runnable() {
			public void run() {
				// get account name (email)
				final String name = accToUse.name;	// account e-mail
				
				// update username string
				runOnUiThread(new Runnable() {
					public void run() {
						((TextView)findViewById(R.id.username)).setText(name);
					}
				});
				
		        Intent myIntent = getIntent();
		        myIntent.putExtra("User", name);
		        
		        // get authentication token from account manager and return it to the main activity	        
		        mAccountMgr.getAuthToken(accToUse, "ah", null, getActivity(), new AccountManagerCallback<Bundle>() {
					public void run(AccountManagerFuture<Bundle> future) {
				        try {
							if (future.getResult().containsKey(AccountManager.KEY_AUTHTOKEN)) {
								// everything is ok, token obtained
								authFinished(accToUse, future.getResult().getString(AccountManager.KEY_AUTHTOKEN));
							}
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
		}).start();
	}
	
	private void refreshToken(Account account, String token)
	{
		((TextView)findViewById(R.id.login)).setText(getString(R.string.auth_refresh));
		mAccountMgr.invalidateAuthToken("com.google", token);
		authenticateUser(account);
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
	
	public Activity getActivity()
	{
		return this;
	}
	
	public void authCancelled()
	{
        Intent myIntent = getIntent();
        setResult(RESULT_FIRST_USER, myIntent);
        finish();
	}
	
	public void authFinished(Account account, String token)
	{
		mNumAttempts++;
		
		// authenticate ingress
		Interface.AuthSuccess success = mApp.getInterface().authenticate(token);
		
		if (success == Interface.AuthSuccess.Successful) {
	        // switch to main activity and set token result
	        Intent myIntent = getIntent();
	        setResult(RESULT_OK, myIntent);
	        finish();
		}
		else if (success == Interface.AuthSuccess.TokenExpired) {
			// token expired, refresh and get a new one
			if (mNumAttempts > mMaxNumAttempts)
				authFailed();
			else
				refreshToken(account, token);
		}
		else {
			// some error occurred
			authFailed();
		}		
	}
	
	public void authFailed()
	{
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
