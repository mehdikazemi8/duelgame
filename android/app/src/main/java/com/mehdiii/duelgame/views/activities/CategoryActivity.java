package com.mehdiii.duelgame.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.Category;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.waiting.WaitingActivity;

public
class CategoryActivity extends ParentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
    }

    public void letsPlay(View v) {

        Category cat = Category.newInstance(Category.CategoryType.WANNA_PLAY);
        category = v.getContentDescription().toString();
        cat.setCategory(v.getContentDescription().toString());
        DuelApp.getInstance().sendMessage(cat.serialize());

        startActivity(new Intent(this, WaitingActivity.class));
        this.finish();
    }

    @Override
    public boolean canHandleChallengeRequest() {
        return true;
    }

    @Override
    public OnCompleteListener getPostChallengeDecisionMadeListener() {
        return new OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                finish();
            }
        };
    }
}
