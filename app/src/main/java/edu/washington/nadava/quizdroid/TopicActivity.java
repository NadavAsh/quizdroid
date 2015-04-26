package edu.washington.nadava.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;


public class TopicActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        Intent intent = getIntent();
        final String topic = intent.getStringExtra(MainActivity.TOPIC_MESSAGE);
        TextView topicText = (TextView)findViewById(R.id.topic_title);
        topicText.setText(topic);

        TextView questionCount = (TextView)findViewById(R.id.text_view_questions);
        questionCount.setText("1");

        TextView description = (TextView)findViewById(R.id.text_view_description);
        switch (topic) {
            case "Math":
                description.setText(R.string.topic_math_description);
                break;
            case "Physics":
                description.setText(R.string.topic_physics_description);
                break;
            case "Marvel Super Heroes":
                description.setText(R.string.topic_super_heroes_description);
                break;
        }

        Button beginButton = (Button)findViewById(R.id.start_quiz_button);
        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quizIntent = new Intent(TopicActivity.this, QuizActivity.class);
                quizIntent.putExtra(MainActivity.TOPIC_MESSAGE, topic);
                startActivity(quizIntent);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topic, menu);
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
