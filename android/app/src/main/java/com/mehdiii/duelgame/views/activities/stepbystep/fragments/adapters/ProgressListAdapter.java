package com.mehdiii.duelgame.views.activities.stepbystep.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.ProgressForStep;
import com.mehdiii.duelgame.models.StepForDuel;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.custom.CustomTextView;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by frshd on 4/7/16.
 */
public class ProgressListAdapter  extends ArrayAdapter<ProgressForStep> {
    List<ProgressForStep> progresses;

    private LayoutInflater layoutInflater;
    CustomTextView textViewLastStep;
    CustomTextView textViewLastStepStar;
    CustomTextView textViewLastStepStarCount;

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
        textViewLastStepStar = (CustomTextView) convertView.findViewById(R.id.progress_star);
        textViewLastStepStarCount = (CustomTextView) convertView.findViewById(R.id.progress_star_count);

        this.textViewLastStepStarCount.setText(progresses.get(position).getStars() + " از " + progresses.get(position).getTotalStars()*3);
        this.textViewLastStep.setText( progresses.get(position).getName());
        this.textViewLastStepStar.setText("c");
        this.textViewLastStepStar.setTypeface(FontHelper.getIcons(getContext()));

//        CustomTextView stepBook = (CustomTextView) convertView.findViewById(R.id.step_book);
//        TextView stepStar = (TextView) convertView.findViewById(R.id.step_stars);
//        stepBook.setText(progresses.get(position).getName());
//        stepStar.setTypeface(FontHelper.getIcons(getContext()));
//        int stars = progresses.get(position).getStars();
//        switch (stars){
//            case -1:
//                stepStar.setText("Z");
//                break;
//            case 0:
//                stepStar.setText("aaa");
//                break;
//            case 1:
//                stepStar.setText("caa");
//                break;
//            case 2:
//                stepStar.setText("cca");
//                break;
//            case 3:
//                stepStar.setText("ccc");
//                break;
//        }
        return convertView;
    }

}
