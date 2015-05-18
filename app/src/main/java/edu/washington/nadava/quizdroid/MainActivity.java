package edu.washington.nadava.quizdroid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
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
import android.widget.Toast;

import edu.washington.nadava.quizdroid.topic.Topic;
import edu.washington.nadava.quizdroid.topic.TopicRepository;


public class MainActivity extends ActionBarActivity {
    public static final String TAG = "MainActivity";
    public static final String TOPIC_MESSAGE = "edu.washington.nadava.quizdroid.TOPIC";

    public static final String DOWNLOAD_FILTER = "edu.washington.nadava.quizdroid.download";
    public static final String DATA_LOCATION = "edu.washington.nadava.quizdroid.DATA_LOCATION";

    private AlarmManager alarmManager;
    private BroadcastReceiver downloadReceiver;
    private PendingIntent downloadIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadPreferences();

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
                if (topicObj.getIcon() != null) {
                    icon.setImageDrawable(topicObj.getIcon());
                }
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
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(downloadReceiver);
        alarmManager.cancel(downloadIntent);
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
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Load shared preferences and initialize the repeating download task.
     */
    private void loadPreferences() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String dataLocation = pref.getString(getString(R.string.data_location),
                "http://tednewardsandbox.site44.com/questions.json");
        int syncFreq = 30;
        try {
            syncFreq = Integer.parseInt(pref.getString(getString(R.string.sync_frequency), "30"));
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing preference 'sync_frequency': " +e .getMessage());
        }
        Log.i(TAG, "location: " + dataLocation + " frequency: " + syncFreq);

        downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "Download broadcast received.");
                Toast.makeText(MainActivity.this, intent.getStringExtra(DATA_LOCATION),
                        Toast.LENGTH_SHORT).show();
            }
        };

        registerReceiver(downloadReceiver, new IntentFilter(DOWNLOAD_FILTER));

        Intent i = new Intent();
        i.setAction(DOWNLOAD_FILTER);
        i.putExtra(DATA_LOCATION, dataLocation);

        downloadIntent = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() - 1000,
                syncFreq * 60 * 1000, downloadIntent);
    }
}
