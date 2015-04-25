package com.mehdiii.duelgame.views.activities.home.fragments.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.home.fragments.FlipableFragment;
import com.mehdiii.duelgame.views.dialogs.AvatarSelectionDialog;

/**
 * Created by omid on 4/5/2015.
 */
public class SettingsFragment extends FlipableFragment {
    private EditText usernameEditText;
    private EditText emailEditText;
    private ImageView avatarImageView;
    private TextView textViewHintAvatat;
    private Spinner spinnerProvince;
    private TextView textViewGirl;
    private SwitchButton switchGender;
    private TextView textViewBoy;
    private SwitchButton switchMusic;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
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

        configure();
    }

    private void configure() {
        FontHelper.setKoodakFor(getActivity(), textViewHintAvatat, textViewGirl, textViewBoy, usernameEditText, emailEditText);
    }

    @Override
    public void onBringToFront() {
        super.onBringToFront();
        if (getView() != null)
            initializeData();
    }

    private void initializeData() {
        usernameEditText.setText(AuthManager.getCurrentUser().getName());
        emailEditText.setText(AuthManager.getCurrentUser().getEmail());
        spinnerProvince.setSelection(AuthManager.getCurrentUser().getProvince());
        switchGender.setChecked(AuthManager.getCurrentUser().getGender() == 1);
    }

    public void chooseAvatar(View v) {
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
        avatarImageView.setImageResource(AvatarHelper.getResourceId(getActivity(), position));
    }
}
