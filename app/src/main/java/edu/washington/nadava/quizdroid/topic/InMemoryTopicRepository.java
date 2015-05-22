package edu.washington.nadava.quizdroid.topic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by nadavash on 5/7/15.
 */
public class InMemoryTopicRepository implements TopicRepository {
    private HashMap<String, Topic> topics;

    public InMemoryTopicRepository() {
        topics = new HashMap<>();

        List<Question> mathQuestions = new ArrayList<>();
        mathQuestions.add(new Question(
                "What is the result of 1 + 2?",
                Arrays.asList("1","2","3","4"), 2));
        mathQuestions.add(new Question(
                "What is the value of PI?",
                Arrays.asList("3.1415...","2.7182...","0.5772...","1.61803..."), 0));
        mathQuestions.add(new Question("Where was Paul Erdos born?",
                Arrays.asList("Bulgaria","England","Germany","Hungary"), 3));
        Topic math = new Topic("Math", "Math problems and history.",
                               null, mathQuestions);


        List<Question> physicsQuestions = new ArrayList<>();
        physicsQuestions.add(new Question(
                "Who came up with the theory of relativity?",
                Arrays.asList("Isaac Newton","Albert Einstein","Nikola Tesla","Stephen Hawking"),
                1));
        physicsQuestions.add(new Question(
                "What is the Earth's gravitational acceleration?",
                Arrays.asList("9.8m/s^2","5.2m/s^2","15m/s^2","2.1m/s^2"), 0));
        physicsQuestions.add(new Question(
                "Who discovered black holes?",
                Arrays.asList("Stephen Hawking","Isaac Newton","Albert Einstein","Nikola Tesla"),
                2));
        Topic physics = new Topic("Physics", "Physics problems and history.",
                                  null, physicsQuestions);

        List<Question> heroesQuestions = new ArrayList<>();
        heroesQuestions.add(new Question(
                "What's Iron Man's real name?",
                Arrays.asList("Peter Parker","John Doe","Robert Banner","Tony Stark"), 3));
        heroesQuestions.add(new Question(
                "How did Spider-Man get his powers?",
                Arrays.asList("Radioactive spider", "A magical spell", "A nuclear bomb", "Pollution"),
                0));
        heroesQuestions.add(new Question(
                "What makes the Robert Bruce Banner become the Hulk?",
                Arrays.asList("Loud noises","Anger","Spicy food","Opera vocals"),
                1));
        Topic superHeroes = new Topic("Marvel Super Heroes",
                "Super heroes, comic books and movies.", null, heroesQuestions);


        topics.put("Marvel Super Heroes", superHeroes);
        topics.put("Physics", physics);
        topics.put("Math", math);
    }

    public Set<String> getAvailableTopics() {
        return new TreeSet<String>(topics.keySet());
    }

    public Topic getTopic(String topic) {
        return topics.get(topic);
    }
}
