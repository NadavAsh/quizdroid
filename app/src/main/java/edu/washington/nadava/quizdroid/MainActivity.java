package edu.washington.nadava.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import edu.washington.nadava.quizdroid.topic.Topic;
import edu.washington.nadava.quizdroid.topic.TopicRepository;


public class MainActivity extends ActionBarActivity {
    public static final String TAG = "MainActivity";
    public static final String TOPIC_MESSAGE = "edu.washington.nadava.quizdroid.TOPIC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle(getString(R.string.app_name) + " - " + getString(R.string.topics));
        QuizApp app = (QuizApp)getApplication();

        final TopicRepository repo = app.getTopicRepository();
        final ListView topicsList = (ListView)findViewById(R.id.topics_list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.icon_simple_list_item_2, android.R.id.text1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                ImageView icon = (ImageView) view.findViewById(R.id.icon);
                String topic = this.getItem(position);
                text1.setText(topic);
                Topic topicObj = repo.getTopic(topic);
                text2.setText(topicObj.getDescription());
                icon.setImageDrawable(topicObj.getIcon());
                return view;
            }
        };
        adapter.addAll(repo.getAvailableTopics());
        topicsList.setAdapter(adapter);

        topicsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String topic = adapter.getItem(position);
                Log.d(TAG, "Topic clicked: " + topic);

                Intent quizActivityIntent = new Intent(MainActivity.this, QuizActivity.class);
                quizActivityIntent.putExtra(TOPIC_MESSAGE, topic);
                startActivity(quizActivityIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
