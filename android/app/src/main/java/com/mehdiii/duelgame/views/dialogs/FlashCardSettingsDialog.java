package com.mehdiii.duelgame.views.dialogs;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.DateManager;
import com.mehdiii.duelgame.managers.FlashCardSettingManager;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.models.FlashCard;
import com.mehdiii.duelgame.models.FlashCardSetting;
import com.mehdiii.duelgame.models.FlashCardsSettings;
import com.mehdiii.duelgame.utils.DeckPersister;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.activities.flashcards.FlashCardActivity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.mehdiii.duelgame.views.activities.ParentActivity.setAlarmForFlashCards;

/**
 * Created by frshd on 5/4/16.
 */
public class FlashCardSettingsDialog extends Dialog implements View.OnClickListener {
    public FlashCardSettingsDialog(Context context, int num, FlashCard card) {
        super(context);
        this.numberOfCards = num;
        this.card = card;
    }
    FlashCard card;
    int numberOfCards;
    NumberPicker dayPicker;
    NumberPicker wordPicker;
    SwitchButton notifSwitch;
    TimePicker notifTimePicker;
    TextView notif;
    TextView notifOn;
    TextView notifOff;
    TextView notifTime;
    TextView wordPick;
    TextView dayPick;
    Button saveButton;
    private PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_flashcard_settings);

        find();
        configure();

    }

    private void find() {

        notif = (TextView) findViewById(R.id.notif_text);
        notifOff = (TextView) findViewById(R.id.notif_off_text);
        notifOn = (TextView) findViewById(R.id.notif_on_text);
        notifTime = (TextView) findViewById(R.id.notif_time_text);
        wordPick = (TextView) findViewById(R.id.word_picker_text);
        dayPick = (TextView) findViewById(R.id.day_picker_text);
        saveButton = (Button) findViewById(R.id.button_save);
        dayPicker = (NumberPicker) findViewById(R.id.day_picker);
        wordPicker = (NumberPicker) findViewById(R.id.word_picker);
        notifSwitch = (SwitchButton) findViewById(R.id.notif_switch);
        notifTimePicker = (TimePicker) findViewById(R.id.notif_time);
    }

    private void configure() {

        saveButton.setOnClickListener(this);
        dayPicker.setMaxValue(100);
        dayPicker.setMinValue(10);
        dayPicker.setValue(100);
        dayPicker.setWrapSelectorWheel(false);
        wordPicker.setMaxValue(100);
        wordPicker.setMinValue(10);
        wordPicker.setValue((numberOfCards * 5) / 100);
        wordPicker.setWrapSelectorWheel(false);
        notifSwitch.setChecked(false);
        notifTimePicker.setEnabled(false);
        notifTimePicker.setVisibility(View.GONE);
        notifTime.setVisibility(View.GONE);
        dayPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                wordPicker.setValue((numberOfCards * 5) / i1);
            }
        });
        wordPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                dayPicker.setValue((numberOfCards * 5) / i1);
            }
        });
        notifSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    notifTimePicker.setEnabled(true);
                    notifTimePicker.setVisibility(View.VISIBLE);
                    notifTime.setVisibility(View.VISIBLE);
                } else {
                    notifTimePicker.setEnabled(false);
                    notifTimePicker.setVisibility(View.GONE);
                    notifTime.setVisibility(View.GONE);
                }
            }
        });

        FontHelper.setKoodakFor(getContext(),notif, notifOff, notifOn, notifTime, wordPick, dayPick, saveButton);

        if(FlashCardSettingManager.getSettingById(getContext(), card.getId()) != null){
            addPreviousData();
        }
    }

    private void addPreviousData() {

        FlashCardSetting flashCardSetting = FlashCardSettingManager.getSettingById(getContext(), card.getId());
        wordPicker.setValue(flashCardSetting.getDailyGoal());
        if(flashCardSetting.getAlarm() != null) {

            notifSwitch.setChecked(true);
            notifTimePicker.setVisibility(View.VISIBLE);
            notifTime.setVisibility(View.VISIBLE);
            notifTimePicker.setEnabled(true);
            Calendar oldAlarm = flashCardSetting.getAlarm();
            notifTimePicker.setCurrentHour(oldAlarm.get(Calendar.HOUR_OF_DAY));
            notifTimePicker.setCurrentMinute(oldAlarm.get(Calendar.MINUTE));

        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_save:
                saveSettings();
                DuelApp.getInstance().toast(R.string.message_settings_save_successful, Toast.LENGTH_LONG);
                dismiss();
                break;
        }
    }

    public void saveSettings(){
        FlashCardSetting flashCardSetting = new FlashCardSetting();
        flashCardSetting.setCardId(card.getId());
        flashCardSetting.setDailyGoal(wordPicker.getValue());

        if(notifSwitch.isChecked()) {
//            AlarmManager manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, notifTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, notifTimePicker.getCurrentMinute());
//            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntent);
            flashCardSetting.setAlarm(calendar);
        } else {
            flashCardSetting.setAlarm(null);
        }
        FlashCardSettingManager.saveSetting(getContext(), flashCardSetting);
    }
}

