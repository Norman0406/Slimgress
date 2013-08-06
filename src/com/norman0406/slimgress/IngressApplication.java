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

import com.norman0406.slimgress.API.Game.GameState;

import android.app.Application;

public class IngressApplication extends Application
{
    private static IngressApplication mSingleton;
    private boolean mLoggedIn = false;
    protected GameState mGame;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mSingleton = this;
        mGame = new GameState();
    }

    @Override
    public void onTerminate()
    {
    }

    public static IngressApplication getInstance()
    {
        return mSingleton;
    }

    public GameState getGame()
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
