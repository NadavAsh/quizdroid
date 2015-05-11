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

import java.util.List;

import edu.washington.nadava.quizdroid.topic.Question;
import edu.washington.nadava.quizdroid.topic.Topic;


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
    private Topic topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        processIntent();

        Bundle args = new Bundle();
        args.putString(MainActivity.TOPIC_MESSAGE, topic.getTitle());
        args.putInt(TopicOverviewFragment.QUESTION_COUNT_MESSAGE, topic.getQuestions().size());

        args.putString(TopicOverviewFragment.DESCRIPTION_MESSAGE,
                topic.getLongDescription() == null ?
                        topic.getDescription() : topic.getLongDescription());

        Fragment topicFragment = new TopicOverviewFragment();
        topicFragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.quiz_fragment_container, topicFragment)
                .commit();
    }

    private void processIntent() {
        Intent intent = getIntent();

        int[] resources = null;
        String topicTitle = intent.getStringExtra(MainActivity.TOPIC_MESSAGE);
        topic = ((QuizApp)getApplication()).getTopicRepository().getTopic(topicTitle);
    }

    @Override
    public void onAnswerSubmittedListener(int answerIndex) {
        int current = numAnswered;

        Question question = topic.getQuestions().get(numAnswered);
        int correctIndex = question.getCorrect();
        boolean correct = answerIndex == correctIndex;
        if (correct) {
            ++numCorrect;
        }
        ++numAnswered;

        Bundle bundle = new Bundle();
        bundle.putInt(NUM_QUESTIONS_MESSAGE, numAnswered);
        bundle.putInt(NUM_CORRECT_MESSAGE, numCorrect);
        bundle.putString(PROMPT_MESSAGE, question.getQuestion());
        bundle.putString(AnswerFragment.CHOICE_MESSAGE, question.getAnswers().get(answerIndex));
        bundle.putString(AnswerFragment.CORRECT_ANSWER_MESSAGE,
                         question.getAnswers().get(correctIndex));
        bundle.putBoolean(AnswerFragment.CORRECT_MESSAGE, correct);
        bundle.putBoolean(AnswerFragment.LAST_MESSAGE, numAnswered >= topic.getQuestions().size());

        Fragment answerFragment = new AnswerFragment();
        answerFragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out)
                .replace(R.id.quiz_fragment_container, answerFragment)
                .commit();
    }

    @Override
    public void onBeginQuiz() {
        Bundle bundle = new Bundle();
        bundle.putString(PROMPT_MESSAGE, topic.getQuestions().get(numAnswered).getQuestion());

        List<String> answers =  topic.getQuestions().get(numAnswered).getAnswers();
        bundle.putStringArray(ANSWERS_MESSAGE, answers.toArray(new String[answers.size()]));

        Fragment fragment = new QuestionFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out)
                .replace(R.id.quiz_fragment_container, fragment)
                .commit();
    }

    @Override
    public void onProceedQuiz() {
        if (numAnswered >= topic.getQuestions().size()) {
            finish();
        } else {
            onBeginQuiz();
        }
    }
}
