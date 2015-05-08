package edu.washington.nadava.quizdroid.topic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nadavash on 5/7/15.
 */
public class Topic {
    private String title;
    private String description;
    private String longDescription;
    private List<Question> questions;

    public Topic() {

    }

    public Topic(String title, String description, String longDescription,
                 List<Question> questions) {
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> value) {
        questions = value;
    }
}
