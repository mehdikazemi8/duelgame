package com.mehdiii.duelgame.views.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlayGameActivity extends MyBaseActivity {

    public static final String ARGUMENT_OPPONENT = "argument_opponent";

    final int DURATION = 20000;
    final int durationNumberOfProblem = 1000;
    final int durationOptions = 1000;
    final int durationCorrectOption = 1000;

    long remainingTimeOfThisQuestion;
    CountDownTimer timeToAnswer = null;
    RotateAnimation rotateTickAnimation = null;
    String resultInfo;

    String correctAnswerStr;
    int correctOption;
    boolean[] choseOption = new boolean[4];
    int numberOfOptionChose;

    int iAnsweredThisTime;
    boolean iAnsweredThisCorrect;
    int opponentAnsweredThisTime;
    int problemIndex;

    boolean hintRemoveClicked;
    boolean hintAgainClicked;

    Button option0Btn, option1Btn, option2Btn, option3Btn;
    ImageView hintRemoveBtn, hintAgainBtn;

    private Display mobileDisplay;
    private Point screenSize = new Point();
    private int screenHeight;

    private ImageView tick;

    MediaPlayer myPlayer;
    int WRONG_ANSWER, CORRECT_ANSWER;

    ProgressBar myProgress, opProgress;

    private String[] round = new String[NUMBER_OF_QUESTIONS];

    BroadcastReceiver mListener = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_ASK_NEXT_QUESTION) {
                endQuestionAnimation(false);
            } else if (type == CommandType.RECEIVE_OPPONENT_SCORE) {
                try {
                    JSONObject parser = new JSONObject(json);

                    if (parser.getInt("ok") == 1) {
                        opponentAnsweredThisTime = parser.getInt("time");
                        if (problemIndex == 5)
                            opponentPoints += 5;
                        else
                            opponentPoints += 3;
                    } else {
                        if (parser.getInt("time") != -1) // wrong answer
                            opponentPoints += -1;
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                setTextView(playGameOpScore, "" + opponentPoints);
                setProgressBar(opProgress, opponentPoints);

            } else if (type == CommandType.RECEIVE_GAME_ENDED) {
                resultInfo = json;
                endQuestionAnimation(true);
            }
        }
    });

    public void cancelDanceHintAgain() {
        if (danceHintAgainX != null && danceHintAgainX.isRunning())
            danceHintAgainX.cancel();
        if (danceHintAgainY != null && danceHintAgainY.isRunning())
            danceHintAgainY.cancel();
    }

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
        choseOption[0] = choseOption[1] = choseOption[2] = choseOption[3] = false;
        hintRemoveClicked = hintAgainClicked = false;

        iAnsweredThisCorrect = false;
        iAnsweredThisTime = -1;
        opponentAnsweredThisTime = -1;
        remainingTimeOfThisQuestion = 20;

        String questionText = questionsToAsk[problemIndex].questionText;
        setTextView(playGameQuestionText, questionText);

        String[] opts = questionsToAsk[problemIndex].options;
        problemIndex++;
        correctAnswerStr = opts[0];

        shuffleArray(opts);
        shuffleArray(opts);

        if (opts[0].equals(correctAnswerStr))
            correctOption = 0;
        else if (opts[1].equals(correctAnswerStr))
            correctOption = 1;
        else if (opts[2].equals(correctAnswerStr))
            correctOption = 2;
        else
            correctOption = 3;

        setButton(option0Btn, opts[0]);
        setButton(option1Btn, opts[1]);
        setButton(option2Btn, opts[2]);
        setButton(option3Btn, opts[3]);

        timeToAnswer = new CountDownTimer(DURATION, 1000) {
            @Override
            public void onTick(long arg0) {
                remainingTimeOfThisQuestion = arg0 / 1000;
            }

            @Override
            public void onFinish() {

                if (iAnsweredThisCorrect == true)
                    return;

                sendGQMinusOne();
            }
        };

        option0Btn.setClickable(true);
        option1Btn.setClickable(true);
        option2Btn.setClickable(true);
        option3Btn.setClickable(true);

        option0Btn.setBackgroundResource(R.drawable.option_button);
        option1Btn.setBackgroundResource(R.drawable.option_button);
        option2Btn.setBackgroundResource(R.drawable.option_button);
        option3Btn.setBackgroundResource(R.drawable.option_button);

//        option0Btn.setVisibility(View.VISIBLE);
//        option1Btn.setVisibility(View.VISIBLE);
//        option2Btn.setVisibility(View.VISIBLE);
//        option3Btn.setVisibility(View.VISIBLE);

        option0Btn.setTextColor(getResources().getColor(R.color.play_game_option_btn_text));
        option1Btn.setTextColor(getResources().getColor(R.color.play_game_option_btn_text));
        option2Btn.setTextColor(getResources().getColor(R.color.play_game_option_btn_text));
        option3Btn.setTextColor(getResources().getColor(R.color.play_game_option_btn_text));

        hintAgainBtn.setClickable(true);
        hintRemoveBtn.setClickable(true);

        hintAgainView.setPivotX(hintAgainView.getRight());
        hintAgainView.setPivotY(0);
        hintRemoveView.setPivotX(0);
        hintRemoveView.setPivotY(0);

        hintAgainViewIsOpen = hintRemoveViewIsOpen = true;
        doAnimateHintOption(hintRemoveView, 0f, 1f, 1000, 1000);
        doAnimateHintOption(hintAgainView, 0f, 1f, 1000, 1000);

        setStartAnimation(0f, 1f, playGameQuestionText, option0Btn, option1Btn, option2Btn, option3Btn);
    }

    public void setStartAnimation(float from, float to, TextView... views) {

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

                        rotateTickAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        rotateTickAnimation.setDuration(20000);
                        rotateTickAnimation.setInterpolator(new LinearInterpolator());
                        tick.startAnimation(rotateTickAnimation);
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

    public void answered(View v) {
        if (iAnsweredThisTime != -1) {
            return;
        }

        numberOfOptionChose += 1;

        iAnsweredThisTime = (int) remainingTimeOfThisQuestion;

        choseOption[Integer.parseInt(v.getContentDescription().toString())] = true;
        doDisableButtons();

        int ok = 0;
        if (correctAnswerStr.compareTo(((Button) v).getText().toString()) == 0) {
            myPlayer = MediaPlayer.create(this, CORRECT_ANSWER);

            iAnsweredThisCorrect = true;

            if (problemIndex == 6)
                userPoints += 5;
            else
                userPoints += 3;

            setTextView(playGameMyScore, "" + userPoints);

            setProgressBar(myProgress, userPoints);

            //((Button) v).setBackgroundColor(Color.GREEN);
            ((Button) v).setTextColor(getResources().getColor(R.color.correct_answer));
            ok = 1;

            hintAgainBtn.setClickable(false);
            hintRemoveBtn.setClickable(false);
        } else {
            myPlayer = MediaPlayer.create(this, WRONG_ANSWER);
            userPoints += -1;

            setTextView(playGameMyScore, "" + userPoints);
            setProgressBar(myProgress, userPoints);

            if (hintAgainClicked == true) {
                iAnsweredThisTime = -1;
                hintAgainClicked = false;

                if (choseOption[0] == false)
                    option0Btn.setClickable(true);
                if (choseOption[1] == false)
                    option1Btn.setClickable(true);
                if (choseOption[2] == false)
                    option2Btn.setClickable(true);
                if (choseOption[3] == false)
                    option3Btn.setClickable(true);
            }
//            ((Button) v).setBackgroundColor(Color.RED);
            ((Button) v).setTextColor(getResources().getColor(R.color.wrong_answer));

            if (numberOfOptionChose == 1 && hintAgainViewIsOpen == true) {
//                hintAgainView.setPivotX(hintAgainView.getX()+hintAgainView.getWidth()/2);
//                hintAgainView.setPivotY(hintAgainView.getY()+hintAgainView.getHeight()/2);

                danceHintAgainX = ObjectAnimator.ofFloat(hintAgainView, "scaleX", 1, 1.1f, 0.95f, 1);
                danceHintAgainX.setDuration(1000);
                danceHintAgainX.setRepeatCount(1);
                danceHintAgainX.setInterpolator(new AccelerateInterpolator());
                danceHintAgainX.setRepeatMode(ObjectAnimator.REVERSE);
                danceHintAgainX.start();

                danceHintAgainY = ObjectAnimator.ofFloat(hintAgainView, "scaleY", 1, 1.1f, 0.95f, 1);
                danceHintAgainY.setDuration(1000);
                danceHintAgainY.setRepeatCount(1);
                danceHintAgainY.setInterpolator(new OvershootInterpolator());
                danceHintAgainY.setRepeatMode(ObjectAnimator.REVERSE);
                danceHintAgainY.start();
            }
        }

        myPlayer.start();

        JSONObject query = new JSONObject();
        try {
            query.put("code", "GQ");
            query.put("time", remainingTimeOfThisQuestion);
            query.put("ok", ok);

            DuelApp.getInstance().sendMessage(query.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (numberOfOptionChose == 2 && hintRemoveClicked == true && iAnsweredThisCorrect == false) {
            iAnsweredThisCorrect = true;
            sendGQMinusOne();
        }
    }

    public void setButton(Button tv, String s) {
        tv.setText(s);
    }

    public void setTextView(TextView tv, String s) {
        tv.setText(s);
    }

    private TextView playGameOpName;
    private TextView playGameOpScore;
    private TextView playGameMyName;
    private TextView playGameMyScore;
    private TextView playGameQuestionText;

    private TextView playGameHintAgainCost;
    private TextView playGameHintAgainText;
    private TextView playGameHintRemoveCost;
    private TextView playGameHintRemoveText;

    private RelativeLayout hintAgainView, hintRemoveView;
    private boolean hintAgainViewIsOpen, hintRemoveViewIsOpen;

    private LinearLayout header;

    private ObjectAnimator danceHintAgainX, danceHintAgainY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        findControls();

        readArguments();

        hintAgainViewIsOpen = hintRemoveViewIsOpen = false;

        round[0] = "سوال اول";
        round[1] = "سوال دوم";
        round[2] = "سوال سوم";
        round[3] = "سوال چهارم";
        round[4] = "سوال پنجم";
        round[5] = "سوال ششم";

        WRONG_ANSWER = R.raw.wrong_answer;
        CORRECT_ANSWER = R.raw.correct_answer;

        LocalBroadcastManager.getInstance(this).registerReceiver(mListener, DuelApp.getInstance().getIntentFilter());

        setTextView(playGameMyName, AuthManager.getCurrentUser().getName());
        setTextView(playGameOpName, opponentUser.getName());

        opponentPoints = userPoints = 0;
        problemIndex = 0;

        mobileDisplay = getWindowManager().getDefaultDisplay();
        screenSize = new Point();
        mobileDisplay.getSize(screenSize);
        screenHeight = screenSize.y;

        configureControls();

        startGameAnimation();
    }

    private void configureControls() {
        FontHelper.setKoodakFor(getApplicationContext(),
                option0Btn, option1Btn, option2Btn, option3Btn,
                playGameOpName, playGameOpScore,
                playGameMyName, playGameMyScore,
                playGameQuestionText,
                playGameHintAgainCost, playGameHintRemoveCost,
                playGameHintAgainText, playGameHintRemoveText);
    }

    private void findControls() {
        tick = (ImageView) findViewById(R.id.play_game_tick);
        playGameOpName = (TextView) findViewById(R.id.play_game_op_name);
        playGameOpScore = (TextView) findViewById(R.id.play_game_op_score);
        playGameMyName = (TextView) findViewById(R.id.play_game_my_name);
        playGameMyScore = (TextView) findViewById(R.id.play_game_my_score);
        playGameQuestionText = (TextView) findViewById(R.id.play_game_question_text);

        option0Btn = (Button) findViewById(R.id.play_game_option_0);
        option1Btn = (Button) findViewById(R.id.play_game_option_1);
        option2Btn = (Button) findViewById(R.id.play_game_option_2);
        option3Btn = (Button) findViewById(R.id.play_game_option_3);
        hintRemoveBtn = (ImageView) findViewById(R.id.play_game_hint_remove);
        hintAgainBtn = (ImageView) findViewById(R.id.play_game_hint_again);


        myProgress = (ProgressBar) findViewById(R.id.play_game_my_progress);
        opProgress = (ProgressBar) findViewById(R.id.play_game_op_progress);

        playGameHintAgainCost = (TextView) findViewById(R.id.play_game_hint_again_cost);
        playGameHintAgainText = (TextView) findViewById(R.id.play_game_hint_again_text);
        playGameHintRemoveCost = (TextView) findViewById(R.id.play_game_hint_remove_cost);
        playGameHintRemoveText = (TextView) findViewById(R.id.play_game_hint_remove_text);

        hintAgainView = (RelativeLayout) findViewById(R.id.play_game_hint_again_view);
        hintRemoveView = (RelativeLayout) findViewById(R.id.play_game_hint_remove_view);
        header = (LinearLayout) findViewById(R.id.play_game_header);
    }

    User opponentUser;

    private void readArguments() {
        Bundle params = getIntent().getExtras();
        if (params == null)
            return;
        String json = params.getString(ARGUMENT_OPPONENT, "");

        if (!json.isEmpty()) {
            opponentUser = BaseModel.deserialize(json, User.class);
        }

    }

    public void sendGQMinusOne() {
        JSONObject query = new JSONObject();
        try {
            query.put("code", "GQ");
            query.put("time", -1);
            query.put("ok", 0);

            DuelApp.getInstance().sendMessage(query.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void doAnimateHintOption(final RelativeLayout hintView, float from, float to, int duration, int startDelay) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(hintView,
                "scaleX", from, to);
        animation.setDuration(duration);
        animation.setStartDelay(startDelay);
        animation.setInterpolator(new LinearInterpolator());

        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                hintView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        animation.start();
    }

    public void doAnimateProgressBar(ProgressBar pb, int fromX, int toX, int duration) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(pb,
                "translationX", fromX, toX);
        animation.setDuration(duration);
        animation.setInterpolator(new LinearInterpolator());
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
        if (rotateTickAnimation != null && rotateTickAnimation.hasEnded() == false)
            rotateTickAnimation.cancel();

        if (timeToAnswer != null) {
            timeToAnswer.cancel();
        }

//        option0Btn.setVisibility(View.INVISIBLE);
//        option1Btn.setVisibility(View.INVISIBLE);
//        option2Btn.setVisibility(View.INVISIBLE);
//        option3Btn.setVisibility(View.INVISIBLE);

        setTextView(playGameQuestionText, round[problemIndex]);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(playGameQuestionText,
                "alpha", 0f, 1f);
        fadeIn.setDuration(1000);
        fadeIn.setInterpolator(new LinearInterpolator());
        fadeIn.start();

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(playGameQuestionText,
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

    public void endQuestionAnimation(final boolean goToResult) {

        ObjectAnimator fadeOut0 = ObjectAnimator.ofFloat(option0Btn, "alpha", 1f, 0f);
        fadeOut0.setDuration(1000);
        fadeOut0.setInterpolator(new LinearInterpolator());

        ObjectAnimator fadeOut1 = ObjectAnimator.ofFloat(option1Btn, "alpha", 1f, 0f);
        fadeOut1.setDuration(1000);
        fadeOut1.setInterpolator(new LinearInterpolator());

        ObjectAnimator fadeOut2 = ObjectAnimator.ofFloat(option2Btn, "alpha", 1f, 0f);
        fadeOut2.setDuration(1000);
        fadeOut2.setInterpolator(new LinearInterpolator());

        ObjectAnimator fadeOut3 = ObjectAnimator.ofFloat(option3Btn, "alpha", 1f, 0f);
        fadeOut3.setDuration(1000);
        fadeOut3.setInterpolator(new LinearInterpolator());

        if (correctOption == 0) {
            option0Btn.setTextColor(getResources().getColor(R.color.correct_answer));
            fadeOut1.start();
            fadeOut2.start();
            fadeOut3.start();
        } else if (correctOption == 1) {
            option1Btn.setTextColor(getResources().getColor(R.color.correct_answer));
            fadeOut0.start();
            fadeOut2.start();
            fadeOut3.start();
        } else if (correctOption == 2) {
            option2Btn.setTextColor(getResources().getColor(R.color.correct_answer));
            fadeOut0.start();
            fadeOut1.start();
            fadeOut3.start();
        } else // if(correctOption == 3)
        {
            option3Btn.setTextColor(getResources().getColor(R.color.correct_answer));
            fadeOut0.start();
            fadeOut1.start();
            fadeOut2.start();
        }

        cancelDanceHintAgain();

        if (hintAgainViewIsOpen == true) {
            doAnimateHintOption(hintAgainView, 1f, 0f, 1000, 0);
            hintAgainViewIsOpen = false;
        }
        if (hintRemoveViewIsOpen == true) {
            doAnimateHintOption(hintRemoveView, 1f, 0f, 1000, 0);
            hintRemoveViewIsOpen = false;
        }

        ObjectAnimator questionFadeOut = ObjectAnimator.ofFloat(playGameQuestionText, "alpha", 1f, 0f);
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

        switch (correctOption) {
            case 0:
                fadeOut0.setStartDelay(1000);
                fadeOut0.start();
                questionFadeOut.start();
                break;
            case 1:
                fadeOut1.setStartDelay(1000);
                fadeOut1.start();
                questionFadeOut.start();
                break;
            case 2:
                fadeOut2.setStartDelay(1000);
                fadeOut2.start();
                questionFadeOut.start();
                break;
            case 3:
                fadeOut3.setStartDelay(1000);
                fadeOut3.start();
                questionFadeOut.start();
                break;
        }
    }

    public void hintRemoveMethod(View v) {
        if (hintRemoveClicked == true)
            return;
        hintRemoveClicked = true;

        if (hintRemoveViewIsOpen == true) {
            doAnimateHintOption(hintRemoveView, 1f, 0f, 100, 0);
            hintRemoveViewIsOpen = false;
        }

        if (numberOfOptionChose == 2 && iAnsweredThisCorrect == false) {
            iAnsweredThisCorrect = true;
            sendGQMinusOne();
        }

        ArrayList<Integer> canRemove = new ArrayList<Integer>();

        if (correctOption != 0 && choseOption[0] == false)
            canRemove.add(0);
        if (correctOption != 1 && choseOption[1] == false)
            canRemove.add(1);
        if (correctOption != 2 && choseOption[2] == false)
            canRemove.add(2);
        if (correctOption != 3 && choseOption[3] == false)
            canRemove.add(3);

        int removeItem = canRemove.get(rand.nextInt((int) canRemove.size()));

        if (removeItem == 0) {
            option0Btn.setVisibility(View.INVISIBLE);
            choseOption[0] = true;
        } else if (removeItem == 1) {
            option1Btn.setVisibility(View.INVISIBLE);
            choseOption[1] = true;
        } else if (removeItem == 2) {
            option2Btn.setVisibility(View.INVISIBLE);
            choseOption[2] = true;
        } else {
            option3Btn.setVisibility(View.INVISIBLE);
            choseOption[3] = true;
        }

        hintRemoveBtn.setClickable(false);
    }

    public void hintAgainMethod(View v) {
        if (iAnsweredThisTime == -1) {
            hintAgainClicked = true;
        }

        if (hintAgainViewIsOpen == true) {
            cancelDanceHintAgain();

            doAnimateHintOption(hintAgainView, 1f, 0f, 100, 0);
            hintAgainViewIsOpen = false;
        }

        iAnsweredThisTime = -1;

        if (choseOption[0] == false)
            option0Btn.setClickable(true);
        if (choseOption[1] == false)
            option1Btn.setClickable(true);
        if (choseOption[2] == false)
            option2Btn.setClickable(true);
        if (choseOption[3] == false)
            option3Btn.setClickable(true);

        hintAgainBtn.setClickable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mListener);
    }

    public void doDisableButtons() {
        option0Btn.setClickable(false);
        option1Btn.setClickable(false);
        option2Btn.setClickable(false);
        option3Btn.setClickable(false);
    }

    @Override
    public void onBackPressed() {

        JSONObject query = new JSONObject();
        try {
            query.put("code", "ULG");
            DuelApp.getInstance().sendMessage(query.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (rotateTickAnimation != null && rotateTickAnimation.hasEnded() == false)
            rotateTickAnimation.cancel();

        if (timeToAnswer != null)
            timeToAnswer.cancel();
        finish();
    }
}
