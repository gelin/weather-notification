/*
 *  Android helper classes.
 *  Copyright (C) 2010  Denis Nelubin aka Gelin
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
import android.preference.ListPreference;
import android.util.AttributeSet;

/**
 *  List preference which displays the selected value as Summary.
 */
public class SummaryListPreference extends ListPreference {

    public SummaryListPreference(Context context) {
        super(context);
    }
    public SummaryListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        super.onSetInitialValue(restoreValue, defaultValue);
        setSummary(getEntry());
    }
    
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        setSummary(getEntry());
    }

}
