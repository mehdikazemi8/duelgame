package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.models.Category;
import com.mehdiii.duelgame.models.CourseForDuel;
import com.mehdiii.duelgame.models.CourseMap;
import com.mehdiii.duelgame.models.StepCourse;
import com.mehdiii.duelgame.models.StepForDuel;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.events.OnFinishActivity;
import com.mehdiii.duelgame.utils.StepManager;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.duelofflinewaiting.DuelOfflineWaitingActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.adapters.CourseListAdapter;
import com.mehdiii.duelgame.views.activities.home.fragments.adapters.StepListAdapter;
import com.mehdiii.duelgame.views.activities.waiting.WaitingActivity;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by frshd on 3/8/16.
 */

public class StepOfflineDuelDialog extends Dialog {

    Category category;
    ListView steps;
    String opponentUserNumber;

    public StepOfflineDuelDialog(Context context,Category category, String opponentUserNumber) {
        super(context);
        this.category = category;
        this.opponentUserNumber = opponentUserNumber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_step_duel);

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        getWindow().setLayout((int) (metrics.widthPixels * 0.9),(int) (metrics.heightPixels * 0.9) );

        findControls();
        configure();
    }


    private void configure() {
        User user = AuthManager.getCurrentUser();
        CourseMap courseMap = user.getCourseMap();
        ArrayList<StepCourse> stepCourses = courseMap.getStepByCategory(category.getCategory());
        final List<CourseForDuel> courseList = new ArrayList<>();
        for ( StepCourse stepCourse: stepCourses){
            for(int j = 0; j < stepCourse.getNum_chapters() ; j++){
                CourseForDuel temp = new CourseForDuel();
                temp.setName(stepCourse.getName());
                temp.setStepId(stepCourse.getCourse_id());
                if (stepCourse.getProgress().size()!=0 && stepCourse.getProgress().size() > j)
                    temp.setStars(stepCourse.getProgress().get(j));
                else
                    temp.setStars(0);
                temp.setChapter(j);
                temp.setCategory(stepCourse.getCategory());
                temp.setBook(stepCourse.getBook());
                courseList.add(temp);
            }
        }
        CourseListAdapter adapter = new CourseListAdapter(getContext(), R.layout.template_step_list_dialog, courseList);
        steps.setAdapter(adapter);
        steps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (courseList.get(i).getStars() <= 0){
                    String message = getContext().getResources().getString(R.string.step_duel_not_passed_yet);
                    AlertDialog ad = new AlertDialog(getContext(), message);
                    ad.show();
                }else {
                    Log.d("TAG", "category to server" + category.serialize());
                    Intent intent = new Intent(getContext(), DuelOfflineWaitingActivity.class);
                    intent.putExtra("opponent_user_number", opponentUserNumber);
                    intent.putExtra("category", courseList.get(i).getCategory());
                    intent.putExtra("book", courseList.get(i).getBook());
                    intent.putExtra("chapter",courseList.get(i).getChapter());
                    intent.putExtra("master", true);
                    getContext().startActivity(intent);
                    dismiss();
                    EventBus.getDefault().post(new OnFinishActivity());
                }

            }
        });

    }

    private void findControls() {
        steps = (ListView) findViewById(R.id.steps_list);
    }

}

