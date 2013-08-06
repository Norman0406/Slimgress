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

import java.io.IOException;

import com.norman0406.slimgress.API.Game.GameState;
import com.norman0406.slimgress.API.Interface.Interface;

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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.TextView;

public class ActivityAuth extends Activity
{
    private IngressApplication mApp = IngressApplication.getInstance();
    private GameState mGame = mApp.getGame();
    private AccountManager mAccountMgr;
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

        // check first if user is already logged in

        if (isLoggedIn()) {
            // user is already logged in, get login data
            SharedPreferences prefs = getSharedPreferences(getApplicationInfo().packageName,  0);
            final String accountName = prefs.getString("account_name", null);
            final String accountToken = prefs.getString("account_token", null);

            // update username string
            runOnUiThread(new Runnable() {
                public void run() {
                    ((TextView)findViewById(R.id.username)).setText(accountName);
                }
            });

            // check if there is a matching account available
            boolean found = false;
            for (Account account : accounts) {
                if (account.name.equals(accountName)) {
                    authFinished(account, accountToken);
                    found = true;
                }
            }

            // specified account not found, simply select an existing one
            if (!found)
                selectAccount(accounts);
        }
        else {
            selectAccount(accounts);
        }
    }

    private void selectAccount(final Account[] accounts)
    {
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

    private boolean isLoggedIn()
    {
        // check if login data exists
        SharedPreferences prefs = getSharedPreferences(getApplicationInfo().packageName,  0);
        String accountName = prefs.getString("account_name", null);
        String accountToken = prefs.getString("account_token", null);

        if (accountName != null && accountToken != null)
            return true;

        return false;
    }

    private void authenticateUser(final Account accToUse)
    {
        // authorize user
        new Thread(new Runnable() {
            @Override
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
                            e.printStackTrace();
                        } catch (AuthenticatorException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
            }
        }).start();
    }

    private void refreshToken(Account account, String token)
    {
        runOnUiThread(new Runnable() {
            public void run() {
                ((TextView)findViewById(R.id.login)).setText(getString(R.string.auth_refresh));
            }
        });

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

    public void authFinished(final Account account, final String token)
    {
        mNumAttempts++;

        new Thread(new Runnable() {
            @Override
            public void run() {
                // authenticate ingress
                Interface.AuthSuccess success = mGame.intAuthenticate(token);

                if (success == Interface.AuthSuccess.Successful) {

                    // save login data
                    SharedPreferences prefs = getSharedPreferences(getApplicationInfo().packageName, 0);
                    Editor editor = prefs.edit();
                    editor.putString("account_name", account.name);
                    editor.putString("account_token", token);
                    editor.commit();

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
        }).start();

    }

    public void authFailed()
    {
        // clear login data
        SharedPreferences prefs = getSharedPreferences(getApplicationInfo().packageName,  0);
        String accountName = prefs.getString("account_name", null);
        String accountToken = prefs.getString("account_token", null);

        if (accountName == null || accountToken == null) {
            Editor editor = prefs.edit();
            if (accountName == null)
                editor.remove("account_name");
            if (accountToken == null)
                editor.remove("account_token");
            editor.commit();
        }

        // switch to main activity
        Intent myIntent = getIntent();
        setResult(RESULT_CANCELED, myIntent);
        finish();
    }
}
