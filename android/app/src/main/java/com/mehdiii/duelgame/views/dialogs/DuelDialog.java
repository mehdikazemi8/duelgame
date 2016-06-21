package com.mehdiii.duelgame.views.dialogs;

/**
 * Created by mehdiii on 12/23/15.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.Category;
import com.mehdiii.duelgame.models.CourseMap;
import com.mehdiii.duelgame.models.StepCourse;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.events.OnFinishActivity;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.duelofflinewaiting.DuelOfflineWaitingActivity;
import com.mehdiii.duelgame.views.activities.waiting.WaitingActivity;

import de.greenrobot.event.EventBus;

public class DuelDialog extends Dialog {
    ListView courses;

    boolean offlineDuel = false;
    String opponentUserNumber;
    boolean stepDuel = false;

    public DuelDialog(Context context) {
        super(context);
    }

    public DuelDialog(Context context, boolean offlineDuel, String opponentUserNumber) {
        super(context);
        this.offlineDuel = offlineDuel;
        this.opponentUserNumber = opponentUserNumber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_duel);

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        getWindow().setLayout((int) (metrics.widthPixels * 0.9), -2);
        findControls();
        configure();
    }

    private void configure() {
        // setting list view that shows courses
        final User user = AuthManager.getCurrentUser();
        if(user == null) {
            DuelApp.getInstance().toast(R.string.toast_try_again, Toast.LENGTH_LONG);
            return;
        }

        String[] coursesNames = getContext().getResources().getStringArray(R.array.categories_values);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.template_course_challenge_dialog, R.id.course_name, coursesNames);
        courses.setAdapter(adapter);
        courses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int[] categories = getContext().getResources().getIntArray(R.array.categories_keys);
                ParentActivity.category = categories[i];
                // 'score' shows the score of his/her last category that he/she has played
                AuthManager.getCurrentUser().setScore(
                        AuthManager.getCurrentUser().getScore(String.valueOf(ParentActivity.category), "week")
                );

//              checking for dynamic duel on chapters
                CourseMap courseMap = user.getCourseMap();
                if(courseMap != null) {
                    for( StepCourse stepCourse : courseMap.getStepCourses()){
                        if( stepCourse.getCategory() == ParentActivity.category && ParentActivity.category == 10004)
                            stepDuel = true;
                    }
                }

                if(offlineDuel) {
                    if(stepDuel){
                        dismiss();
                        Category cat = Category.newInstance(Category.CategoryType.WANNA_PLAY);
                        cat.setCategory(categories[i]);
                        StepOfflineDuelDialog stepOfflineDuelDialog = new StepOfflineDuelDialog(getContext(), cat, opponentUserNumber);
                        stepOfflineDuelDialog.show();
                        return;
                    } else {
                        Intent intent = new Intent(getContext(), DuelOfflineWaitingActivity.class);
                        intent.putExtra("opponent_user_number", opponentUserNumber);
                        intent.putExtra("category", ParentActivity.category);
                        intent.putExtra("master", true);
                        getContext().startActivity(intent);
                        EventBus.getDefault().post(new OnFinishActivity());
                    }
                } else {

                    Category cat = Category.newInstance(Category.CategoryType.WANNA_PLAY);
                    cat.setCategory(categories[i]);

                    // frshd added this

                    Log.d("TAG", "mostaghim " + i);

                    if (stepDuel){
                        dismiss();
                        StepDuelDialog stepDuelDialog = new StepDuelDialog(getContext(), cat);
                        stepDuelDialog.show();
                        return;
                    }
                    DuelApp.getInstance().sendMessage(cat.serialize());
                    getContext().startActivity(new Intent(getContext(), WaitingActivity.class));
                }
                dismiss();
            }
        });
    }

    private void findControls() {
        courses = (ListView) findViewById(R.id.courses_list);
    }
}
