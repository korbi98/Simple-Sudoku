package com.korbi.simplesudoku.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.korbi.simplesudoku.R;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    static String versionNumber;
    static String getPackageName;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        getPackageName = getPackageName();
        try{
            versionNumber = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        }
        catch (PackageManager.NameNotFoundException e){e.printStackTrace();}

        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MainPreferenceFragement()).commit();
    }

    public static class MainPreferenceFragement extends PreferenceFragment{
        @Override
        public void onCreate(final Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            bindPreferenceSummaryToValue(findPreference(getString(R.string.game_settings_difficulty_key)));

            Preference version = findPreference(getString(R.string.about_version_key));
            version.setSummary(versionNumber);

            Preference sendFeedback = findPreference(getString(R.string.key_send_feedback));
            sendFeedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String mailLink = "mailto:info@korbinian-moser.de?" +
                                        "subject=Simple Sudoku feedback";

                    Intent sendMail = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse(mailLink);
                    sendMail.setData(data);
                    startActivity(sendMail);
                    return true;
                }
            });

            Preference rateApp = findPreference(getString(R.string.about_rate_app_key));
            rateApp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + getPackageName)));
                    } catch (android.content.ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" +
                                        getPackageName)));
                    }
                    return true;
                }
            });

            Preference otherApps = findPreference(getString(R.string.about_other_apps_key));
            otherApps.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://developer?id=Korbinian+Moser")));
                    } catch (android.content.ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/developer?id=Korbinian+Moser")));
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static void bindPreferenceSummaryToValue(Preference preference){
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue){
            String stringValue = newValue.toString();

            if (preference instanceof ListPreference){
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                if (stringValue.equals("10")){
                    preference.setSummary("10\n" + context.getString(R.string.long_loadtime_warning));
                }
                else{
                    preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
                }
            }

            else {
                preference.setSummary(stringValue);
            }
            return true;
        }

    };

}
