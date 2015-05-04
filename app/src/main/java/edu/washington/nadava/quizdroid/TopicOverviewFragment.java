package edu.washington.nadava.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class TopicOverviewFragment extends Fragment {
    public static final String QUESTION_COUNT_MESSAGE =
            "edu.washington.nadava.quizdroid.QUESTION_COUNT";

    private String topic;
    private int questionCount;

    private OnBeginQuizListener beginQuizListener;

    public TopicOverviewFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            beginQuizListener = (OnBeginQuizListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnBeginQuizListener.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        topic = args.getString(MainActivity.TOPIC_MESSAGE);
        questionCount = args.getInt(QUESTION_COUNT_MESSAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topic_overview, container, false);

        TextView topicText = (TextView)view.findViewById(R.id.topic_title);
        topicText.setText(topic);

        TextView questionCount = (TextView)view.findViewById(R.id.text_view_questions);
        questionCount.setText(Integer.toString(this.questionCount));

        TextView description = (TextView)view.findViewById(R.id.text_view_description);
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

        Button beginButton = (Button)view.findViewById(R.id.start_quiz_button);
        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginQuizListener.onBeginQuiz();
            }
        });

        return view;
    }

    public interface OnBeginQuizListener {
        public void onBeginQuiz();
    }
}
