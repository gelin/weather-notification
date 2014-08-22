/*
 *  Android Weather Notification.
 *  Copyright (C) 2010  Denis Nelubin aka Gelin
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  http://gelin.ru
 *  mailto:den@gelin.ru
 */

package ru.gelin.android.weather.notification.skin;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.preference.TwoStatePreference;
import ru.gelin.android.preference.SwitchPreference;

import static ru.gelin.android.weather.notification.IntentParameters.ACTION_WEATHER_SKIN_PREFERENCES;
import static ru.gelin.android.weather.notification.skin.PreferenceKeys.SKIN_ENABLED_PATTERN;

/**
 * 	Information about skin
 * 	A version which is used in Android 4.x
 */
public class SkinInfo4 extends SkinInfo {

    protected SkinInfo4(String id) {
        super(id);
    }

    @Override
    public Preference getSwitchPreference(Context context) {
        TwoStatePreference pref = new SwitchPreference(context);
        pref.setKey(String.format(SKIN_ENABLED_PATTERN, getId()));
        pref.setTitle(getBroadcastReceiverLabel());
        pref.setSummary(R.string.skin_tap_to_config);
        pref.setChecked(isEnabled());
        pref.setOrder(this.order);
        Intent intent = new Intent(ACTION_WEATHER_SKIN_PREFERENCES);
        intent.setClassName(getPackageName(), getConfigActivityClass());
        pref.setIntent(intent);
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                TwoStatePreference switchPreference = (TwoStatePreference)preference;
                switchPreference.setChecked(!switchPreference.isChecked());     //to avoid changing of the state by clicking not to the switch
                return false;
            }
        });
        return pref;
    }

}
