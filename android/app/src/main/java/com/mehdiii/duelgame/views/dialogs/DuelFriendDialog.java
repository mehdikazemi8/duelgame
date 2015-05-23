package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.Friend;
import com.mehdiii.duelgame.models.WannaChallenge;
import com.mehdiii.duelgame.utils.FontHelper;

/**
 * Created by Omid on 5/23/2015.
 */
public class DuelFriendDialog extends Dialog {
    Button okayButton;
    EditText messageEditText;
    Spinner categorySpinner;

    public interface OnResult {
        void getChallenge(WannaChallenge wannaChallenge);
    }

    private OnResult onResult;

    public OnResult getOnResult() {
        return onResult;
    }

    public void setOnResult(OnResult onResult) {
        this.onResult = onResult;
    }

    public DuelFriendDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_duel_friend);

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        getWindow().setLayout((int) (metrics.widthPixels * 0.9), -2);
        findControls();
        configure();
    }

    private void configure() {
        FontHelper.setKoodakFor(getContext(), okayButton, messageEditText);
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] categoryKeys = getContext().getResources().getStringArray(R.array.categories_keys);
                int selectedCat = categorySpinner.getSelectedItemPosition();
                int categoryId = -1;
                try {
                    categoryId = Integer.parseInt(categoryKeys[selectedCat]);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }

                // if category is not selected display an error
                // TODO
                if (categoryId == -1)
                    return;
                String message = messageEditText.getText().toString().trim();
                if (onResult != null) {
                    onResult.getChallenge(new WannaChallenge(null, categoryId, message));
                }
            }
        });
    }

    private void findControls() {
        categorySpinner = (Spinner) findViewById(R.id.spinner_category);
        messageEditText = (EditText) findViewById(R.id.editText_message);
        okayButton = (Button) findViewById(R.id.button_okay);
    }
}
