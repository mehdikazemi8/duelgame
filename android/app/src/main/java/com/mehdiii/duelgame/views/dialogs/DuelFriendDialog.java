package com.mehdiii.duelgame.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.WannaChallenge;
import com.mehdiii.duelgame.utils.FontHelper;

public class DuelFriendDialog extends Dialog {
    EditText messageEditText;
    ListView courses;

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
        FontHelper.setKoodakFor(getContext(), messageEditText);

        // setting list view that shows courses
        String[] coursesNames = getContext().getResources().getStringArray(R.array.categories_values);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.template_course_challenge_dialog, R.id.course_name, coursesNames);
        courses.setAdapter(adapter);
        courses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] categories = getContext().getResources().getStringArray(R.array.categories_keys);
                int categoryId = Integer.parseInt(categories[i]);
                String message = messageEditText.getText().toString().trim();
                if (onResult != null) {
                    onResult.getChallenge(new WannaChallenge(null, categoryId, message));
                }
            }
        });
    }

    private void findControls() {
        messageEditText = (EditText) findViewById(R.id.editText_message);
        courses = (ListView) findViewById(R.id.courses_list);
    }
}
