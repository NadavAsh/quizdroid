package edu.washington.nadava.quizdroid.topic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nadavash on 5/7/15.
 */
public class Question {
    private String question;
    private List<String> answers;
    private int correct;

    public Question() {

    }

    public Question(String question, List<String> answers, int correct) {
        this.question = question;
        this.answers = answers;
        this.correct = correct;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String value) {
        question = value;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> value) {
        answers = value;
    }

    public int getCorrect() {
        return correct;
    }

    public void getCorrect(int value) {
        correct = value;
    }
}
