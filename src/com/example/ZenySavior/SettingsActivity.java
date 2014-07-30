package com.example.ZenySavior;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.LinearLayout;

/**
 * Created by Jacky on 7/27/2014.
 */
public class SettingsActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.settings);
        //LinearLayout container = (LinearLayout)findViewById(R.id.settings_container);

        getFragmentManager().beginTransaction()
                .add(android.R.id.content,new SettingsFragment())
                .commit();
    }


    public static class SettingsFragment extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

}
