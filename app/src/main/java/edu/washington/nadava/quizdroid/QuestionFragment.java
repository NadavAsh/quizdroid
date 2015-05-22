package edu.washington.nadava.quizdroid;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {
    private String question;
    private String[] answers;

    private OnAnswerSubmittedListener submittedListener;

    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            submittedListener = (OnAnswerSubmittedListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnAnswerSubmittedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        question = args.getString(QuizActivity.PROMPT_MESSAGE);
        answers = args.getStringArray(QuizActivity.ANSWERS_MESSAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_question, container, false);
        TextView promptText = (TextView)view.findViewById(R.id.text_view_prompt);
        promptText.setText(question);

        final RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);
        for (int i = 0; i < answers.length; ++i) {
            RadioButton answerButton = new RadioButton(this.getActivity());
            answerButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            answerButton.setText(answers[i]);
            answerButton.setTag(i);
            radioGroup.addView(answerButton);
        }

        final Button submitButton = (Button)view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton checked =
                        (RadioButton)view.findViewById(radioGroup.getCheckedRadioButtonId());
                int tag = ((Integer)checked.getTag()).intValue();
                submittedListener.onAnswerSubmittedListener(tag);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                submitButton.setEnabled(true);
            }
        });
        return view;
    }

    public interface OnAnswerSubmittedListener {
        public void onAnswerSubmittedListener(int answerIndex);
    }
}
