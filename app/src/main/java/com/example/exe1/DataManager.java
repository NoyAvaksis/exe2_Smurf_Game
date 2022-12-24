package com.example.exe1;

import android.util.Log;

import com.google.gson.Gson;

public class DataManager {

    private static DataManager _instance = new DataManager();
    private eGameMode gameMode;
    public static final String GAME_MODE="GAME_MODE";
    public static final int NUM_OF_RECORDS=10;
    private static final String EXTRA_USERS_DETAILS = "EXTRA_USER_DETAILS";


    public DataManager() {
    }

    public static DataManager get_instance() {
        return _instance;
    }

    public static void set_instance(DataManager _instance) {
        DataManager._instance = _instance;
    }

    public void updateTopRecords(UserDetails userDetails){
        Records records =getTopRecords();
        records.updateTop(userDetails);
        String top_string = new Gson().toJson(records);
        MySp.get_my_SP().putStringToSP(EXTRA_USERS_DETAILS,top_string);
    }

    public Records getTopRecords() {
        String top_string = MySp.get_my_SP().getStringFromSP(EXTRA_USERS_DETAILS,"{}");
        Records topRecords = new Gson().fromJson(top_string,Records.class);
        Log.d("mylog",topRecords.toString());
        return topRecords;
    }

    public static DataManager getDataManager(){
        return _instance;
    }

    public eGameMode getGameMode() {
        return gameMode;
    }

    public DataManager setGameMode(eGameMode gameMode) {
        this.gameMode = gameMode;
        return this;
    }
}
