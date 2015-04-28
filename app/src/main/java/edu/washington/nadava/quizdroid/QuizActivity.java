package edu.washington.nadava.quizdroid;

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


public class QuizActivity extends ActionBarActivity {
    public static final String TAG = "QuizActivity";
    public static final String ANSWER_MESSAGE = "edu.washington.nadava.quizdroid.ANSWER";
    public static final String CHOICE_MESSAGE = "edu.washington.nadava.quizdroid.CHOICE";
    public static final String CORRECT_MESSAGE = "edu.washington.nadava.quizdroid.CORRECT";
    public static final String NUM_QUESTIONS_MESSAGE =
            "edu.washington.nadava.quizdroid.NUM_QUESTIONS";
    public static final String NUM_CORRECT_MESSAGE = "edu.washington.nadava.quizdroid.NUM_CORRECT";
    public static final String PROMPT_MESSAGE = "edu.washington.nadava.quizdroid.PROMPT";

    private int numQuestions;
    private int numCorrect;
    private String question;
    private String[] answers;
    private int correctAnswer;
    private String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        processIntent();

        TextView promptText = (TextView)findViewById(R.id.text_view_prompt);
        promptText.setText(question);

        final RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        for (int i = 0; i < answers.length; ++i) {
            RadioButton answerButton = new RadioButton(this);
            answerButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            answerButton.setText(answers[i]);
            answerButton.setTag(i);
            radioGroup.addView(answerButton);
        }

        final Button submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton checked =
                        (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                int tag = ((Integer)checked.getTag()).intValue();
                Log.d(TAG, "Submitted answer #" + tag);

                Intent answerIntent = new Intent(QuizActivity.this, AnswerActivity.class);
                answerIntent.putExtra(MainActivity.TOPIC_MESSAGE, topic);
                answerIntent.putExtra(PROMPT_MESSAGE, question);
                answerIntent.putExtra(NUM_QUESTIONS_MESSAGE, numQuestions + 1);
                answerIntent.putExtra(NUM_CORRECT_MESSAGE, tag == correctAnswer ? numCorrect + 1 :
                                      numCorrect);
                answerIntent.putExtra(ANSWER_MESSAGE, answers[correctAnswer]);
                answerIntent.putExtra(CHOICE_MESSAGE, checked.getText().toString());
                answerIntent.putExtra(CORRECT_MESSAGE, tag == correctAnswer);
                startActivity(answerIntent);
                finish();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                submitButton.setEnabled(true);
            }
        });
    }

    private void processIntent() {
        Intent intent = getIntent();
        numQuestions = intent.getIntExtra(NUM_QUESTIONS_MESSAGE, 0);
        numCorrect = intent.getIntExtra(NUM_CORRECT_MESSAGE, 0);

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
        question = res.getStringArray(resources[0])[numQuestions];
        answers = res.getStringArray(resources[1])[numQuestions].split("\\|");
        correctAnswer = res.getIntArray(resources[2])[numQuestions];
    }
}
