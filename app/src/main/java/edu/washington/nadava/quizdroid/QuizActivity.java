package edu.washington.nadava.quizdroid;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class QuizActivity extends ActionBarActivity implements QuestionFragment.OnAnswerSubmittedListener,
        TopicOverviewFragment.OnBeginQuizListener {
    public static final String TAG = "QuizActivity";
    public static final String NUM_QUESTIONS_MESSAGE =
            "edu.washington.nadava.quizdroid.NUM_QUESTIONS";
    public static final String NUM_CORRECT_MESSAGE = "edu.washington.nadava.quizdroid.NUM_CORRECT";
    public static final String PROMPT_MESSAGE = "edu.washington.nadava.quizdroid.PROMPT";
    public static final String ANSWERS_MESSAGE = "edu.washington.nadava.quizdroid.ANSWERS";

    private int numAnswered;
    private int numCorrect;
    private String[] questions;
    private String[] answers;
    private int[] correctAnswers;
    private String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        processIntent();

        Bundle args = new Bundle();
        args.putString(MainActivity.TOPIC_MESSAGE, topic);
        args.putInt(TopicOverviewFragment.QUESTION_COUNT_MESSAGE, questions.length);

        Fragment topicFragment = new TopicOverviewFragment();
        topicFragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.quiz_fragment_container, topicFragment)
                .commit();
    }

    private void processIntent() {
        Intent intent = getIntent();

        int[] resources = null;
        topic = intent.getStringExtra(MainActivity.TOPIC_MESSAGE);
        switch (topic) {
            case "Math":
                resources = new int[] {
                        R.array.math_questions,
                        R.array.math_answers,
                        R.array.math_correct_answers
                };
                break;
            case "Physics":
                resources = new int[] {
                        R.array.physics_questions,
                        R.array.physics_answers,
                        R.array.physics_correct_answers
                };
                break;
            case "Marvel Super Heroes":
                resources = new int[] {
                        R.array.heroes_questions,
                        R.array.heroes_answers,
                        R.array.heroes_correct_answers
                };
                break;
        }

        Resources res = getResources();
        questions = res.getStringArray(resources[0]);
        answers = res.getStringArray(resources[1]);
        correctAnswers = res.getIntArray(resources[2]);
    }

    @Override
    public void onAnswerSubmittedListener(int answerIndex) {
        if (answerIndex == correctAnswers[numAnswered]) {
            ++numCorrect;
        }
        ++numAnswered;

        onBeginQuiz();
    }

    @Override
    public void onBeginQuiz() {
        Bundle bundle = new Bundle();
        bundle.putString(PROMPT_MESSAGE, questions[numAnswered]);
        bundle.putStringArray(ANSWERS_MESSAGE, answers[numAnswered].split("\\|"));

        Fragment fragment = new QuestionFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.quiz_fragment_container, fragment)
                .commit();
    }
}
