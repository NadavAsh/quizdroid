package edu.washington.nadava.quizdroid.topic;

import android.content.res.Resources;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.*;

/**
 * Created by nadavash on 5/7/15.
 */
public class JsonTopicRepository implements TopicRepository {
    public HashMap<String, Topic> topics;

    public JsonTopicRepository(InputStream reader) throws JSONException {
        topics = new HashMap<>();

        String text = new Scanner(reader).useDelimiter("\\A").next();
        JSONArray jsonTopics = new JSONArray(text);
        for (int i = 0; i < jsonTopics.length(); ++i) {
            Topic topic = Topic.fromJson(jsonTopics.getJSONObject(i));
            topics.put(topic.getTitle(), topic);
        }
    }

    @Override
    public Set<String> getAvailableTopics() {
        return new TreeSet<>(topics.keySet());
    }

    @Override
    public Topic getTopic(String topic) {
        return topics.get(topic);
    }
}
