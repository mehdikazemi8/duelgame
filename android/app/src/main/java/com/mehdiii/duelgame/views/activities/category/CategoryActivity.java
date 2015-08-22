package com.mehdiii.duelgame.views.activities.category;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.Category;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.category.fragments.RankPlayFragment;
import com.mehdiii.duelgame.views.activities.ranking.RankingActivity;
import com.mehdiii.duelgame.views.activities.waiting.WaitingActivity;

public class CategoryActivity extends ParentActivity {

    final int NUMBER_OF_COURSES = 5;
    private Fragment[] rankPlayFragment = new Fragment[NUMBER_OF_COURSES];
    private int[] fragmentHolderId = new int[NUMBER_OF_COURSES];
    private TextView[] course = new TextView[NUMBER_OF_COURSES];

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

        course[0] = (TextView) findViewById(R.id.adabiat_title);
        course[1] = (TextView) findViewById(R.id.arabi_title);
        course[2] = (TextView) findViewById(R.id.din_o_zendegi_title);
        course[3] = (TextView) findViewById(R.id.zaban_englisi_title);
        course[4] = (TextView) findViewById(R.id.shimi_title);
    }

    private void configure() {
        FontHelper.setKoodakFor(this, course[0], course[1], course[2], course[3], course[4]);
    }

    public void clicked(View view) {

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
    }

    public void wannaRace(View view) {
        Log.d("aaaaa", "wannaRace " + view.getContentDescription().toString());
    }

    public void wannaPlay(View view) {
        Category cat = Category.newInstance(Category.CategoryType.WANNA_PLAY);
        ParentActivity.category = String.valueOf(Integer.parseInt(view.getContentDescription().toString()) + 10000 + 1);
        cat.setCategory(ParentActivity.category);
        DuelApp.getInstance().sendMessage(cat.serialize());
        // 'score' shows the score of his/her last category that he/she has played
        AuthManager.getCurrentUser().setScore(
                AuthManager.getCurrentUser().getScore(ParentActivity.category, "week")
        );
        startActivity(new Intent(this, WaitingActivity.class));
        this.finish();
    }

    public void showRank(View view) {
        ParentActivity.category = String.valueOf(Integer.parseInt(view.getContentDescription().toString()) + 10000 + 1);
        startActivity(new Intent(this, RankingActivity.class));
    }
}
