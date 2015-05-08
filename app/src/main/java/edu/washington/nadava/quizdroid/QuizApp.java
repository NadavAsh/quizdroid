package edu.washington.nadava.quizdroid;

import android.app.Application;
import android.util.Log;

import edu.washington.nadava.quizdroid.topic.InMemoryTopicRepository;
import edu.washington.nadava.quizdroid.topic.TopicRepository;

/**
 * Created by nadavash on 5/7/15.
 */
public class QuizApp extends Application {
    public static final String TAG = "QuizApp";

    private static QuizApp instance;

    private TopicRepository topicRepo;

    public QuizApp() {
        if (instance == null) {
            instance = this;
        } else {
            throw new RuntimeException("Cannot instantiate more than one instance of QuizApp.");
        }

        topicRepo = new InMemoryTopicRepository();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Loaded application class.");
    }

    public TopicRepository getTopicRepository() {
        return topicRepo;
    }
}
