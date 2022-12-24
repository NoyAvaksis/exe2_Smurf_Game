package com.example.exe1;

import android.app.Application;
import com.example.exe1.MySp;
import com.example.exe1.GameService;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        GameService.initHelper(this);
        MySp.initHelper(this);

    }
}