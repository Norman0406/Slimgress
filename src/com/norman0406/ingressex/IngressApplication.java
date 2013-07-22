package com.norman0406.ingressex;

import com.norman0406.ingressex.API.IngressInterface;
import com.norman0406.ingressex.API.Interface;

import android.app.Application;
import android.content.SharedPreferences;

public class IngressApplication extends Application
{
	private static IngressApplication singleton;
	private boolean loggedIn = false;
	protected IngressInterface ingress = IngressInterface.getInstance();
	private String prefsFile = "IngressExPrefs";
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		SharedPreferences prefs = getSharedPreferences(prefsFile,  0);
		
		// TODO: load data
				
		singleton = this;
	}

	@Override
	public void onTerminate()
	{
		SharedPreferences prefs = getSharedPreferences(prefsFile,  0);
		SharedPreferences.Editor editor = prefs.edit();
		
		// TODO: store data in editor
		
		editor.commit();
	}

    public static IngressApplication getInstance()
    {
        return singleton;
    }
	
	public IngressInterface getInterface()
	{
		return ingress;
	}
	
	public boolean isLoggedIn()
	{
		return loggedIn;
	}
	
	public void setLoggedIn(boolean loggedIn)
	{
		this.loggedIn = loggedIn;
	}
}
