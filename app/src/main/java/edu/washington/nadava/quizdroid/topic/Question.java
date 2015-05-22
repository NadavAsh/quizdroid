package edu.washington.nadava.quizdroid.topic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nadavash on 5/7/15.
 */
public class Question {
    private String question;
    private List<String> answers;
    private int correct;

    public static Question fromJson(JSONObject jsonQuestion) throws JSONException {
        Question question = new Question();
        question.setQuestion(jsonQuestion.getString("text"));

        JSONArray jsonAnswers = jsonQuestion.getJSONArray("answers");
        List<String> answers = new ArrayList<>();
        for (int i = 0; i < jsonAnswers.length(); ++i) {
            answers.add(jsonAnswers.getString(i));
        }
        question.setAnswers(answers);
        question.setCorrect(Integer.parseInt(jsonQuestion.getString("answer")) - 1);

        return question;
    }

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

    public void setCorrect(int value) {
        correct = value;
    }
}
