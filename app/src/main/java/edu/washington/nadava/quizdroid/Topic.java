package edu.washington.nadava.quizdroid;

import java.util.ArrayList;

/**
 * Created by nadavash on 5/7/15.
 */
public class Topic {
    private String title;
    private String description;
    private String longDescription;
    private ArrayList<Question> questions;

    public Topic() {

    }

    public Topic(String title, String description, String longDescription,
                 ArrayList<Question> questions) {
        this.title = title;
        this.description = description;
        this.longDescription = longDescription;
        this.questions = questions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        title = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        description = value;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String value) {
        longDescription = value;
    }
}
