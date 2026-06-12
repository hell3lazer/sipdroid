package org.sipdroid.sipua;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatDelegate;

public class SipdroidApp extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String PREF_THEME = "app_theme";
    public static final String THEME_SYSTEM = "-1";
    public static final String THEME_LIGHT = "1";
    public static final String THEME_DARK = "2";

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        applyTheme(prefs.getString(PREF_THEME, THEME_SYSTEM));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (PREF_THEME.equals(key)) {
            applyTheme(sharedPreferences.getString(PREF_THEME, THEME_SYSTEM));
        }
    }

    private void applyTheme(String themeValue) {
        switch (themeValue) {
            case THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case THEME_SYSTEM:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
}
