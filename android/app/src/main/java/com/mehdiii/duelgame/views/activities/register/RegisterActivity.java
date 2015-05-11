package com.mehdiii.duelgame.views.activities.register;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.GlobalPreferenceManager;
import com.mehdiii.duelgame.models.User;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.utils.AvatarHelper;
import com.mehdiii.duelgame.utils.DeviceManager;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.OnCompleteListener;
import com.mehdiii.duelgame.views.activities.ParentActivity;
import com.mehdiii.duelgame.views.activities.home.HomeActivity;
import com.mehdiii.duelgame.views.activities.splash.StartActivity;
import com.mehdiii.duelgame.views.dialogs.AvatarSelectionDialog;

public class RegisterActivity extends ParentActivity {

    private final String SHORTCUT_CREATED_TAG = "shortcut_created";

    String userId;
    TextView hintTextView;
    TextView girlTextView;
    TextView boyTextView;
    EditText usernameEditText;
    EditText emailEditText;
    Button registerButton;
    ImageView selectedAvatarImageView;
    Spinner provinceSpinner;
    SwitchButton genderSwitch;
    User user = new User();
    ProgressDialog dialog = null;

    BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_LOGIN_INFO) {
                if (dialog != null)
                    dialog.dismiss();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        }
    });


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /**
         * find child views and set fonts
         */
        find();
        configure();

        createShortCut();

        /**
         * listen for data changes
         */
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, DuelApp.getInstance().getIntentFilter());


        /**
         * generate device unique identifier
         **/
        userId = DeviceManager.getDeviceId(RegisterActivity.this);

//        if (DuelApp.getInstance().getSocket() == null || !DuelApp.getInstance().getSocket().isConnected()) {
//            startActivity(new Intent(this, TryToConnectActivity.class));
//            finish();
//        }
    }

    private void find() {
        selectedAvatarImageView = (ImageView) findViewById(R.id.start_his_avatar);
        usernameEditText = (EditText) findViewById(R.id.editText_username);
        emailEditText = (EditText) findViewById(R.id.editText_email);
        registerButton = (Button) findViewById(R.id.button_register);
//        chooseAvatarButton = (Button) findViewById(R.id.button);
        provinceSpinner = (Spinner) findViewById(R.id.spinner_province);
        hintTextView = (TextView) findViewById(R.id.textView_hint_avatat);
        girlTextView = (TextView) findViewById(R.id.textView_girl);
        boyTextView = (TextView) findViewById(R.id.textView_boy);
        genderSwitch = (SwitchButton) findViewById(R.id.switch_gender);
    }

    private void configure() {
        /**
         * set font to controls
         */
        FontHelper.setKoodakFor(this, usernameEditText, registerButton, emailEditText, girlTextView, boyTextView, hintTextView);
        // set default gender value
        genderSwitch.setChecked(true);

        // add click listeners for gender textViews
        this.girlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (genderSwitch.isChecked())
                    genderSwitch.performClick();
            }
        });

        this.boyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!genderSwitch.isChecked())
                    genderSwitch.performClick();
            }
        });
    }

    private boolean validateForm() {
        // TODO the logic in this part can be troublesome, review it ASAP.
        if (usernameEditText.getText().length() == 0) {
            Toast toast = Toast.makeText(this, "لطفا اسم خود را وارد نمایید.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return false;

        }

        if (provinceSpinner.getSelectedItem().toString().equals("انتخاب استان")) {
            Toast toast = Toast.makeText(this, "لطفا استان خود را انتخاب نمایید.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return false;
        }
        return true;
    }

    public void registerMe(View v) {
        user.setName(usernameEditText.getText().toString());
        user.setEmail(emailEditText.getText().toString());
        user.setProvince(provinceSpinner.getSelectedItemPosition());
        user.setGender(genderSwitch.isChecked() ? 1 : 0);

        if (validateForm()) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("لطفا کمی صبر کنید");
            dialog.setCancelable(false);
            dialog.show();

            user.setDeviceId(userId);
            DuelApp.getInstance().sendMessage(user.serialize(CommandType.SEND_REGISTER));
        }
    }

    public void createShortCut() {
        boolean hasShortcutBeenCreatedBefore = GlobalPreferenceManager.readBoolean(this, SHORTCUT_CREATED_TAG, false);
        if(hasShortcutBeenCreatedBefore)
            return;

        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcutIntent.putExtra("duplicate", false);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
        Parcelable icon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_launcher);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext(), StartActivity.class));
        sendBroadcast(shortcutIntent);

        GlobalPreferenceManager.writeBoolean(this, SHORTCUT_CREATED_TAG, true);
    }

    public void chooseAvatar(View v) {
        AvatarSelectionDialog dialog = new AvatarSelectionDialog();
        dialog.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Object data) {
                setAvatar((int) data);
            }
        });
        dialog.show(getSupportFragmentManager(), "DIALOG_AVATAR_CHOOSER");
    }

    @Override
    public void onResume() {
        super.onResume();
        setAvatar(user.getAvatar());
    }

    private void setAvatar(int position) {
        user.setAvatar(position);
        selectedAvatarImageView.setImageResource(AvatarHelper.getResourceId(this, position));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}
