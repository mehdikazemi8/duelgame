package com.mehdiii.duelgame.views.activities.home.fragments.settings;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;
import com.mehdiii.duelgame.views.dialogs.AvatarSelectionDialog;

/**
 * Created by omid on 4/5/2015.
 */
public class SettingsFragment extends FlippableFragment implements View.OnClickListener {
    private EditText usernameEditText;
    private EditText emailEditText;
    private ImageView avatarImageView;
    private TextView textViewHintAvatat;
    private Spinner spinnerProvince;
    private TextView textViewGirl;
    private SwitchButton switchGender;
    private SwitchButton switchMusic;
    private TextView textViewBoy;
    Button saveButton;

    User newSettings = new User();

    BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_LOGIN_INFO) {
                User updatedUser = BaseModel.deserialize(json, User.class);
                AuthManager.getCurrentUser().setName(updatedUser.getName());
                AuthManager.getCurrentUser().setEmail(updatedUser.getEmail());
                AuthManager.getCurrentUser().setAvatar(updatedUser.getAvatar());
                AuthManager.getCurrentUser().setProvince(updatedUser.getProvince());
            }
        }
    });

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

        avatarImageView = (ImageView) view.findViewById(R.id.imageView_avatar);
        textViewHintAvatat = (TextView) view.findViewById(R.id.textView_hint_avatat);
        spinnerProvince = (Spinner) view.findViewById(R.id.spinner_province);
        textViewGirl = (TextView) view.findViewById(R.id.textView_girl);
        switchGender = (SwitchButton) view.findViewById(R.id.switch_gender);
        textViewBoy = (TextView) view.findViewById(R.id.textView_boy);
        switchMusic = (SwitchButton) view.findViewById(R.id.switch_music);
        usernameEditText = (EditText) view.findViewById(R.id.editText_username);
        emailEditText = (EditText) view.findViewById(R.id.editText_email);
        saveButton = (Button) view.findViewById(R.id.button_save);

        configure();
    }

    private void configure() {
        FontHelper.setKoodakFor(getActivity(), textViewHintAvatat, textViewGirl, textViewBoy, usernameEditText, emailEditText, saveButton);
        avatarImageView.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onBringToFront() {
        super.onBringToFront();
        newSettings = new User();
        if (getView() != null)
            initializeData();
    }

    private void initializeData() {
        User user = AuthManager.getCurrentUser();
        usernameEditText.setText(user.getName());
        emailEditText.setText(user.getEmail());
        spinnerProvince.setSelection(user.getProvince());
        switchGender.setChecked(user.getGender() == 1);
        avatarImageView.setImageResource(AvatarHelper.getResourceId(getActivity(), user.getAvatar()));
    }

    private void chooseAvatar() {
        AvatarSelectionDialog dialog = new AvatarSelectionDialog();
        dialog.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                setAvatar((int) data);
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
        }
    }

    private void saveSettings() {

        if (validateForm()) {
            User currentUser = AuthManager.getCurrentUser();
            newSettings.setId(currentUser.getId());
            newSettings.setName(this.usernameEditText.getText().toString().trim());
            newSettings.setEmail(this.emailEditText.getText().toString().trim());
            newSettings.setProvince(this.spinnerProvince.getSelectedItemPosition());
            DuelApp.getInstance().sendMessage(newSettings.serialize(CommandType.SEND_REGISTER));
        }
    }

    private boolean validateForm() {
        if (usernameEditText.getText().length() == 0) {
            Toast toast = Toast.makeText(getActivity(), "لطفا اسم خود را وارد نمایید.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return false;

        }

        if (spinnerProvince.getSelectedItem().toString().equals("استان")) {
            Toast toast = Toast.makeText(getActivity(), "لطفا استان خود را انتخاب نمایید.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return false;
        }
        return true;
    }

}
