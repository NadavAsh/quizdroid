package edu.washington.nadava.quizdroid;

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
            answerButton.setTag(i);
            radioGroup.addView(answerButton);
        }

        Button submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton checked =
                        (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                int tag = ((Integer)checked.getTag()).intValue();
                Log.d(TAG, "Submitted answer #" + tag);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
