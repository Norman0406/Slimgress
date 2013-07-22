package com.norman0406.ingressex;

import com.norman0406.ingressex.API.IngressInterface;
import com.norman0406.ingressex.API.Interface;

import android.app.Application;
import android.content.SharedPreferences;

public class IngressApplication extends Application
{
	private static IngressApplication mSingleton;
	private boolean mLoggedIn = false;
	protected IngressInterface mIngress = IngressInterface.getInstance();
	
	@Override
	public void onCreate()
	{
		super.onCreate();
						
		mSingleton = this;
	}

	@Override
	public void onTerminate()
	{
	}

    public static IngressApplication getInstance()
    {
        return mSingleton;
    }
	
	public IngressInterface getInterface()
	{
		return mIngress;
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
