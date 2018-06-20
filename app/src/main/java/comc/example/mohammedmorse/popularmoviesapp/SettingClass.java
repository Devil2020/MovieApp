package comc.example.mohammedmorse.popularmoviesapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

/**
 * Created by Mohammed Morse on 16/06/2018.
 */

public class SettingClass extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    addPreferencesFromResource(R.xml.setting);
    SetSummary();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String movieChoise=sharedPreferences.getString("movies","Populer");
        Bundle bundle=new Bundle();
        if(movieChoise.equals("Populer")){
            bundle.putInt("WhichUrl",1);
            onSaveInstanceState(bundle);
        }
        else {
            bundle.putInt("WhichUrl",2);
            onSaveInstanceState(bundle);
        }
        SetSummary();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }
    public void SetSummary(){
        ListPreference listPreference= (ListPreference) findPreference("movies");
        if(listPreference.getValue()==null){
            listPreference.setValueIndex(0);
        }
        else{
            listPreference.setSummary(listPreference.getValue().toString());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
