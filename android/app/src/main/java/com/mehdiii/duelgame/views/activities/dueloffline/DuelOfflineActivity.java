package com.mehdiii.duelgame.views.activities.dueloffline;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.GetQuestion;
import com.mehdiii.duelgame.models.ProblemCollection;
import com.mehdiii.duelgame.models.Question;
import com.mehdiii.duelgame.models.ReportProblem;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.DuelMusicPlayer;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.result.GameResultActivity;
import com.mehdiii.duelgame.views.custom.CustomTextView;
import com.mehdiii.duelgame.views.custom.FontFitButton;
import com.mehdiii.duelgame.views.dialogs.ConfirmDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by mehdiii on 2/1/16.
 */
public class DuelOfflineActivity extends ParentActivity {

    public static final String GAME_DATA_JSON = "gameDataJson";
    public static final String IS_MASTER = "IS_MASTER";

    private boolean isMaster;
    final int DURATION = 20000;
    final int NOP = 4;
    final int durationNumberOfProblem = 1000;
    final int durationOptions = 1000;
    final int durationCorrectOption = 1000;

    int collectedDiamond;

    long remainingTimeOfThisQuestion;
    CountDownTimer timeToAnswer = null;
    ScaleAnimation timerScaleAnimation = null;
    String resultInfo;

    String correctAnswerStr;
    int correctOption;
    int[] choseOption = new int[NOP];
    int numberOfOptionChose;

    int iAnsweredThisTime;
    boolean iAnsweredThis;
    int problemIndex;

    boolean hintRemoveClicked;
    int clickedHintRemove;

    FontFitButton[] optionBtn = new FontFitButton[NOP];

    private Display mobileDisplay;
    private Point screenSize = new Point();
    private int screenHeight;

    private ImageView removeOptionImageView;
    private ImageView reportProblemImageView;
    private ImageView[] userTick = new ImageView[NUMBER_OF_QUESTIONS];
    private ImageView[] oppTick = new ImageView[NUMBER_OF_QUESTIONS];
    private int oppTickIndex = 0;
    private int userTickIndex = 0;

    DuelMusicPlayer myPlayer;
    int WRONG_ANSWER, CORRECT_ANSWER;

    ProgressBar myProgress, opProgress;
    private TextView headerBackground;
    private CustomTextView diamondCounter;

    ArrayList<Integer> correctOptionsArrayList = new ArrayList<Integer>();

    private final int HINT_REMOVE_COST = 97;

    private String[] round = new String[NUMBER_OF_QUESTIONS];

    public void setProgressBar(ProgressBar pb, int progress) {
        if (progress < 0) {
            pb.setProgressDrawable(getResources().getDrawable(R.drawable.vertical_progress_bar_red));
            progress *= -1;
        } else
            pb.setProgressDrawable(getResources().getDrawable(R.drawable.vertical_progress_bar));

        pb.setProgress(progress);
    }

    public void askQuestion() {
        numberOfOptionChose = 0;
        choseOption[0] = choseOption[1] = choseOption[2] = choseOption[3] = 0;
        hintRemoveClicked = false;
        clickedHintRemove = 0;

        iAnsweredThis = false;
        iAnsweredThisTime = -1;
        remainingTimeOfThisQuestion = 20;

        setTextView(questionTextView, questionsToAsk.get(problemIndex).getQuestionText());

        List<String> opts = questionsToAsk.get(problemIndex).getOptions();
        problemIndex++;
        correctAnswerStr = opts.get(0);

        shuffleArray(opts);
        shuffleArray(opts);

        for(int k = 0; k < NOP; k ++) {
            if(opts.get(k).equals(correctAnswerStr)) {
                correctOption = k;
            }
        }
        correctOptionsArrayList.add(correctOption);
        for(int k = 0; k < NOP; k++) {
            setButton(optionBtn[k], opts.get(k));
        }

        timeToAnswer = new CountDownTimer(DURATION, 1000) {
            @Override
            public void onTick(long arg0) {
                remainingTimeOfThisQuestion = arg0 / 1000;
            }

            @Override
            public void onFinish() {
                if (iAnsweredThis == true)
                    return;

                /*
                If the dialog is created at all and it is showing then we dismiss it
                */
                if (removeOptionDialog != null && removeOptionDialog.isShowing())
                    removeOptionDialog.dismiss();
                setUserTick(false);
                sendGQMinusOne();
            }
        };

        changeButtonsClickableState(true);

        for(int k = 0; k < NOP; k ++) {
            optionBtn[k].setBackgroundResource(R.drawable.option_button);
            optionBtn[k].setTextColor(getResources().getColor(R.color.play_game_option_btn_text));
        }

        setStartAnimation(0f, 1f, questionTextView, optionBtn[0], optionBtn[1], optionBtn[2], optionBtn[3]);
    }

    public void setStartAnimation(float from, float to, TextView... views) {

        // fade in the bomb
        removeOptionImageView.setAlpha(0f);
        removeOptionImageView.setClickable(true);
        removeOptionImageView.setVisibility(View.VISIBLE);
        ObjectAnimator removeOptionsFadeIn = ObjectAnimator.ofFloat(removeOptionImageView,
                "alpha", 0f, 1f);
        removeOptionsFadeIn.setDuration(1000);
        removeOptionsFadeIn.setInterpolator(new LinearInterpolator());
        removeOptionsFadeIn.setStartDelay(1000);
        removeOptionsFadeIn.start();

        // fade in report problem
        reportProblemImageView.setAlpha(0f);
        reportProblemImageView.setClickable(true);
        reportProblemImageView.setVisibility(View.VISIBLE);
        ObjectAnimator reportProblemFadeIn = ObjectAnimator.ofFloat(reportProblemImageView,
                "alpha", 0f, 1f);
        reportProblemFadeIn.setDuration(1000);
        reportProblemFadeIn.setInterpolator(new LinearInterpolator());
        reportProblemFadeIn.setStartDelay(1000);
        reportProblemFadeIn.start();
        reportProblemImageView.setClickable(true);

        int idx = 0;
        for (TextView view : views) {
            view.setAlpha(0f);
            view.setVisibility(View.VISIBLE);

            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view,
                    "alpha", 0f, 1f);
            fadeIn.setDuration(1000);
            fadeIn.setInterpolator(new LinearInterpolator());
            if (idx != 0) {
                fadeIn.setStartDelay(1000);
            }
            if (idx == 1) {
                fadeIn.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        timeToAnswer.start();

                        timerScaleAnimation = new ScaleAnimation(1f, 0f, 1f, 1f, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF);
                        timerScaleAnimation.setDuration(DURATION);
                        timerScaleAnimation.setInterpolator(new LinearInterpolator());
                        timerScaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                changeButtonsClickableState(false);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        headerBackground.startAnimation(timerScaleAnimation);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
            }
            fadeIn.start();
            idx++;
        }
    }

    private void animateGainingDiamond(final int thisQuestionDiamond) {

        AnimationSet all = new AnimationSet(false);

        Animation scaleText = new ScaleAnimation(1f, 1.5f, 1f, 1.5f);
        scaleText.setDuration(400);
        scaleText.setInterpolator(new DecelerateInterpolator());
        //all.addAnimation(scaleText);

        //int dx = collectedDiamondGroup.getWidth();

        Animation translateText = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        translateText.setDuration(400);
        translateText.setInterpolator(new DecelerateInterpolator());
        translateText.setAnimationListener(new Animation.AnimationListener() {
                                               @Override
                                               public void onAnimationStart(Animation animation) {
                                                   collectedDiamondTextViewTmp.setText("" + thisQuestionDiamond);
                                                   collectedDiamondGroup.setVisibility(View.VISIBLE);
                                               }

                                               @Override
                                               public void onAnimationEnd(Animation animation) {
                                                   collectedDiamondGroup.setVisibility(View.INVISIBLE);
                                                   collectedDiamondTextView.setText(String.valueOf(collectedDiamond));

                                                   diamondCounter.setText(String.valueOf(collectedDiamond));
                                               }

                                               @Override
                                               public void onAnimationRepeat(Animation animation) {
                                               }
                                           }
        );
        all.addAnimation(translateText);

        collectedDiamondGroup.startAnimation(all);
    }

    private void setUserTick(boolean correct) {
        if(correct) {
            userTick[userTickIndex].setImageResource(R.drawable.tick);
        } else {
            userTick[userTickIndex].setImageResource(R.drawable.cross);
        }
        userTick[userTickIndex++].setVisibility(View.VISIBLE);
    }

    private void setOppTick(boolean correct) {
        if(correct) {
            oppTick[oppTickIndex].setImageResource(R.drawable.tick);
        } else {
            oppTick[oppTickIndex].setImageResource(R.drawable.cross);
        }
        oppTick[oppTickIndex++].setVisibility(View.VISIBLE);
    }

    public void answered(View v) {

//        if (iAnsweredThisTime != -1) {
//            return;
//        }

        /*
        after he chooses one wrong option, he can click hint AGAIN option
        then he can put his fingers on two options (one wrong and one correct and both of them will
        be choosed.
        by this condition I won't let him do this
         */
        if (numberOfOptionChose == 1)
            return;

        removeOptionImageView.setClickable(false);
        int clickedButtonIndex = Integer.parseInt(v.getContentDescription().toString());

        numberOfOptionChose += 1;
        if (numberOfOptionChose == 2) {
            // there is no other possibility to choose options, he has chosen 2 options already
            changeButtonsClickableState(false);
        }

        iAnsweredThisTime = (int) remainingTimeOfThisQuestion;
        choseOption[clickedButtonIndex] = 1;

//        Lets not disable all buttons, instead just disable the button which has been clicked last
        ((FontFitButton) v).setClickable(false);
//        changeButtonsClickableState(false);

        iAnsweredThis = true;

        int ok = 0;
        if (correctAnswerStr.compareTo(((FontFitButton) v).getText().toString()) == 0) {
            setUserTick(true);

            // answered correct, lets disable all buttons
            changeButtonsClickableState(false);

            myPlayer = new DuelMusicPlayer(this, CORRECT_ANSWER, false);
            collectedDiamond += iAnsweredThisTime;
            animateGainingDiamond(iAnsweredThisTime);

            if (problemIndex == 6)
                userPoints += 5;
            else
                userPoints += 3;

            setTextView(userPointsTextView, "" + userPoints);
            setProgressBar(myProgress, userPoints);

            //((Button) v).setBackgroundColor(Color.GREEN);
            ((FontFitButton) v).setTextColor(getResources().getColor(R.color.correct_answer));
            ok = 1;
        } else {
            setUserTick(false);
            myPlayer = new DuelMusicPlayer(this, WRONG_ANSWER, false);
//            userPoints += -1;

//            setTextView(userPointsTextView, "" + userPoints);
//            setProgressBar(myProgress, userPoints);

            for(int k = 0; k < NOP; k ++){
                if(choseOption[k] == 0) {
                    optionBtn[k].setTextColor(getResources().getColor(R.color.gray_light));
                }
            }

            ((FontFitButton) v).setTextColor(getResources().getColor(R.color.wrong_answer));
            optionBtn[correctOption].setTextColor(getResources().getColor(R.color.correct_answer));

            if (numberOfOptionChose == 1) {

            }
        }
        myPlayer.execute();

//        // send result of user choice
//        GetQuestion request = new GetQuestion();
//        request.setHintAgain(0);
//        request.setHintRemove(0);
//        if (clickedHintRemove == 1) request.setHintRemove(1);
//        if(ok == 1) {
//            request.setTime(remainingTimeOfThisQuestion);
//        } else {
//            request.setTime(-1);
//        }
//        request.setOk(ok);
//        DuelApp.getInstance().sendMessage(request.serialize(CommandType.SEND_GET_QUESTION));
        endQuestionAnimation(false);
    }

    public void setButton(FontFitButton tv, String s) {
        tv.setText(s);
    }

    public void setTextView(TextView tv, String s) {
        tv.setText(s);
    }

    private TextView opponentNameTextView;
    private TextView opponentPointsTextView;
    private TextView userNameTextView;
    private TextView userPointsTextView;
    private TextView questionTextView;

    private ImageView userAvatar;
    private ImageView opponentAvatar;

    private TextView collectedDiamondTextView;
    private TextView collectedDiamondTextViewTmp;
    private LinearLayout collectedDiamondGroup;

    private LinearLayout header;

    DuelMusicPlayer musicPlayer;

    ConfirmDialog removeOptionDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duel_offline);

        findControls();

        readArguments();

        round[0] = "سوال اول";
        round[1] = "سوال دوم";
        round[2] = "سوال سوم";
        round[3] = "سوال چهارم";
        round[4] = "سوال پنجم";
        round[5] = "سوال آخر";

        WRONG_ANSWER = R.raw.wrong_answer;
        CORRECT_ANSWER = R.raw.correct_answer;

        LocalBroadcastManager.getInstance(this).registerReceiver(mListener, DuelApp.getInstance().getIntentFilter());

        opponentPoints = userPoints = 0;
        problemIndex = 0;
        collectedDiamond = 0;

        mobileDisplay = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        mobileDisplay.getSize(screenSize);
        screenHeight = screenSize.y;

        configureControls();

        startGameAnimation();

        musicPlayer = new DuelMusicPlayer(DuelOfflineActivity.this, R.raw.game, true);
        musicPlayer.execute();
    }

    private void configureControls() {
        FontHelper.setKoodakFor(getApplicationContext(),
                optionBtn[0], optionBtn[1], optionBtn[2], optionBtn[3],
                opponentNameTextView, opponentPointsTextView,
                userNameTextView, userPointsTextView,
                questionTextView,
                collectedDiamondTextView, collectedDiamondTextViewTmp);

        userNameTextView.setText(AuthManager.getCurrentUser().getName());
        userAvatar.setImageResource(AvatarHelper.getResourceId(DuelOfflineActivity.this, AuthManager.getCurrentUser().getAvatar()));

        opponentNameTextView.setText(opponentUser.getName());
        opponentAvatar.setImageResource(AvatarHelper.getResourceId(DuelOfflineActivity.this, opponentUser.getAvatar()));

        reportProblemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.d("TAG", "reportProblem onClick");
                final int reportProblemIndex = problemIndex - 1;
                ConfirmDialog dialog = new ConfirmDialog(DuelOfflineActivity.this, getResources().getString(R.string.report_problem_confirm));
                dialog.setOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(Object data) {
                        if ((Boolean) data) {
                            if(0 <= reportProblemIndex && reportProblemIndex < questionsToAsk.size()) {
                                ReportProblem reportProblem = new ReportProblem(CommandType.REPORT_PROBLEM);
                                reportProblem.setCategory(Integer.valueOf(category));
                                reportProblem.setDescription(String.valueOf(questionsToAsk.get(reportProblemIndex).getQuestionNumberInServer()));
                                DuelApp.getInstance().sendMessage(reportProblem.serialize());
                            }
                            DuelApp.getInstance().toast(R.string.report_problem_thanks, Toast.LENGTH_SHORT);
                            view.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    private void findControls() {
        reportProblemImageView = (ImageView) findViewById(R.id.report_problem);
        opponentNameTextView = (TextView) findViewById(R.id.play_game_opponent_name);
        opponentPointsTextView = (TextView) findViewById(R.id.play_game_opponent_score);
        userNameTextView = (TextView) findViewById(R.id.play_game_user_name);
        userPointsTextView = (TextView) findViewById(R.id.play_game_user_score);
        questionTextView = (TextView) findViewById(R.id.play_game_question_text);

        userAvatar = (ImageView) findViewById(R.id.play_game_user_avatar);
        opponentAvatar = (ImageView) findViewById(R.id.play_game_opponent_avatar);

        optionBtn[0] = (FontFitButton) findViewById(R.id.play_game_option_0);
        optionBtn[1] = (FontFitButton) findViewById(R.id.play_game_option_1);
        optionBtn[2] = (FontFitButton) findViewById(R.id.play_game_option_2);
        optionBtn[3] = (FontFitButton) findViewById(R.id.play_game_option_3);

        myProgress = (ProgressBar) findViewById(R.id.play_game_my_progress);
        opProgress = (ProgressBar) findViewById(R.id.play_game_op_progress);

        header = (LinearLayout) findViewById(R.id.play_game_header);

        collectedDiamondTextView = (TextView) findViewById(R.id.play_game_collected_diamond);
        collectedDiamondTextViewTmp = (TextView) findViewById(R.id.play_game_collected_diamond_tmp);
        collectedDiamondGroup = (LinearLayout) findViewById(R.id.play_game_collected_diamond_group);

        removeOptionImageView = (ImageView) findViewById(R.id.remove_option_imageview);
        reportProblemImageView = (ImageView) findViewById(R.id.report_problem);

        userTick[0] = (ImageView) findViewById(R.id.user_tick0);
        userTick[1] = (ImageView) findViewById(R.id.user_tick1);
        userTick[2] = (ImageView) findViewById(R.id.user_tick2);
        userTick[3] = (ImageView) findViewById(R.id.user_tick3);
        userTick[4] = (ImageView) findViewById(R.id.user_tick4);
        userTick[5] = (ImageView) findViewById(R.id.user_tick5);

        oppTick[0] = (ImageView) findViewById(R.id.op_tick0);
        oppTick[1] = (ImageView) findViewById(R.id.op_tick1);
        oppTick[2] = (ImageView) findViewById(R.id.op_tick2);
        oppTick[3] = (ImageView) findViewById(R.id.op_tick3);
        oppTick[4] = (ImageView) findViewById(R.id.op_tick4);
        oppTick[5] = (ImageView) findViewById(R.id.op_tick5);

        headerBackground = (TextView) findViewById(R.id.header_background);
        diamondCounter = (CustomTextView) findViewById(R.id.collected_diamond_counter);
    }

    User opponentUser = null;
    User user = null;

    private void readArguments() {
        Bundle params = getIntent().getExtras();
        if (params == null)
            return;

        String json = params.getString(GAME_DATA_JSON, "");
        isMaster = params.getBoolean(IS_MASTER);

        try {
            if (!json.isEmpty()) {
                opponentUser = BaseModel.deserialize(new JSONObject(json).getString("opponent"), User.class);
                ProblemCollection collection = BaseModel.deserialize(json, ProblemCollection.class);
                if (collection == null)
                    return;
                questionsToAsk = collection.getQuestions();
                for(Question q : questionsToAsk) {
                    Log.d("TAG", "jjj " + q.getQuestionText());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        user = AuthManager.getCurrentUser();
    }

    public void sendGQMinusOne() {
//        GetQuestion request = new GetQuestion();
//        request.setHintAgain(0);
//        request.setHintRemove(0);
//        if (clickedHintRemove == 1) request.setHintRemove(1);
//        request.setTime(-1);
//        request.setOk(0);
//        DuelApp.getInstance().sendMessage(request.serialize(CommandType.SEND_GET_QUESTION));
        endQuestionAnimation(false);
    }

    public void doAnimateProgressBar(ProgressBar pb, int fromX, int toX, int duration) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(pb,
                "translationX", fromX, toX);
        animation.setDuration(duration);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    public void startGameAnimation() {
        doAnimateProgressBar(myProgress, 100, 0, 500);
        doAnimateProgressBar(opProgress, -100, 0, 500);

        ObjectAnimator animation = ObjectAnimator.ofFloat(header,
                "translationY", -500, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new OvershootInterpolator());
        animation.start();

        startQuestionAnimation();
    }

    public void startQuestionAnimation() {
        if (timerScaleAnimation != null && timerScaleAnimation.hasEnded() == false)
            timerScaleAnimation.cancel();

        if (timeToAnswer != null) {
            timeToAnswer.cancel();
        }

        setTextView(questionTextView, round[problemIndex]);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(questionTextView,
                "alpha", 0f, 1f);
        fadeIn.setDuration(1000);
        fadeIn.setInterpolator(new LinearInterpolator());
        fadeIn.start();

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(questionTextView,
                "alpha", 1f, 0f);
        fadeOut.setDuration(1000);
        fadeOut.setStartDelay(1000);
        fadeOut.setInterpolator(new LinearInterpolator());

        fadeOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                askQuestion();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        fadeOut.start();
    }

    public void endQuestionAnimation(boolean alaki) {
        final boolean goToResult;
        Log.d("TAG", "abcd " + problemIndex);
        if(problemIndex == NUMBER_OF_QUESTIONS)
            goToResult = true;
        else
            goToResult = false;

        // fade out the bomb
        ObjectAnimator removeOptionsFadeOut = ObjectAnimator.ofFloat(removeOptionImageView,
                "alpha", 1f, 0f);
        removeOptionsFadeOut.setDuration(1000);
        removeOptionsFadeOut.setInterpolator(new LinearInterpolator());
        removeOptionsFadeOut.start();

        // fade out the report problem
        ObjectAnimator reportProblemFadeOut = ObjectAnimator.ofFloat(reportProblemImageView,
                "alpha", 1f, 0f);
        reportProblemFadeOut.setDuration(1000);
        reportProblemFadeOut.setInterpolator(new LinearInterpolator());
        reportProblemFadeOut.start();
        reportProblemImageView.setClickable(false);

        ObjectAnimator[] fadeOutAnim = new ObjectAnimator[NOP];
        for(int k = 0; k < NOP; k ++) {
            fadeOutAnim[k] = ObjectAnimator.ofFloat(optionBtn[k], "alpha", 1f, 0f);
            fadeOutAnim[k].setDuration(1000);
            fadeOutAnim[k].setInterpolator(new LinearInterpolator());
        }

        for(int k = 0; k < NOP; k ++) {
            if(correctOption == k) {
                optionBtn[k].setTextColor(getResources().getColor(R.color.correct_answer));
            } else {
                fadeOutAnim[k].start();
            }
        }

        ObjectAnimator questionFadeOut = ObjectAnimator.ofFloat(questionTextView, "alpha", 1f, 0f);
        questionFadeOut.setDuration(1000);
        questionFadeOut.setStartDelay(1000);
        questionFadeOut.setInterpolator(new LinearInterpolator());
        questionFadeOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (goToResult == true) {

                    Intent i = new Intent(getApplicationContext(), GameResultActivity.class);
                    i.putExtra(GameResultActivity.ARGUMENT_RESULT_INFO, resultInfo);
                    i.putExtra(GameResultActivity.ARGUMENT_OPPONENT, opponentUser.serialize());
                    i.putExtra(GameResultActivity.ARGUMENT_DIAMOND, collectedDiamond);

                    i.putIntegerArrayListExtra(GameResultActivity.ARGUMENT_CORRECT_OPTIONS, correctOptionsArrayList);

                    startActivity(i);
                    finish();
                } else {
                    startQuestionAnimation();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        for(int k = 0; k < NOP; k ++) {
            if(correctOption == k) {
                fadeOutAnim[k].setStartDelay(1000);
                fadeOutAnim[k].start();
                questionFadeOut.start();
            }
        }
    }

    public void hintRemoveTapped(final View v) {
        removeOptionDialog = new ConfirmDialog(DuelOfflineActivity.this, "حذف دو گزینه ۹۷ الماس", true);

        removeOptionDialog.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                if ((Boolean) data) {
                    hintRemoveMethod(v);
                }
            }
        });
        removeOptionDialog.show();
    }

    public void hintRemoveMethod(View v) {
        if (hintRemoveClicked)
            return;
        hintRemoveClicked = true;

        if (user.getDiamond() < HINT_REMOVE_COST) {
            showToast("متاسفانه الماس کافی ندارید.");
            v.setVisibility(View.INVISIBLE);
            return;
        } else {
            user.decreaseDiamond(HINT_REMOVE_COST);
            clickedHintRemove = 1;
            Tracker tracker = DuelApp.getInstance().getTracker(DuelApp.TrackerName.GLOBAL_TRACKER);
            // Build and send an Event.
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("button_click")
                    .setAction("report_button")
                    .setLabel("hint_remove")
                    .build());
            v.setVisibility(View.INVISIBLE);
        }

        ArrayList<Integer> canRemove = new ArrayList<Integer>();

        if (correctOption != 0 && choseOption[0] == 0)
            canRemove.add(0);
        if (correctOption != 1 && choseOption[1] == 0)
            canRemove.add(1);
        if (correctOption != 2 && choseOption[2] == 0)
            canRemove.add(2);
        if (correctOption != 3 && choseOption[3] == 0)
            canRemove.add(3);

        long seed = System.nanoTime();
        Collections.shuffle(canRemove, new Random(seed));

        int removeItem = canRemove.get(0);
        int removeItem1 = canRemove.get(1);

        for(int k = 0; k < NOP; k ++) {
            if(removeItem == k || removeItem1 == k) {
                choseOption[k] = 1;
                optionBtn[k].setVisibility(View.INVISIBLE);
            }
        }

//        hintRemoveBtn.setClickable(false);
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(DuelOfflineActivity.this, message,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);

        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        FontHelper.setKoodakFor(DuelOfflineActivity.this, toastTV);
        toast.show();
    }


    @Override
    public void onPause() {
        super.onPause();

        musicPlayer.pauseSound();
    }

    @Override
    public void onResume() {
        super.onResume();
//        setData();

        musicPlayer.playSound();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mListener);
    }

    public void changeButtonsClickableState(boolean state) {
        for(int k = 0; k < NOP; k ++) {
            optionBtn[k].setClickable(state);
        }
    }

    @Override
    public void onBackPressed() {
        ConfirmDialog dialog = new ConfirmDialog(this, getResources().getString(R.string.message_are_you_sure_to_quite_game));
        dialog.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                if ((Boolean) data) {
                    goodbye();
                }
            }
        });
        dialog.show();
    }

    private void goodbye() {
        DuelApp.getInstance().sendMessage(new BaseModel(CommandType.SEND_USER_LEFT_GAME).serialize());

        if (timerScaleAnimation != null && timerScaleAnimation.hasEnded() == false)
            timerScaleAnimation.cancel();

        if (timeToAnswer != null)
            timeToAnswer.cancel();
        finish();
    }

    BroadcastReceiver mListener = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_ASK_NEXT_QUESTION) {
                endQuestionAnimation(false);
            } else if (type == CommandType.RECEIVE_OPPONENT_SCORE) {
//                DuelApp.getInstance().toast(R.string.win_caption, Toast.LENGTH_LONG);
                try {
                    JSONObject parser = new JSONObject(json);
                    if (parser.getInt("ok") == 1) {
                        if (problemIndex == 6)
                            opponentPoints += 5;
                        else
                            opponentPoints += 3;

                        setOppTick(true);
                    } else {
//                        if (parser.getInt("time") != -1) // wrong answer
//                            opponentPoints += -1;
                        setOppTick(false);
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                setTextView(opponentPointsTextView, "" + opponentPoints);
                setProgressBar(opProgress, opponentPoints);

            } else if (type == CommandType.RECEIVE_GAME_ENDED) {
                resultInfo = json;
                endQuestionAnimation(true);
            }
        }
    });
}
