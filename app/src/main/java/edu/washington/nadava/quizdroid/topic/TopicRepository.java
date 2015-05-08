package edu.washington.nadava.quizdroid.topic;

import java.util.Set;

/**
 * An interface to represent a repository of topic data for the QuizDroid application.
 */
public interface TopicRepository {
    public Set<String> getAvailableTopics();

    public Topic getTopic(String topic);
}
