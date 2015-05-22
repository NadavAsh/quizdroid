package edu.washington.nadava.quizdroid;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.*;
import android.database.Cursor;
import android.net.*;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import edu.washington.nadava.quizdroid.topic.Topic;
import edu.washington.nadava.quizdroid.topic.TopicRepository;


public class MainActivity extends ActionBarActivity {
    public static final String TAG = "MainActivity";
    public static final String TOPIC_MESSAGE = "edu.washington.nadava.quizdroid.TOPIC";

    public static final String ALARM_FILTER = "edu.washington.nadava.quizdroid.alarm";
    public static final String DATA_LOCATION = "edu.washington.nadava.quizdroid.DATA_LOCATION";

    public static final String DOWNLOAD_FILTER = "edu.washington.nadava.quizdroid.download";

    private AlarmManager alarmManager;
    private BroadcastReceiver alarmReceiver;
    private PendingIntent alarmIntent;

    private DownloadManager downloadManager;
    private BroadcastReceiver downloadReceiver;
    private PendingIntent downloadIntent;

    private String downloadLocation;
    private long downloadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDownload();


        this.setTitle(getString(R.string.app_name) + " - " + getString(R.string.topics));
        QuizApp app = (QuizApp)getApplication();
        app.loadTopicRepo(getFileStreamPath("questions.json"));

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
        if (alarmReceiver != null) unregisterReceiver(alarmReceiver);
        if (downloadReceiver != null) unregisterReceiver(downloadReceiver);
        if (alarmManager != null && alarmIntent != null) alarmManager.cancel(alarmIntent);
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
        downloadLocation = getString(R.string.download_location);
        String dataLocation = pref.getString(getString(R.string.data_location), downloadLocation);
        int syncFreq = 30;
        try {
            syncFreq = Integer.parseInt(pref.getString(getString(R.string.sync_frequency), "30"));
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing preference 'sync_frequency': " +e .getMessage());
        }
        Log.i(TAG, "location: " + dataLocation + " frequency: " + syncFreq);

        alarmReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "Alarm broadcast received.");
                String dataLocation = intent.getStringExtra(DATA_LOCATION);
                Toast.makeText(MainActivity.this, dataLocation, Toast.LENGTH_SHORT).show();

                DownloadManager.Request request =
                        new DownloadManager.Request(Uri.parse(dataLocation));

                request.setTitle("QuizDroid")
                        .setDescription("Downloading quizdroid data.")
                        .setDestinationInExternalFilesDir(MainActivity.this,
                                Environment.DIRECTORY_DOWNLOADS, "questions.json");

                downloadId = downloadManager.enqueue(request);
            }
        };

        registerReceiver(alarmReceiver, new IntentFilter(ALARM_FILTER));

        Intent i = new Intent();
        i.setAction(ALARM_FILTER);
        i.putExtra(DATA_LOCATION, dataLocation);
        alarmIntent = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() - 10000,
                syncFreq * 60 * 1000, alarmIntent);
        try{
            alarmIntent.send();
        } catch (Exception e) { }
    }

    private void setupDownloadManager() {
        downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == downloadId) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor c = downloadManager.query(query);

                    if(c.moveToFirst()) {
                        int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        Log.d("DM Sample", "Status Check: " + status);
                        switch (status) {
                            case DownloadManager.STATUS_SUCCESSFUL:;
                                Uri uri;
                                ParcelFileDescriptor parcel;
                                uri = downloadManager.getUriForDownloadedFile(downloadId);
                                try {
                                    parcel = downloadManager.openDownloadedFile(downloadId);
                                    FileInputStream stream = new FileInputStream(parcel.getFileDescriptor());
                                    FileOutputStream out = new FileOutputStream(getFileStreamPath("questions.json"));

                                    while (stream.available() > 0) {
                                        byte[] buf = new byte[stream.available()];
                                        int read = stream.read(buf);
                                        out.write(buf, 0, read);
                                    }

                                    stream.close();
                                    out.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case DownloadManager.STATUS_FAILED:
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("File download failed! Goodbye...");
                                builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                builder.create().show();
                                break;
                        }
                    }
                }
            }
        };

        registerReceiver(downloadReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void initDownload() {
        // Check airplane mode:
        boolean airplane = Settings.System.getInt(getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;

        ConnectivityManager connManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = connManager.getActiveNetworkInfo();

        if (net == null || !net.isConnectedOrConnecting()) {
            Log.i(TAG, "Not connected to the internet");

            if (airplane) {
                Log.i(TAG, "In airplane mode");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("In airplane mode. Would you like to go to Settings to disable it?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS));
                        initDownload();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.create().show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("No internet! Goodbye...");
                builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.create().show();
            }
        } else {
            setupDownloadManager();
            loadPreferences();
        }
    }
}
