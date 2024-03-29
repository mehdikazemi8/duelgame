package com.mehdiii.duelgame.views.activities.home.fragments.settings;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.models.ChangePage;
import com.mehdiii.duelgame.models.DeliveryReport;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.events.OnSoundStateChanged;
import com.mehdiii.duelgame.models.events.OnPurchaseResult;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;
import com.mehdiii.duelgame.views.custom.CustomTextView;
import com.mehdiii.duelgame.views.dialogs.AvatarSelectionDialog;

import de.greenrobot.event.EventBus;

/**
 * Created by omid on 4/5/2015.
 */
public class SettingsFragment extends FlippableFragment implements View.OnClickListener {
    public static final String PREFERENCE_VOICE = "preference_voice";

    CustomTextView diamondCount;
    Button refillButton;
    Button buyDiamondButton;
    CustomTextView textViewHearts;

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText schoolEditText;
    private Spinner spinnerMajor;
    private ImageView avatarImageView;
    private TextView textViewHintAvatar;
    private Spinner spinnerProvince;
    private TextView textViewGirl;
    private TextView textViewSoundOn;
    private TextView textViewSoundOff;
    private SwitchButton switchGender;
    private SwitchButton switchMusic;
    private TextView textViewBoy;
    Button saveButton;

    User newSettings = new User();
    ProgressDialog progressDialog = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, DuelApp.getInstance().getIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        find(view);
        configure();
    }

    private void find(View view) {
        avatarImageView = (ImageView) view.findViewById(R.id.imageView_avatar);
        textViewHintAvatar = (TextView) view.findViewById(R.id.textView_hint_avatat);
        spinnerProvince = (Spinner) view.findViewById(R.id.spinner_province);
        textViewGirl = (TextView) view.findViewById(R.id.textView_girl);
        switchGender = (SwitchButton) view.findViewById(R.id.switch_gender);
        textViewBoy = (TextView) view.findViewById(R.id.textView_boy);
        switchMusic = (SwitchButton) view.findViewById(R.id.switch_music);
        usernameEditText = (EditText) view.findViewById(R.id.editText_username);
        emailEditText = (EditText) view.findViewById(R.id.editText_email);
        schoolEditText= (EditText) view.findViewById(R.id.editText_school);
        spinnerMajor = (Spinner) view.findViewById(R.id.editText_field);
        saveButton = (Button) view.findViewById(R.id.button_save);
        textViewSoundOn = (TextView) view.findViewById(R.id.textView_music_on);
        textViewSoundOff = (TextView) view.findViewById(R.id.textView_music_off);
        diamondCount = (CustomTextView) view.findViewById(R.id.home_diamond_cnt);
        textViewHearts = (CustomTextView) view.findViewById(R.id.textView_heart);
        buyDiamondButton = (Button) view.findViewById(R.id.button_buy_diamond);
        refillButton = (Button) view.findViewById(R.id.button_refill);
    }

    private void configure() {
        FontHelper.setKoodakFor(getActivity(), textViewHintAvatar, textViewGirl, textViewBoy, usernameEditText, emailEditText, schoolEditText, saveButton, textViewSoundOff, textViewSoundOn);
        avatarImageView.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        switchMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                GlobalPreferenceManager.writeBoolean(getActivity(), PREFERENCE_VOICE, b);
                EventBus.getDefault().post(new OnSoundStateChanged(b));
            }
        });
        diamondCount.setText(String.valueOf(AuthManager.getCurrentUser().getDiamond()));
        buyDiamondButton.setTypeface(FontHelper.getIcons(getActivity()));
        buyDiamondButton.setOnClickListener(this);
        refillButton.setTypeface(FontHelper.getIcons(getActivity()));
        refillButton.setOnClickListener(this);
        textViewHearts.setText(String.valueOf(AuthManager.getCurrentUser().getHeart()));
        initializeData();
    }

    @Override
    public void onBringToFront() {
        super.onBringToFront();
        newSettings = new User();
        newSettings.setAvatar(AuthManager.getCurrentUser().getAvatar());

        if (getView() != null)
            initializeData();
        else {
            Log.d("TAG", "view is null");
        }
    }

    private void initializeData() {
        newSettings.setAvatar(AuthManager.getCurrentUser().getAvatar());

        User user = AuthManager.getCurrentUser();
        usernameEditText.setText(user.getName());
        emailEditText.setText(user.getEmail());
        schoolEditText.setText(user.getSchool());
        if(user.getMajor() != null)
            spinnerMajor.setSelection(Integer.valueOf(user.getMajor()));
        spinnerProvince.setSelection(user.getProvince());
        switchGender.setChecked(user.getGender() == 1);
        avatarImageView.setImageResource(AvatarHelper.getResourceId(getActivity(), user.getAvatar()));
        switchMusic.setChecked(GlobalPreferenceManager.readBoolean(getActivity(), PREFERENCE_VOICE, true));
    }

    private void chooseAvatar() {
        AvatarSelectionDialog dialog = new AvatarSelectionDialog();
        dialog.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                setAvatar((Integer) data);
            }
        });
        dialog.show(getFragmentManager(), "DIALOG_AVATAR_CHOOSER");
    }

    private void setAvatar(int position) {
        newSettings.setAvatar(position);
        avatarImageView.setImageResource(AvatarHelper.getResourceId(getActivity(), position));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_avatar:
                chooseAvatar();
                break;
            case R.id.button_save:
                saveSettings();
                break;
            case R.id.button_buy_diamond:
            case R.id.button_refill:
                refillHeart();
                break;
        }
    }
    private void refillHeart() {
        EventBus.getDefault().post(new ChangePage(ParentActivity.STORE_PAGE));
    }
    private void saveSettings() {

        if (validateForm()) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("لطفا کمی صبر کنید");
            progressDialog.setCancelable(false);
            progressDialog.show();
            User currentUser = AuthManager.getCurrentUser();
            newSettings.setId(currentUser.getId());
            newSettings.setName(this.usernameEditText.getText().toString().trim());
            newSettings.setEmail(this.emailEditText.getText().toString().trim());
            newSettings.setSchool(this.schoolEditText.getText().toString().trim());
            newSettings.setMajor(String.valueOf(this.spinnerMajor.getSelectedItemPosition()));
            newSettings.setProvince(this.spinnerProvince.getSelectedItemPosition());
            newSettings.setGender(this.switchGender.isChecked() ? 1 : 0);
            Log.d("TAG", "user edited"+ newSettings.serialize(CommandType.SEND_UPDATE_SETTINGS));
            DuelApp.getInstance().sendMessage(newSettings.serialize(CommandType.SEND_UPDATE_SETTINGS));
        }
    }

    private boolean validateForm() {
        // TODO the logic in this part can be troublesome, review it ASAP.
        if (usernameEditText.getText().length() == 0) {
            Toast toast = Toast.makeText(getActivity(), "لطفا اسم خود را وارد نمایید.", Toast.LENGTH_SHORT);
            toast.show();
            return false;

        }

        if (spinnerProvince.getSelectedItem().toString().equals("انتخاب استان")) {
            Toast toast = Toast.makeText(getActivity(), "لطفا استان خود را انتخاب نمایید.", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_UPDATE_SETTINGS) {
                if (progressDialog != null)
                    progressDialog.dismiss();

                DeliveryReport report = BaseModel.deserialize(json, DeliveryReport.class);
                int message = 0;
                if (report != null && report.getStatusType() == DeliveryReport.DeliveryReportType.SUCCESSFUL) {

                    AuthManager.getCurrentUser().setName(newSettings.getName());
                    AuthManager.getCurrentUser().setGender(newSettings.getGender());
                    AuthManager.getCurrentUser().setEmail(newSettings.getEmail());
                    AuthManager.getCurrentUser().setAvatar(newSettings.getAvatar());
                    AuthManager.getCurrentUser().setProvince(newSettings.getProvince());
                    AuthManager.getCurrentUser().setMajor(newSettings.getMajor());
                    AuthManager.getCurrentUser().setSchool(newSettings.getSchool());
                    // notify user changed to update ui
                    EventBus.getDefault().post(new OnPurchaseResult());
                    // set message for toast
                    message = R.string.message_settings_save_successful;
                } else message = R.string.message_settings_save_failed;

                DuelApp.getInstance().toast(message, Toast.LENGTH_SHORT);
            }
        }
    });


}
