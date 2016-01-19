package com.mehdiii.duelgame.views.activities.quiz.fragments;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.models.BoughtQuiz;
import com.mehdiii.duelgame.models.CourseResult;
import com.mehdiii.duelgame.models.GetBuyQuizRequest;
import com.mehdiii.duelgame.models.Quiz;
import com.mehdiii.duelgame.models.QuizResult;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.activities.quiz.fragments.adapters.CourseResultAdapter;
import com.mehdiii.duelgame.views.custom.CustomButton;
import com.mehdiii.duelgame.views.custom.CustomTextView;
import com.mehdiii.duelgame.views.dialogs.AlertDialog;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by mehdiii on 1/14/16.
 */
public class QuizResultFragment extends Fragment {

    String quizId;
    ListView listView;

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, DuelApp.getInstance().getIntentFilter());
    }

    public static QuizResultFragment getInstance() {
        return new QuizResultFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        quizId = getArguments().getString("id");
        return inflater.inflate(R.layout.fragment_quiz_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        find(view);
        configure();
        DuelApp.getInstance().sendMessage(new QuizResult(CommandType.GET_QUIZ_RESULTS, quizId).serialize());
    }

    private void find(View view) {
        listView = (ListView) view.findViewById(R.id.list_view_course_results);
    }

    private void configure() {
    }

    private BroadcastReceiver broadcastReceiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if(type == CommandType.RECEIVE_QUIZ_RESULTS) {
                Log.d("TAG", "result " + json);

                QuizResult quizResult = QuizResult.deserialize(json, QuizResult.class);
                CourseResultAdapter adapter = new CourseResultAdapter(getActivity(), R.layout.template_course_result, quizResult.getResults());
                listView.setAdapter(adapter);

                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });
}
