package com.mehdiii.duelgame.views.activities.category;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.Category;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.ScoreHelper;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.category.fragments.RankPlayFragment;
import com.mehdiii.duelgame.views.activities.ranking.RankingActivity;
import com.mehdiii.duelgame.views.activities.waiting.WaitingActivity;

public class CategoryActivity extends ParentActivity {

    final int NUMBER_OF_COURSES = 6;
    private String[] categories = {"10001", "10002", "10003", "10004", "10005", "10006"};
    private Fragment[] rankPlayFragment = new Fragment[NUMBER_OF_COURSES];
    private int[] fragmentHolderId = new int[NUMBER_OF_COURSES];
    private TextView[] courseTitle = new TextView[NUMBER_OF_COURSES];
    private TextView[] courseLevel = new TextView[NUMBER_OF_COURSES];
    private TextView[] courseLevelRange = new TextView[NUMBER_OF_COURSES];
    private ProgressBar[] courseLevelProgress = new ProgressBar[NUMBER_OF_COURSES];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        find();
        configure();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean canHandleChallengeRequest() {
        return true;
    }

    @Override
    public OnCompleteListener onDecisionMadeListener() {
        return new OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                finish();
            }
        };
    }

    private void find() {
        for (int i = 0; i < NUMBER_OF_COURSES; i++) {
            rankPlayFragment[i] = Fragment.instantiate(this, RankPlayFragment.class.getName(), null);
            Bundle bundle = new Bundle();
            bundle.putInt("category", i);
            rankPlayFragment[i].setArguments(bundle);
        }

        fragmentHolderId[0] = R.id.fragment_holder_adabiat;
        fragmentHolderId[1] = R.id.fragment_holder_arabi;
        fragmentHolderId[2] = R.id.fragment_holder_din_o_zendegi;
        fragmentHolderId[3] = R.id.fragment_holder_zaban_englisi;
        fragmentHolderId[4] = R.id.fragment_holder_shimi;
        fragmentHolderId[5] = R.id.fragment_holder_zist;

        courseTitle[0] = (TextView) findViewById(R.id.adabiat_title);
        courseTitle[1] = (TextView) findViewById(R.id.arabi_title);
        courseTitle[2] = (TextView) findViewById(R.id.din_o_zendegi_title);
        courseTitle[3] = (TextView) findViewById(R.id.zaban_englisi_title);
        courseTitle[4] = (TextView) findViewById(R.id.shimi_title);
        courseTitle[5] = (TextView) findViewById(R.id.zist_title);

        courseLevel[0] = (TextView) findViewById(R.id.adabiat_level);
        courseLevel[1] = (TextView) findViewById(R.id.arabi_level);
        courseLevel[2] = (TextView) findViewById(R.id.din_o_zendegi_level);
        courseLevel[3] = (TextView) findViewById(R.id.zaban_englisi_level);
        courseLevel[4] = (TextView) findViewById(R.id.shimi_level);
        courseLevel[5] = (TextView) findViewById(R.id.zist_level);

        courseLevelRange[0] = (TextView) findViewById(R.id.adabiat_level_range);
        courseLevelRange[1] = (TextView) findViewById(R.id.arabi_level_range);
        courseLevelRange[2] = (TextView) findViewById(R.id.din_o_zendegi_level_range);
        courseLevelRange[3] = (TextView) findViewById(R.id.zaban_englisi_level_range);
        courseLevelRange[4] = (TextView) findViewById(R.id.shimi_level_range);
        courseLevelRange[5] = (TextView) findViewById(R.id.zist_level_range);

        courseLevelProgress[0] = (ProgressBar) findViewById(R.id.adabiat_level_progress);
        courseLevelProgress[1] = (ProgressBar) findViewById(R.id.arabi_level_progress);
        courseLevelProgress[2] = (ProgressBar) findViewById(R.id.din_o_zendegi_level_progress);
        courseLevelProgress[3] = (ProgressBar) findViewById(R.id.zaban_englisi_level_progress);
        courseLevelProgress[4] = (ProgressBar) findViewById(R.id.shimi_level_progress);
        courseLevelProgress[5] = (ProgressBar) findViewById(R.id.zist_level_progress);
    }

    private void configure() {
        FontHelper.setKoodakFor(this,
                courseTitle[0], courseTitle[1], courseTitle[2], courseTitle[3], courseTitle[4], courseTitle[5],
                courseLevel[0], courseLevel[1], courseLevel[2], courseLevel[3], courseLevel[4], courseLevel[5],
                courseLevelRange[0], courseLevelRange[1], courseLevelRange[2], courseLevelRange[3], courseLevelRange[4], courseLevelRange[5]);

        for(int c = 0; c < NUMBER_OF_COURSES; c ++) {
            int userScore = AuthManager.getCurrentUser().getScore(categories[c], "overall");
            courseLevel[c].setText(String.valueOf(ScoreHelper.getLevel(userScore)));
            courseLevelProgress[c].setProgress(ScoreHelper.getThisLevelPercentage(userScore));
            courseLevelRange[c].setText(ScoreHelper.getThisLevelRange(userScore));
        }
    }

    public void clicked(View view) {
        ParentActivity.category = Integer.parseInt(view.getContentDescription().toString()) + 10000 + 1;
        startActivity(new Intent(this, RankingActivity.class));

        /*
        int courseId = Integer.parseInt(view.getContentDescription().toString());

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            getFragmentManager().beginTransaction().add(fragmentHolderId[courseId], rankPlayFragment[courseId])
                    .addToBackStack(String.valueOf(courseId)).commit();
        } else {
            if (getFragmentManager().getBackStackEntryAt(0).getName().toString().equals(String.valueOf(courseId))) {
//                    getFragmentManager().beginTransaction().remove(rankPlayFragmentMath).commit();
                getFragmentManager().popBackStack();
            } else {
//                    getFragmentManager().beginTransaction().remove(rankPlayFragmentArabic).commit();
                getFragmentManager().popBackStack();


                getFragmentManager().beginTransaction().add(fragmentHolderId[courseId], rankPlayFragment[courseId])
                        .addToBackStack(String.valueOf(courseId)).commit();
            }
        }
        */
    }

    public void wannaRace(View view) {
        Log.d("aaaaa", "wannaRace " + view.getContentDescription().toString());
    }

    public void wannaPlay(View view) {
        Category cat = Category.newInstance(Category.CategoryType.WANNA_PLAY);
        ParentActivity.category = Integer.parseInt(view.getContentDescription().toString()) + 10000 + 1;
        cat.setCategory(ParentActivity.category);
        DuelApp.getInstance().sendMessage(cat.serialize());
        // 'score' shows the score of his/her last category that he/she has played
        AuthManager.getCurrentUser().setScore(
                AuthManager.getCurrentUser().getScore(String.valueOf(ParentActivity.category), "week")
        );
        startActivity(new Intent(this, WaitingActivity.class));
        this.finish();
    }

    public void showRank(View view) {
        ParentActivity.category = Integer.parseInt(view.getContentDescription().toString()) + 10000 + 1;
        startActivity(new Intent(this, RankingActivity.class));
    }
}
