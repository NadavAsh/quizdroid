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


public class QuizActivity extends ActionBarActivity implements
        QuestionFragment.OnAnswerSubmittedListener, TopicOverviewFragment.OnBeginQuizListener,
        AnswerFragment.OnProceedQuizListener {

    public static final String TAG = "QuizActivity";
    public static final String NUM_QUESTIONS_MESSAGE =
            "edu.washington.nadava.quizdroid.NUM_QUESTIONS";
    public static final String NUM_CORRECT_MESSAGE = "edu.washington.nadava.quizdroid.NUM_CORRECT";
    public static final String PROMPT_MESSAGE = "edu.washington.nadava.quizdroid.PROMPT";
    public static final String ANSWERS_MESSAGE = "edu.washington.nadava.quizdroid.ANSWERS";

    private int numAnswered;
    private int numCorrect;
    private String[] questions;
    private String[][] answers;
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

        String[] preAnswers = res.getStringArray(resources[1]);
        answers = new String[preAnswers.length][];
        for (int i = 0; i < preAnswers.length; ++i) {
            answers[i] = preAnswers[i].split("\\|");
        }

        correctAnswers = res.getIntArray(resources[2]);
    }

    @Override
    public void onAnswerSubmittedListener(int answerIndex) {
        int current = numAnswered;

        int correctIndex = correctAnswers[numAnswered];
        boolean correct = answerIndex == correctIndex;
        if (correct) {
            ++numCorrect;
        }
        ++numAnswered;

        Bundle bundle = new Bundle();
        bundle.putInt(NUM_QUESTIONS_MESSAGE, numAnswered);
        bundle.putInt(NUM_CORRECT_MESSAGE, numCorrect);
        bundle.putString(PROMPT_MESSAGE, questions[current]);
        bundle.putString(AnswerFragment.CHOICE_MESSAGE, answers[current][answerIndex]);
        bundle.putString(AnswerFragment.CORRECT_ANSWER_MESSAGE, answers[current][correctIndex]);
        bundle.putBoolean(AnswerFragment.CORRECT_MESSAGE, correct);
        bundle.putBoolean(AnswerFragment.LAST_MESSAGE, numAnswered >= questions.length);

        Fragment answerFragment = new AnswerFragment();
        answerFragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.quiz_fragment_container, answerFragment)
                .commit();
    }

    @Override
    public void onBeginQuiz() {
        Bundle bundle = new Bundle();
        bundle.putString(PROMPT_MESSAGE, questions[numAnswered]);
        bundle.putStringArray(ANSWERS_MESSAGE, answers[numAnswered]);

        Fragment fragment = new QuestionFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.quiz_fragment_container, fragment)
                .commit();
    }

    @Override
    public void onProceedQuiz() {
        if (numAnswered >= questions.length) {
            finish();
        } else {
            onBeginQuiz();
        }
    }
}
