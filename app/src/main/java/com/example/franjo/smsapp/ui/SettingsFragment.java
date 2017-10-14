package com.example.franjo.smsapp.ui;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.franjo.smsapp.R;


public class SettingsFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

    }

}
