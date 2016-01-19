package com.mehdiii.duelgame.views.activities.quiz.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.CourseResult;
import com.mehdiii.duelgame.views.custom.CustomTextView;

import java.util.List;

/**
 * Created by mehdiii on 1/19/16.
 */
public class CourseResultAdapter extends ArrayAdapter<CourseResult> {

    LayoutInflater inflater;

    public CourseResultAdapter(Context context, int resource, List<CourseResult> items) {
        super(context, resource, items);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.template_course_result, null);
            ViewHolder holder = new ViewHolder();
            holder.averagePercent = (CustomTextView) convertView.findViewById(R.id.average_percent);
            holder.userPercent = (CustomTextView) convertView.findViewById(R.id.user_percent);
            holder.courseName = (CustomTextView) convertView.findViewById(R.id.course_name);
            holder.rank = (CustomTextView) convertView.findViewById(R.id.rank);
            holder.maximumPercent = (CustomTextView) convertView.findViewById(R.id.maximum_percent);
            convertView.setTag(holder);
        }

        initViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initViews(CourseResult courseResult, ViewHolder holder) {
        holder.courseName.setText(String.valueOf(courseResult.getCourseName()));
        holder.averagePercent.setText(String.format("%.2f", courseResult.getAvgPercent()));
        holder.maximumPercent.setText(String.format("%.2f", courseResult.getMaxPercent()));
        if(courseResult.getRank() != -1) {
            holder.rank.setText(String.valueOf(courseResult.getRank()));
            holder.userPercent.setText(String.valueOf(courseResult.getUserPercent()));
        } else {
            holder.rank.setText(String.valueOf("-"));
            holder.userPercent.setText("-");
        }
    }

    protected class ViewHolder {
        CustomTextView rank;
        CustomTextView maximumPercent;
        CustomTextView averagePercent;
        CustomTextView userPercent;
        CustomTextView courseName;
    }
}
