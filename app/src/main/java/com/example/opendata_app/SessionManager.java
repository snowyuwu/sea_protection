package com.example.opendata_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    Context context;
    SharedPreferences sharedPreferences;
    int PRIVATE_MODE=0;
    private static final  String PREF_NAME="refresh";
    public SharedPreferences.Editor editor;
    public static final String AREA= "area";
    public static final String SWIM = "swim";
    public static final String DIVING = "diving";
    public static final String SURFING = "surfing";
    public static final String JETSKI = "jetski";
    public static final String BANANABOAT = "bananaboat";
    public static final String AQUABOARD = "aquaboard";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context){
        this.context= context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void refresh(String area,String swim,String diving,String surfing,String jetski,String bananaboat,String aquaboard){
        editor.putString(AREA,area);
        editor.putString(SWIM,swim);
        editor.putString(DIVING,diving);
        editor.putString(SURFING,surfing);
        editor.putString(JETSKI,jetski);
        editor.putString(BANANABOAT,bananaboat);
        editor.putString(AQUABOARD,aquaboard);
        editor.apply();
    }

    public HashMap<String,String> getPlaceDetails(){
        HashMap<String,String> details =new HashMap<>();
        details.put(AREA,sharedPreferences.getString(AREA,null));
        details.put(SWIM,sharedPreferences.getString(SWIM,null));
        details.put(DIVING,sharedPreferences.getString(DIVING,null));
        details.put(SURFING,sharedPreferences.getString(SURFING,null));
        details.put(JETSKI,sharedPreferences.getString(JETSKI,null));
        details.put(BANANABOAT,sharedPreferences.getString(BANANABOAT,null));
        details.put(AQUABOARD,sharedPreferences.getString(AQUABOARD,null));
        return details;
    }
}
