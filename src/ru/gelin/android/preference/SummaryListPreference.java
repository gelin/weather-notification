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
