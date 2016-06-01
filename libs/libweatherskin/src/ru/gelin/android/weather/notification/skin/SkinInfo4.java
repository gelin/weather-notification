/*
 * Copyright 2010—2016 Denis Nelubin and others.
 *
 * This file is part of Weather Notification.
 *
 * Weather Notification is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Weather Notification is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Weather Notification.  If not, see http://www.gnu.org/licenses/.
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
