package edu.washington.nadava.quizdroid;

import android.app.Application;
import android.util.Log;

/**
 * Created by nadavash on 5/7/15.
 */
public class QuizApp extends Application {
    public static final String TAG = "QuizApp";

    private static QuizApp instance;

    public QuizApp() {
        if (instance == null) {
            instance = this;
        } else {
            throw new RuntimeException("Cannot instantiate more than one instance of QuizApp.");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Loaded application class.");
    }
}
