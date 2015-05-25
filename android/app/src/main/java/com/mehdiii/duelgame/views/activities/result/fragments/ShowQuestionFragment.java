package com.mehdiii.duelgame.views.activities.result.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.views.custom.FontFitButton;

import java.util.ArrayList;

/**
 * Created by MeHdi on 5/25/2015.
 */
public class ShowQuestionFragment extends Fragment {

    public static String ARGUMENT_STATEMENT = "argument_statement";
    public static String ARGUMENT_OPTIONS = "argument_options";
    public static String ARGUMENT_CORRECT_OPTION = "argument_correct_option";

    ArrayList<FontFitButton> optionsBtn = new ArrayList<FontFitButton>();
    TextView statementTextView;

    ArrayList<String> optionsStr;
    String statementStr;
    int correctOption;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("trace", "onCreateView ShowQuestionFragment");
        return inflater.inflate(R.layout.fragment_show_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("trace", "onViewCreated ShowQuestionFragment");

        findControls(view);
        readArguments();
        loadQuestion();
    }

    private void findControls(View view) {
        Log.d("trace", "findControls ShowQuestionFragment");

        optionsBtn.add((FontFitButton) view.findViewById(R.id.review_option_0));
        optionsBtn.add((FontFitButton) view.findViewById(R.id.review_option_1));
        optionsBtn.add((FontFitButton) view.findViewById(R.id.review_option_2));
        optionsBtn.add((FontFitButton) view.findViewById(R.id.review_option_3));

        for(int i = 0; i < 4; i ++)
            optionsBtn.get(i).setClickable(false);

        statementTextView = (TextView) view.findViewById(R.id.review_question_text);
    }

    private void readArguments() {
        Log.d("trace", "readArguments ShowQuestionFragment");

        Bundle bundle = getArguments();
        optionsStr = bundle.getStringArrayList(ARGUMENT_OPTIONS);
        statementStr = bundle.getString(ARGUMENT_STATEMENT);
        correctOption = bundle.getInt(ARGUMENT_CORRECT_OPTION);
    }

    private void loadQuestion() {
        Log.d("trace", "loadQuestion ShowQuestionFragment");

        for(int i = 0; i < 4; i ++)
            Log.d("option", optionsStr.get(i));

        for (int i = 0; i < 4; i++)
            optionsBtn.get(i).setText(optionsStr.get(i));
        statementTextView.setText(statementStr);
        optionsBtn.get(correctOption).setTextColor(getResources().getColor(R.color.correct_answer));
    }
}
