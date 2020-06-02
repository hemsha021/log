package vion.logtracks.utils;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //FirebaseApp.initializeApp(this);
    }
}

