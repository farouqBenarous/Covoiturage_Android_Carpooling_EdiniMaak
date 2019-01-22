package com.example.sony.tabhost.Helper;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import com.bumptech.glide.load.engine.Resource;

import java.util.Locale;

public class Helper {

private static  final  String SELECTED_LANGUAGE = "Helper.Helper.Selected.Language";


public static Context  onAttache (Context context) {
String lang = getPersistedData (context, Locale.getDefault().getLanguage());
    return setLocale(context,lang);}

 public static Context  onAttache (Context context, String defaultLanguage) {
        String lang = getPersistedData (context, defaultLanguage);
        return setLocale(context,lang);  }


public static Context setLocale(Context context, String lang) {
Persist(context,lang);
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
    return updateRessources (context,lang);

return updateRessourcesLegaycy (context,lang);
}
    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateRessources(Context context, String lang) {
    Locale locale = new Locale(lang);
    Locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        return  context.createConfigurationContext(config);


    }


    @SuppressWarnings("deprecation")
    private static Context updateRessourcesLegaycy(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources resource =context.getResources();
        Configuration config = resource.getConfiguration();

        config.locale = locale;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            config.setLayoutDirection(locale);
            resource.updateConfiguration(config,resource.getDisplayMetrics());
        return context;
    }










    private static void Persist(Context context, String lang) {
SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
SharedPreferences.Editor editor =  pref .edit();
editor.putString(SELECTED_LANGUAGE,lang);
editor.apply();

}


    private static String getPersistedData(Context context, String language) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE,language);


}


}
