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

    public QuizCardAdapter(Context context, int resource, List<Quiz> items) {
        super(context, resource, items);
        inflater = LayoutInflater.from(context);
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
    private String calculateTimeToQuiz(int sec) {
        if(sec <= 0) {
            sec *= -1;
            if(sec > DAY) {
                return String.valueOf(sec / DAY) + " روز پس از آزمون";
            }
            if(sec > HOUR) {
                return String.valueOf(sec / HOUR) + " ساعت پس از آزمون";
            }
            return String.valueOf(sec / MINUTE) + "دقیقه پس از آزمون";
        } else {
            if(sec > DAY) {
                return String.valueOf(sec / DAY) + " روز مانده به شروع آزمون";
            }
            if(sec > HOUR) {
                return String.valueOf(sec / HOUR) + " ساعت مانده به شروع آزمون";
            }
            return String.valueOf(sec / MINUTE) + " دقیقه مانده به شروع آزمون";
        }
    }

    private void initViews(Quiz quiz, ViewHolder holder) {
        if(quiz.getStatus().equals("due")) {
            holder.container.setBackgroundColor(getContext().getResources().getColor(R.color.gray_very_light));
        } else if(quiz.getStatus().equals("future")) {
            holder.container.setBackgroundColor(getContext().getResources().getColor(R.color.blue_light));
        } else if(quiz.getStatus().equals("running")) {
            holder.container.setBackgroundColor(getContext().getResources().getColor(R.color.green_very_light));
        }

        holder.timeToQuiz.setText(calculateTimeToQuiz(quiz.getTimeToQuiz()));
        holder.title.setText(quiz.getTitle());
    }

    protected class ViewHolder {
        LinearLayout container;
        CustomTextView title;
        CustomTextView timeToQuiz;
    }
}
