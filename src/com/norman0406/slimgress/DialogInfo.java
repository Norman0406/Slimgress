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

import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class DialogInfo extends Dialog
{
    public DialogInfo(Context context)
    {
        super(context);
        setContentView(R.layout.dialog_infobox);

        getWindow().setWindowAnimations(R.style.FadeAnimation);
        //getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
        getWindow().setBackgroundDrawable(new ColorDrawable(0));

        // set additional parameters
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.0f;
        getWindow().setAttributes(lp);

        ((TextView)findViewById(R.id.message)).setVisibility(View.INVISIBLE);
        ((TextView)findViewById(R.id.title)).setVisibility(View.INVISIBLE);
    }

    public DialogInfo setMessage(String msg)
    {
        ((TextView)findViewById(R.id.message)).setText(msg);
        ((TextView)findViewById(R.id.message)).setVisibility(View.VISIBLE);
        return this;
    }

    public DialogInfo setTitle(String title)
    {
        ((TextView)findViewById(R.id.title)).setText(title);
        ((TextView)findViewById(R.id.title)).setVisibility(View.VISIBLE);
        return this;
    }

    public DialogInfo setDismissDelay()
    {
        return setDismissDelay(3000);
    }

    public DialogInfo setDismissDelay(int delay)
    {
        // automatically dismiss dialog after 3 seconds
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog)
            {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run()
                    {
                        dialog.dismiss();
                    }
                }, 3000);
            }
        });
        return this;
    }

    public DialogInfo setTouchable(boolean isTouchable)
    {
        if (isTouchable) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        return this;
    }
}
