package com.mehdiii.duelgame.views.activities.quiz.fragments.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.Quiz;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.custom.CustomTextView;

import java.util.List;

/**
 * Created by mehdiii on 1/14/16.
 */
public class QuizCardAdapter extends ArrayAdapter<Quiz> {

    private LayoutInflater inflater;

    private final int DAY = 24 * 60 * 60;
    private final int HOUR = 60 * 60;
    private final int MINUTE = 60;
    private String status;

    public QuizCardAdapter(Context context, int resource, List<Quiz> items, String status) {
        super(context, resource, items);
        inflater = LayoutInflater.from(context);
        this.status = status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("TAG", "status " + getItem(position).getStatus());

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.template_quiz_card, null);

            ViewHolder holder = new ViewHolder();
            holder.title = (CustomTextView) convertView.findViewById(R.id.quiz_title);
            holder.timeToQuiz = (CustomTextView) convertView.findViewById(R.id.time_to_quiz);
            holder.container = (LinearLayout) convertView.findViewById(R.id.container_wrapper);
            holder.isTaken = (CustomTextView) convertView.findViewById(R.id.is_taken);
            convertView.setTag(holder);
        }

        initViews(getItem(position), (ViewHolder) convertView.getTag());

        return convertView;
    }

    /**
     * @param sec
     * if sec <= 0 then quiz has started
     * if sec > 0 then we have time to quiz
     */
    private String calculateTimeToQuiz(int sec, String status) {
        if(status.equals("due")) {
            if(sec > DAY)
                return String.valueOf(sec / DAY) + " روز پس از آزمون";
            if(sec > HOUR)
                return String.valueOf(sec / HOUR) + " ساعت پس از آزمون";
            return String.valueOf(sec / MINUTE) + "دقیقه پس از آزمون";
        } else if(status.equals("future")) {
            if(sec > DAY)
                return String.valueOf(sec / DAY) + " روز مانده به شروع آزمون";
            if(sec > HOUR)
                return String.valueOf(sec / HOUR) + " ساعت مانده به شروع آزمون";
            return String.valueOf(sec / MINUTE) + " دقیقه مانده به شروع آزمون";
        } else {
            if(sec > DAY)
                return String.valueOf(sec / DAY) + " روز تا پایان آزمون";
            if(sec > HOUR)
                return String.valueOf(sec / HOUR) + " ساعت تا پایان آزمون";
            return String.valueOf(sec / MINUTE) + " دقیقه تا پایان آزمون";
        }
    }

    private void initViews(Quiz quiz, ViewHolder holder) {
        if(status.equals("due")) {
            holder.container.setBackgroundColor(getContext().getResources().getColor(R.color.gray_very_light));
        } else if(status.equals("future")) {
            holder.container.setBackgroundColor(getContext().getResources().getColor(R.color.blue_light));
        } else if(status.equals("running")) {
            holder.container.setBackgroundColor(getContext().getResources().getColor(R.color.green_very_light));
        }

        holder.timeToQuiz.setText(calculateTimeToQuiz(quiz.getTimeToQuiz(), status));
        holder.title.setText(quiz.getTitle());
        holder.isTaken.setTypeface(FontHelper.getIcons(getContext()));
        if (quiz.getTaken()==true){
            holder.isTaken.setText("e");
            holder.isTaken.setTextColor(getContext().getResources().getColor(R.color.green));
        }
        else{
            holder.isTaken.setText("d");
            holder.isTaken.setTextColor(getContext().getResources().getColor(R.color.red));
        }
    }

    protected class ViewHolder {
        LinearLayout container;
        CustomTextView title;
        CustomTextView timeToQuiz;
        CustomTextView isTaken;
    }
}
