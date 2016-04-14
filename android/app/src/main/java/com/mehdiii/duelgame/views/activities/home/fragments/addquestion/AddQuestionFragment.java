package com.mehdiii.duelgame.views.activities.home.fragments.addquestion;

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
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mehdiii.duelgame.DuelApp;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.managers.AuthManager;
import com.mehdiii.duelgame.models.DeliveryReport;
import com.mehdiii.duelgame.models.NewQuestion;
import com.mehdiii.duelgame.models.QuestionForQuiz;
import com.mehdiii.duelgame.models.base.BaseModel;
import com.mehdiii.duelgame.models.base.CommandType;
import com.mehdiii.duelgame.models.events.OnPurchaseResult;
import com.mehdiii.duelgame.utils.DuelBroadcastReceiver;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.utils.OnMessageReceivedListener;
import com.mehdiii.duelgame.views.activities.home.fragments.FlippableFragment;

import java.util.ArrayList;
import java.util.HashSet;

import de.greenrobot.event.EventBus;

/**
 * Created by frshd on 4/13/16.
 */
public class AddQuestionFragment extends FlippableFragment implements View.OnClickListener  {
    ScrollView scroll;
    TextView headerText;
    EditText questionText;
    EditText answerText;
    EditText option2;
    EditText option3;
    EditText option4;
    EditText description;
    Spinner course;
    Spinner chapter;
    Button sendButton;

    NewQuestion newQuestion = new NewQuestion();
    ProgressDialog progressDialog = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_question, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        find(view);
        configure();
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

    private void find(View view) {
        scroll = (ScrollView) view.findViewById(R.id.scrollView);
        headerText = (TextView) view.findViewById(R.id.header);
        questionText = (EditText) view.findViewById(R.id.question_text);
        answerText = (EditText) view.findViewById(R.id.answer_text);
        option2 = (EditText) view.findViewById(R.id.option_2);
        option3 = (EditText) view.findViewById(R.id.option_3);
        option4 = (EditText) view.findViewById(R.id.option_4);
        description = (EditText) view.findViewById(R.id.description);
        course = (Spinner) view.findViewById(R.id.course_name);
        chapter = (Spinner) view.findViewById(R.id.chapter);
        sendButton = (Button) view.findViewById(R.id.button_send);
    }

    private void configure() {

        FontHelper.setKoodakFor(getActivity(),headerText , questionText, answerText, option2, option3,
                option4, description, sendButton);
        sendButton.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_send:
                sendQuestion();
                break;
        }
    }

    private void sendQuestion() {
        if(validateForm()){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("لطفا کمی صبر کنید");
            progressDialog.setCancelable(false);
            progressDialog.show();
            int[] courseIds = this.getResources().getIntArray(R.array.coursesId);
            int[] chapterIds = this.getResources().getIntArray(R.array.chaptersId);
            int courseId = courseIds[this.course.getSelectedItemPosition()];
            int chapterId = chapterIds[this.chapter.getSelectedItemPosition()];
            newQuestion.setCourse(courseId);
            newQuestion.setChapter(chapterId);
            newQuestion.setAnswer(this.answerText.getText().toString());
            newQuestion.setOption2(this.option2.getText().toString());
            newQuestion.setOption3(this.option3.getText().toString());
            newQuestion.setOption4(this.option4.getText().toString());
            newQuestion.setDescription(this.description.getText().toString());
            newQuestion.setQuestion_text(this.questionText.getText().toString());
            Log.d("TAG", "new question" + newQuestion.serialize(CommandType.SEND_NEW_QUESTION));
            DuelApp.getInstance().sendMessage(newQuestion.serialize(CommandType.SEND_NEW_QUESTION));
        }
    }
    private boolean validateForm() {
        if (questionText.getText().length() == 0) {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.add_question_validate_question), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (answerText.getText().length() == 0) {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.add_question_validate_answer), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (option2.getText().length() == 0) {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.add_question_validate_option2), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (option3.getText().length() == 0) {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.add_question_validate_option3), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (option4.getText().length() == 0) {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.add_question_validate_option4), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (course.getSelectedItem().toString().equals("انتخاب کتاب")) {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.add_question_validate_book), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if (chapter.getSelectedItem().toString().equals("انتخاب درس")) {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.add_question_validate_chapter), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        HashSet<String> hs = new HashSet<>();
        hs.add(answerText.getText().toString());
        hs.add(option2.getText().toString());
        hs.add(option3.getText().toString());
        hs.add(option4.getText().toString());
        if (hs.size()<4){
            Toast toast = Toast.makeText(getActivity(), getString(R.string.add_question_duplicate_answer), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    BroadcastReceiver receiver = new DuelBroadcastReceiver(new OnMessageReceivedListener() {
        @Override
        public void onReceive(String json, CommandType type) {
            if (type == CommandType.RECEIVE_NEW_QUESTION) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                questionText.setText("");
                answerText.setText("");
                option2.setText("");
                option3.setText("");
                option4.setText("");
                description.setText("");
                course.setSelection(0);
                chapter.setSelection(0);
                int message = 0;
                message = R.string.new_question_send_successful;
                DuelApp.getInstance().toast(message, Toast.LENGTH_SHORT);
                scroll.fullScroll(ScrollView.FOCUS_UP);
            }
        }
    });
}
