package com.mehdiii.duelgame.views.activities.stepbystep.fragments.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.ProgressForStep;
import com.mehdiii.duelgame.views.custom.CustomTextView;

import java.util.List;

/**
 * Created by frshd on 4/7/16.
 */
public class ProgressListAdapter  extends ArrayAdapter<ProgressForStep> {
    List<ProgressForStep> progresses;

    private LayoutInflater layoutInflater;
    CustomTextView textViewLastStep;
    CircleProgress circleProgress;

    public ProgressListAdapter(Context context, int resource, List<ProgressForStep> steps) {
        super(context, resource);
        this.progresses = steps;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (progresses != null)
            return progresses.size();
        else
            return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.template_step_progress, null);
        }

        textViewLastStep = (CustomTextView) convertView.findViewById(R.id.progress);
        circleProgress = (CircleProgress) convertView.findViewById(R.id.donut_progress);
        this.textViewLastStep.setText(progresses.get(position).getName());
        circleProgress.setFinishedColor(getContext().getResources().getColor(R.color.purple_sexy));
        circleProgress.setUnfinishedColor(getContext().getResources().getColor(R.color.purple_sexy_trans));
        int progressPercentage = (int)((progresses.get(position).getStars() / (float) (progresses.get(position).getTotalStars() * 3)) * 100);
        circleProgress.setProgress(progressPercentage);
        circleProgress.setTextColor(getContext().getResources().getColor(R.color.white));

        circleProgress.setTextSize(75.f);

        return convertView;
    }
}
