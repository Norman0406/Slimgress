package com.norman0406.ingressex;

import com.norman0406.ingressex.API.Interface;

import android.app.Application;

public class IngressApplication extends Application {

	private static IngressApplication singleton;
	private boolean loggedIn = false;
	protected Interface ingressInterface = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;

		ingressInterface = new Interface();
	}

    public static IngressApplication getInstance() {
        return singleton;
    }
	
	public Interface getInterface() {
		return ingressInterface;
	}
	
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
}
