package edu.washington.nadava.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AnswerActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        Intent intent = getIntent();
        final int numQuestions = intent.getIntExtra(QuizActivity.NUM_QUESTIONS_MESSAGE, 0);
        final int numCorrect = intent.getIntExtra(QuizActivity.NUM_CORRECT_MESSAGE, 0);
        final String topic = intent.getStringExtra(MainActivity.TOPIC_MESSAGE);
        String prompt = intent.getStringExtra(QuizActivity.PROMPT_MESSAGE);

        TextView promptTextView = (TextView)findViewById(R.id.promptTextView);
        promptTextView.setText(prompt);

        TextView yourAnswer = (TextView)findViewById(R.id.yourAnswerTextView);
        yourAnswer.setText(intent.getStringExtra(QuizActivity.CHOICE_MESSAGE));

        TextView correctAnswer = (TextView)findViewById(R.id.correctAnswerTextView);
        correctAnswer.setText(intent.getStringExtra(QuizActivity.ANSWER_MESSAGE));

        if (!intent.getBooleanExtra(QuizActivity.CORRECT_MESSAGE, true)) {
            TextView correct = (TextView)findViewById(R.id.correctTextView);
            correct.setText(R.string.answered_incorrectly);
        }

        TextView statsText = (TextView)findViewById(R.id.statsTextView);
        statsText.setText(
                String.format(getString(R.string.quiz_stats), numCorrect, numQuestions));

        Button nextButton = (Button)findViewById(R.id.nextButton);
        if (numQuestions < 3) {
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent quizIntent = new Intent(AnswerActivity.this, QuizActivity.class);
                    quizIntent.putExtra(MainActivity.TOPIC_MESSAGE, topic);
                    quizIntent.putExtra(QuizActivity.NUM_QUESTIONS_MESSAGE, numQuestions);
                    quizIntent.putExtra(QuizActivity.NUM_CORRECT_MESSAGE, numCorrect);
                    startActivity(quizIntent);
                    finish();
                }
            });
        } else {
            nextButton.setText(R.string.finish_quiz);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
