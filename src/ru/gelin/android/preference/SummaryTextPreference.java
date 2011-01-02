package ru.gelin.android.preference;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

/**
 *  Edit text preference which displays the entered value as Summary.
 */
public class SummaryTextPreference extends EditTextPreference {

    public SummaryTextPreference(Context context) {
        super(context);
    }
    public SummaryTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public SummaryTextPreference(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        super.onSetInitialValue(restoreValue, defaultValue);
        setSummary(getText());
    }
    
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        setSummary(getText());
    }

}
