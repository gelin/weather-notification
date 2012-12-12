/*
 *  Android helper classes.
 *  Copyright (C) 2012  caoyachao@hotmail.com, Denis Nelubin
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  http://gelin.ru
 *  mailto:den@gelin.ru
 */

package ru.gelin.android.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

/**
 *  The correct version of SwitchPreference which fixes this issue:
 *  http://code.google.com/p/android/issues/detail?id=26194
 *  Also has not-words, but some characters for on/off states of the switch.
 */
public class SwitchPreference extends android.preference.SwitchPreference {

    /**
     * Construct a new SwitchPreference with the given style options.
     *
     * @param context The Context that will style this preference
     * @param attrs Style attributes that differ from the default
     * @param defStyle Theme attribute defining the default style options
     */
    public SwitchPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSwitchText();
    }

    /**
     * Construct a new SwitchPreference with the given style options.
     *
     * @param context The Context that will style this preference
     * @param attrs Style attributes that differ from the default
     */
    public SwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSwitchText();
    }

    /**
     * Construct a new SwitchPreference with default style options.
     *
     * @param context The Context that will style this preference
     */
    public SwitchPreference(Context context) {
        super(context, null);
        initSwitchText();
    }

    private void initSwitchText() {
        //if (getSwitchTextOn() == null) {  TODO: allow to define from attributes
            setSwitchTextOn(R.string.switch_on);
        //}
        //if (getSwitchTextOff() == null) {
            setSwitchTextOff(R.string.switch_off);
        //}
    }

    @Override
    protected void onBindView(View view) {
        // Clean listener before invoke SwitchPreference.onBindView
        ViewGroup viewGroup= (ViewGroup)view;
        clearListenerInViewGroup(viewGroup);
        super.onBindView(view);
    }

    /**
     * Clear listener in Switch for specify ViewGroup.
     *
     * @param viewGroup The ViewGroup that will need to clear the listener.
     */
    private void clearListenerInViewGroup(ViewGroup viewGroup) {
        if (null == viewGroup) {
            return;
        }

        int count = viewGroup.getChildCount();
        for(int n = 0; n < count; ++n) {
            View childView = viewGroup.getChildAt(n);
            if(childView instanceof Switch) {
                final Switch switchView = (Switch) childView;
                switchView.setOnCheckedChangeListener(null);
                return;
            } else if (childView instanceof ViewGroup){
                ViewGroup childGroup = (ViewGroup)childView;
                clearListenerInViewGroup(childGroup);
            }
        }
    }

}