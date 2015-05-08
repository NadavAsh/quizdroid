package edu.washington.nadava.quizdroid;

/**
 * An interface to represent a repository of topic data for the QuizDroid application.
 */
public interface TopicRepository {
    public String[] getAvailableTopics();

    public Topic getTopic(String topic);
}
