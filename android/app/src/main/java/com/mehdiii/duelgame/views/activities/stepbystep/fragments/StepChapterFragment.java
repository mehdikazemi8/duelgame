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
import com.mehdiii.duelgame.models.events.OnStepCompleted;
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

import de.greenrobot.event.EventBus;


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
            cfs.setName(String.valueOf(i + 1));
            if (stars.size()!=0 && stars.size()>i) {
                cfs.setStars(stars.get(i));
            }
            else if(stars.size()!=0 && stars.size()==i)
                cfs.setStars(0);
            else if(stars.size()==0 && i==0)
                cfs.setStars(0);
            else
                cfs.setStars(-1);
            chapterForSteps.add(i,cfs);
        }
        StepChapterListAdapter adapter = new StepChapterListAdapter(getActivity(), R.layout.template_step_chapter,chapterForSteps);
        chaptersList.setAdapter(adapter);
        chaptersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checkRetake(i);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    public void onPause() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onDestroy();
        EventBus.getDefault().register(this);
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
        else if (stars == -1)
            return;
        else
            startStep(i);
    }

    private void startStep(int i) {
        CourseChapterQuestion ccq = new CourseChapterQuestion();
        ccq.setCourse_id(getArguments().getString("courseId"));
        ccq.setChapter_index(i);
        DuelApp.getInstance().sendMessage(ccq.serialize(CommandType.GET_COURSE_CHAPTER_QUESTION));
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
            bundle.putInt("book", getArguments().getInt("book"));
            int chapInd = spc.getChapter_index() + 1;
            bundle.putString("stepName", getArguments().getString("courseName")+" درس"+chapInd);
            stepFragment = StepFragment.getInstance();
            stepFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().
                    add(R.id.step_fragment_holder, stepFragment).
                    addToBackStack(null).commit();
        }
    });


    public void onEvent(OnStepCompleted event) {
        putStepStars();
    }

    private void putStepStars() {
        int chapterCount = getArguments().getInt("chapterCount");
        ArrayList<Integer> stars = AuthManager.getCurrentUser()
                .getCourseMap()
                .getStepByCategoryAndBook(getArguments().getInt("category"), getArguments().getInt("book"))
                .getProgress();

        chapterForSteps = new ArrayList<>();
        ArrayList <Integer> intArray = new ArrayList<>();

        for (int i=0; i < chapterCount; i++) {
            ChapterForStep cfs = new ChapterForStep();
            cfs.setName(String.valueOf(i + 1));

            if(stars.size() == 0) {
                if(i == 0) {
                    cfs.setStars(0);
                } else {
                    cfs.setStars(-1);
                }
            } else {
                if(i < stars.size()) {
                    cfs.setStars(stars.get(i));
                } else if(i == stars.size()) {
                    cfs.setStars(0);
                } else {
                    cfs.setStars(-1);
                }
            }
            chapterForSteps.add(cfs);
        }

//        AuthManager.getCurrentUser()
//                .getCourseMap()
//                .getStepByCategoryAndBook(getArguments().getInt("category"), getArguments().getInt("book"))
//                .setProgress(intArray);

        StepChapterListAdapter adapter = new StepChapterListAdapter(getActivity(), R.layout.template_step_chapter,chapterForSteps);
        chaptersList.setAdapter(adapter);
    }
}
