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
import com.mehdiii.duelgame.models.WannaChallenge;
import com.mehdiii.duelgame.utils.StepManager;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.adapters.CourseListAdapter;
import com.mehdiii.duelgame.views.activities.home.fragments.adapters.StepListAdapter;
import com.mehdiii.duelgame.views.activities.waiting.WaitingActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frshd on 3/6/16.
 */
public class StepDuelDialog extends Dialog {

    Category category;
    ListView steps;
    String opponentUserNumeber;
    boolean isMaster = false;
    boolean challengeDirectInZaban = false;

    public StepDuelDialog(Context context, Category category) {
        super(context);
        this.category = category;

        Log.d("TAG", "abcd StepDuelDialog");
    }

    public StepDuelDialog(Context context, WannaChallenge challenge, String opponentUserNumeber, boolean isMaster) {
        super(context);
        this.category = new Category();

        category.setCategory(challenge.getCategory());

        this.opponentUserNumeber = opponentUserNumeber;
        this.isMaster = isMaster;
        this.challengeDirectInZaban = true;

        Log.d("TAG", "abcd StepDuelDialog");
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
                if (stepCourse.getProgress().size()!=0  && stepCourse.getProgress().size()>j)
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

                Log.d("TAG", "abcd challenge in zaban 11111");

                if (courseList.get(i).getStars() <= 0){
                    String message = getContext().getResources().getString(R.string.step_duel_not_passed_yet);
                    AlertDialog ad = new AlertDialog(getContext(), message);
                    ad.show();
                }else {
                    if(challengeDirectInZaban == false) {
                        ParentActivity.book = "0";
                        ParentActivity.chapter = "0";
                        category.setBook(String.valueOf(courseList.get(i).getBook()));
                        category.setChapter(String.valueOf(courseList.get(i).getChapter()+1));
                        Log.d("TAG", "category to server" + category.serialize());
                        DuelApp.getInstance().sendMessage(category.serialize());
                        getContext().startActivity(new Intent(getContext(), WaitingActivity.class));
                    } else {
                        Intent i2 = new Intent(getContext(), WaitingActivity.class);
                        i2.putExtra("user_number", opponentUserNumeber);
                        i2.putExtra("message", getContext().getString(R.string.message_duel_with_friends_default));
                        i2.putExtra("master", true);
                        i2.putExtra("category", courseList.get(i).getCategory());
                        i2.putExtra("book", courseList.get(i).getBook());
                        i2.putExtra("chapter",courseList.get(i).getChapter());
                        getContext().startActivity(i2);
                    }

                    dismiss();
                }
            }
        });

    }

    private void findControls() {
        steps = (ListView) findViewById(R.id.steps_list);
    }

}
