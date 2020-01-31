package com.droidafricana.globalmail.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.droidafricana.globalmail.R;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {
    public static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.pref_news_main);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference p = prefScreen.getPreference(i);
            if (!(p instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(p.getKey(), "");
                assert value != null;
                setPreferenceSummary(p, value);
            }
        }

        //Set an onPreferenceChangeListener
        Preference preference = findPreference(getString(R.string.pref_notification_interval_key));
        assert preference != null;
        preference.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if ((key.equals(getString(R.string.pref_sort_by_key))) ||
                (key.equals(getString(R.string.pref_endpoint_key))) ||
                key.equals(getString(R.string.pref_country_news_to_display_key))) {
            PREFERENCES_HAVE_BEEN_UPDATED = true;
        }

        Preference preference = findPreference(key);
        if (null != preference) {
            if (!(preference instanceof CheckBoxPreference)) {
                setPreferenceSummary(preference,
                        Objects.requireNonNull(sharedPreferences.getString(key, "")));
            }
        }
    }

    private void setPreferenceSummary(Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            /* For list preferences, look up the correct display value in */
            /* the preference's 'entries' list (since they have separate labels/values). */
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        //Make sure that the interval for notifications is sufficient.
        Toast error = Toast.makeText(getActivity(),
                getString(R.string.prefs_please_enter_number_between_1_and_24), Toast.LENGTH_LONG);
        String intervalKey = getString(R.string.pref_notification_interval_key);
        if (preference.getKey().equals(intervalKey)) {
            String stringIntervalSize = (String) newValue;
            try {
                int interval = Integer.parseInt(stringIntervalSize);
                if (interval < 1 || interval > 24) {
                    if (error != null) error.show();
                    return false;
                }
            } catch (NumberFormatException e) {
                if (error != null) error.show();
                return false;
            }
        }
        return true;
    }
}
