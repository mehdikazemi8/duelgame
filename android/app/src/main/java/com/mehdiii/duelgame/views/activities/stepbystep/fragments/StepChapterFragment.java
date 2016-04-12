package com.mehdiii.duelgame.views.activities.stepbystep.fragments;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.models.ChapterForStep;
import com.mehdiii.duelgame.models.CourseChapterQuestion;
import com.mehdiii.duelgame.models.CourseMap;
import com.mehdiii.duelgame.models.QuestionForQuiz;
import com.mehdiii.duelgame.models.Quiz;
import com.mehdiii.duelgame.models.StepProblemCollection;
import com.mehdiii.duelgame.models.UseDiamond;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.CategoryManager;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.utils.StepManager;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.stepbystep.fragments.adapters.StepChapterListAdapter;
import com.mehdiii.duelgame.views.custom.CustomTextView;
import com.mehdiii.duelgame.views.dialogs.AlertDialog;
import com.mehdiii.duelgame.views.dialogs.RetakeStepDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by frshd on 4/10/16.
 */
public class StepChapterFragment extends Fragment {

    private final int RETAKE_DIAMOND = 290;
    Quiz quiz;
    Fragment stepFragment;
    CustomTextView header;
    ListView chaptersList;
    ArrayList<ChapterForStep> chapterForSteps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step_chapter, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
//        viewWidth = metrics.widthPixels;
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, DuelApp.getInstance().getIntentFilter());
        find(view);
        configure();
    }

    private void find(View view) {
        header = (CustomTextView) view.findViewById(R.id.chapter_header);
        chaptersList = (ListView) view.findViewById(R.id.chapter_list);

    }

    private void configure() {
        header.setText(getArguments().getString("courseName"));
        int chapterCount = getArguments().getInt("chapterCount");
        ArrayList<Integer> stars = getArguments().getIntegerArrayList("chapterStars");

        chapterForSteps = new ArrayList<>();
        for (int i=0; i<chapterCount; i++){
            ChapterForStep cfs = new ChapterForStep();
            cfs.setName(String.valueOf(i+1));
            cfs.setStars(stars.get(i));
            chapterForSteps.add(i,cfs);
        }
        StepChapterListAdapter adapter = new StepChapterListAdapter(getActivity(), R.layout.template_step_chapter,chapterForSteps);
        chaptersList.setAdapter(adapter);
        chaptersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TAG", "clicked " + i);

                checkRetake(i);
//                Bundle bundle = new Bundle();
//                bundle.putString("courseName", courseMap.getStepCourses().get(i).getName());
//                bundle.putIntegerArrayList("chapterStars", courseMap.getStepCourses().get(i).getProgress());
//                bundle.putInt("chapterCount", courseMap.getStepCourses().get(i).getNum_chapters());

//                chapterFragment = StepChapterFragment.getInstance();
//                chapterFragment.setArguments(bundle);
//                getSupportFragmentManager().beginTransaction()
//                        .add( R.id.chapter_fragment_holder, chapterFragment)
//                        .addToBackStack(null).commit();

            }
        });
    }

    private void checkRetake(final int i) {

        int stars = chapterForSteps.get(i).getStars();
        if (stars > 0) {
            final RetakeStepDialog retakeStepDialog = new RetakeStepDialog(getActivity(),
                    String.format(getResources().getString(R.string.caption_diamond_cnt), AuthManager.getCurrentUser().getDiamond()) +
                            getResources().getString(R.string.caption_retake_step));

            retakeStepDialog.setOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(Object data) {
                    if ((boolean) data) {
                        if (AuthManager.getCurrentUser().getDiamond() >= RETAKE_DIAMOND) {
                            UseDiamond ud = new UseDiamond();
                            AuthManager.getCurrentUser().decreaseDiamond(RETAKE_DIAMOND);
                            ud.setAmount(RETAKE_DIAMOND);
                            ud.setCommand(CommandType.USE_DIAMOND);
                            DuelApp.getInstance().sendMessage(ud.serialize());
                            Log.d("TAG", "used diamond" + ud.serialize() + AuthManager.getCurrentUser().getDiamond());
                            startStep(i);
                        } else {
                            String message = getResources().getString(R.string.not_enough_diamond);
                            AlertDialog ad = new AlertDialog(getActivity(), message);
                            ad.show();
                        }
                    }
                }
            });
            retakeStepDialog.show();
        }
    }

//    @Override
//    public void onClick(View view) {
//
//    }
    private void startStep(int i) {
        CourseChapterQuestion ccq = new CourseChapterQuestion();
        ccq.setCourse_id(getArguments().getString("courseId"));
        ccq.setChapter_index(i);
        DuelApp.getInstance().sendMessage(ccq.serialize(CommandType.GET_COURSE_CHAPTER_QUESTION));

//        FOR INITIAL TEST TILL ARIAN COME
//        Bundle bundle = new Bundle();
//        ArrayList<QuestionForQuiz> parsedQuestions = new ArrayList<>();
//        Map<String, Integer> questionArchive;
//        questionArchive = new HashMap<>();
//        questionArchive.put("1000411", R.array.cbc1000411);
//        parsedQuestions = getQuestions(questionArchive.get("10004111"));
//        quiz = new Quiz();
//        bundle.putString("quiz", quiz.serialize());
//        stepFragment = StepFragment.getInstance();
//        stepFragment.setArguments(bundle);
//        getActivity().getSupportFragmentManager().beginTransaction().
//                add(R.id.step_fragment_holder, stepFragment).
//                addToBackStack(null).commit();

    }
    public static StepChapterFragment getInstance() {
        return new StepChapterFragment();
    }

    private BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            Bundle bundle = new Bundle();
            StepProblemCollection spc = StepProblemCollection.deserialize(json, StepProblemCollection.class);
            Log.d("TAG", "questions recieved" + spc.serialize());
            quiz = new Quiz();
            quiz.setQuestions(spc.getQuestions());

            bundle.putString("quiz", quiz.serialize());
            bundle.putString("courseId", getArguments().getString("courseId"));
            bundle.putInt("chapterIndex", spc.getChapter_index());
            bundle.putInt("category", getArguments().getInt("category"));
            bundle.putInt("book", getArguments().getInt("book") );

            stepFragment = StepFragment.getInstance();
            stepFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().
                    add(R.id.step_fragment_holder, stepFragment).
                    addToBackStack(null).commit();
        }
    });

    public ArrayList<QuestionForQuiz> getQuestions(int stepId) {
        String[] questions = getResources().getStringArray(stepId);
        ArrayList<QuestionForQuiz> parsedQuestions = new ArrayList<>();
        for (String question : questions) {
            String[] splitted = question.split("###");
            QuestionForQuiz newquestion = new QuestionForQuiz();
            ArrayList<String> options = new ArrayList<>();
            for (int iterator = 0; iterator < splitted.length; iterator++) {
                if (iterator == 0)
                    newquestion.setQuestionText(splitted[iterator]);
                else if (iterator == 1)
                    newquestion.setAnswer(splitted[iterator]);
                if (iterator > 0)
                    options.add(splitted[iterator]);
            }
            newquestion.setOptions(options);
            newquestion.setCategory("10004");
            newquestion.setCourseName(CategoryManager.getCategory(getActivity(), 10004));
            newquestion.setDescription("bello");
            parsedQuestions.add(newquestion);
        }
        return parsedQuestions;
    }
}
