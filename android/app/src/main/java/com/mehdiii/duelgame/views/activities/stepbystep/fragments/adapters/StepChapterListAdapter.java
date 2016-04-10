package com.mehdiii.duelgame.views.activities.stepbystep.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mehdiii.duelgame.R;
import com.mehdiii.duelgame.models.ChapterForStep;
import com.mehdiii.duelgame.utils.FontHelper;
import com.mehdiii.duelgame.views.custom.CustomTextView;

import java.util.List;

/**
 * Created by frshd on 4/10/16.
 */
public class StepChapterListAdapter extends ArrayAdapter<ChapterForStep> {
    List<ChapterForStep> chapters;
    private LayoutInflater layoutInflater;
    CustomTextView chapterName;
    CustomTextView chapterStar;

    public StepChapterListAdapter(Context context, int resource, List<ChapterForStep> objects) {
        super(context, resource, objects);
        this.chapters = objects;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.template_step_chapter, null);
        }

        chapterName = (CustomTextView) convertView.findViewById(R.id.chapter);
        chapterStar = (CustomTextView) convertView.findViewById(R.id.chapter_star);
        chapterName.setText("درس "+chapters.get(position).getName());
        chapterStar.setText(chapters.get(position).getStars() + " ستاره");

//        this.textViewLastStepStarCount.setText(progresses.get(position).getStars() + " از " + progresses.get(position).getTotalStars());
//        this.textViewLastStep.setText( progresses.get(position).getName());
//        this.textViewLastStepStar.setText("c");
//        this.textViewLastStepStar.setTypeface(FontHelper.getIcons(getContext()));

        return convertView;
    }


}
