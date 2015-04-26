package edu.washington.nadava.quizdroid;

import android.content.Intent;
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

    private String correctAnswer;
    private int numQuestions;
    private int numCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        TextView promptText = (TextView)findViewById(R.id.text_view_prompt);
        promptText.setText("Hello?");

        final RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        for (int i = 0; i < 4; ++i) {
            RadioButton answerButton = new RadioButton(this);
            answerButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            answerButton.setText("Answer " + i);
            answerButton.setTag(i == 2);
            if (i == 2) {
                correctAnswer = answerButton.getText().toString();
            }
            radioGroup.addView(answerButton);
        }

        final Button submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton checked =
                        (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                boolean tag = ((Boolean)checked.getTag()).booleanValue();
                Log.d(TAG, "Submitted answer #" + tag);

                Intent answerIntent = new Intent(QuizActivity.this, AnswerActivity.class);
                answerIntent.putExtra(ANSWER_MESSAGE, correctAnswer);
                answerIntent.putExtra(CHOICE_MESSAGE, checked.getText().toString());
                answerIntent.putExtra(CORRECT_MESSAGE, tag);
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
    }
}
