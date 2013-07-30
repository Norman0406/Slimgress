package com.norman0406.ingressex;

import com.norman0406.ingressex.API.Game;

import android.app.Application;

public class IngressApplication extends Application
{
	private static IngressApplication mSingleton;
	private boolean mLoggedIn = false;
	protected Game mGame;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
						
		mSingleton = this;
		mGame = new Game();
	}

	@Override
	public void onTerminate()
	{
	}

    public static IngressApplication getInstance()
    {
        return mSingleton;
    }
	
	public Game getGame()
	{
		return mGame;
	}
	
	public boolean isLoggedIn()
	{
		return mLoggedIn;
	}
	
	public void setLoggedIn(boolean loggedIn)
	{
		mLoggedIn = loggedIn;
	}
}
