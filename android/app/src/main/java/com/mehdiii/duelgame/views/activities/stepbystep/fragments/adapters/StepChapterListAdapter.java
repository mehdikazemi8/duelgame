package com.mehdiii.duelgame.views.activities.stepbystep.fragments.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

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
    LinearLayout chapterHolder;

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
        chapterHolder = (LinearLayout) convertView.findViewById(R.id.chapter_holder);
        chapterName.setText("درس " + chapters.get(position).getName());
        chapterStar.setTypeface(FontHelper.getIcons(getContext()));
        int stars = chapters.get(position).getStars();
        Log.d("TAG", "stars of chap "+stars);
        switch (stars){
            case -1:
                chapterStar.setText("Z");
                chapterHolder.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                break;
            case 0:
                chapterStar.setText("aaa");
                chapterHolder.setBackgroundColor(getContext().getResources().getColor(R.color.purple_sexy));
                break;
            case 1:
                chapterStar.setText("caa");
                chapterHolder.setBackgroundColor(getContext().getResources().getColor(R.color.purple_sexy));
                break;
            case 2:
                chapterStar.setText("cca");
                chapterHolder.setBackgroundColor(getContext().getResources().getColor(R.color.purple_sexy));
                break;
            case 3:
                chapterStar.setText("ccc");
                chapterHolder.setBackgroundColor(getContext().getResources().getColor(R.color.purple_sexy));
                break;
        }

//        chapterStar.setText(chapters.get(position).getStars() + " ستاره");

        return convertView;
    }


}
