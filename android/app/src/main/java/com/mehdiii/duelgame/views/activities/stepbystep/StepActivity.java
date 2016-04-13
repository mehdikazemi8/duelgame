package com.mehdiii.duelgame.views.activities.stepbystep;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.async.GetQuizQuestions;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.CourseMap;
import com.mehdiii.duelgame.models.ProgressForStep;
import com.mehdiii.duelgame.models.StepCourse;
import com.mehdiii.duelgame.models.StepForDuel;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.adapters.StepListAdapter;
import com.mehdiii.duelgame.views.activities.stepbystep.fragments.StepChapterFragment;
import com.mehdiii.duelgame.views.activities.stepbystep.fragments.StepFragment;
import com.mehdiii.duelgame.views.activities.stepbystep.fragments.adapters.ProgressListAdapter;
import com.mehdiii.duelgame.views.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frshd on 4/5/16.
 */
public class StepActivity extends ParentActivity implements View.OnClickListener {

    ListView courseList;
    Fragment chapterFragment;
    CourseMap courseMap;
    List<ProgressForStep> progressList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Log.d("TAG", "step progress" + AuthManager.getCurrentUser().getCourseMap().serialize());

        find();
        configure();
    }

    private void find() {
        courseList = (ListView) findViewById(R.id.course_list);
    }

    private void configure() {

        progressList = new ArrayList<>();
        courseMap = AuthManager.getCurrentUser().getCourseMap();
        for(StepCourse cm : courseMap.getStepCourses()){
            Log.d("TAG", "step courses " + cm.getName() +  cm.getProgress());
            int stars = 0;
            for(int s : cm.getProgress()){
                stars += s;
            }
            ProgressForStep sfd = new ProgressForStep();
            sfd.setStars(stars);
            sfd.setTotalStars(cm.getNum_chapters());
            sfd.setName(cm.getName());
            progressList.add(sfd);
        }
        ProgressListAdapter adapter = new ProgressListAdapter(this, R.layout.template_step_progress, progressList);
        courseList.setAdapter(adapter);
        courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TAG", "clicked "+i);

                Bundle bundle = new Bundle();
                bundle.putString("courseName", courseMap.getStepCourses().get(i).getName());
                bundle.putString("courseId", courseMap.getStepCourses().get(i).getCourse_id());
                bundle.putIntegerArrayList("chapterStars", courseMap.getStepCourses().get(i).getProgress());
                bundle.putInt("chapterCount", courseMap.getStepCourses().get(i).getNum_chapters());
                bundle.putInt("category", courseMap.getStepCourses().get(i).getCategory());
                bundle.putInt("book", courseMap.getStepCourses().get(i).getBook());
                chapterFragment = StepChapterFragment.getInstance();
                chapterFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .add( R.id.chapter_fragment_holder, chapterFragment)
                        .addToBackStack(null).commit();
            }
            });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cbc1000411_holder:
                break;
        }
    }



}
