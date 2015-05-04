package edu.washington.nadava.quizdroid;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnswerFragment extends Fragment {
    public static final String CHOICE_MESSAGE = "edu.washington.nadava.quizdroid.CHOICE";
    public static final String CORRECT_ANSWER_MESSAGE =
            "edu.washington.nadava.quizdroid.CORRECT_ANSWER";
    public static final String CORRECT_MESSAGE = "edu.washington.nadava.quizdroid.CORRECT";
    public static final String LAST_MESSAGE = "edu.washington.nadava.quizdroid.LAST";

    private int numQuestions;
    private int numCorrect;
    private String prompt;
    private String choice;
    private String answer;
    private boolean correct;
    private boolean last;

    private OnProceedQuizListener proceedListener;

    public AnswerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            proceedListener = (OnProceedQuizListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " should implement OnProceedQuizListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        numQuestions = args.getInt(QuizActivity.NUM_QUESTIONS_MESSAGE);
        numCorrect = args.getInt(QuizActivity.NUM_CORRECT_MESSAGE);
        prompt = args.getString(QuizActivity.PROMPT_MESSAGE);
        choice = args.getString(CHOICE_MESSAGE);
        answer = args.getString(CORRECT_ANSWER_MESSAGE);
        correct = args.getBoolean(CORRECT_MESSAGE);
        last = args.getBoolean(LAST_MESSAGE, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_answer, container, false);

        TextView promptTextView = (TextView)view.findViewById(R.id.promptTextView);
        promptTextView.setText(prompt);

        TextView yourAnswer = (TextView)view.findViewById(R.id.yourAnswerTextView);
        yourAnswer.setText(choice);

        TextView correctAnswer = (TextView)view.findViewById(R.id.correctAnswerTextView);
        correctAnswer.setText(answer);

        if (!correct) {
            TextView correct = (TextView)view.findViewById(R.id.correctTextView);
            correct.setText(R.string.answered_incorrectly);
        }

        TextView statsText = (TextView)view.findViewById(R.id.statsTextView);
        statsText.setText(
                String.format(getString(R.string.quiz_stats), numCorrect, numQuestions));

        Button nextButton = (Button)view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedListener.onProceedQuiz();
            }
        });

        if (last) {
            nextButton.setText(R.string.finish_quiz);
        }


        return view;
    }

    public interface OnProceedQuizListener {
        public void onProceedQuiz();
    }
}
