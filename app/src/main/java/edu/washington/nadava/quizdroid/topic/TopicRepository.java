package edu.washington.nadava.quizdroid.topic;

import edu.washington.nadava.quizdroid.Topic;

/**
 * An interface to represent a repository of topic data for the QuizDroid application.
 */
public interface TopicRepository {
    public String[] getAvailableTopics();

    public Topic getTopic(String topic);
}
