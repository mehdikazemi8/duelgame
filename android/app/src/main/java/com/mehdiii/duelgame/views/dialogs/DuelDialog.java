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
import android.widget.EditText;
import android.widget.ListView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.Category;
import com.mehdiii.duelgame.models.WannaChallenge;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.waiting.WaitingActivity;

public class DuelDialog extends Dialog {
    ListView courses;

    public DuelDialog(Context context) {
        super(context);
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
        String[] coursesNames = getContext().getResources().getStringArray(R.array.categories_values);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.template_course_challenge_dialog, R.id.course_name, coursesNames);
        courses.setAdapter(adapter);
        courses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("TAG", "aaa");
                String[] categories = getContext().getResources().getStringArray(R.array.categories_keys);
                Category cat = Category.newInstance(Category.CategoryType.WANNA_PLAY);
                ParentActivity.category = categories[i];
                cat.setCategory(categories[i]);
                // 'score' shows the score of his/her last category that he/she has played
                AuthManager.getCurrentUser().setScore(
                        AuthManager.getCurrentUser().getScore(ParentActivity.category, "week")
                );
                DuelApp.getInstance().sendMessage(cat.serialize());
                getContext().startActivity(new Intent(getContext(), WaitingActivity.class));
                dismiss();
            }
        });

    }

    private void findControls() {
        courses = (ListView) findViewById(R.id.courses_list);
    }
}
