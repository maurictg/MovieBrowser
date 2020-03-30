package com.avans.movieapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.avans.movieapp.R;
import com.avans.movieapp.fragments.ProfileFragment;

import java.util.zip.Inflater;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            ProfileFragment profileFragment = new ProfileFragment();
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SwitchPreferenceCompat theme = findPreference("theme");
            EditTextPreference etpUsername = findPreference("username");
            etpUsername.setOnPreferenceChangeListener(new EditTextPreference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    System.out.println(newValue.toString());
                    etpUsername.setText(newValue.toString());
                    return true;
                }
            });

            if (theme != null) {
                theme.setOnPreferenceChangeListener((preference, o) -> {
                    boolean isOn = (boolean) o;
                    if (isOn) {
                        System.out.println("Theme Dark");
                    } else {
                        System.out.println("Theme Light");
                    }
                    return true;
                });
            }

        }
    }
}