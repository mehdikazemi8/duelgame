package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.ScoreHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MeHdi on 5/19/2015.
 */
public class ScoresDialog extends Dialog {

    TextView level1, level2, level3;
    TextView ta1, ta2, ta3, ta4;
    TextView from1, from2, from3, from4;
    TextView to1, to2, to3, to4;
    TextView title;

    public ScoresDialog(Context context) {
        super(context);
    }

    private void findControls(LinearLayout layout) {
        level1 = (TextView) layout.findViewById(R.id.level1);
        level2 = (TextView) layout.findViewById(R.id.level2);
        level3 = (TextView) layout.findViewById(R.id.level3);

        ta1 = (TextView) layout.findViewById(R.id.ta1);
        ta2 = (TextView) layout.findViewById(R.id.ta2);
        ta3 = (TextView) layout.findViewById(R.id.ta3);
        ta4 = (TextView) layout.findViewById(R.id.ta4);

        from1 = (TextView) layout.findViewById(R.id.from1);
        from2 = (TextView) layout.findViewById(R.id.from2);
        from3 = (TextView) layout.findViewById(R.id.from3);
        from4 = (TextView) layout.findViewById(R.id.from4);

        to1 = (TextView) layout.findViewById(R.id.to1);
        to2 = (TextView) layout.findViewById(R.id.to2);
        to3 = (TextView) layout.findViewById(R.id.to3);
        to4 = (TextView) layout.findViewById(R.id.to4);

        title = (TextView) layout.findViewById(R.id.title);
    }

    int levelSum;
    int titleSum;
    int levelIndex;
    int titleIndex;
    int userScore;
    List<String> titles;
    List<Integer> scoreTitle;
    List<Integer> scoreLevel;

    private void setBackGroundColor(TextView tv, int start, int end)
    {
        if(start <= userScore && userScore <= end)
            tv.setBackgroundColor(getContext().getResources().getColor(R.color.golden_yellow));
    }

    private void configureControls(boolean lastTitle) {
        FontHelper.setKoodakFor(getOwnerActivity(), ta1, ta2, ta3, ta4,
                from1, from2, from3, from4,
                to1, to2, to3, to4,
                level1, level2, level3,
                title);

        title.setText(titles.get(titleIndex));

        if (!lastTitle) {
            level1.setText(String.valueOf(levelIndex + 1));
            level2.setText(String.valueOf(levelIndex + 2));
            level3.setText(String.valueOf(levelIndex + 3));

            if (levelSum == 0) {
                from1.setText(String.valueOf(levelSum));
                if(userScore == 0)
                    level1.setBackgroundColor(getContext().getResources().getColor(R.color.golden_yellow));
            }
            else
                from1.setText(String.valueOf(levelSum + 1));
            to1.setText(String.valueOf(levelSum + scoreLevel.get(levelIndex)));
            setBackGroundColor(level1, levelSum, levelSum + scoreLevel.get(levelIndex));
            levelSum += scoreLevel.get(levelIndex);
            levelIndex++;

            from2.setText(String.valueOf(levelSum + 1));
            to2.setText(String.valueOf(levelSum + scoreLevel.get(levelIndex)));
            setBackGroundColor(level1, levelSum, levelSum + scoreLevel.get(levelIndex));
            levelSum += scoreLevel.get(levelIndex);
            levelIndex++;

            from3.setText(String.valueOf(levelSum + 1));
            to3.setText(String.valueOf(levelSum + scoreLevel.get(levelIndex)));
            setBackGroundColor(level1, levelSum, levelSum+scoreLevel.get(levelIndex));
            levelSum += scoreLevel.get(levelIndex);
            levelIndex++;

            if (titleSum == 0)
                from4.setText(String.valueOf(titleSum));
            else
                from4.setText(String.valueOf(titleSum + 1));
            to4.setText(String.valueOf(titleSum + scoreTitle.get(titleIndex)));
            titleSum += scoreTitle.get(titleIndex);
            titleIndex++;

        } else {
            level1.setText(String.valueOf(levelIndex + 1));
            level2.setText("");
            level3.setText("");

            ta2.setText("");
            ta3.setText("");

            from1.setText(String.valueOf(levelSum + 1));
            to1.setText("...");

            from2.setText("");
            to2.setText("");

            from3.setText("");
            to3.setText("");

            from4.setText(String.valueOf(titleSum + 1));
            to4.setText("...");
            titleIndex++;
        }
    }

    TextView userScoreText, userScoreInt, levelText, titleText;

    private void findHeaderControls() {
        userScoreText = (TextView) findViewById(R.id.scores_user_score_text);
        userScoreInt = (TextView) findViewById(R.id.scores_user_score_int);
        levelText = (TextView) findViewById(R.id.scores_level_text);
        titleText = (TextView) findViewById(R.id.scores_title_text);
    }

    private void configureHeaderControls() {
        FontHelper.setKoodakFor(getOwnerActivity(), userScoreInt, userScoreText, levelText, titleText);
        userScoreInt.setText(String.valueOf(AuthManager.getCurrentUser().getScore()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_scores);

        userScore = AuthManager.getCurrentUser().getScore();
        findHeaderControls();
        configureHeaderControls();

        titles = ScoreHelper.getTitles();
        scoreTitle = ScoreHelper.getTitleScores();
        scoreLevel = ScoreHelper.getLevelScores();

        levelIndex = titleIndex = 0;
        levelSum = titleSum = 0;
        LinearLayout body = (LinearLayout) findViewById(R.id.scores_body);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);

        CreateLevelViews task = new CreateLevelViews(body, progressBar);
        task.execute();

    }

    private class CreateLevelViews extends AsyncTask<Void, Void, List<LinearLayout>> {
        LinearLayout body;
        ProgressBar progressBar;
        public CreateLevelViews(LinearLayout body, ProgressBar progressBar) {
            this.body = body;
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<LinearLayout> doInBackground(Void... params) {
            LayoutInflater inflater = getLayoutInflater();
            List<LinearLayout> list = new ArrayList<>();

            for (int i = 0; i < 9; i++) {
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.template_scores, null);
                findControls(layout);
                configureControls(i == 8);
                list.add(layout);
                // body.addView(layout);
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<LinearLayout> linearLayouts) {
            super.onPostExecute(linearLayouts);

            this.progressBar.setVisibility(View.GONE);
            for ( LinearLayout layout : linearLayouts) {
                this.body.addView(layout);
            }
        }
    }

}
