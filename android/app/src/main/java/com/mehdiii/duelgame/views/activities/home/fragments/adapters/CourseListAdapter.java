package com.mehdiii.duelgame.views.activities.home.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.CourseForDuel;
import com.mehdiii.duelgame.models.StepForDuel;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.custom.CustomTextView;

import java.util.List;

/**
 * Created by frshd on 4/13/16.
 */
public class CourseListAdapter extends ArrayAdapter<CourseForDuel> {
    List<CourseForDuel> steps;
    private LayoutInflater layoutInflater;

    public CourseListAdapter(Context context, int resource, List<CourseForDuel> steps) {
        super(context, resource);
        this.steps = steps;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (steps != null)
            return steps.size();
        else
            return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.template_step_list_dialog, null);
        }
        CustomTextView stepBook = (CustomTextView) convertView.findViewById(R.id.step_book);
        TextView stepStar = (TextView) convertView.findViewById(R.id.step_stars);
        int chapterNum = steps.get(position).getChapter() + 1;
        stepBook.setText(steps.get(position).getName() + "\nدرس " + chapterNum);
        stepStar.setTypeface(FontHelper.getIcons(getContext()));
        int stars = steps.get(position).getStars();
        switch (stars){
            case -1:
                stepStar.setText("Z");
                break;
            case 0:
                stepStar.setText("Z");
                break;
            case 1:
                stepStar.setText("caa");
                break;
            case 2:
                stepStar.setText("cca");
                break;
            case 3:
                stepStar.setText("ccc");
                break;
        }
        return convertView;
    }

}
