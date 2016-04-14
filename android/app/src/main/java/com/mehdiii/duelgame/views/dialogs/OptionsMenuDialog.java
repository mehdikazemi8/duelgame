package com.mehdiii.duelgame.views.dialogs;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.ChangePage;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.category.CategoryActivity;
import com.mehdiii.duelgame.views.activities.flashcards.FlashCardActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.addquestion.AddQuestionFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.duelhourtotal.DuelHourTotalFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.home.HomeFragment;
import com.mehdiii.duelgame.views.activities.home.fragments.store.StoreFragment;

import de.greenrobot.event.EventBus;

/**
 * Created by mehdiii on 12/29/15.
 */
public class OptionsMenuDialog extends DialogFragment implements View.OnClickListener {

    private final String INSTAGRAM_PACKAGE = "com.instagram.android";

    Context context;

    LinearLayout coursesRankings;
    LinearLayout store;
    LinearLayout telegramChannel;
    LinearLayout instagram;
    LinearLayout contactUs;
    LinearLayout settings;
    LinearLayout totalDuelHour;
    LinearLayout flashCard;
    LinearLayout addQuestion;

    public OptionsMenuDialog() {
        super();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        getDialog().getWindow().setAttributes(p);

        return inflater.inflate(R.layout.dialog_options_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        find(view);
        configure();
    }

    private void find(View view) {
        coursesRankings = (LinearLayout) view.findViewById(R.id.option_courses_rankings);
        store = (LinearLayout) view.findViewById(R.id.option_store);
        telegramChannel = (LinearLayout) view.findViewById(R.id.option_telegram_channel);
        instagram = (LinearLayout) view.findViewById(R.id.option_instagram);
        contactUs = (LinearLayout) view.findViewById(R.id.option_contact_us);
        settings = (LinearLayout) view.findViewById(R.id.option_settings);
        totalDuelHour = (LinearLayout) view.findViewById(R.id.option_total_duel_hour);
        flashCard = (LinearLayout) view.findViewById(R.id.option_flash_card);
        addQuestion = (LinearLayout) view.findViewById(R.id.option_add_question);
    }

    private void configure() {
        coursesRankings.setOnClickListener(this);
        store.setOnClickListener(this);
        telegramChannel.setOnClickListener(this);
        instagram.setOnClickListener(this);
        contactUs.setOnClickListener(this);
        settings.setOnClickListener(this);
        totalDuelHour.setOnClickListener(this);
        flashCard.setOnClickListener(this);
        addQuestion.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        dismiss();

        switch (view.getId()) {
            case R.id.option_courses_rankings:
                context.startActivity(new Intent(context, CategoryActivity.class));
                break;

            case R.id.option_store:
                EventBus.getDefault().post(new ChangePage(ParentActivity.STORE_PAGE));
                break;

            case R.id.option_instagram:
                try{
                    if(getContext().getPackageManager().getPackageInfo(INSTAGRAM_PACKAGE, 0) != null) {
                        Intent instaIntent = getContext().getPackageManager().getLaunchIntentForPackage(INSTAGRAM_PACKAGE);
                        instaIntent.setComponent(new ComponentName(INSTAGRAM_PACKAGE, "com.instagram.android.activity.UrlHandlerActivity"));
                        instaIntent.setData(Uri.parse("https://www.instagram.com/p/BB18CnnHKI0/"));
                        startActivity(instaIntent);
                    } else {
                        Intent instagramIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/duelkonkoor/"));
                        startActivity(instagramIntent);
                    }
                } catch (Exception e) {
                    Log.d("TAG", "aaxx 33");
                    Intent instagramIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/duelkonkoor/"));
                    startActivity(instagramIntent);
                    e.printStackTrace();
                }
                break;

            case R.id.option_telegram_channel:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://telegram.me/duelkonkoor"));
                startActivity(browserIntent);
                break;

            case R.id.option_contact_us:
                sendReport();
                break;

            case R.id.option_settings:
                EventBus.getDefault().post(new ChangePage(ParentActivity.SETTINGS_PAGE));
                break;

            case R.id.option_total_duel_hour:
                DuelHourTotalFragment fragment = (DuelHourTotalFragment) Fragment.instantiate(getActivity(), DuelHourTotalFragment.class.getName(), null);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_holder, fragment, ParentActivity.DUEL_HOUR_TOTAL_FRAGMENT)
                        .addToBackStack(null)
                        .commit();
                break;

            case R.id.option_flash_card:
                Intent intent = new Intent(getContext(), FlashCardActivity.class);
                startActivity(intent);
                break;
            case R.id.option_add_question:
//                EventBus.getDefault().post(new ChangePage(ParentActivity.ADD_QUESTION_PAGE));
                AddQuestionFragment addQuestionFragmentment = (AddQuestionFragment) Fragment.instantiate(getActivity(), AddQuestionFragment.class.getName(), null);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_holder, addQuestionFragmentment, ParentActivity.ADD_QUESTION_FRAGMENT)
                        .addToBackStack(null)
                        .commit();
                break;

        }
    }

    private void sendReport() {
        Intent send = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode("duelapp@gmail.com") +
                "?subject=" + Uri.encode(getResources().getString(R.string.caption_send_report_title)) +
                "&body=" + Uri.encode("");
        Uri uri = Uri.parse(uriText);

        send.setData(uri);
        startActivity(Intent.createChooser(send, "Send mail..."));
    }
}
